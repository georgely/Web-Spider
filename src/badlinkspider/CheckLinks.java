package badlinkspider;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class CheckLinks extends JFrame implements Runnable, ISpiderReportable {
	JLabel label1 = new JLabel();
	JButton begin = new JButton();
	JButton back = new JButton("Back to main page");
	/**
	 * The URL being processed
	 */
	JTextField url = new JTextField();
	JScrollPane errorScroll = new JScrollPane();
	/**
	 * A place to store the errors created
	 */
	JTextArea errors = new JTextArea();
	JLabel current = new JLabel();
	JLabel goodLinksLabel = new JLabel();
	JLabel badLinksLabel = new JLabel();

	/**
	 * The constructor. Perform setup here.
	 */
	public CheckLinks() {
		setTitle("Find Dead Links");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setSize(425, 350);
		setVisible(false);
		label1.setText("Enter a URL:");
		getContentPane().add(label1);
		label1.setBounds(12, 12, 84, 12);
		begin.setText("Begin");
		begin.setActionCommand("Begin");
		getContentPane().add(begin);
		begin.setBounds(12, 36, 84, 24);
		getContentPane().add(url);
		url.setBounds(108, 36, 288, 24);
		errorScroll.setAutoscrolls(true);
		errorScroll
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		errorScroll
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		errorScroll.setOpaque(true);
		getContentPane().add(errorScroll);
		errorScroll.setBounds(12, 120, 384, 156);
		errors.setEditable(false);
		errorScroll.getViewport().add(errors);
		errors.setBounds(0, 0, 366, 138);
		current.setText("Currently Processing: ");
		getContentPane().add(current);
		current.setBounds(12, 72, 384, 12);
		goodLinksLabel.setText("Good Links: 0");
		getContentPane().add(goodLinksLabel);
		goodLinksLabel.setBounds(12, 96, 192, 12);
		badLinksLabel.setText("Bad Links: 0");
		getContentPane().add(badLinksLabel);
		badLinksLabel.setBounds(216, 96, 96, 12);
		SymAction lSymAction = new SymAction();
		begin.addActionListener(lSymAction);
		getContentPane().add(back);
		back.setBounds(265, 300, 140, 24);

		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new welcome().setVisible(true);
				setVisible(false);
			}
		});

	}

	public static void main(String args[]) {
		(new CheckLinks()).setVisible(true);
	}

	
	 // Add notifications.
	public void addNotify() {
		// Record the size of the window prior to calling parent's
		// addNotify.
		Dimension size = getSize();
		super.addNotify();

		if (frameSizeAdjusted)
			return;
		frameSizeAdjusted = true;

		// Adjust size of frame according to the insets and menu bar
		Insets insets = getInsets();
		javax.swing.JMenuBar menuBar = getRootPane().getJMenuBar();
		int menuBarHeight = 0;
		if (menuBar != null)
			menuBarHeight = menuBar.getPreferredSize().height;
		setSize(insets.left + insets.right + size.width, insets.top
				+ insets.bottom + size.height + menuBarHeight);
	}

	// Used by addNotify
	boolean frameSizeAdjusted = false;
	protected Thread backgroundThread;// The background spider thread
	protected Spider spider;// The spider object being used
	protected URL base;// The URL that the spider began with
	protected int badLinksCount = 0;// How many bad links have been found
	protected int goodLinksCount = 0; // How many good links have been found

	/* Internal class used to dispatch events*/
	class SymAction implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			Object object = event.getSource();
			if (object == begin)
				begin_actionPerformed(event);
		}
	}

	/**
	 * Called when the begin or cancel buttons are clicked The event associated
	 * with the button.
	 */
	void begin_actionPerformed(java.awt.event.ActionEvent event) {
		if (backgroundThread == null) {
			begin.setLabel("Cancel");
			backgroundThread = new Thread(this);
			backgroundThread.start();
			goodLinksCount = 0;
			badLinksCount = 0;
		} else {
			spider.cancel();
		}

	}

	/**
	 * Perform the background thread operation. This method actually starts the
	 * background thread.
	 */
	public void run() {
		try {
			errors.setText("");
			spider = new Spider(this);
			spider.clear();
			base = new URL(url.getText());
			spider.addURL(base);
			spider.begin();
			Runnable doLater = new Runnable() {
				public void run() {
					begin.setText("Begin");
				}
			};
			SwingUtilities.invokeLater(doLater);
			backgroundThread = null;

		} catch (MalformedURLException e) {
			UpdateErrors err = new UpdateErrors();
			err.msg = "Bad address.";
			SwingUtilities.invokeLater(err);

		}
	}

	/*
	 * Called by the spider when a URL is found. It is here that links are
	 * validated. base The page that the link was found on. url The actual link
	 * address.
	 */
	public boolean spiderFoundURL(URL base, URL url) {
		UpdateCurrentStats cs = new UpdateCurrentStats();
		cs.msg = url.toString();
		SwingUtilities.invokeLater(cs);

		if (!checkLink(url)) {
			UpdateErrors err = new UpdateErrors();
			err.msg = url + "(on page " + base + ")\n";
			SwingUtilities.invokeLater(err);
			badLinksCount++;
			return false;
		}

		goodLinksCount++;
		if (!url.getHost().equalsIgnoreCase(base.getHost()))
			return false;
		else
			return true;
	}

	/* Called when a URL error is found url The URL that resulted in an error. */
	public void spiderURLError(URL url) {
	}

	/**
	 * Called internally to check whether a link is good url The link that is
	 * being checked. True if the link was good, false otherwise.
	 */
	protected boolean checkLink(URL url) {
		try {
			URLConnection connection = url.openConnection();
			connection.connect();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Called when the spider finds an e-mail address email The email address
	 * the spider found.
	 */
	public void spiderFoundEMail(String email) {
	}

	/* Internal class used to update the error information in a Thread-Safe way */

	class UpdateErrors implements Runnable {
		public String msg;

		public void run() {
			errors.append(msg);
		}
	}

	/* Used to update the current status information in a "Thread-Safe" way */

	class UpdateCurrentStats implements Runnable {
		public String msg;

		public void run() {
			current.setText("Currently Processing: " + msg);
			goodLinksLabel.setText("Good Links: " + goodLinksCount);
			badLinksLabel.setText("Bad Links: " + badLinksCount);
		}
	}
}