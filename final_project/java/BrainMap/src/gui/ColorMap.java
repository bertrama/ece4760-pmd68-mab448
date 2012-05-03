/*
 * Copyright (c) 2012. Patrick Dear
 */

package gui;

import main.BrainMapConstants;
import main.ChannelPosition;
import serial.SerialConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * False color 'image' of channel values
 */
public class ColorMap extends JFrame {

	private int[][] value_map = new int[BrainMapConstants.ARRAY_WIDTH][BrainMapConstants.ARRAY_HEIGHT];

	public ColorMap() {
		super("ColorMap");
		setMinimumSize(new Dimension(300, 150));



		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(300, 100));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = .9;
		c.weighty = .9;
		final MapCanvas mc = new MapCanvas();
		//final JPanel panel = new JPanel();
		//panel.add(mc, c);
		this.add(mc,c);
		//this.setBorder(BorderFactory.createTitledBorder("ColorMap"));

		// Setup update timer
		new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (SerialConnection.connection != null && SerialConnection.connection.isOpen()) {
					for (int adc = 0; adc < BrainMapConstants.NUM_ADCS; adc++) {
						int[] data = SerialConnection.connection.get_adc_data(adc);
						if (data != null) {
							for (int i = 0; i < BrainMapConstants.CHANNELS_PER_ADC; i++) {
								ChannelPosition p = BrainMapConstants.CHANNEL_POSITION_MAP.get(BrainMapConstants.getChannelHash(adc, i));
								//System.out.println(value_map);
								value_map[p.x][p.y] = data[i];
								mc.repaint();
							}
						}
					}

				}
			}
		}).start();

		this.setVisible(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}


	private class MapCanvas extends Canvas {

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;
			int width = getWidth();
			int height = getHeight();


			// Draw a white background
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, width, height);

			// Draw main points
			double x_scale = width / BrainMapConstants.ARRAY_WIDTH;
			double y_scale = height / BrainMapConstants.ARRAY_HEIGHT;
			for (int col = 0; col < BrainMapConstants.ARRAY_WIDTH; col++) {
				for (int row = 0; row < BrainMapConstants.ARRAY_HEIGHT; row++) {
					//value_map[col][row] = (row + col + value_map[col][row]) % 256;
					g2.setColor(Color.getHSBColor(value_map[col][row] / 256f * 0.8f, 1f, 1f));
					g2.fill(new Rectangle2D.Double(col * x_scale, row * y_scale, (col + 1) * x_scale, (row + 1) * y_scale));
				}
			}

			// Draw a border
			g2.setColor(Color.LIGHT_GRAY);
			g2.drawLine(0, height - 1, width - 1, height - 1); // bottom line
			g2.drawLine(width - 1, 1, width - 1, height - 1); // right side line
			g2.drawLine(0, 0, width - 1, 0); // top line
			g2.drawLine(0, 0, 0, height - 1); // left side line

		}
	}
}
