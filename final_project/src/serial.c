/************************************************************************\
  = serial.c
  = ECE 4760 Final Project
  = Copyright 2012 Patrick Dear, Mark Bunney 

  Contains functions used for serial (UART) communications

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
#include <string.h>
#include "serial.h"
#include "adc.h"

/**
 * Initialize serial connecton
 */
void serial_init(void) {
	//DDRD |=  (1 << PD1);
	#if F_CPU < 20000000UL && defined(U2X0)
		UCSR0A = (1 << U2X0);
		UBRR0L = (F_CPU / (8UL * UART_BAUD)) - 1;
	#else
		UBRR0L = (F_CPU / (16UL * UART_BAUD)) - 1;
	#endif
	UCSR0B = (1 << TXEN0) | (1 << RXEN0); // Enable receive and transmit
	//UCSR0C = (1 << UCSZ01) | (1 << UCSZ00); // UART, no parity, 1 stop bit, 8-bit frame
}

/**
 * Write a string over the UART connection
 */
void serial_write_str(char * s, uint16_t len) {
	uint16_t i;
	for (i = 0; i < len; i++) {
		serial_write_byte(*(s + i));
	}
}

/**
 * Write a byte over the UART connection
 */
void serial_write_byte(char c) {
	while (!(UCSR0A & (1 << UDRE0)));
	UDR0 = c;
}

/**
 * Write the entirety of a data frame over the UART
 */
void write_data_frame(sample_frame_t * frame) {
	uint8_t channel_offset = 0; // Do this for efficiency, avoid multiplies
	uint8_t i, j;
	for (i = 0; i < NUM_ADCS; i++) {
		serial_write_str("AD",2);
		serial_write_byte(i);
		for (j = 0; j < CHANNELS_PER_ADC; j++) {
			serial_write_byte(*(((uint8_t *)frame) + channel_offset + j));
		}
		channel_offset += CHANNELS_PER_ADC;
	}
}


