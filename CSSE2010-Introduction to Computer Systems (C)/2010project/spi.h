/*
 * spi.h
 *
 * Author: Peter Sutton. Modified by Jiefeng Hou
 */ 

#ifndef SPI_H_
#define SPI_H_

// Set up SPI communication as a master.
// clockdivider should be one of 2,4,8,16,32,64,128
void spi_setup_master(uint8_t clockdivider);

// Send and receive an SPI byte. This function will take at least 8 
// cyles of the divided clock
uint8_t spi_send_byte(uint8_t byte);

#endif /* SPI_H_ */