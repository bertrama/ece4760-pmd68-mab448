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

#include <stdint.h>
#include <avr/io.h>
#include "adc.h"
#include "pins.h"

// macros
#define cs_set(x,y) (x |= (1<<(y)))
#define cs_clr(x,y) (x &= ~(1<<(y)))

/**
 * Get samples from specified ADC chip
 */
void sample_adc(uint8_t * result_buffer) {
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

	// Initialize CS pins
	DDR_ADC0 |= (1<<PIN_ADC0);
	DDR_ADC1 |= (1<<PIN_ADC1);
	DDR_ADC2 |= (1<<PIN_ADC2);
	DDR_ADC3 |= (1<<PIN_ADC3);
	DDR_ADC4 |= (1<<PIN_ADC4);
	DDR_ADC5 |= (1<<PIN_ADC5);
	DDR_ADC6 |= (1<<PIN_ADC6);
	DDR_ADC7 |= (1<<PIN_ADC7);
	cs_set(PORT_ADC0,PIN_ADC0);
	cs_set(PORT_ADC1,PIN_ADC1);
	cs_set(PORT_ADC2,PIN_ADC2);
	cs_set(PORT_ADC3,PIN_ADC3);
	cs_set(PORT_ADC4,PIN_ADC4);
	cs_set(PORT_ADC5,PIN_ADC5);
	cs_set(PORT_ADC6,PIN_ADC6);
	cs_set(PORT_ADC7,PIN_ADC7);

	// Get calibration values




}

