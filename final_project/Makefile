################################################################################
#  = Makefile
#  = ECE 4760 Final Project
#  = Copyright 2012 Patrick Dear, Mark Bunney 
#
# ========================================================================
#  This program is free software: you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program.  If not, see <http://www.gnu.org/licenses/>.
################################################################################

CC = avr-gcc
OBJDUMP = avr-objdump
CFLAGS = -Wall -Os -mmcu=atmega644 -DF_CPU=16000000
SRCS = main.c adc.c serial.c
OBJS = $(SRCS:.c=.o)
TARGET = final_project
DISASM = $(TARGET).dis
HEX = $(TARGET).hex
VPATH  = src

PORT = /dev/tty.PL2303-*

.PHONY: all clean dis flash hex

all: $(TARGET)

$(OBJS): $(SRCS)
	$(CC) -c $^ $(CFLAGS)

$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) -o $@ $^

$(DISASM): $(TARGET)
	$(OBJDUMP) -d  $^ > $@

dis: $(DISASM)

hex: $(TARGET)
	avr-objcopy -O ihex $(TARGET) $(HEX)

flash: hex
	avrdude -P $(PORT) -p m644p -c STK500 -U flash:w:$(HEX)

clean:
	rm -rf $(OBJS) $(TARGET) $(DISASM) $(HEX)

