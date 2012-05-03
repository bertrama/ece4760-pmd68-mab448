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
#define PORT_ADC0 PORTB
#define PORT_ADC1 PORTB
#define PORT_ADC2 PORTB
#define PORT_ADC3 PORTB
#define PORT_MOSI PORTB
#define PORT_MISO PORTB
#define PORT_SCK  PORTB
#define DDR_ADC0  DDRB
#define DDR_ADC1  DDRB
#define DDR_ADC2  DDRB
#define DDR_ADC3  DDRB
#define DDR_MOSI  DDRB
#define DDR_MISO  DDRB
#define DDR_SCK   DDRB
#define DDR_SPI   DDRB
#define PIN_ADC0  0
#define PIN_ADC1  1
#define PIN_ADC2  2
#define PIN_ADC3  3
#define PIN_MOSI  5 // Darker green
#define PIN_MISO  6 // Lighter green
#define PIN_SCK   7 // Blue wire

// LED driver pins
#define DDR_BLANK1   DDRA
#define PORT_BLANK1  PORTA
#define PIN_BLANK1   PINA
#define BLANK1       PA4
#define DDR_SIN1     DDRA
#define PORT_SIN1    PORTA
#define PIN_SIN1     PINA
#define SIN1         PA0

#define DDR_BLANK2   DDRA
#define PORT_BLANK2  PORTA
#define PIN_BLANK2   PINA
#define BLANK2       PA5
#define DDR_SIN2     DDRA
#define PORT_SIN2    PORTA
#define PIN_SIN2     PINA
#define SIN2         PA1

#define DDR_BLANK3   DDRA
#define PORT_BLANK3  PORTA
#define PIN_BLANK3   PINA
#define BLANK3       PA6
#define DDR_SIN3     DDRA
#define PORT_SIN3    PORTA
#define PIN_SIN3     PINA
#define SIN3         PA2

#define DDR_LED_CLK  DDRD
#define PORT_LED_CLK PORTD
#define PIN_LED_CLK  PIND
#define LED_CLK      PD6

#define DDR_XTAL     DDRA
#define PORT_XTAL    PORTA
#define PIN_XTAL     PINA
#define XTAL         PA3
#define DDR_GS_CLK   DDRD
#define PORT_GS_CLK  PORTD
#define PIN_GS_CLK   PIND
#define GS_CLK       PD7
#define DDR_VPRG     DDRA
#define PORT_VPRG    PORTA
#define PIN_VPRG     PINA
#define VPRG         PA7



#endif

