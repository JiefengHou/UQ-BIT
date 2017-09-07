/*
 * serialio.h
 *
 * Author: Peter Sutton. Modified by Jiefeng Hou
 * 
 * Module to allow standard input/output routines to be used via 
 * serial port 0. The init_serial_stdio() method must be called before
 * any standard IO methods (e.g. printf). We use interrupt-based serial
 * IO and a circular buffer to store output messages. (This allows us 
 * to print many characters at once to the buffer and have them 
 * output by the UART as speed permits.) Interrupts must be enabled 
 * globally for this module to work (after init_serial_stdio() is called).
 *
 */

#ifndef SERIALIO_H_
#define SERIALIO_H_

#include <stdint.h>

/* Initialise serial IO using the UART. baudrate specifies the desired
 * baudrate (e.g. 19200) and echo determines whether incoming characters
 * are echoed back to the UART output as they are received (zero means no
 * echo, non-zero means echo)
 */
void init_serial_stdio(long baudrate, int8_t echo);

/* Test if input is available from the serial port. Return 0 if not,
 * non-zero otherwise.
 */
int8_t serial_input_available(void);

/* Discard any input waiting to be read from the serial port. (Characters may
 * have been typed when we didn't want them - clear them.
 */
void clear_serial_input_buffer(void);

#endif /* SERIALIO_H_ */