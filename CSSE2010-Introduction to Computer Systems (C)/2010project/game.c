/*
 * game.c
 *
 * Author: Peter Sutton. Modified by Jiefeng Hou
 */ 

#include "game.h"
#include "ledmatrix.h"
#include "pixel_colour.h"
#include "score.h"
#include "timer0.h"
#include <stdint.h>

///////////////////////////////// Global variables //////////////////////
// frog_row and frog_column store the current position of the frog. Row 
// numbers are from 0 to 7; column numbers are from 0 to 15. 
static int8_t frog_row;
static int8_t frog_column;

// Boolean flag to indicate whether the frog is alive or not
static uint8_t frog_alive;

// Vehicle data - 32 bits in each lane which we loop continuously. A 1
// indicates the presence of a vehicle, 0 is empty.
// Index 0 to 2 corresponds to lanes 1 to 3 respectively. Lanes 1 and 3
// will move to the right; lane 2 will move to the left.
#define LANE_DATA_WIDTH 32	// must be power of 2
static uint32_t lane_data[3] = {
		0b11000011000110001100000110011000,
		0b00110000011000011000001100001100,
		0b00001111000011110000111100001111
};
		
// Log data - 32 bits for each log channel which we loop continuously.
// A 1 indicates the presence of a log, 0 is empty.
// Index 0 to 1 corresponds to rows 5 and 6 respectively. Row 5 will move
// to the left; row 6 will move to the right
#define LOG_DATA_WIDTH 32 // must be power of 2
static uint32_t log_data[2] = {
		0b11110001100111000111100011111000,
		0b11100110111101100001110110011100
};

// Lane positions. The bit position (0 to 31) of the lane_data above that is
// currently in column 0 of the display (left hand side). (Bit position
// 0 is the least significant bit.) For a lane position of N, the display
// will show bits N to N+15 from left to right (wrapping around if N+15 
// exceeds 31). 
static int8_t lane_position[3];

// Log positions. Same principle as lane positions.
static int8_t log_position[2];

// Colours
#define COLOUR_FROG 0xFF // bright yellow
#define COLOUR_DEAD_FROG 0x33 // dim yellow
#define COLOUR_EDGES 0x12 // light orange - for roadside and riverbank
#define COLOUR_WATER 0x00 // black
#define COLOUR_ROAD 0x00 // black
#define COLOUR_LOGS 0x3C // orange
PixelColour vehicle_colours[3] = { COLOUR_RED, COLOUR_GREEN, COLOUR_RED }; // by lane

// Rows
#define START_ROW 0	// row position where the frog starts
#define FIRST_VEHICLE_ROW 1
#define SECOND_VEHICLE_ROW 2
#define THIRD_VEHICLE_ROW 3
#define HALFWAY_ROW 4 // row position where the frog can rest
#define FIRST_RIVER_ROW 5
#define SECOND_RIVER_ROW 6
#define RIVERBANK_ROW 7 // row position where the frog finishes

// River bank pattern. Note that the least significant bit in this
// pattern (RHS) corresponds to column 0 on the display (LHS).
#define RIVERBANK 0b1011011011011011
static uint16_t riverbank;
// riverbank_status is a bit pattern similar to riverbank but will
// only have zeroes where there are unoccupied holes. When this is all 1's
// then the game/level is complete
static uint16_t riverbank_status;


/////////////////////////////// Function Prototypes for Helper Functions ///////
// These functions are defined after the public functions. Comments are with the
// definitions.
static uint8_t frog_alive_at(uint8_t row, uint8_t column);
static void redraw_whole_display(void);
static void redraw_row(uint8_t row);
static void redraw_roadside(uint8_t row);
static void redraw_traffic_lane(uint8_t lane);
static void redraw_river_channel(uint8_t channel);
static void redraw_riverbank(void);
static void redraw_frog(void);
		
/////////////////////////////// Public Functions ///////////////////////////////
// These functions are defined in the same order as declared in game.h

// Reset the game
void init_game(void) {
	// Initial random lane and log positions, Y from 0 to 31
	// seeding a random number
	srand((unsigned)get_clock_ticks());
	lane_position[0] = rand()%31;
	lane_position[1] = rand()%31;
	lane_position[2] = rand()%31;
	log_position[0] = rand()%31;
	log_position[1] = rand()%31;
	
	// Initial riverbank pattern
	riverbank = RIVERBANK;
	riverbank_status = RIVERBANK;
	
	redraw_whole_display();
	
	// Add a frog to the roadside - this will redraw the frog
	put_frog_at_start();
}

// Add a frog to the game
void put_frog_at_start(void) {
	// Initial starting random position of frog (X,0), X from 0 to 15
	frog_row = 0;
	frog_column = rand()%15;
	
	// Frog is initially alive
	frog_alive = 1;
	
	// Show the frog
	redraw_frog();
}

// This function assumes that the frog is not in row 7 (the top row). A frog in row 7 is out
// of the game.
void move_frog_forward(void) {
	// Redraw the row the frog is currently on (this will remove the frog)
	redraw_row(frog_row);
	
	// Check whether this move will cause the frog to die or not
	frog_alive = frog_alive_at(frog_row+1, frog_column);
	
	// Move the frog position forward and show the frog. 
	// We do this whether the frog is alive or not. 
	frog_row++;
	redraw_frog();
	
	// If the frog has ended up successfully in row 7 - add it to the riverbank_status flag
	if(frog_alive && frog_row == RIVERBANK_ROW) {
		riverbank_status |= (1<<frog_column);
	}
}

void move_frog_backward(void) {
	if(frog_row != START_ROW) {
		// Redraw the row the frog is currently on (this will remove the frog)
		redraw_row(frog_row);
		
		// Check whether this move will cause the frog to die or not
		frog_alive = frog_alive_at(frog_row-1, frog_column);
		
		// Move the frog position forward and show the frog. 
		// We do this whether the frog is alive or not. 
		frog_row--;
		redraw_frog();
	}
}

void move_frog_left(void) {
	// Unimplemented
	// Comments to aid implementation:
	// If the frog is already at the left hand side then do nothing (can't move further)
	// Otherwise redraw the row the frog is currently on (i.e. without the frog), check 
	// whether the frog will live or not, update the frog position and redraw the frog.
	if(frog_column != 0) {
		// Redraw the row the frog is currently on (this will remove the frog)
		redraw_row(frog_row);
		
		// Check whether this move will cause the frog to die or not
		frog_alive = frog_alive_at(frog_row, frog_column-1);
		
		// Move the frog position forward and show the frog. 
		// We do this whether the frog is alive or not. 
		frog_column--;
		redraw_frog();
	}
}

void move_frog_right(void) {
	if(frog_column != 15) {
		// Redraw the row the frog is currently on (this will remove the frog)
		redraw_row(frog_row);
		
		// Check whether this move will cause the frog to die or not
		frog_alive = frog_alive_at(frog_row, frog_column+1);
		
		// Move the frog position forward and show the frog. 
		// We do this whether the frog is alive or not. 
		frog_column++;
		redraw_frog();
	}
}

void set_frog_alive(void) {
	frog_alive = 1;
}

void set_frog_die(void) {
	frog_alive = 0;
}

uint8_t get_frog_row(void) {
	return frog_row;
}

uint8_t get_frog_column(void) {
	return frog_column;
}

uint8_t is_riverbank_full(void) {
	return (riverbank_status == 0xFFFF);
}

uint8_t frog_has_reached_riverbank(void) {
	return (frog_row == RIVERBANK_ROW);
}

uint8_t is_frog_alive(void) {
	return frog_alive;
}

// Scroll the given lane of traffic. (lane value must be 0 to 2)
void scroll_lane(uint8_t lane, int8_t direction) {
	uint8_t frog_is_in_this_row = (frog_row == lane + FIRST_VEHICLE_ROW);
	
	// Work out the new lane position.
	// Wrap numbers around if they go out of range
	// A direction of -1 indicates movement to the left which means we
	// start from a higher bit position in column 0
	lane_position[lane] -= direction;
	if(lane_position[lane] < 0) {
		lane_position[lane] = LANE_DATA_WIDTH-1;
	} else if(lane_position[lane] >= LANE_DATA_WIDTH) {
		lane_position[lane] = 0;
	}
	// Update whether the frog will be alive or not. (The frog hasn't moved but
	// it may have been hit by a vehicle.)
	frog_alive = frog_alive_at(frog_row, frog_column);
	
	// Show the lane on the display
	redraw_traffic_lane(lane);
	
	// If the frog is in this row, show it
	if(frog_is_in_this_row) {
		redraw_frog();
	}
}


void scroll_log_channel(uint8_t channel, int8_t direction) {
	uint8_t frog_is_in_this_row = (frog_row == channel + FIRST_RIVER_ROW);
	// Note, if the frog is in this row then it will be on a log
	
	if(frog_is_in_this_row) {
		// Check if they're going to hit the edge - don't let the frog
		// go beyond the edge
		if(direction == 1 && frog_column == 15) {
			frog_alive = 0; // hit right edge
		} else if(direction == -1 && frog_column == 0) {
			frog_alive = 0; // hit left edge
		} else {
			// Move the frog with the log - they're not going to hit the edge
			frog_column += direction;
		}
	}
		
	// Work out the new log position.
	// Wrap numbers around if they go out of range
	log_position[channel] -= direction;
	if(log_position[channel] < 0) {
		log_position[channel] = LOG_DATA_WIDTH-1;
	} else if(log_position[channel] >= LOG_DATA_WIDTH) {
		log_position[channel] = 0;
	}
		
	// Work out the log data to send to the display
	redraw_river_channel(channel);
		
	// If the frog is in this row, put them on the log
	if(frog_is_in_this_row) {
		redraw_frog();
	}
}

/////////////////////////////// Private (Helper) Functions /////////////////////

// Return 1 if the frog can jump to the given position (i.e. it is not occupied by 
// a vehicle), or, if in the river, then it IS occupied by a log, or, if the final
// riverbank then that space is free.
static uint8_t frog_alive_at(uint8_t row, uint8_t column) {
	uint8_t lane, channel, bit_position;
	switch(row) {
		case 0: // always safe
		case 4: // always safe
			return 1;
			break;
		case 1:
		case 2:
		case 3:
			lane = row - 1;
			bit_position = lane_position[lane] + column;
			if(bit_position >= LANE_DATA_WIDTH) {
				bit_position -= LANE_DATA_WIDTH;
			}
			return !((lane_data[lane] >> bit_position) & 1);
			break;
		case 5:
		case 6:
			channel = row - 5;
			bit_position = log_position[channel] + column;
			if(bit_position >= LOG_DATA_WIDTH) {
				bit_position -= LOG_DATA_WIDTH;
			}
			return (log_data[channel] >> bit_position) & 1;
			break;
		case 7:
			return !((riverbank_status >> column) & 1);
			break;	
	}
	// Should never get here (unless row invalid)
	return 0;	
}

// Redraw the rows on the game field. The frog is not redrawn.
static void redraw_whole_display(void) {
	// Clear the display
	ledmatrix_clear();
	
	// Start with the starting and halfway rows
	redraw_roadside(START_ROW);
	redraw_roadside(HALFWAY_ROW);

	// Redraw traffic lanes
	for(uint8_t lane=0; lane<=2; lane++) {
		redraw_traffic_lane(lane);
	}
	// Redraw river
	for(uint8_t channel=0; channel<=1; channel++) {
		redraw_river_channel(channel);
	}
	// Redraw riverbank
	redraw_riverbank();
}

// Redraw the row with the given number (0 to 7). The frog is not redrawn.
static void redraw_row(uint8_t row) {	
	// Remove frog from current position (we need to update the display
	// so it shows the right colour pixel in its place). We know the frog
	// must be either on a road edge, on the road or on a log.
	switch(row) {
		case START_ROW:
		case HALFWAY_ROW:
			redraw_roadside(row);
			break;
		case FIRST_VEHICLE_ROW:
		case SECOND_VEHICLE_ROW:
		case THIRD_VEHICLE_ROW:
			redraw_traffic_lane(row-1);
			break;
		case FIRST_RIVER_ROW:
		case SECOND_RIVER_ROW:
			redraw_river_channel(row-5);
			break;
		case RIVERBANK_ROW:
			redraw_riverbank();
			break;
		default:
			// Invalid row - ignore
			break;
	}
}


// Redraw the given roadside row (0 or 4). The frog is not redrawn.
static void redraw_roadside(uint8_t row) {
	MatrixRow row_display_data;
	uint8_t i;
	for(i=0;i<=15;i++) {
		row_display_data[i] = COLOUR_EDGES;
	}
	ledmatrix_update_row(row, row_display_data);
}

// Redraw the given traffic lane (0, 1, 2). The frog is not redrawn.
static void redraw_traffic_lane(uint8_t lane) {
	MatrixRow row_display_data;
	uint8_t i;
	uint8_t bit_position = lane_position[lane];
	for(i=0; i<=15; i++) {
		if((lane_data[lane] >> bit_position) & 1) {
			row_display_data[i] = vehicle_colours[lane];
			} else {
			row_display_data[i] = COLOUR_ROAD;
		}
		bit_position++;
		if(bit_position >= LANE_DATA_WIDTH) {
			// Wrap around in our lane data
			bit_position = 0;
		}
	}
	ledmatrix_update_row(lane+FIRST_VEHICLE_ROW, row_display_data);
}

// Redraw the given river channel (0 or 1). The frog is not redrawn.
static void redraw_river_channel(uint8_t channel) {
	MatrixRow row_display_data;
	uint8_t i;
	uint8_t bit_position = log_position[channel];
	for(i=0; i<=15; i++) {
		if((log_data[channel] >> bit_position) & 1) {
			row_display_data[i] = COLOUR_LOGS;
			} else {
			row_display_data[i] = COLOUR_WATER;
		}
		bit_position++;
		if(bit_position >= LOG_DATA_WIDTH) {
			bit_position = 0;
		}
	}
	ledmatrix_update_row(channel+FIRST_RIVER_ROW, row_display_data);
}

// Redraw the riverbank (top row). Previous frogs which have made it to a hole
// at the top are shown.
static void redraw_riverbank(void) {
	MatrixRow row_display_data;
	uint8_t i;
	// Blank out spaces in our rowdata where there are holes in the riverbank
	for(i=0; i<= 15; i++) {
		if((riverbank >> i) & 1) {
			// Riverbank edge
			row_display_data[i] = COLOUR_EDGES;
		} else if ((riverbank_status >> i) & 1) {
			// Frog occupying a hole
			row_display_data[i] = COLOUR_FROG;
		} else {
			// Empty hole
			row_display_data[i] = 0;
		}
	}
	// Output our riverbank to the display
	ledmatrix_update_row(RIVERBANK_ROW, row_display_data);
}

// Redraw the frog in its current position.
static void redraw_frog(void) {
	if(frog_alive) {
		ledmatrix_update_pixel(frog_column, frog_row, COLOUR_FROG);
	} else {
		ledmatrix_update_pixel(frog_column, frog_row, COLOUR_DEAD_FROG);
	}
}

void clear_frog(void) {
	ledmatrix_update_pixel(frog_column, frog_row, COLOUR_DEAD_FROG);
}