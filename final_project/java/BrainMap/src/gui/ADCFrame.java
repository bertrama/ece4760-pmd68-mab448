package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.BrainMap;
import serial.SerialConnection;

/**
 * Display area for current sample values for a given ADC channel
 */
public class ADCFrame extends JFrame {

	private final int adc_no;
	private JLabel[] adc_vals;

	public ADCFrame(final int adc_no) {
		super("ADC #"+adc_no);
		this.setLocation(adc_no*200 % 1200 + 100, adc_no/6 * 300);
		adc_vals = new JLabel[BrainMap.CHANNELS_PER_ADC];
		this.adc_no = adc_no;

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.weightx = 0.2;
		c.weighty = 0.1;
		c.insets = new Insets(2,2,2,2);
		for (int i = 0; i < BrainMap.CHANNELS_PER_ADC; i++) {
			c.gridy = i/2;
			c.gridx = (i % 2 == 0) ? 0 : 1;
			adc_vals[i] = new JLabel("Channel " + i + ": 0");
			this.add(adc_vals[i],c);
			adc_vals[i].setPreferredSize(new Dimension(100,30));
			adc_vals[i].setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		}

		new Timer(10,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (SerialConnection.connection != null && SerialConnection.connection.isOpen()) {
					int[] data = SerialConnection.connection.get_adc_data(adc_no);
					for (int i = 0; i < BrainMap.CHANNELS_PER_ADC; i++) {
						adc_vals[i].setText("Channel " + i + ": " + data[i]);
					}
				}
			}
		}).start();

		this.pack();
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	public void updateValues(int[] values) {
		for (int i = 0; i < BrainMap.CHANNELS_PER_ADC; i++) {
			adc_vals[i].setText("Channel " + i + ": " + values[i]);
		}
	}

	private static class ValueCell extends JPanel {
		;
	}
}
