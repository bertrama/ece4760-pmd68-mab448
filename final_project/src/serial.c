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
#include <avr/interrupt.h>
#include <string.h>
#include "serial.h"
#include "common.h"
#include "adc.h"

#define BUFFER_SIZE 8
#define BUFFER_INDEX_MASK 0x07
uint8_t buffer_head = 0;
uint8_t buffer_tail = 0;
uint8_t serial_rx_buffer[BUFFER_SIZE];

extern volatile uint8_t flags;

/**
 * Initialize serial connecton
 */
void serial_init(void) {
	#if F_CPU < 20000000UL && defined(U2X0)
		UCSR0A = (1 << U2X0);
		UBRR0L = (F_CPU / (8UL * UART_BAUD)) - 1;
	#else
		UBRR0L = (F_CPU / (16UL * UART_BAUD)) - 1;
	#endif
	UCSR0B = (1 << TXEN0) | (1 << RXEN0) | (1 << RXCIE0); // Enable receive and transmit
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
void serial_write_frame(sample_frame_t * frame) {
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

/**
 * Handle serial commands
 */
void serial_handle() {
	static uint8_t handler_state = SERIAL_ST_IDLE;
	while (buffer_head != buffer_tail) {
		switch (handler_state) {
			case (SERIAL_ST_IDLE):
				switch (serial_rx_buffer[buffer_head]) {
					case (CMD_SEND_DATA): flags |= FLAG_DATA; break;
					case (CMD_HALT_DATA): flags &= ~FLAG_DATA; break;
					case (CMD_SET_LEDS): break;
					case (CMD_TOGGLE_HBT): flags ^= FLAG_HBT; serial_write_byte(CMD_TOGGLE_HBT); break;
					case (CMD_NOP): break; // do nothing
					default:; // shouldn't ever end up here
				}
				break;
			case (SERIAL_ST_LED1H): break;
			case (SERIAL_ST_LED1L): break;
			case (SERIAL_ST_LED2H): break;
			case (SERIAL_ST_LED2L): break;
			case (SERIAL_ST_LED3H): break;
			case (SERIAL_ST_LED3L): break;
			default: ; // shouldn't end up here either
		}

		buffer_head++;
		buffer_head &= BUFFER_INDEX_MASK;
	}
}

/**
 * UART RX interrupt handler
 */
ISR(USART0_RX_vect) {
	// we should probably do some checking here to make sure we aren't overwriting data
	// however, if this happens, we're going to be running into other problems anyways, so meh...
	buffer_tail = (buffer_tail + 1) & BUFFER_INDEX_MASK;
	serial_rx_buffer[buffer_tail] = UDR0;
}

