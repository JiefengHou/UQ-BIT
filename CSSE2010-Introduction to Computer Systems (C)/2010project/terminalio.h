/*
 * terminalio.h
 *
 * Author: Peter Sutton. Modified by Jiefeng Hou
 *
 * Functions for interacting with the terminal. These should be used
 * to encapsulate all sending of escape sequences.
 */

#ifndef TERMINAL_IO_H_
#define TERMINAL_IO_H_

#include <stdint.h>

/*
 * x and y are measured relative to the top left of the screen. First
 * column is 1, first row is 1.
 *
 * The display parameter is a number between 0 and 47 (not all values
 * are valid).
 */

void move_cursor(int x, int y);
void normal_display_mode(void);
void reverse_video(void);
void clear_terminal(void);
void clear_to_end_of_line(void);
void set_display_attribute(int8_t parameter);

/*
 * Draw a reverse video line on the terminal. startx must be <= endx.
 * starty must be <= endy
 */
void draw_horizontal_line(int y, int startx, int endx);
void draw_vertical_line(int x, int starty, int endy);

#endif /* TERMINAL_IO_H */