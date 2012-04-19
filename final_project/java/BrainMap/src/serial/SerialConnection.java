/*
 * Copyright (c) 2012. Patrick Dear
 */

package serial;
import jssc.*;
import main.BrainMap;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

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

	// Instance fields
	private SerialPort serialPort;
	private String name;
	private final int[][] adcData = new int[BrainMap.NUM_ADCS][BrainMap.CHANNELS_PER_ADC];
	private final LinkedList<byte[]> raw_data = new LinkedList<byte[]>();
	private DataParser dataParser;

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
				names[i] = files[i].getAbsolutePath();
			}
			return names;
		}
		else
			return SerialPortList.getPortNames();
	}

	public SerialConnection(String s) {
		serialPort = new SerialPort(s);
		name = s;
	}

	public void open() throws SerialPortException {
		serialPort.openPort();
		serialPort.addEventListener(new SerialPortEventListener() {
			@Override
			public void serialEvent(SerialPortEvent serialPortEvent) {
				if (serialPortEvent.isERR()) {
					System.err.println("Error event in serial communication");
				}
				else if (serialPortEvent.isRXCHAR()) {
					try {
						synchronized (raw_data) {
							byte[] bytes = serialPort.readBytes();
							System.out.println("RXed " + bytes.length + " bytes");
							raw_data.add(bytes);
						}
					}
					catch (SerialPortException e) {
						System.err.println("Error in serial communication, couldn't read received bytes");
					}
				}
			}
		});
		dataParser = new DataParser(raw_data,adcData);
		(new Thread(dataParser)).start();
	}

	public boolean isOpen() {
		return serialPort.isOpened();
	}

	public void close() throws SerialPortException {
		serialPort.closePort();
		dataParser.die();
	}

	public String getName() {
		return name;
	}


	private static class DataParser implements Runnable {
		final LinkedList<byte[]> raw_data;
		final int[][] adc_data;
		boolean keepGoing = true;

		public DataParser(LinkedList<byte[]> raw_data, int[][] adc_data) {
			this.raw_data = raw_data;
			this.adc_data = adc_data;
		}

		@Override
		public void run() {
			int adc_no = 0;
			int channel_no = 0;
			byte[] prev_bytes = new byte[2];
			while (keepGoing) {
				synchronized (raw_data) {
					while (!raw_data.isEmpty()) {
						for (byte b : raw_data.poll()) {
							if (channel_no == BrainMap.CHANNELS_PER_ADC) {
								// Ignore stuff until we receive 'AD'
								if (prev_bytes[0] == 'D' && prev_bytes[1] == 'A') {
									adc_no = b;
									channel_no = 0;
								}
							}
							else {
								synchronized (adc_data) {
									adc_data[adc_no][channel_no] = b;
								}
								System.out.printf("ADC %d, channel %d: %d\n",adc_no, channel_no,b);
								channel_no += 1;
							}
							prev_bytes[1] = prev_bytes[0];
							prev_bytes[0] = b;
							if (b == 'A') {
								System.out.println("Got 'A'");
							}
						}
					}
				}

				try {
					//System.out.println("data parser sleeping.");
					Thread.sleep(100);
				}
				catch (InterruptedException e) {
					System.err.println("DataParser thread interrupted!");
				}
			}
			System.out.println("data parser quitting.");
		}

		public void die() {
			keepGoing = false;
		}
	}
}
