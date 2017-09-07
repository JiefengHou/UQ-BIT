
#include <stdio.h>
#include <avr/interrupt.h>


void init_joystick(void) {		
	// Set up ADC - AVCC reference, right adjust
	// Input selection doesn't matter yet - we'll swap this around in the while
	// loop below.
	ADMUX = (1<<REFS0);
	// Turn on the ADC (but don't start a conversion yet). Choose a clock
	// divider of 64. (The ADC clock must be somewhere
	// between 50kHz and 200kHz. We will divide our 8MHz clock by 64
	// to give us 125kHz.)
	ADCSRA = (1<<ADEN)|(1<<ADPS2)|(1<<ADPS1);

	
}

