/*
 * Copyright (c) 2012. Patrick Dear
 */
package main;
import gui.ADCFrame;
import gui.ControlFrame;
import serial.SerialConnection;

import javax.swing.*;

/**
 * Main entry point for BrainMap application
 */
public class BrainMap {

	public static final int CHANNELS_PER_ADC = 11;
	public static final int NUM_ADCS = 4;

	public static SerialConnection connection;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGui();
			}
		});
	}

	private static void createAndShowGui() {
		new ControlFrame();
	}

}
