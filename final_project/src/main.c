/************************************************************************\
  = main.c
  = ECE 4760 Final Project
  = Copyright 2012 Patrick Dear, Mark Bunney 

  Entry point for execution

 ========================================================================
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
\************************************************************************/

#include <avr/io.h>
#include <avr/interrupt.h>
#include <util/delay.h>
#include <string.h>
#include <stdio.h>
#include "serial.h"
#include "common.h"
#include "led.h"
#include "adc.h"

// most recent samples
sample_frame_t adc_frames[NUM_ADC_FRAMES];

// global flags
volatile uint8_t flags = FLAG_HBT;

volatile uint8_t sample_frame_r; // most recently read from adc frame
volatile uint8_t sample_frame_w; // most recently written to adc frame

uint8_t asdf = 0;
int main(void) {
	// initialize stuff
	adc_init();
	serial_init();
	//led_driver_set(0xFFFF,0xFFFF,0xFFFF);
	led_driver_set(0,0,0);
	led_driver_init();

	// Setup sampling interrupt, 20 ms period
	TCCR1B = (1<<WGM12) | (1<<CS12); // CTC, divide by 256
	OCR1A = 1249;
	TIMSK1 = 2;

	DDRD |= 0x04 | 0x08 | 0x10;
	PORTD |= 0x04;

	sei();

	while(1) {
		serial_handle();
		if (flags & FLAG_HBT)
			PORTD ^= 0x04;

		// Check for data, and operate on it if there is some
		if (sample_frame_r != sample_frame_w) {
			PORTD |= 0x10;
			sample_frame_r = (sample_frame_r + 1) & NUM_ADC_FRAMES_MASK;

			//if (flags & FLAG_DATA)
			serial_write_frame(&(adc_frames[sample_frame_r]));
			PORTD &= ~0x10;
		}
	}

	return 0;
}

/**
 * Timer 1 compare match ISR
 */
ISR(TIMER1_COMPA_vect) {
	uint8_t frame_next;
	PORTD |= 0x08;
	frame_next = (sample_frame_w + 1) & NUM_ADC_FRAMES_MASK;

	// Do actual sampling
	// use LED set 1
	led_driver_set(0xFFFF,0,0);
	// Sample first 9 channels from ADC0
	adc_get_samples(&(adc_frames[frame_next].adc0_data[0]),0,0,9);
	// Sample next 3 channels from ADC1
	adc_get_samples(&(adc_frames[frame_next].adc1_data[0]),1,0,3);

	led_driver_set(0,0xFFFF,0);
	// Sample next 6 channels from ADC1
	adc_get_samples(&(adc_frames[frame_next].adc1_data[0]),1,3,6);
	// Sample first 6 channels from ADC2
	adc_get_samples(&(adc_frames[frame_next].adc2_data[0]),2,0,6);

	led_driver_set(0,0,0xFFFF);
	// Sample next 3 channels from ADC2
	adc_get_samples(&(adc_frames[frame_next].adc2_data[0]),2,6,3);
	// Sample next 9 channels from ADC3
	adc_get_samples(&(adc_frames[frame_next].adc3_data[0]),3,0,9);

	led_driver_set(0,0,0);

	//adc_get_frame(&(adc_frames[frame_next]));
	sample_frame_w = frame_next;
	PORTD &= ~0x08;
}

