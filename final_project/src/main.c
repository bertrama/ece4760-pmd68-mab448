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
#include "adc.h"

uint8_t sample_buffer[CHANNELS_PER_ADC];
char message[] = "ADC values are:\r\n";
char str_buffer[16];
sample_frame_t adc_frame;

int main(void) {
	// initialize stuff
	adc_init();
	serial_init();

	// Setup sampling interrupt
	TIMSK0 = 2;
	OCR0A = 249;
	TCCR0A = 0x02;
	TCCR0B = 0x03;

	DDRD = 0xFF;
	PORTD = 0xFF;

	sei();

	//serial_write_str(message,strlen(message));
	while(1) {
		//serial_write_str("asdf",4);
		//adc_get_samples(&(sample_buffer[0]), 0);
		/*for (i = 0; i < 1; i++) {
			sprintf(str_buffer,"channel %u: %03u (0x%02X)\t",i,sample_buffer[i],sample_buffer[i]);
			serial_write_str(str_buffer,strlen(str_buffer));
			if (i % 4 == 3)
				serial_write_str("\r\n",2);
		}
		serial_write_str("\r\n",2);
		*/
		//sprintf(str_buffer,"%03u (0x%03X)\r\n",sample_buffer[i],sample_buffer[i]);
		//serial_write_str("\b\b\b",3);
		//serial_write_str(str_buffer,strlen(str_buffer));
		_delay_ms(100);
		PORTD ^= 0x04;
	}

	return 0;
}

/**
 * Timer 0 compare match ISR
 */
volatile uint8_t do_sample = 20;
ISR(TIMER0_COMPA_vect) {
	if (do_sample == 0) {
		do_sample = 20;
		PORTC ^= 0x02;
		adc_get_frame(&adc_frame);
		serial_write_frame(&adc_frame);
	}
	else {
		do_sample--;
	}
}

