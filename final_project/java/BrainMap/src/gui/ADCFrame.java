package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Display area for current sample values for a given ADC channel
 */
public class ADCFrame extends JFrame {

	private int adc_no;
	private int adc_vals[];

	public ADCFrame(int adc_no) {
		super("ADC #"+adc_no);
		adc_vals = new int[main.BrainMap.CHANNELS_PER_ADC];
		this.adc_no = adc_no;

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.weightx = 0.2;
		c.weighty = 0.1;
		c.insets = new Insets(2,2,2,2);
		this.add(new JLabel("ADC "+adc_no),c);

		this.pack();
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	public void updateValues(int[] values) {
		;
	}

	private static class ValueCell extends JPanel {
		;
	}
}
