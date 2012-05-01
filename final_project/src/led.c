/************************************************************************\
  = led.c
  = ECE 4760 Final Project
  = Copyright 2012 Patrick Dear, Mark Bunney 

  Contains functions related to LED driver manipulation

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
#include <stdint.h>
#include "led.h"
#include "pins.h"

// Macros 
#define toggle_BLANK1() PORT_BLANK1 ^= (1<<BLANK1)
#define GS_CLK1_high()  PORT_GS_CLK1 |= (1<<GS_CLK1);
#define GS_CLK1_low()   PORT_GS_CLK1 &= ~(1<<GS_CLK1);

#define VPRG1_high()    PORT_VPRG1 |= (1<<VPRG1);
#define VPRG1_low()     PORT_VPRG1 &= ~(1<<VPRG1);


#define BLANK1_high()   PORT_BLANK1 |= (1<<BLANK1)
#define BLANK1_low()    PORT_BLANK1 &= ~(1<<BLANK1)
#define XTAL1_high()    PORT_XTAL1 |= (1<<XTAL1)
#define XTAL1_low()     PORT_XTAL1 &= ~(1<<XTAL1)
#define SIN1_high()     PORT_SIN1 |= (1<<SIN1)
#define SIN1_low()      PORT_SIN1 &= ~(1<<SIN1)
#define SIN1_toggle()   PORT_SIN1 ^= (1<<SIN1)

#define LED_CLK_high() PORT_LED_CLK |= (1<<LED_CLK)
#define LED_CLK_low()  PORT_LED_CLK &= ~(1<<LED_CLK)

uint16_t DC1 = 0xBFFF;	//0xBFFF turns off just red led TODO: remove
uint16_t DC2 = 0xFFFF;
uint16_t DC3 = 0xFFFF;


void led_driver_set(uint16_t data1, uint16_t data2, uint16_t data3){
	uint16_t i = 0;

	VPRG1_high(); //program dot corrections
	
	for (i = 1; i != 0; i <<= 1){
		//shift out in parallel to the three chips
		if (data1 & i){
			SIN1_high();
		}
		else {
			SIN1_low();
		}
		//toggle 6 times to set dot correction to 0b111111 or 0b000000
		LED_CLK_high();	
		LED_CLK_low();

		LED_CLK_high();	
		LED_CLK_low();

		LED_CLK_high();	
		LED_CLK_low();

		LED_CLK_high();	
		LED_CLK_low();

		LED_CLK_high();	
		LED_CLK_low();

		LED_CLK_high();	
		LED_CLK_low();


	//	data1 <<= 1; //move to next port
		data2 <<= 1; //move to next port
		data3 <<= 1; //move to next port

	}
	SIN1_low(); //return to 0
	//strobe latch to shift in 96 bits of new data
	XTAL1_high();// XTAL2_high();	XTAL3_high();
	XTAL1_low();//  XTAL2_low();	XTAL3_low();
}




void led_driver_init(void) {
	uint8_t i;
	// Setup pin directions/initial values
	DDR_BLANK1 |= 1<<BLANK1;
	PORT_BLANK1 &= ~(1<<BLANK1);

	DDR_SIN1 |= 1<<SIN1;
	PORT_SIN1 &= ~(1<<SIN1);
	
	DDR_LED_CLK |= 1<<LED_CLK;
	PORT_LED_CLK &= ~(1<<LED_CLK);
	
	DDR_XTAL1 |= 1<<XTAL1;
	PORT_XTAL1 &= ~(1<<XTAL1);

	DDR_GS_CLK1 |= 1<<GS_CLK1;		
	PORT_GS_CLK1 &= ~(1<<GS_CLK1);

	// Initialize
	SIN1_high(); //want this at max, but really any value greater than 0 works
	BLANK1_high(); //turn off all outputs
	VPRG1_low(); //program GS PWM values
	for	(i=0; i<192; i++){ //12 bits * 16 outputs = 192 bits to shift
		LED_CLK_high();	
		LED_CLK_low();
	}
	SIN1_low();
	//strobe latch to shift in 192 bits of new data
	XTAL1_high();// XTAL2_high();	XTAL3_high();
	XTAL1_low();//  XTAL2_low();	XTAL3_low();
	
	//everything on
	BLANK1_low();
//	BLANK2_low();
//	BLANK3_low();

	GS_CLK1_high();
	GS_CLK1_low();
}

