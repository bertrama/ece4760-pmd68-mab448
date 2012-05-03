package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;


import main.BrainMapConstants;
import serial.SerialConnection;

/**
 * Display area for current sample values for a given ADC channel
 */
public class ADCFrame extends JFrame {

	private ValuePanel[] adc_vals;
	private PlotArea plot_area;
	private static final Color[] channel_colors = {Color.BLUE, Color.GREEN, Color.RED, Color.ORANGE, Color.CYAN, Color.MAGENTA, Color.PINK, Color.GRAY, Color.BLACK, Color.LIGHT_GRAY, Color.YELLOW};

	public ADCFrame(final int adc_no) {
		super("ADC #" + adc_no);
		this.setLocation(adc_no * 200 % 1200 + 100, adc_no / 6 * 300);
		adc_vals = new ValuePanel[BrainMapConstants.CHANNELS_PER_ADC];

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.insets = new Insets(2, 2, 2, 2);
		c.fill = GridBagConstraints.NONE;
		for (int i = 0; i < BrainMapConstants.CHANNELS_PER_ADC; i++) {
			c.gridy = i / 2;
			c.gridx = (i % 2 == 0) ? 0 : 1;
			adc_vals[i] = new ValuePanel(i);//JLabel("Channel " + i + ": 0");
			this.add(adc_vals[i], c);
			//adc_vals[i].setPreferredSize(new Dimension(120, 30));
			//adc_vals[i].setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		}

		// Add plot area
		c.gridheight = c.gridy + 1;
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = .9;
		c.weighty = .9;
		c.fill = GridBagConstraints.BOTH;
		JPanel plot_panel = new JPanel();
		plot_panel.setBorder(BorderFactory.createTitledBorder("Data Plot"));
		plot_panel.setPreferredSize(new Dimension(300, 100));
		plot_panel.setLayout(new GridBagLayout());
		plot_area = new PlotArea("Time", "Amplitude", 255f, 4000);
		//plot_area.setPreferredSize(new Dimension(100,100));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		plot_panel.add(plot_area, c);
		this.add(plot_panel, c);

		// Setup update timer
		new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (SerialConnection.connection != null && SerialConnection.connection.isOpen()) {
					int[] data = SerialConnection.connection.get_adc_data(adc_no);
					for (int i = 0; i < BrainMapConstants.CHANNELS_PER_ADC; i++) {
						adc_vals[i].setText("Channel " + i + ": " + data[i]);
						plot_area.addDataPoint(i, data[i]);
					}
				}
			}
		}).start();

		this.pack();
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	/*public void updateValues(int[] values) {
		for (int i = 0; i < BrainMap.CHANNELS_PER_ADC; i++) {
			adc_vals[i].setText("Channel " + i + ": " + values[i]);
		}
	}*/

	private class ValuePanel extends JPanel {
		final int channel_no;
		public JLabel label;
		public ValuePanel(int ch) {
			this.channel_no = ch;
			this.setLayout(new GridBagLayout());
			this.setPreferredSize(new Dimension(150, 30));
			//channel_data_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			GridBagConstraints panel_gbc = new GridBagConstraints();
			final JCheckBox checkBox = new JCheckBox("", false);
			checkBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					plot_area.setChannelVisible(channel_no, checkBox.isSelected());
				}
			});
			panel_gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			panel_gbc.fill = GridBagConstraints.VERTICAL;
			panel_gbc.weighty = .1;
			this.add(checkBox, panel_gbc);
			panel_gbc.weightx = .9;
			panel_gbc.gridx = 1;
			label = new JLabel("Channel " + ch + ": " + 0);
			this.add(label, panel_gbc);

		}

		public void setText(String s) {
			label.setText(s);
		}
	}

	private static class PlotArea extends Canvas {
		private String y_label;
		private String x_label;
		private float x_len;
		private float y_max;
		private float[][] dispData;
		private boolean[] is_visible;
		private int[] indexes;

		public PlotArea(String xlabel, String ylabel, float y_max, int x_len) {
			this.x_label = xlabel;
			this.y_label = ylabel;
			this.y_max = y_max;
			this.x_len = x_len;
			dispData = new float[BrainMapConstants.CHANNELS_PER_ADC][x_len];
			is_visible = new boolean[BrainMapConstants.CHANNELS_PER_ADC];
			indexes = new int[BrainMapConstants.CHANNELS_PER_ADC];
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			int width = getWidth();
			int height = getHeight();
			int x0 = height - 10; // y position of x axis
			int xfull = width - 10; // maximum possible x
			int y0 = 10; // x position of y axis
			int yfull = (10); // y range

			// Draw background and axis'
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, width, height);
			g2.setColor(Color.LIGHT_GRAY);
			g2.drawLine(0, x0, width, x0);
			g2.drawLine(10, 0, 10, height);
			g2.drawString(y_label, 0f, 15.0f);
			g2.drawString(x_label, width - 10 * x_label.length(), x0 - 10);

			// Draw a border
			g2.setColor(Color.LIGHT_GRAY);
			g2.drawLine(0, height - 1, width - 1, height - 1); // bottom line
			g2.drawLine(width - 1, 1, width - 1, height - 1); // right side line
			g2.drawLine(0, 0, width - 1, 0); // top line
			g2.drawLine(0, 0, 0, height - 1); // left side line

			float x_inc = (xfull - y0) / (float) x_len;
			float y_scale = (yfull - x0) / y_max;//-(yfull) / y_max;
			//System.out.println("y_scale is "+y_scale);


			// Draw lines
			for (int channel = 0; channel < BrainMapConstants.CHANNELS_PER_ADC; channel++) {
				if (is_visible[channel]) {
					//System.out.println();
					g2.setColor(channel_colors[channel % channel_colors.length]);
					GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
							dispData[channel].length);
					polygon.moveTo(y0, y_scale * dispData[channel][0] + x0);
					for (int i = 1; i < dispData[channel].length; i++) {
						polygon.lineTo(i * x_inc + y0, y_scale * dispData[channel][i] + x0);
						//System.out.println("Trying to add " +i*x_inc+", "+ y_scale*dispData[i] + x0);
					}
					g2.draw(polygon);
				}
			}
		}

		public void addDataPoint(int channel, float d) {
			dispData[channel][indexes[channel]] = d;
			indexes[channel] = (indexes[channel] + 1) % dispData[channel].length;
			this.repaint();
		}

		public void resizeAxes(int x_len, float y_max) {
			this.y_max = y_max;
			dispData = new float[BrainMapConstants.CHANNELS_PER_ADC][x_len];
			this.repaint();
		}

		public void setChannelVisible(int channel, boolean visible) {
			is_visible[channel] = visible;
			this.repaint();
		}
	}
}
