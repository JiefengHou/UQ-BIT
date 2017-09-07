/*
 * scrolling_char_display.h
 *
 * Author: Peter Sutton. Modified by Jiefeng Hou
 */

#ifndef SCROLLING_CHAR_DISPLAY_H_
#define SCROLLING_CHAR_DISPLAY_H_

#include <stdint.h>
#include "pixel_colour.h"

/* Set the colour of all text to be scrolled. If this is called
 * whilst text is scrolling then the colour may change part way
 * through a character. Default colour is red.
 */
void set_text_colour(PixelColour colour);

void init_scrolling_display(void);

/* Sets the text to be displayed. The message will only
 * be displayed after the current message (if any). (Only
 * one message can be queued for display (i.e. to be 
 * displayed after the current message) - from the last
 * call to this function.) Note that this string is not 
 * copied, so it is important that this string not change
 * after this function is called while the string is still
 * being displayed.
 */
void set_scrolling_display_text(char* string);

/* Scroll the display. Should be called whenever the display
 * is to be scrolled one pixel to the left. It is recommended that
 * this function NOT be called from an interrupt service routine as
 * it will wait for SPI communication to be finished before returning. 
 * This could take over 1ms.
 * Returns 1 while a message is still scrolling, 0 when done.
 */
uint8_t scroll_display(void);
	
#endif /* SCROLLING_CHAR_DISPLAY_H_ */