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
#include <util/delay.h>
#include <string.h>
#include <stdio.h>
#include "serial.h"
#include "adc.h"

uint8_t sample_buffer[CHANNELS_PER_ADC];
char message[] = "ADC values are:\r\n";
char str_buffer[16];

int main(void) {
	uint8_t i;
	// initialize stuff
	adc_init();
	serial_init();

	while(1) {
		adc_get_samples(&(sample_buffer[0]), 0);
		serial_write_str(message,strlen(message));
		for (i = 0; i < CHANNELS_PER_ADC; i++) {
			sprintf(str_buffer,"channel %u: %03u (0x%02X)\t",i,sample_buffer[i],sample_buffer[i]);
			serial_write_str(str_buffer,strlen(str_buffer));
			if (i % 4 == 3)
				serial_write_str("\r\n",2);
		}
		serial_write_str("\r\n",2);
		_delay_ms(500);
	}

	return 0;
}

