/*
 * Copyright (c) 2012. Patrick Dear
 */

package gui;

import jssc.SerialPortException;
import serial.SerialConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Control panel for serial connections
 */
public class SerialControl extends JPanel {

	private JComboBox portComboBox;
	private JButton refreshButton;
	private JButton connectButton;
	private JLabel connectLabel;

	public SerialControl() {
		this.setBorder(BorderFactory.createTitledBorder("Serial Connection"));
		this.setLayout(new GridBagLayout());
		//this.setMinimumSize(new Dimension(300,30));
		portComboBox = new JComboBox();
		portComboBox.setPreferredSize(new Dimension(200, 30));
		refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				refreshList();
			}
		});
		connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (SerialConnection.connection == null || !SerialConnection.connection.isOpen()) {
					try {
						if (SerialConnection.connection != null && SerialConnection.connection.isOpen())
							SerialConnection.connection.close();
						SerialConnection.connection = new SerialConnection((String)portComboBox.getSelectedItem());
					}
					catch (SerialPortException e) {
						ControlFrame.showWarning("Couldn't create connection for port\n"+portComboBox.getSelectedItem(),"Error");
						e.printStackTrace();
						return;
					}
					try {
						SerialConnection.connection.open();
					}
					catch (SerialPortException e) {
						ControlFrame.showWarning("Couldn't open connection for port\n"+portComboBox.getSelectedItem(),"Error");
						e.printStackTrace();
						return;
					}
					connectButton.setText("Disconnect");
					connectLabel.setText("Connected to " + SerialConnection.connection.getName());
				}
				else {
					try {
						SerialConnection.connection.close();
					}
					catch (SerialPortException e) {
						ControlFrame.showWarning("Error closing connection for port\n"+portComboBox.getSelectedItem(),"Error");
						e.printStackTrace();
					}
					SerialConnection.connection = null;
					connectButton.setText("Connect");
					connectLabel.setText("Not Connected");
				}
			}
		});
		connectLabel = new JLabel("Not Connected");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		this.add(portComboBox,c);
		c.gridx = 1;
		this.add(refreshButton,c);
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 1;
		this.add(connectButton,c);
		c.gridx = 0;
		c.gridy = 2;
		this.add(connectLabel,c);
	}

	private void refreshList() {
		portComboBox.removeAllItems();
		for (String s : SerialConnection.getPortNames()) {
			portComboBox.addItem(s);
		}
	}

}
