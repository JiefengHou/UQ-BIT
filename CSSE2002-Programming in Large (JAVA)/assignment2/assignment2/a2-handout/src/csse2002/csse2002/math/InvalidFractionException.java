package csse2002.math;

/**
 * An exception indicating an invalid BigFraction.
 */
@SuppressWarnings("serial")
public class InvalidFractionException extends RuntimeException {

    public InvalidFractionException() {
	super();
    }

    public InvalidFractionException(String s) {
	super(s);
    }

}
