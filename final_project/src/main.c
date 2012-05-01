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

int main(void) {
	// initialize stuff
	adc_init();
	serial_init();
	led_driver_init();

	// Setup sampling interrupt, 1 ms period
	TIMSK0 = 2;
	OCR0A = 249;
	TCCR0A = 0x02;
	TCCR0B = 0x03;

	DDRD = 0xFF;
	PORTD = 0xFF;

	sei();

	while(1) {
		_delay_ms(100);
		serial_handle();
		if (flags & FLAG_HBT)
			PORTD ^= 0x04;

		// Check for data, and operate on it if there is some
		if (sample_frame_r != sample_frame_w) {
			sample_frame_r = (sample_frame_r + 1) & NUM_ADC_FRAMES_MASK;
		}
	}

	return 0;
}

/**
 * Timer 0 compare match ISR
 */
volatile uint8_t do_sample = 20;
ISR(TIMER0_COMPA_vect) {
	uint8_t frame_next;
	if (do_sample == 0) {
		do_sample = 20;
		frame_next = (sample_frame_w + 1) & NUM_ADC_FRAMES_MASK;
		adc_get_frame(&(adc_frames[frame_next]));
		sample_frame_w = frame_next;
	}
	else {
		do_sample--;
	}
}

