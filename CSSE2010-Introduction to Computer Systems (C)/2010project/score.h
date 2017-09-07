/*
 * score.h
 * 
 * Author: Peter Sutton. Modified by Jiefeng Hou
 */

#ifndef SCORE_H_
#define SCORE_H_

#include <stdint.h>

void init_score(void);
void add_to_score(uint16_t value);
uint16_t get_score(void);
uint16_t get_highscore(void);
int set_highscore(uint16_t maxvalue, uint16_t value);

#endif /* SCORE_H_ */