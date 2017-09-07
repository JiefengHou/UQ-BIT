/*
 * ledmatrix.c
 *
 * Author: Peter Sutton. Modified by Jiefeng Hou
 * 
 * See the LED matrix Reference for details of the SPI commands used.
 */ 

#include <avr/io.h>
#include "ledmatrix.h"
#include "spi.h"

#define CMD_UPDATE_ALL 0x00
#define CMD_UPDATE_PIXEL 0x01
#define CMD_UPDATE_ROW 0x02
#define CMD_UPDATE_COL 0x03
#define CMD_SHIFT_DISPLAY 0x04
#define CMD_CLEAR_SCREEN 0x0F

void ledmatrix_setup(void) {
	// Setup SPI - we divide the clock by 128.
	// (This speed guarantees the SPI buffer will never overflow.)
	spi_setup_master(128);
}

void ledmatrix_update_all(MatrixData data) {
	(void)spi_send_byte(CMD_UPDATE_ALL);
	for(uint8_t y=0; y<MATRIX_NUM_ROWS; y++) {
		for(uint8_t x=0; x<MATRIX_NUM_COLUMNS; x++) {
			(void)spi_send_byte(data[x][y]);
		}
	}
}

void ledmatrix_update_pixel(uint8_t x, uint8_t y, PixelColour pixel) {
	(void)spi_send_byte(CMD_UPDATE_PIXEL);
	(void)spi_send_byte( ((y & 0x07)<<4) | (x & 0x0F));
	(void)spi_send_byte(pixel);
}

void ledmatrix_update_row(uint8_t y, MatrixRow row) {
	(void)spi_send_byte(CMD_UPDATE_ROW);
	(void)spi_send_byte(y & 0x07);	// row number
	for(uint8_t x = 0; x<MATRIX_NUM_COLUMNS; x++) {
		(void)spi_send_byte(row[x]);
	}
}

void ledmatrix_update_column(uint8_t x, MatrixColumn col) {
	(void)spi_send_byte(CMD_UPDATE_COL);
	(void)spi_send_byte(x & 0x0F); // column number
	for(uint8_t y = 0; y<MATRIX_NUM_ROWS; y++) {
		(void)spi_send_byte(col[y]);
	}
}

void ledmatrix_shift_display_left(void) {
	(void)spi_send_byte(CMD_SHIFT_DISPLAY);
	(void)spi_send_byte(0x02);
}

void ledmatrix_shift_display_right(void) {
	(void)spi_send_byte(CMD_SHIFT_DISPLAY);
	(void)spi_send_byte(0x01);
}

void ledmatrix_shift_display_up(void) {
	(void)spi_send_byte(CMD_SHIFT_DISPLAY);
	(void)spi_send_byte(0x08);
}

void ledmatrix_shift_display_down(void) {
	(void)spi_send_byte(CMD_SHIFT_DISPLAY);
	(void)spi_send_byte(0x04);
}

void ledmatrix_clear(void) {
	(void)spi_send_byte(CMD_CLEAR_SCREEN);
}