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

/**
 * Initialize serial connecton
 */
void serial_init(void) {
	UCSR0B = (1 << TXEN0) | (1 << RXEN0); // Enable receive and transmit
	UCSR0C = (1 << UCSZ01) | (1 << UCSZ00); // UART, no parity, 1 stop bit, 8-bit frame
	UBRR0 = 103; // 9600 baud
}

/**
 * Write a string over the UART connection
 */
void serial_write_str(char * s, uint16_t len) {
	uint16_t i;
	for (i = 0; i < len; i++) {
		UDR0 = *(s + i);
		while (!(UCSR0A & (1 << TXC0)));
	}
}

