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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.minecraft.crash.CrashReport;
import tk.wurst_client.WurstClient;
import tk.wurst_client.utils.JsonUtils;
import tk.wurst_client.utils.MiscUtils;

import com.google.gson.JsonObject;

public class WurstCrashReporter extends JFrame
{
	private static boolean crashed = false;
	private CrashReport report;
	private JLabel lblPleaseReportThis;
	private JButton btnDontSend;
	private JButton btnSendReport;
	private JScrollPane scrollPane;
	private Component rigidArea;
	
	public WurstCrashReporter(CrashReport report)
	{
		crashed = true;
		setTitle("Wurst has crashed!");
		setAlwaysOnTop(true);
		setSize(new Dimension(450, 300));
		setLocationRelativeTo(null);
		this.report = report;
		getContentPane().setLayout(
			new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		lblPleaseReportThis =
			new JLabel(
				"<html>\r\n<body width=\"300px\">\r\n<br>\r\n<p><b>Wurst has crashed. Please report this crash so that we can fix the bug that caused it and prevent it from happening again in the future.</b>\r\n<br>\r\n<br>\r\n<hr>\r\n<br>\r\n<p>We have created an anonymous crash report that you can send to us by using the button below. Adding a comment describing what you were doing just before the crash occurred can help us to understand the problem.\r\n<br>\r\n<br>");
		lblPleaseReportThis.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblPleaseReportThis.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblPleaseReportThis);
		
		scrollPane = new JScrollPane();
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
		
		btnSendReport = new JButton("Send Report");
		btnSendReport.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				sendReport(textArea.getText());
			}
		});
		panel.add(btnSendReport);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		panel.add(rigidArea);
		
		btnDontSend = new JButton("Don't Send");
		btnDontSend.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					dispose();
				}catch(Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});
		panel.add(btnDontSend);
		setVisible(true);
	}
	
	private void sendReport(String comment)
	{
		JsonObject jsonReport = new JsonObject();
		jsonReport.addProperty("trace", report.getCauseStackTraceOrString());
		jsonReport.addProperty("comment", comment);
		jsonReport.addProperty("vWurst", WurstClient.VERSION);
		jsonReport.addProperty("vMinecraft", "1.8");
		jsonReport.addProperty("vJava", System.getProperty("java.version"));
		jsonReport.addProperty("vJava", System.getProperty("os.name"));
		String response;
		try
		{
			response = MiscUtils.post(new URL("https://bugs.wurst-client.tk/report/"),
				JsonUtils.gson.toJson(jsonReport), "application/json");
			System.out.println(response);
		}catch(Exception e)
		{
			response = "Crash report could not be sent. Please check your internet connection.";
			e.printStackTrace();
		}
		
		lblPleaseReportThis.setText(
				"<html>\r\n<body width=\"300px\">\r\n<br>\r\n"
				+ response
				+ "\r\n<br>\r\n<br>");
		scrollPane.setVisible(false);
		btnSendReport.setVisible(false);
		rigidArea.setVisible(false);
		btnDontSend.setText("OK");
	}

	public static boolean isCrashed()
	{
		return crashed;
	}
}
