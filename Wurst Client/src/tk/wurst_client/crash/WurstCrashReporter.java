/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.crash;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.minecraft.crash.CrashReport;

public class WurstCrashReporter extends JFrame
{
	private CrashReport report;
	
	public WurstCrashReporter(CrashReport report)
	{
		setUndecorated(true);
		setTitle("Wurst has crashed!");
		setAlwaysOnTop(true);
		setSize(new Dimension(450, 300));
		setLocationRelativeTo(null);
		this.report = report;
		getContentPane().setLayout(
			new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		JLabel lblPleaseReportThis =
			new JLabel(
				"<html>\r\n<body width=\"300px\">\r\n<br>\r\n<p><b>Wurst has crashed. Please report this crash so that we can fix the bug that caused it and prevent it from happening again in the future.</b>\r\n<br>\r\n<br>\r\n<hr>\r\n<br>\r\n<p>We have created an anonymous crash report that you can send to us by using the button below. Adding a comment describing what you were doing just before the crash occurred can help us to understand the problem.\r\n<br>\r\n<br>");
		lblPleaseReportThis.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblPleaseReportThis.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblPleaseReportThis);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setMaximumSize(new Dimension(400, 32767));
		scrollPane.setBorder(new TitledBorder(UIManager
			.getBorder("TitledBorder.border"), "Comment", TitledBorder.CENTER,
			TitledBorder.TOP, null, new Color(0, 0, 0)));
		getContentPane().add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(4, 4, 4, 4));
		getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JButton btnSendReport = new JButton("Send Report");
		panel.add(btnSendReport);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		panel.add(rigidArea);
		
		JButton btnDontSend = new JButton("Don't Send");
		panel.add(btnDontSend);
		setVisible(true);
	}
}
