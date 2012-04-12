/************************************************************************\
  = pins.h
  = ECE 4760 Final Project
  = Copyright 2012 Patrick Dear, Mark Bunney 

  Contains pin definitions for the ATMega644 microcontroller

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

#ifndef _PINS_H_
#define _PINS_H_

// Ensure avr/io.h is included
#include <avr/io.h>

// ADC/SPI ports/pins
#define PORT_ADC0 PORTA
#define PORT_ADC1 PORTA
#define PORT_ADC2 PORTA
#define PORT_ADC3 PORTA
#define PORT_ADC4 PORTA
#define PORT_ADC5 PORTA
#define PORT_ADC6 PORTA
#define PORT_ADC7 PORTA
#define PORT_MOSI PORTB
#define PORT_MISO PORTB
#define PORT_SCK  PORTB
#define DDR_ADC0  DDRA
#define DDR_ADC1  DDRA
#define DDR_ADC2  DDRA
#define DDR_ADC3  DDRA
#define DDR_ADC4  DDRA
#define DDR_ADC5  DDRA
#define DDR_ADC6  DDRA
#define DDR_ADC7  DDRA
#define DDR_MOSI  DDRB
#define DDR_MISO  DDRB
#define DDR_SCK   DDRB
#define DDR_SPI   DDRB
#define PIN_ADC0  0
#define PIN_ADC1  1
#define PIN_ADC2  2
#define PIN_ADC3  3
#define PIN_ADC4  4
#define PIN_ADC5  5
#define PIN_ADC6  6
#define PIN_ADC7  7
#define PIN_MOSI  5
#define PIN_MISO  6
#define PIN_SCK   7

#endif

