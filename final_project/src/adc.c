/************************************************************************\
  = adc.c
  = ECE 4760 Final Project
  = Copyright 2012 Patrick Dear, Mark Bunney 

  Contains functions related to sampling with TI TLC1543/TLC1542 ADCs

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
#include "adc.h"

/**
 * Get samples from specified ADC chip
 */
void sample_adc(uint8_t * result_buffer, ) {
	;
}

/**
 * Initilize the ATMega SPI module + ADCs.
 *
 * The maximum serial clock frequency is 2.1 MHz
 */
void adc_init(void) {
	// Initialize SPI module
	SPCR = (1<<SPE)|(1<<MSTR)|(1<<SPR0); // Assuming 16MHz clock, use divide by 16

	// Get calibration values
}

