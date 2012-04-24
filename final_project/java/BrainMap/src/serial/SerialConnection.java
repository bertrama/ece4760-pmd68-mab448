/*
 * Copyright (c) 2012. Patrick Dear
 */

package serial;

import jssc.*;
import main.BrainMapConstants;

import java.io.*;
import java.util.LinkedList;

/**
 * Handles serial connection with ATMega644
 */
public class SerialConnection {
	public static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().contains("mac");

	public static SerialConnection connection = null;

	// Instance fields
	private SerialPort serialPort;
	private String name;
	private final int[][] adcData = new int[BrainMapConstants.NUM_ADCS][BrainMapConstants.CHANNELS_PER_ADC];
	private final LinkedList<int[]> raw_data = new LinkedList<int[]>();
	private DataParser dataParser;

	/**
	 * Get all of the valid serial ports that can be connected to
	 */
	public static String[] getPortNames() {
		if (IS_MAC) {
			String[] names;
			File dir = new File("/dev");
			File[] files = dir.listFiles(new FilenameFilter() {
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
		} else
			return SerialPortList.getPortNames();
	}

	public SerialConnection(String s) {
		serialPort = new SerialPort(s);
		name = s;
	}

	public void open() throws SerialPortException {
		serialPort.openPort();
		serialPort.setParams(SerialPort.BAUDRATE_115200,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		serialPort.addEventListener(new SerialPortEventListener() {
			@Override
			public void serialEvent(SerialPortEvent serialPortEvent) {
				if (serialPortEvent.isERR()) {
					System.err.println("[SerialConnection] Error event in serial communication");
				} else {
					try {
						synchronized (raw_data) {
							int[] data = serialPort.readIntArray();
							if (data.length != 0)
								raw_data.add(data);
						}
					} catch (SerialPortException e) {
						System.err.println("[SerialConnection] Error in serial communication, couldn't read received bytes");
					}
				}
			}
		});
		dataParser = new DataParser(raw_data, adcData);
		(new Thread(dataParser)).start();
	}

	public boolean isOpen() {
		return serialPort.isOpened();
	}

	public void close() throws SerialPortException {
		serialPort.purgePort(SerialPort.PURGE_RXABORT | SerialPort.PURGE_RXCLEAR);
		serialPort.closePort();
		dataParser.die();
	}

	public String getName() {
		return name;
	}

	public int[] get_adc_data(int adc_no) {
		int[] data = new int[BrainMapConstants.CHANNELS_PER_ADC];
		synchronized (adcData) {
			for (int i = 0; i < BrainMapConstants.CHANNELS_PER_ADC; i++) {
				data[i] = adcData[adc_no][i];
			}
		}
		return data;
	}


	private static class DataParser implements Runnable {
		final LinkedList<int[]> raw_data;
		final int[][] adc_data;
		boolean keepGoing = true;
		private boolean dump_data = true;
		File dumpFile;

		public DataParser(LinkedList<int[]> raw_data, int[][] adc_data) {
			this.raw_data = raw_data;
			this.adc_data = adc_data;
		}

		@Override
		public void run() {
			int adc_no = 0;
			int channel_no = 0;
			int[] prev_bytes = new int[2];
			FileWriter fileWriter = null;
			System.out.println("[DataParser] starting...");
			if (dump_data) {
				try {
					//dumpFile.createNewFile();
					fileWriter = new FileWriter("serialdump.txt");
				} catch (IOException e) {
					System.err.println("[DataParser] Error creating file writer");
					e.printStackTrace();
				}
			}
			while (keepGoing) {
				synchronized (raw_data) {
					while (!raw_data.isEmpty()) {
						for (int b : raw_data.poll()) {
							if (channel_no == BrainMapConstants.CHANNELS_PER_ADC) {
								// Ignore stuff until we receive 'AD'
								if (prev_bytes[0] == 'D' && prev_bytes[1] == 'A') {
									adc_no = b;
									channel_no = 0;
								}
							} else {
								if (adc_no < BrainMapConstants.NUM_ADCS && channel_no < BrainMapConstants.CHANNELS_PER_ADC) {
									synchronized (adc_data) {
										adc_data[adc_no][channel_no] = b;
									}
									channel_no += 1;
									if (adc_no == 0 && channel_no == BrainMapConstants.CHANNELS_PER_ADC && fileWriter != null) {
										try {
											synchronized (adc_data) {
												for (int i = 0; i < BrainMapConstants.CHANNELS_PER_ADC; i++)
													fileWriter.write(" " + adc_data[0][i]);
											}
											fileWriter.write("\n");
										} catch (IOException e) {
											System.err.println("Error printing data to file");
											e.printStackTrace();
										}
									}
								} else {
									// Something bad has happened... try to resynchronize
									System.err.println("[DataParser] Synchronization error, trying to fix it...");
									channel_no = BrainMapConstants.CHANNELS_PER_ADC;
								}
								//System.out.printf("ADC %d, channel %d: %d\n",adc_no, channel_no,b);
							}
							prev_bytes[1] = prev_bytes[0];
							prev_bytes[0] = b;
						}
					}
				}

				try {
					//System.out.println("data parser sleeping.");
					Thread.sleep(10);
				} catch (InterruptedException e) {
					System.err.println("[DataParser] Interrupted!");
				}
			}
			if (dump_data && fileWriter != null) {
				try {
					fileWriter.flush();
					fileWriter.close();
				} catch (IOException e) {
					System.err.println("[DataParser] Error closing serial dump file");
					e.printStackTrace();
				}
			}
			System.out.println("[DataParser] quitting.");
		}

		public void die() {
			keepGoing = false;
		}
	}
}
