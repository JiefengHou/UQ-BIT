/*
 * terminalio.c
 *
 * Author: Peter Sutton. Modified by Jiefeng Hou
 */

#include <stdio.h>
#include <stdint.h>

#include <avr/pgmspace.h>


void move_cursor(int x, int y) {
    printf_P(PSTR("\x1b[%d;%dH"), y, x);
}

void normal_display_mode(void) {
	printf_P(PSTR("\x1b[0m"));
}

void reverse_video(void) {
	printf_P(PSTR("\x1b[7m"));
}

void clear_terminal(void) {
	printf_P(PSTR("\x1b[2J"));
}

void clear_to_end_of_line(void) {
	printf_P(PSTR("\x1b[K"));
}

void set_display_attribute(int8_t parameter) {
	printf_P(PSTR("\x1b[%dm"), parameter);
}

void draw_horizontal_line(int y, int startx, int endx) {
	int i;
	move_cursor(startx, y);
	reverse_video();
	for(i=startx; i <= endx; i++) {
		printf(" ");	/* No need to use printf_P - printing 
						 * a single character gets optimised
						 * to a putchar call 
						 */
	}
	normal_display_mode();
}

void draw_vertical_line(int x, int starty, int endy) {
	int i;
	move_cursor(x, starty);
	reverse_video();
	for(i=starty; i < endy; i++) {
		printf(" ");
		/* Move down one and back to the left one */
		printf_P(PSTR("\x1b[B\x1b[D"));
	}
	printf(" ");
	normal_display_mode();
}


		
		
