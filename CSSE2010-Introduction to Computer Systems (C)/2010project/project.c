/*
 * FroggerProject.c
 *
 * Main file
 *
 * Author: Peter Sutton. Modified by Jiefeng Hou
 */ 

#include <avr/io.h>
#include <avr/interrupt.h>
#include <avr/pgmspace.h>
#include <avr/eeprom.h> 
#include <stdio.h>

#include "ledmatrix.h"
#include "scrolling_char_display.h"
#include "buttons.h"
#include "serialio.h"
#include "terminalio.h"
#include "score.h"
#include "timer0.h"
#include "game.h"
#include "joystick.h"

#define F_CPU 8000000L
#include <util/delay.h>

// Function prototypes - these are defined below (after main()) in the order
// given here
void initialise_hardware(void);
void splash_screen(void);
void new_game(void);
void play_game(void);
void handle_game_over(void);
void show_top5(void);
void init_top5(void); 

// ASCII code for Escape character
#define ESCAPE_CHAR 27

/////////////////////////////// main //////////////////////////////////
int main(void) {
	// Setup hardware and call backs. This will turn on 
	// interrupts.
	initialise_hardware();
	//setup the top5
	init_top5();
	//setup joystick
	init_joystick();
	// Show the splash screen message. Returns when display
	// is complete
	splash_screen();
	
	while(1) {
		new_game();
		play_game();
		handle_game_over();
	}
}

void initialise_hardware(void) {
	ledmatrix_setup();
	init_button_interrupts();
	// Setup serial port for 19200 baud communication with no echo
	// of incoming characters
	init_serial_stdio(19200,0);
	
	init_timer0();
	
	// Turn on global interrupts
	sei();
}

void splash_screen(void) {
	// Clear terminal screen and output a message
	clear_terminal();
	// show top5 in terminal
	show_top5();
	move_cursor(10,10);
	printf_P(PSTR("Frogger"));
	move_cursor(10,12);
	printf_P(PSTR("CSSE2010/7201 project by Jiefeng Hou"));
	
	// Output the scrolling message to the LED matrix
	// and wait for a push button to be pushed.
	ledmatrix_clear();
	set_text_colour(COLOUR_ORANGE);
	init_scrolling_display();
	while(1) {
		// student number scroll on the LED display
		set_scrolling_display_text("43034002");
		// Scroll the message until it has scrolled off the 
		// display or a button is pushed or serial input is received
		while(scroll_display()) {
			_delay_ms(150);
			if(button_pushed() != -1) {
				return;
			}
		}
	}
}

void new_game(void) {
	// Initialise the game and display
	init_game();
	
	// Clear the serial terminal
	clear_terminal();
	
	// Initialise the score
	init_score();
	
	// Clear a button push or serial input if any are waiting
	// (The cast to void means the return value is ignored.)
	(void)button_pushed();
	clear_serial_input_buffer();
}

void play_game(void) {
	uint32_t current_time, last_move_time[5], count_time, button_repeat_time, joystick_time;
	int8_t button;
	char serial_input, escape_sequence_char, Direction;
	uint8_t characters_into_escape_sequence = 0;
	//set level n = 1
	uint8_t n = 1;
	char level[10];
	//set a scroll time 
	uint16_t scroll_time;
	uint16_t current_score;
	//set chance = 3 at the beginning, and set portA(pin5,6,7) to connect LD0 TO LD2
	uint8_t chances = 3;
	DDRA = 0xF0;
	PORTA = 0xE0;
	//set DDRC to output seven-segment
	DDRC = 0xFF;
	//set DDRD pin2 to connect cc
	DDRD = (1<<2);
	//set count time is 20
	uint8_t t = 20;
	//set seven segment data and cc
	uint8_t seven_seg_data[10] = {63,6,91,79,102,109,125,7,127,111};
	uint8_t seven_seg_cc = 0;
	//set joystick data
	uint8_t mid_x = 1, mid_y = 1, a=1;
	uint16_t value, last_value;
	uint8_t x_or_y = 0;	



	//output high score in terminal
    move_cursor(10,12);
    printf("High Score: %u",get_highscore());

	//output score in terminal
	move_cursor(10,14);
	printf("Score: %u",get_score());

	//output chances in terminal
    move_cursor(10,16);
    printf("Lives: %u",chances);
	
	// Get the current time and remember this as the last time the vehicles
	// and logs were moved.
	current_time = get_clock_ticks();
	for(int i=0;i<5;i++) {
		last_move_time[i] = current_time;
	}

	//get current time
	count_time = get_clock_ticks();
	
	// We play the game while the frog is alive and we haven't filled up the 
	// far riverbank
	while(is_frog_alive()) {	

		//t = t - 1 every 1s
		if(get_clock_ticks() >= count_time + 1000) {
			if(t==0) {
				set_frog_die();
			}
			t--;
			count_time = get_clock_ticks();
		}

		//output to seven segment
		seven_seg_cc = 1 ^ seven_seg_cc;
		if(seven_seg_cc == 1 && t >= 10) {
			PORTC = seven_seg_data[t/10];
			PORTD = (1<<2);
		} else {
			PORTC = seven_seg_data[t%10];
			PORTD = 0;
		}

		//when the riverbank is full, go to next level
		if(is_riverbank_full()) {
			n++;
			PORTC = 0;
			sprintf(level, "level %u", n);
			//level scroll on the LED display
			init_scrolling_display();
			set_scrolling_display_text(level);
			while(1) {
				_delay_ms(150);
				if(scroll_display() == 0) {
					break;
				}		
			}
			//resotre a chance if chance < 3
			if(chances < 3) {
				chances++;
				PORTA = (PORTA<<1);
				//update the chance in terminal
			    move_cursor(10,16);
			    printf("Lives: %u",chances);
			}
			init_game();
			//reset the counter
			t=20;	
			count_time = get_clock_ticks();	
		}


		// Check for input - which could be a button push or serial input.
		// Serial input may be part of an escape sequence, e.g. ESC [ D
		// is a left cursor key press. At most one of the following three
		// variables will be set to a value other than -1 if input is available.
		// (We don't initalise button to -1 since button_pushed() will return -1
		// if no button pushes are waiting to be returned.)
		// Button pushes take priority over serial input. If there are both then
		// we'll retrieve the serial input the next time through this loop
		serial_input = -1;
		escape_sequence_char = -1;
		button = button_pushed();
		
		if(button == -1) {
			// No push button was pushed, see if there is any serial input
			if(serial_input_available()) {
				// Serial data was available - read the data from standard input
				serial_input = fgetc(stdin);
				// Check if the character is part of an escape sequence
				if(characters_into_escape_sequence == 0 && serial_input == ESCAPE_CHAR) {
					// We've hit the first character in an escape sequence (escape)
					characters_into_escape_sequence++;
					serial_input = -1; // Don't further process this character
				} else if(characters_into_escape_sequence == 1 && serial_input == '[') {
					// We've hit the second character in an escape sequence
					characters_into_escape_sequence++;
					serial_input = -1; // Don't further process this character
				} else if(characters_into_escape_sequence == 2) {
					// Third (and last) character in the escape sequence
					escape_sequence_char = serial_input;
					serial_input = -1;  // Don't further process this character - we
										// deal with it as part of the escape sequence
					characters_into_escape_sequence = 0;
				} else {
					// Character was not part of an escape sequence (or we received
					// an invalid second character in the sequence). We'll process 
					// the data in the serial_input variable.
					characters_into_escape_sequence = 0;
				}
			}
		}

		//Set joystick
		// Set the ADC mux to choose ADC if x_or_y is 0, ADC1 if x_or_y is 1
		if(x_or_y == 0) {
			ADMUX &= ~1;
		} else {
			ADMUX |= 1;
		}
		// Start the ADC conversion
		ADCSRA |= (1<<ADSC);
		
		while(ADCSRA & (1<<ADSC)) {
			; 
		}
		value = ADC; // read the value
		//set X middle range
		if(x_or_y == 0 && (value > 400 && value < 600)) {
			mid_x = 1;
		}
		//set Y middle range
		if(x_or_y == 1 && (value > 400 && value < 600)) {
			mid_y = 1;
		}

		if(mid_x == 1 && mid_y == 1) {
			a=1;
		}
		
		if(x_or_y == 0) {			
			//set X left range
			if(value < 400) {
				mid_x = 0;
				if(a==1) {
					Direction = 'l';
					joystick_time = get_clock_ticks();
					if((value>100 && value<400) && ((last_value>600 && last_value<900) || (last_value>100 && last_value<400))) {
						a=1;
					} else a=0;						
				}

				if(get_clock_ticks() > joystick_time + 500) {
					Direction = 'l';
					joystick_time = get_clock_ticks();
				}
			} 
			//set X right range
			if(value > 600) {
				mid_x = 0;
				if(a==1) {
					Direction = 'r';
					joystick_time = get_clock_ticks();
					if((value>600 && value<900) && ((last_value>600 && last_value<900) || (last_value>100 && last_value<400))) {
						a=1;
					} else a=0;		
				}					
				if(get_clock_ticks() > joystick_time + 500) {
					Direction = 'r';
					joystick_time = get_clock_ticks();
				}
			}
			

		} else {			
			//set Y down range
			if(value < 100) {
				mid_y = 0;
				if(a==1) {
					Direction = 'd';
					joystick_time = get_clock_ticks();
					a=0;
				}			

				if(get_clock_ticks() > joystick_time + 500) {
					Direction = 'd';
					joystick_time = get_clock_ticks();
				}
			} 
			//set Y left-down, right-down range
			if((value > 100 && value < 400) && ((last_value < 400 && last_value > 100) || (last_value < 900 && last_value > 600))) {
				mid_y = 0;
				if(a==1) {
					Direction = 'd';
					joystick_time = get_clock_ticks();
					a=0;
				}			

				if(get_clock_ticks() > joystick_time + 500) {
					Direction = 'd';
					joystick_time = get_clock_ticks();
				}
			}
			//set Y left-up,right-up range
			if((value > 600 && value < 900) && ((last_value < 400 && last_value > 100) || (last_value < 900 && last_value > 600))) {
				mid_y = 0;
				if(a==1) {
					Direction = 'u';
					joystick_time= get_clock_ticks();
					a=0;
				}			

				if(get_clock_ticks() > joystick_time+ 500) {
					Direction = 'u';
					joystick_time = get_clock_ticks();
				}
			}
			//set Y up range
			if(value > 900) {
				mid_y = 0;
				if(a==1) {
					Direction = 'u';
					joystick_time= get_clock_ticks();
					a=0;
				}					
				if(get_clock_ticks() > joystick_time + 500) {
					Direction = 'u';
					joystick_time = get_clock_ticks();
				}
			}
		}
		//store last value
		last_value = value;
		// Next time through the loop, do the other direction
		x_or_y ^= 1;

		//start timer if push button
		if(button != -1) {
			button_repeat_time = get_clock_ticks();
		}
		
		// Process the input. 
		if(button==3 || escape_sequence_char=='D' || serial_input=='L' || serial_input=='l' || Direction=='l') {
			// Attempt to move left
			move_frog_left();		
			Direction = 'm';
		} else if(button==2 || escape_sequence_char=='A' || serial_input=='U' || serial_input=='u' || Direction=='u') {
			// Attempt to move forward
			move_frog_forward();
			Direction = 'm';
		} else if(button==1 || escape_sequence_char=='B' || serial_input=='D' || serial_input=='d' || Direction=='d') {
			// Attempt to move down
			move_frog_backward();
			Direction = 'm';
		} else if(button==0 || escape_sequence_char=='C' || serial_input=='R' || serial_input=='r' || Direction=='r') {
			// Attempt to move right
			move_frog_right();
			Direction = 'm';
		} else if(serial_input == 'p' || serial_input == 'P') {
			//pause the game until 'p' or 'P'
			move_cursor(10,10);
		    printf_P(PSTR("Pause"));		
			while(1) {
				//stop the counter
				seven_seg_cc = 1 ^ seven_seg_cc;
				if(seven_seg_cc == 1 && t >= 10) {
					PORTC = seven_seg_data[t/10];
					PORTD = (1<<2);
				} else {
					PORTC = seven_seg_data[t%10];
					PORTD = 0;
				}				

				//press again
				if(serial_input_available()) {
					serial_input = fgetc(stdin);
					if(serial_input == 'p' || serial_input == 'P') {
						move_cursor(10,10);
				    	clear_to_end_of_line();					
						break;
					}
				}
			}
		} else if(serial_input == 'n' || serial_input == 'N') {
			//when press n or N, the game will restart
			while(1) {
				new_game();
				play_game();
				handle_game_over();
			}
		} 

		if(is_frog_alive() && frog_has_reached_riverbank()) {
			// Frog reached the other side successfully but the
			// riverbank isn't full, put a new frog at the start
			put_frog_at_start();
			//when frog reach riverbank, socre increase 1
			add_to_score(1);
			//reset the counter
			t=20;	
			count_time = get_clock_ticks();	
			//update socre in terminal
			move_cursor(10,14);
			printf("Score: %u",get_score());
		}

		//auto repeat
		if(get_clock_ticks() > button_repeat_time + 500) {
			if((PINB & 0x0F) == 8) {
				move_frog_left();
			} 
			if((PINB & 0x0F) == 4) {
				move_frog_forward();
			} 
			if((PINB & 0x0F) == 2) {
				move_frog_backward();
			}if((PINB & 0x0F) == 1) {
				move_frog_right();
			}
			button_repeat_time  = get_clock_ticks();
		}

		
		current_time = get_clock_ticks();
		scroll_time = 1000;
		current_score = get_score();
		//speed up as the score gets higher
		if(current_score >= 1 && current_score < 3) {
			scroll_time = scroll_time - 50;
		} if(current_score >= 3 && current_score < 5) {
			scroll_time = scroll_time - 75;
		} if(current_score >= 5 && current_score < 8) {
			scroll_time = scroll_time - 100;
		} if(current_score >= 8 && current_score < 10) {
			scroll_time = scroll_time - 125;
		} if(current_score >= 10 && current_score < 13) {
			scroll_time = scroll_time - 150;
		} if(current_score >= 13 && current_score < 15) {
			scroll_time = scroll_time - 175;
		} if(current_score >= 15 && current_score < 20) {
			scroll_time = scroll_time - 200;
		} if(current_score >= 20) {
			scroll_time = scroll_time - 250;
		}

		//set scrolling speed, each of lanes/channels has different speeds. They are between 600ms and 1200ms
		if(is_frog_alive()) {
			if(current_time >= last_move_time[0] + (scroll_time - 100)) {
				scroll_lane(0, 1);
				last_move_time[0] = current_time;
			} else if(current_time >= last_move_time[1] + (scroll_time - 150)) {
				scroll_lane(1, -1);
				last_move_time[1] = current_time;
			} else if(current_time >= last_move_time[2] + scroll_time) {
				scroll_lane(2, 1);
				last_move_time[2] = current_time;
			} else if(current_time >= last_move_time[3] + (scroll_time + 100)) {
				scroll_log_channel(0, -1);
				last_move_time[3] = current_time;
			} else if(current_time >= last_move_time[4] + (scroll_time + 200)) {
				scroll_log_channel(1, 1);
				last_move_time[4] = current_time;
			}
		}

		//when the frog is die
		if(!is_frog_alive()) {
			chances--;
			//turn off one LED
			PORTA = (PORTA>>1);
			if(chances==0) {
				//black seven segment
				PORTC = 0;	
				//set the new top5 if get a new record
		    	uint8_t address1=11,address2=61;
			    for(int i=0;i<5;i++) {
				    char score[10];
				    eeprom_read_block (( void *) score, ( const void *) address1, 10);
				    int Score;
				    if((int)(score[0] - '0')>=0) {
				    	Score = (int)(score[1] - '0');
				    } if((int)(score[1] - '0')>=0) {
				    	Score = (int)(score[0] - '0')*10 + (int)(score[1] - '0');
				    } if((int)(score[2] - '0')>=0) {
				    	Score = (int)(score[0] - '0')*100 + (int)(score[1] - '0')*10 + (int)(score[1] - '0');
				    } 
				    if(Score < (int)get_score()) {
				    	if(i!=4) {
				    		for(int a=0;a<4-i;a++) {
							    char Name1[10];
							    char score1[10];
							    eeprom_read_block (( void *) Name1, ( const void *) address2, 10);
							    eeprom_read_block (( void *) score1, ( const void *) address2+10, 10);

							   	eeprom_update_block (( const void *) Name1, ( void *) address2+20, 10);
								eeprom_update_block (( const void *) score1, ( void *) address2+30, 10);	
								address2 = address2 - 20;
				    		}
				    	} 
				    	char input;
				    	char Name[10];
				    	int ii = 0;
				    	clear_terminal();
				    	//get name from user
						move_cursor(20,9);
			    		printf_P(PSTR("New top5 record"));
						move_cursor(10,11);
			    		printf_P(PSTR("Input your name: "));	
			    		while((input = fgetc(stdin)) != EOF ) {	 
			    			if(input == '\n') {
			    				Name[ii++] = '\0';
			    				break;		
			    			}
			    			
			    			Name[ii] = input;
			    			ii++;
			    			printf("%c",input);	
			    		}	
			    		if(get_score()<10) {
							score[0] = (char)(((int)'0')+(int)get_score());
							score[1] = '\0';			    			
			    		} if(get_score()<100 && get_score()>=10) {
							score[0] = (char)(((int)'0')+(int)(get_score()/10));
							score[1] = (char)(((int)'0')+(int)(get_score()%10));
							score[2] = '\0';				    			
			    		} if(get_score()<1000 && get_score()>=100) {
							score[0] = (char)(((int)'0')+(int)(get_score()/100));
							score[1] = (char)(((int)'0')+(int)(get_score()%100/10));
							score[2] = (char)(((int)'0')+(int)(get_score()%100%10));
							score[3] = '\0';				    			
			    		}

						//write data to EEPROM
						eeprom_update_block (( const void *) Name, ( void *) address1-10, 10);
						eeprom_update_block (( const void *) score, ( void *) address1, 10);	
						break;	
				    }
				    address1 = address1 + 20;
			    }		
				break;
			}
			clear_frog();
			set_frog_alive();
			put_frog_at_start();
			//reset the counter
			t=20;	
			count_time = get_clock_ticks();	
			//update the chance in terminal
		    move_cursor(10,16);
		    printf("Lives: %u",chances);

		}
	}
	// We get here if the frog is dead or the riverbank is full
	// The game is over.
}

void handle_game_over() {
	clear_terminal();
	// show top5 in terminal
	show_top5();
	//compare current score to current high score
	if(set_highscore(get_highscore(),get_score())) {
		move_cursor(10,10);
		printf_P(PSTR("New High Score!"));	
	}
	//update the high score in terminal
	move_cursor(10,11);
	printf("High Score: %u",get_highscore());	
	move_cursor(10,12);
	printf("Score: %u",get_score());
	move_cursor(10,14);
	printf_P(PSTR("GAME OVER"));
	move_cursor(10,15);
	printf_P(PSTR("Press a button to start again"));
	while(button_pushed() == -1) {
		; // wait
	}
}

void show_top5() {
    uint8_t address=1;
    int y=3;	
    move_cursor(10,y-1);
    printf_P(PSTR("TOP-5"));
    for(int i=0;i<5;i++) {
	    char Name[10];
	    char score[10];
	    eeprom_read_block (( void *) Name, ( const void *) address, 10);
	    eeprom_read_block (( void *) score, ( const void *) address+10, 10);
		move_cursor(10,y);
	    printf("Name: %s, Socre: %s",Name,score); 
	    address = address + 20;
	    y = y + 1;  	
    }	
}

void init_top5() {	
	uint8_t address=1;
	for(int i=0;i<5;i++) {
   		uint8_t name; 
	    name = eeprom_read_byte (( uint8_t *)address);	    
	    if(name==255) {
	    	char Name1[10] = {'E','m','p','t','y','\0'};
	    	char score1[10] = {'0','\0'};
	    	eeprom_update_block (( const void *) Name1, ( void *) address, 10);
	    	eeprom_update_block (( const void *) score1, ( void *) address+10, 10);
	    }
	    address = address + 20; 		
	}
}