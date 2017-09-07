/*
 * score.c
 *
 * Written by Peter Sutton. Modified by Jiefeng Hou
 */

#include "score.h"

uint16_t score;
uint16_t highsocre = 0;

void init_score(void) {
	score = 0;
}

void add_to_score(uint16_t value) {
	score += value;
}

uint16_t get_score(void) {
	return score;
}

uint16_t get_highscore(void) {
	return highsocre;
}

int set_highscore(uint16_t maxvalue, uint16_t value) {
	if (maxvalue < value) {
		highsocre = value;
		return 1;
	}
	return 0;
}
