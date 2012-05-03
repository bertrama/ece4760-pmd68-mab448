/************************************************************************\
  = common.h
  = ECE 4760 Final Project
  = Copyright 2012 Patrick Dear, Mark Bunney 

  Contains definitions related to entire project

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

#ifndef _COMMON_H_
#define _COMMON_H_

// flags
#define FLAG_HBT  0x01
#define FLAG_DATA 0x02

#define NUM_ADC_FRAMES 16
#define NUM_ADC_FRAMES_MASK 0x0F

#define SAMPLE_INTERVAL 19 // This value plus 1 == sample period in ms

#endif

