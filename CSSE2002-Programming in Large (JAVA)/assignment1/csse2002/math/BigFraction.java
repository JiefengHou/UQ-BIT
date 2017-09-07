package csse2002.math;

import java.math.BigInteger;

/**
 * <p>
 * An immutable representation of a rational number as a fraction with
 * BigInteger-valued numerator and denominator, in <i>reduced normal form</i>.
 * </p>
 * 
 * <p>
 * A fraction with BigInteger-valued numerator n and denominator m is in reduced
 * normal form when it satisfies (using informal notation for implication,
 * inequalities etc): <br>
 * (n=0 => m=1) && (n!=0 => gcd(n,m) = 1 && m > 0).
 * </p>
 */

public class BigFraction implements Comparable<BigFraction> {

	/** The fraction constant zero. */
	public final static BigFraction ZERO = new BigFraction(0, 1);
	/** The fraction constant one. */
	public final static BigFraction ONE = new BigFraction(1);

	// fraction numerator in reduced normal form
	private BigInteger n;
	// fraction denominator in reduced normal form
	private BigInteger m;

	/*
	 * invariant: numerator n and denominator m of the fraction are in reduced
	 * normal form after constructor is complete.
	 */

	/**
	 * Creates a new fraction in reduced normal form, with value equivalent to
	 * numerator.
	 * 
	 * @param numerator
	 *            The given numerator.
	 */
	public BigFraction(int numerator) {
		this(numerator, 1);
	}

	/**
	 * Creates a new fraction in reduced normal form, with value equivalent to
	 * numerator divided by denominator. An InvalidFractionException is thrown
	 * if the given denominator is zero.
	 * 
	 * @param numerator
	 *            The given numerator.
	 * @param denominator
	 *            The given denominator.
	 * @throws InvalidFractionException
	 *             If the given denominator equals 0.
	 */
	public BigFraction(int numerator, int denominator)
			throws InvalidFractionException {
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}

	/**
	 * Creates a new fraction in reduced normal form, with value equivalent to n
	 * divided by m. An InvalidFractionException is thrown if the given
	 * denominator is zero.
	 * 
	 * @param n
	 *            The given numerator.
	 * @param m
	 *            The given denominator.
	 * @throws InvalidFractionException
	 *             If the given denominator m equals 0.
	 */
	private BigFraction(BigInteger n, BigInteger m)
			throws InvalidFractionException {
		this.n = n;
		this.m = m;
		// check that denominator is non-zero
		if (m.equals(BigInteger.ZERO)) {
			throw new InvalidFractionException("Denominator cannot be zero.");
		}
		// update n and m so that they are in reduced normal form
		getNormalForm();
	}

	/**
	 * Updates n and m to be the numerator and denominator of fraction n/m in
	 * reduced normal form. We assume that m is not equal to zero.
	 * 
	 */
	private void getNormalForm() throws InvalidFractionException {
		if (n.equals(BigInteger.ZERO)) {
			// if n is zero then set m to be one
			m = BigInteger.ONE;
		} else {
			// correct so that numerator is non-negative
			if (m.compareTo(BigInteger.ZERO) < 0) {
				m = m.negate();
				n = n.negate();
			}
			// reduce n and m by their greatest common divisor
			BigInteger d = n.gcd(m); // the gcd of n and m
			n = n.divide(d);
			m = m.divide(d);
		}
	}

	/**
	 * Returns a new fraction equal to this plus f.
	 * 
	 * @param f
	 *            The fraction to be added to this one.
	 * @return The value of this added to f.
	 */
	public BigFraction add(BigFraction f) {
		// numerator of result
		BigInteger nr = n.multiply(f.m).add(f.n.multiply(m));
		// denominator of result
		BigInteger mr = m.multiply(f.m);
		return new BigFraction(nr, mr);
	}

	/**
	 * Returns a new fraction equal to this minus f.
	 * 
	 * @param f
	 *            The fraction to be added to this one.
	 * @return The value of this minus f.
	 */
	public BigFraction subtract(BigFraction f) {
		// numerator of result
		BigInteger nr = n.multiply(f.m).subtract(f.n.multiply(m));
		// denominator of result
		BigInteger mr = m.multiply(f.m);
		return new BigFraction(nr, mr);
	}

	/**
	 * Returns a new fraction equal to this multiplied by f.
	 * 
	 * @param f
	 *            The fraction to be added to this one.
	 * @return The value of this multiplied by f.
	 */
	public BigFraction multiply(BigFraction f) {
		BigInteger nr = n.multiply(f.n); // numerator of result
		BigInteger mr = m.multiply(f.m); // denominator of result
		return new BigFraction(nr, mr);
	}

	/**
	 * Returns a new fraction equal to this divided by f.
	 * 
	 * @param f
	 *            The fraction that will be used to divide this.
	 * @return The value of this divided by f.
	 * @throws InvalidFractionException
	 *             Thrown if the resulting denominator is zero.
	 */
	public BigFraction divide(BigFraction f) throws InvalidFractionException {
		BigInteger nr = n.multiply(f.m); // numerator of result
		BigInteger mr = m.multiply(f.n); // denominator of result
		return new BigFraction(nr, mr);
	}

	/**
	 * Returns the numerator.
	 * 
	 * @return The numerator of this fraction.
	 */
	public BigInteger getNumerator() {
		return n;
	}

	/**
	 * Returns the denominator.
	 * 
	 * @return The denominator of this fraction.
	 */
	public BigInteger getDenominator() {
		return m;
	}

	/**
	 * Used to calculate if this represents a rational-value in the range [0,1].
	 * 
	 * @return True iff this fraction has a value in the range [0,1].
	 */
	public boolean isAProbability() {
		return (this.compareTo(BigFraction.ZERO) >= 0 && this
				.compareTo(BigFraction.ONE) <= 0);
	}

	/**
	 * Used to calculate one minus this.
	 * 
	 * @return One minus this.
	 */
	public BigFraction complement() {
		return BigFraction.ONE.subtract(this);
	}

	/**
	 * Returns the string "N/M" where N is the toString representation of the
	 * BigInteger-valued numerator and M is the toString representation of the
	 * BigInteger-valued denominator.
	 */
	@Override
	public String toString() {
		if (!m.equals(BigInteger.ONE)) {
			return n + "/" + m;
		} else {
			return n + "";
		}
	}

	/**
	 * Returns true iff o is a BigFraction representing the same rational number
	 * as this.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BigFraction)) {
			return false;
		}
		BigFraction f = (BigFraction) o;
		return n.equals(f.n) && m.equals(f.m);
	}

	@Override
	public int hashCode() {
		/*
		 * Compute a polynomial hash code using an odd prime base P.
		 */
		final int P = 31; // an odd prime base
		return n.intValue() * P + m.intValue();
	}

	/**
	 * Returns 0 iff BigFraction f and this are equal, -1 if the rational-value
	 * of this is less than that of f, and 1 if the rational-value of this is
	 * greater than that of f.
	 */
	@Override
	public int compareTo(BigFraction f) {
		// numerator of this on common denominator m * f.m
		BigInteger cn1 = n.multiply(f.m);
		// numerator of f on common denominator m * f.m
		BigInteger cn2 = f.n.multiply(m);
		return cn1.compareTo(cn2);
	}

	/**
	 * Determines whether this fraction is internally consistent.
	 * 
	 * @return true if this fraction is internally consistent, and false
	 *         otherwise
	 */
	public boolean checkInv() {
		if (n.equals(BigInteger.ZERO)) {
			if (!m.equals(BigInteger.ONE))
				return false;
		} else {
			if (m.compareTo(BigInteger.ZERO) < 0) {
				return false;
			}
			if (!n.gcd(m).equals(BigInteger.ONE)) {
				return false;
			}
		}
		return true;
	}

}
