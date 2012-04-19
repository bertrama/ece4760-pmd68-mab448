/*
 * Copyright (c) 2012. Patrick Dear
 */

package serial;
import jssc.*;
import java.io.*;

/**
 * Handles serial connection with ATMega644
 */
public class SerialConnection {
	public static final boolean IS_MAC;
	static {
		String vers = System.getProperty("os.name").toLowerCase();
		if (vers.indexOf("mac") != -1) {
			IS_MAC = true;
		} else {
			IS_MAC = false;
		}
	}
	public static SerialConnection connection = null;


	private SerialPort serialPort;

	/**
	 * Get all of the valid serial ports that can be connected to
	 * @return
	 */
	public static String[] getPortNames() {
		if (IS_MAC) {
			String[] names;
			File dir = new File("/dev");
			File [] files = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith("tty.");
				}
			});
			names = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				names[i] = files[i].getName();
			}
			return names;
		}
		else
			return SerialPortList.getPortNames();
	}

	public SerialConnection(String s) {
		serialPort = new SerialPort(s);
	}

	public void open() throws SerialPortException {
		serialPort.openPort();
	}

	public boolean isOpen() {
		return serialPort.isOpened();
	}

	public void close() throws SerialPortException {
		serialPort.closePort();
	}
}
