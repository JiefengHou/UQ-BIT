/*
 * ledmatrix.h
 *
 * Author: Peter Sutton. Modified by Jiefeng Hou
 */ 


#ifndef LEDMATRIX_H_
#define LEDMATRIX_H_

#include <stdint.h>
#include "pixel_colour.h"

// The matrix has 16 columns (x ranges from 0 to 15, left to right) and 
// 8 rows (y ranges from 0 to 7, bottom to top)
#define MATRIX_NUM_COLUMNS 16
#define MATRIX_NUM_ROWS 8

// Data types which can be used to store display information
typedef PixelColour MatrixData[MATRIX_NUM_COLUMNS][MATRIX_NUM_ROWS];
typedef PixelColour MatrixRow[MATRIX_NUM_COLUMNS];
typedef PixelColour MatrixColumn[MATRIX_NUM_ROWS];

// Setup SPI communication with the LED matrix
void ledmatrix_setup(void);

// Functions to update the display
void ledmatrix_update_all(MatrixData data);
void ledmatrix_update_pixel(uint8_t x, uint8_t y, PixelColour pixel);
void ledmatrix_update_row(uint8_t y, MatrixRow row);
void ledmatrix_update_column(uint8_t x, MatrixColumn col);
void ledmatrix_shift_display_left(void);
void ledmatrix_shift_display_right(void);
void ledmatrix_shift_display_up(void);
void ledmatrix_shift_display_down(void);
void ledmatrix_clear(void);

#endif /* LEDMATRIX_H_ */