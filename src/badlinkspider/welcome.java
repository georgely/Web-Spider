package badlinkspider;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.net.*;
import java.io.*;

public class welcome extends JFrame {
	JButton deadl = new JButton("Check for Deadlinks");
	JButton hyperl = new JButton("Check for Hyperlinks");
	JButton img = new JButton("Check for Medialinks");
	JLabel jt = new JLabel("Welcome to use the spider ! ");
	JLabel tt = new JLabel("Please select the action below:", JLabel.CENTER);

	public welcome() {
		setTitle("Welcome to use our spider");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 300);
		configuretopPanel();
		configurebottomPanel();
		setVisible(true);

	}

	private void configurebottomPanel() {
		JPanel bottomPanel = new JPanel(new FlowLayout());
		bottomPanel.add(deadl);
		bottomPanel.add(hyperl);
		bottomPanel.add(img);
		add(bottomPanel, BorderLayout.SOUTH);
		deadl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new CheckLinks().setVisible(true);
				setVisible(false);
			}
		});
		hyperl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Hyperlink().setVisible(true);
				setVisible(false);
			}
		});
		img.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Imglink().setVisible(true);
				setVisible(false);
			}
		});
	}

	private void configuretopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(jt, BorderLayout.PAGE_START);
		topPanel.add(tt, BorderLayout.CENTER);
		add(topPanel, BorderLayout.CENTER);
		jt.setFont(new Font(Font.DIALOG, Font.ITALIC, 18));
		tt.setFont(new Font(Font.DIALOG, Font.ITALIC, 20));
	}

	public static void main(String args[]) {
		new welcome();
	}
}
