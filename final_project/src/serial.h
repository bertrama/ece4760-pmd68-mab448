/************************************************************************\
  = serial.h
  = ECE 4760 Final Project
  = Copyright 2012 Patrick Dear, Mark Bunney 

  Contains definitions related to UART communications

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

#ifndef _SERIAL_H_
#define _SERIAL_H_

// required includes
#include <stdint.h>
#include "adc.h"

// baud rate
#define UART_BAUD 115200

// serial commands
#define CMD_SEND_DATA 0xAA
#define CMD_HALT_DATA 0xBB
#define CMD_SET_LEDS  0xCC

// function prototypes
void serial_init(void);
void serial_write_str(char *, uint16_t);
void serial_write_byte(char);
void serial_write_frame(sample_frame_t *);

#endif

