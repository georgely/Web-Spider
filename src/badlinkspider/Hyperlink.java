package badlinkspider;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.*;
import java.io.*;

public class Hyperlink extends JFrame {
	JButton begin = new JButton("Begin");
	JLabel t1 = new JLabel("Enter a URL:");
	JLabel t2 = new JLabel("The result will show after you click show :");
	JTextArea ja = new JTextArea(100, 50);
	JTextField url = new JTextField();
	JScrollPane errorScroll = new JScrollPane();
	JButton back = new JButton("Back to main page");

	public Hyperlink() {
		setTitle("Find Hyper links");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(435, 380);
		getContentPane().setLayout(null);

		getContentPane().add(t1);
		t1.setBounds(12, 12, 84, 12);
		begin.setActionCommand("Begin");
		getContentPane().add(begin);
		begin.setBounds(12, 36, 84, 24);
		getContentPane().add(url);
		url.setBounds(108, 36, 288, 24);

		getContentPane().add(t2);
		t2.setBounds(12, 80, 300, 20);

		errorScroll.setAutoscrolls(true);
		errorScroll
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		errorScroll
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		errorScroll.setOpaque(true);
		getContentPane().add(errorScroll);
		errorScroll.setBounds(12, 120, 384, 156);
		ja.setEditable(false);
		errorScroll.getViewport().add(ja);
		ja.setBounds(0, 0, 366, 138);
		back.setBounds(265, 300, 140, 24);
		getContentPane().add(back);
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new welcome().setVisible(true);
				setVisible(false);
			}
		});

		begin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Document doc = null;
				try {
					base = new URL(url.getText());
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // get the URL
				try {
					doc = Jsoup.connect(base.toString()).get();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Elements links = doc.select("a[href]");
				Elements imports = doc.select("link[href]");
				String Ilink = null;

				ja.append("Hyperlink size:" + links.size() + "\n");
				for (Element link : links) {
					Ilink = "<" + link.attr("abs:href") + ">";
					ja.append(Ilink + "\n");
				}

			}
		});
	}// end for Hyperlink

	/**
	 * The URL that the spider began with
	 */
	protected URL base;

	public static void main(String args[]) {
		(new Hyperlink()).setVisible(true);
	}// end for main
}
