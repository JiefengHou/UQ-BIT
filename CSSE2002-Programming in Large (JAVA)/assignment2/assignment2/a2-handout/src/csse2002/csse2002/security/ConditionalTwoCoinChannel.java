package csse2002.security;

import csse2002.math.*;

/**
 * Informants may use two-coin channels (see {@link TwoCoinChannel}) to
 * communicate information about a Boolean-valued secret to spies. <br>
 * <br>
 * 
 * In fact, informants -- being omnipotent -- may choose to communicate such a
 * message to a spy based on the current knowledge-state of the spy. (Where, see
 * {@link KnowledgeDistribution}, the knowledge-state of the spy is a
 * distribution on the two possible values of the secret, describing the
 * likelihood of either possibility in the spy's current state.)<br>
 * <br>
 * 
 * This immutable class keeps track of a knowledge-state and a two-coin channel.
 * It is used to describe the condition -- the knowledge-state -- under which an
 * informant will use the aforementioned two-coin channel to communicate to a
 * spy. <br>
 * <br>
 * 
 */
public class ConditionalTwoCoinChannel {

    // The two-coin channel, that will be used to leak information to the spy,
    // if the spy is initially in a state in which she knows that the secret is
    // true with probability aPriori (and false with the complementary
    // probability).
    private final TwoCoinChannel channel;
    // The probability that the secret is true in the knowledge-state condition
    // of this.
    private final BigFraction aPriori;

    /*
     * invariant: Neither channel nor aPriori are null and
     * aPriori.isAProbability()
     */

    /**
     * Given a knowledge state ks in which the spy knows the secret is true with
     * probability aPriori (and false with the complement of aPriori), construct
     * a new conditional two-coin channel with knowledge-state ks and two-coin
     * channel c.
     * 
     * @param aPriori
     *            The probability that the secret is true in the knowledge state
     *            ks.
     * @param c
     *            The two-coin channel, that will be used to leak information to
     *            the spy if the spy is in knowledge state ks.
     * 
     * @throws NullPointerException
     *             If either argument is null-valued
     * @throws InvalidProbabilityException
     *             If aPriori is not a probability
     */
    public ConditionalTwoCoinChannel(BigFraction aPriori, TwoCoinChannel c) {
	if (aPriori == null || c == null) {
	    throw new NullPointerException("Parameters cannot be null.");
	}
	if (!aPriori.isAProbability()) {
	    throw new InvalidProbabilityException(
		    "Parameter aPriori must be a valid probability.");
	}
	this.aPriori = aPriori;
	this.channel = c;
    }

    /**
     * Returns the probability that the secret is true in the knowledge-state of
     * this. (That is the knowledge-state that the spy must be in for the
     * informant to leak information to the spy using getTwoCoinChannel().)
     */
    public BigFraction getCondition() {
	return aPriori;
    }

    /**
     * Returns the two-coin channel that will be used to leak information to the
     * spy if the spy is in knowledge state described by getCondition().
     */
    public TwoCoinChannel getTwoCoinChannel() {
	return channel;
    }

    /**
     * Returns the probability that the outcome of the private coin flip will be
     * (outcome? heads: tails) given that the secret is initially known to be
     * true with probability getCondition(), and false with the complementary
     * probability.
     */
    public BigFraction outcomeProbability(boolean outcome) {
	return channel.outcomeProbability(aPriori, outcome);
    }

    /**
     * Given that the secret is initially known to be true with probability
     * getCondition() (and false with the remaining probability), this method
     * returns: <br>
     * <br>
     * 
     * 1. BigFraction.ZERO, if the probability of the outcome (outcome? heads:
     * tails) is zero, and <br>
     * <br>
     * 
     * 2. the probability that the secret is true, given that the outcome of the
     * private coin flip is (outcome? heads: tails), otherwise.
     */
    public BigFraction aPosteriori(boolean outcome) {
	return channel.aPosteriori(aPriori, outcome);
    }

    /**
     * Returns representation of this conditional two-coin channel as the String<br>
     * <br>
     * 
     * "if true@APRIORI then (C1, C2)"<br>
     * <br>
     * 
     * where APRIORI is the string representation of this.getCondition(), and
     * (C1, C2) is the string representation of this.getTwoCoinChannel().
     */
    @Override
    public String toString() {
	return "if " + "true@" + aPriori + " then " + channel.toString();
    }

    /**
     * Returns true iff o is a ConditionalTwoCoinChannel with such that
     * knowledge-state of o is equivalent to the knowledge-state of this, and
     * the two-coin channel of o is equivalent to the two-coin channel of this.<br>
     * <br>
     * 
     * (Two knowledge-states are equivalent if they have the same likelihood of
     * the secret being true and false, respectively.)
     */
    @Override
    public boolean equals(Object o) {
	if (!(o instanceof ConditionalTwoCoinChannel)) {
	    return false;
	}
	ConditionalTwoCoinChannel c = (ConditionalTwoCoinChannel) o;
	return aPriori.equals(c.aPriori) && channel.equals(c.channel);
    }

    @Override
    public int hashCode() {
	/*
	 * Compute a polynomial hash code using an odd prime base P.
	 */
	final int P = 31; // an odd prime base
	return aPriori.hashCode() * P + channel.hashCode();
    }

    /**
     * Determines whether this class is internally consistent.
     * 
     * @return true if this class is internally consistent, and false otherwise
     */
    public boolean checkInv() {
	return aPriori != null && aPriori.isAProbability() && channel != null;
    }

}
