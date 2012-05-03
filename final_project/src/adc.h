/************************************************************************\
  = adc.h
  = ECE 4760 Final Project
  = Copyright 2012 Patrick Dear, Mark Bunney 

  Contains definitions related to sampling with TI TLC1543/TLC1542 ADCs

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

#ifndef _ADC_H_
#define _ADC_H_

// definitions
#define NUM_ADCS 4
#define CHANNELS_PER_ADC 9

typedef struct _sample_frame_t {
	uint8_t adc0_data[CHANNELS_PER_ADC];
	uint8_t adc1_data[CHANNELS_PER_ADC];
	uint8_t adc2_data[CHANNELS_PER_ADC];
	uint8_t adc3_data[CHANNELS_PER_ADC];
} sample_frame_t;

// Function prototypes
void adc_init(void);
void adc_get_samples(uint8_t * buffer, uint8_t adc_no);
void adc_get_frame(sample_frame_t *);

#endif

