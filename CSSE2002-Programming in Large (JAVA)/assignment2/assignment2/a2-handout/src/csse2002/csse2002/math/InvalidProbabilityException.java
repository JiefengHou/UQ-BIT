package csse2002.math;

/**
 * An exception indicating that a value is not a probability.
 */
@SuppressWarnings("serial")
public class InvalidProbabilityException extends RuntimeException {

    public InvalidProbabilityException() {
	super();
    }

    public InvalidProbabilityException(String s) {
	super(s);
    }

}
