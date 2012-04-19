/*
 * Copyright (c) 2012. Patrick Dear
 */

package gui;

import main.BrainMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created with IntelliJ IDEA.
 * User: patrick
 * Date: 4/15/12
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ControlFrame extends JFrame {

	private ADCFrameControls[] adcFrameControls = new ADCFrameControls[BrainMap.NUM_ADCS];

	public ControlFrame() {
		super("BrainMap");
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Instantiate ADC view frames
		JPanel adcFramePanel = new JPanel(new GridBagLayout());
		c.insets = new Insets(1,1,1,1);
		for (int i = 0; i < BrainMap.NUM_ADCS; i++) {
			c.gridx = (i % 2 == 0) ? 0 : 1;
			c.gridy = i/2;
			adcFrameControls[i] = new ADCFrameControls(i);
			adcFramePanel.add(adcFrameControls[i],c);
		}
		adcFramePanel.setBorder(BorderFactory.createTitledBorder("Raw ADC Data"));
		c.gridx = 0;
		c.gridy = 0;
		this.add(adcFramePanel,c);
		c.gridy = 1;
		this.add(new SerialControl(),c);

		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setMinimumSize(this.getSize());
	}

	private static class ADCFrameControls extends JPanel {
		private int adc_no;
		private JButton viewButton;
		private ADCFrame adcFrame;

		public ADCFrameControls(int adc_no) {
			this.adc_no = adc_no;
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
