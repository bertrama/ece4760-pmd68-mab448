/*
 * Copyright (c) 2012. Patrick Dear
 */

package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * Created with IntelliJ IDEA.
 * User: patrick
 * Date: 4/19/12
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlotFrame extends JFrame {

	private PlotArea plotArea;

	public PlotFrame(String string, float y_max) {
		super(string);
		this.setMinimumSize(new Dimension(200, 100));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		plotArea = new PlotArea("x","y",y_max,1024);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		this.add(plotArea, c);

		this.pack();
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	public void addDataPoint(float f) {
		plotArea.addDataPoint(f);
	}

	private static class PlotArea extends Canvas {
		private String y_label;
		private String x_label;
		private float y_max;
		private float[] dispData;
		private int index = 0;

		public PlotArea(String xlabel, String ylabel, float y_max, int x_len) {
			this.x_label = xlabel;
			this.y_label = ylabel;
			this.y_max = y_max;
			dispData = new float[x_len];
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			int width = getWidth();
			int height = getHeight();
			int x0 = height / 2; // y position of x axis
			int xfull = width - 10; // maximum possible x
			int y0 = 10; // x position of y axis
			int yfull = (height / 2 - 10); // y range

			// Draw background and axis'
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, width, height);
			g2.setColor(Color.GRAY);
			g2.drawLine(0, x0, width, x0);
			g2.drawLine(10, 0, 10, height);
			g2.drawString(y_label, 0f, 15.0f);
			g2.drawString(x_label, width - 10, height / 2 + 15);

			g2.setColor(Color.BLUE);
			float x_inc = (xfull - y0) / (float) dispData.length;
			float y_scale = -(yfull) / y_max;
			//System.out.println("y_scale is "+y_scale);

			GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
					dispData.length);
			polygon.moveTo(y0, y_scale * dispData[0] + x0);

			for (int i = 1; i < dispData.length; i++) {
				polygon.lineTo(i * x_inc + y0, y_scale * dispData[i] + x0);
				//System.out.println("Trying to add " +i*x_inc+", "+ y_scale*dispData[i] + x0);
			}

			g2.draw(polygon);
		}

		public void addDataPoint(float d) {
			dispData[index] = d;
			index = (index + 1) % dispData.length;
			this.repaint();
		}

		public void resizeAxes(int x_len, float y_max) {
			this.y_max = y_max;
			dispData = new float[x_len];
		}
	}

}

