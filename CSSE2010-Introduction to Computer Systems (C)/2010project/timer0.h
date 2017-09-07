/*
 * timer0.h
 *
 * Author: Peter Sutton. Modified by Jiefeng Hou
 *
 * We set up timer 0 to give us an interrupt
 * every millisecond. Tasks that have to occur
 * regularly (every millisecond or few) can be added 
 * to the interrupt handler (in timer0.c) or can
 * be added to the main event loop that checks the
 * clock tick value. This value (32 bits) can be 
 * obtained using the get_clock_ticks() function.
 * (Any tasks undertaken in the interrupt handler
 * should be kept short so that we don't run the 
 * risk of missing an interrupt in future.)
 */

#ifndef TIMER0_H_
#define TIMER0_H_

#include <stdint.h>

/* Set up our timer to give us an interrupt every millisecond
 * and update our time reference.
 */
void init_timer0(void);

/* Return the current clock tick value - milliseconds since the timer was
 * initialised.
 */
uint32_t get_clock_ticks(void);

#endif