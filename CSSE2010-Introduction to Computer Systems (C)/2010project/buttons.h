/*
 * buttons.h
 *
 * Author: Peter Sutton. Modified by Jiefeng Hou
 *
 * We assume four push buttons (B0 to B3) are connected to pins B0 to B3. We configure
 * pin change interrupts on these pins.
 */ 


#ifndef BUTTONS_H_
#define BUTTONS_H_

#include <stdint.h>

/* Set up pin change interrupts on pins B0 to B3.
 * It is assumed that global interrupts are off when this function is called
 * and are enabled sometime after this function is called.
 */
void init_button_interrupts(void);

/* Return the last button pushed (0 to 3) or -1 if there are no
 * button pushes to return. (A small queue of button pushes is
 * kept. This function should be called frequently enough to
 * ensure the queue does not overflow. Excess button pushes are
 * discarded.)
 */

int8_t button_pushed(void);


#endif /* BUTTONS_H_ */