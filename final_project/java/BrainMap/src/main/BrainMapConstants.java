/*
 * Copyright (c) 2012. Patrick Dear
 */

package main;

import java.util.HashMap;

/**
 * Constants for entire project
 */
public class BrainMapConstants {
	public static final int CHANNELS_PER_ADC = 11;
	public static final int NUM_ADCS = 4;
	//public static final int NUM_CHANNELS = 36;
	public static final int ARRAY_WIDTH = 9;
	public static final int ARRAY_HEIGHT = 4;
	public static final HashMap<Integer, ChannelPosition> CHANNEL_POSITION_MAP;

	static {
		CHANNEL_POSITION_MAP = new HashMap<Integer, ChannelPosition>();
		/*
		 * Assume the position map is something like:
		 *  ----|----|----|----|----|----|----|----| ...
		 * | 0-0| 0-7| 0-8| 1-4| 1-5| ...
		 * |----|----|----|----|----|----|----|----| ...
		 * | 0-1| 0-6| 0-9| 1-3| 1-6| ...
		 * |----|----|----|----|----|----|----|----| ...
		 * | 0-2| 0-5|0-10| 1-2| 1-7|1-10| ...
		 * |----|----|----|----|----|----|----|----| ...
		 * | 0-3| 0-4| 1-0| 1-1| 1-8| 1-9| ...
		 *  ----|----|----|----|----|----|----|----| ...
		 *
		 *  Use computer graphics-like coordinates (i.e., y increases downwards)
		 */

		// ADC 0
		CHANNEL_POSITION_MAP.put(getChannelHash(0, 0), new ChannelPosition(0, 0));
		CHANNEL_POSITION_MAP.put(getChannelHash(0, 1), new ChannelPosition(0, 1));
		CHANNEL_POSITION_MAP.put(getChannelHash(0, 2), new ChannelPosition(0, 2));
		CHANNEL_POSITION_MAP.put(getChannelHash(0, 3), new ChannelPosition(0, 3));
		CHANNEL_POSITION_MAP.put(getChannelHash(0, 4), new ChannelPosition(1, 0));
		CHANNEL_POSITION_MAP.put(getChannelHash(0, 5), new ChannelPosition(1, 1));
		CHANNEL_POSITION_MAP.put(getChannelHash(0, 6), new ChannelPosition(1, 2));
		CHANNEL_POSITION_MAP.put(getChannelHash(0, 7), new ChannelPosition(1, 3));
		CHANNEL_POSITION_MAP.put(getChannelHash(0, 8), new ChannelPosition(2, 0));
		CHANNEL_POSITION_MAP.put(getChannelHash(0, 9), new ChannelPosition(2, 1));
		CHANNEL_POSITION_MAP.put(getChannelHash(0, 10), new ChannelPosition(2, 2));

		// ADC 1
		CHANNEL_POSITION_MAP.put(getChannelHash(1, 0), new ChannelPosition(2, 3));
		CHANNEL_POSITION_MAP.put(getChannelHash(1, 1), new ChannelPosition(3, 3));
		CHANNEL_POSITION_MAP.put(getChannelHash(1, 2), new ChannelPosition(3, 2));
		CHANNEL_POSITION_MAP.put(getChannelHash(1, 3), new ChannelPosition(3, 1));
		CHANNEL_POSITION_MAP.put(getChannelHash(1, 4), new ChannelPosition(3, 0));
		CHANNEL_POSITION_MAP.put(getChannelHash(1, 5), new ChannelPosition(4, 0));
		CHANNEL_POSITION_MAP.put(getChannelHash(1, 6), new ChannelPosition(4, 1));
		CHANNEL_POSITION_MAP.put(getChannelHash(1, 7), new ChannelPosition(4, 2));
		CHANNEL_POSITION_MAP.put(getChannelHash(1, 8), new ChannelPosition(4, 3));
		CHANNEL_POSITION_MAP.put(getChannelHash(1, 9), new ChannelPosition(5, 3));
		CHANNEL_POSITION_MAP.put(getChannelHash(1, 10), new ChannelPosition(5, 2));

		// ADC 2
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 0), new ChannelPosition(5, 1));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 1), new ChannelPosition(5, 0));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 2), new ChannelPosition(6, 0));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 3), new ChannelPosition(6, 1));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 4), new ChannelPosition(6, 2));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 5), new ChannelPosition(6, 3));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 6), new ChannelPosition(7, 3));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 7), new ChannelPosition(7, 2));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 8), new ChannelPosition(7, 1));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 9), new ChannelPosition(7, 0));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 10), new ChannelPosition(8, 0));

		// ADC 3
		CHANNEL_POSITION_MAP.put(getChannelHash(3, 0), new ChannelPosition(8, 1));
		CHANNEL_POSITION_MAP.put(getChannelHash(3, 1), new ChannelPosition(8, 2));
		CHANNEL_POSITION_MAP.put(getChannelHash(3, 2), new ChannelPosition(8, 3));
		/*
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 3), new ChannelPosition(9, 3));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 4), new ChannelPosition(9, 2));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 5), new ChannelPosition(9, 1));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 6), new ChannelPosition(9, 0));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 7), new ChannelPosition(10, 0));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 8), new ChannelPosition(10, 1));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 9), new ChannelPosition(10, 2));
		CHANNEL_POSITION_MAP.put(getChannelHash(2, 10), new ChannelPosition(10, 3));
		*/
	}

	public static Integer getChannelHash(int adc_no, int channel) {
		return CHANNELS_PER_ADC * adc_no + channel;
	}

}
