/*
 * Copyright (c) 2012. Patrick Dear
 */

import gui.ControlFrame;

import javax.swing.*;

/**
 * Main entry point for BrainMap application
 */
public class BrainMap {

	//public static SerialConnection connection;

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
