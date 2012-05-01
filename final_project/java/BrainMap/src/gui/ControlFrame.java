/*
 * Copyright (c) 2012. Patrick Dear
 */

package gui;

import main.BrainMapConstants;
import serial.SerialConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main GUI frame
 */
public class ControlFrame extends JFrame {

	public ControlFrame() {
		super("BrainMap");
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Instantiate ADC view frames
		ADCFrameControls[] adcFrameControls = new ADCFrameControls[BrainMapConstants.NUM_ADCS];
		JPanel adcFramePanel = new JPanel(new GridBagLayout());
		//adcFramePanel.setPreferredSize(new Dimension(300,200));
		c.insets = new Insets(1,1,1,1);
		for (int i = 0; i < BrainMapConstants.NUM_ADCS; i++) {
			c.gridx = (i % 2 == 0) ? 0 : 1;
			c.gridy = i/2;
			adcFrameControls[i] = new ADCFrameControls(i);
			adcFramePanel.add(adcFrameControls[i],c);
		}
		adcFramePanel.setBorder(BorderFactory.createTitledBorder("Raw ADC Data"));
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = .1;
		c.weighty = .1;
		this.add(adcFramePanel,c);
		c.gridy = 1;
		SerialControl sc = new SerialControl();
		//sc.setPreferredSize(new Dimension(300,200));
		this.add(sc,c);

		final JButton colorMapButton = new JButton("Show Color Map");
		final ColorMap cm = new ColorMap();
		cm.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent windowEvent) {}

			@Override
			public void windowClosing(WindowEvent windowEvent) {
				colorMapButton.setText("Show ColorMap");
			}

			@Override
			public void windowClosed(WindowEvent windowEvent) {}

			@Override
			public void windowIconified(WindowEvent windowEvent) {}

			@Override
			public void windowDeiconified(WindowEvent windowEvent) {}

			@Override
			public void windowActivated(WindowEvent windowEvent) {}

			@Override
			public void windowDeactivated(WindowEvent windowEvent) {}
		});
		colorMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (cm.isVisible()) {
					colorMapButton.setText("Show Color Map");
					cm.setVisible(false);
				}
				else {
					colorMapButton.setText("Hide Color Map");
					cm.setVisible(true);
				}
			}
		});
		c.gridy = 2;
		this.add(colorMapButton,c);

		// add LED heartbeat control
		JButton hbtButton = new JButton("Toggle Heartbeat");
		hbtButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (SerialConnection.connection != null && SerialConnection.connection.isOpen())
					SerialConnection.connection.write_byte(SerialConnection.CMD_TOGGLE_HBT);
				else {
					showWarning("No serial port has been opened","Warning");
				}
			}
		});
		c.gridy = 3;
		this.add(hbtButton,c);

		// add send data control
		final JCheckBox sendDataCheckbox = new JCheckBox("Send Data");
		sendDataCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (SerialConnection.connection != null && SerialConnection.connection.isOpen()) {
					if (sendDataCheckbox.isSelected()) {
						SerialConnection.connection.write_byte(SerialConnection.CMD_SEND_DATA);
					}
					else {
						SerialConnection.connection.write_byte(SerialConnection.CMD_HALT_DATA);
					}
				}
				else {
					showWarning("No serial port has been opened","Warning");
				}
			}
		});
		c.gridy = 4;
		this.add(sendDataCheckbox,c);

		JButton nopButton = new JButton("Send NOP");
		nopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (SerialConnection.connection != null && SerialConnection.connection.isOpen())
					SerialConnection.connection.write_byte(SerialConnection.CMD_NOP);
				else {
					showWarning("No serial port has been opened","Warning");
				}
			}
		});
		c.gridy = 5;
		this.add(nopButton,c);

		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setMinimumSize(this.getSize());
	}

	private static class ADCFrameControls extends JPanel {
		//private int adc_no;
		private JButton viewButton;
		private ADCFrame adcFrame;

		public ADCFrameControls(int adc_no) {
			//this.adc_no = adc_no;
			viewButton = new JButton("Show");
			adcFrame = new ADCFrame(adc_no);
			viewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					if (adcFrame.isVisible()) {
						viewButton.setText("Show");
						adcFrame.setVisible(false);
					}
					else {
						adcFrame.setVisible(true);
						viewButton.setText("Hide");
					}
				}
			});

			adcFrame.addWindowListener(new WindowListener() {
				@Override
				public void windowOpened(WindowEvent windowEvent) {

				}
				@Override
				public void windowClosing(WindowEvent windowEvent) {
					viewButton.setText("Show");
				}
				@Override
				public void windowClosed(WindowEvent windowEvent) {}

				@Override
				public void windowIconified(WindowEvent windowEvent) {}

				@Override
				public void windowDeiconified(WindowEvent windowEvent) {}

				@Override
				public void windowActivated(WindowEvent windowEvent) {}

				@Override
				public void windowDeactivated(WindowEvent windowEvent) {}
			});
			this.setLayout(new FlowLayout(FlowLayout.TRAILING));
			this.add(new JLabel("ADC #"+adc_no));
			this.add(viewButton);
			//this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			viewButton.setPreferredSize(new Dimension(80,30));
			this.setPreferredSize(new Dimension(150, 30));
		}
	}

	public static void showWarning(String message,String title) {
		JOptionPane.showMessageDialog(getFrames()[0],message,title,JOptionPane.ERROR_MESSAGE);
	}

}
