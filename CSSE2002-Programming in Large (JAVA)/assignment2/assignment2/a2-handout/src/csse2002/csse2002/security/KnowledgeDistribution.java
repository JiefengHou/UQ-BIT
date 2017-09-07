package csse2002.security;

import java.util.*;

import csse2002.math.*;

/**
 * A spy's knowledge of a Boolean-valued secret is described by a distribution
 * describing the likelihood that the secret is true and the likelihood that the
 * secret is false. Such a distribution is referred to as a
 * <i>knowledge-state</i>. For simplicity, knowledge-states are represented
 * simply by a BigFraction denoting the likelihood that the secret is true in
 * the knowledge-state. (With the likelihood of the secret being false
 * implicitly represented by the complement of that probability.) <br>
 * <br>
 * 
 * Through interactions with informants via two-coin channels (see
 * {@link TwoCoinChannel}), a spy's knowledge of the secret may change,
 * probabilistically. For example, consider a spy who is initially certain that
 * the secret is equally likely to be true or false. Following an encounter with
 * an informant, who communicates with her using a two-coin channel with bias
 * 3/4 for the first coin, and 1/4 for the second, she will update her knowledge
 * of the state based on whether or not the result of the informant's coin flip
 * was heads or tails. With probability 1/2 the outcome will be heads and she
 * will learn that the secret is true with probability 3/4; and with probability
 * 1/2 the outcome will be tails and she will learn that the secret is true with
 * probability 1/4 (and false with the complementary probability).<br>
 * <br>
 * 
 * This class is a mutable representation of a discrete <i>sub-distribution</i>
 * on knowledge-states. Such a sub-distribution represents a mapping from
 * knowledge-states to probabilities, in which the <i>weight</i> of the
 * distribution -- the sum of the probabilities of all the knowledge states in
 * the distribution-- must be less than or equal to one. (We allow
 * sub-distributions so that this class can be used to build total (i.e.
 * one-summing) distributions.)<br>
 * <br>
 * 
 * A KnowledgeDistribution may be used to model the likelihood that a spy has
 * certain states of knowledge following interactions with various informants
 * via two-coin channels. In the example above, for instance, the spy starts
 * with a KnowledgeDistribution:<br>
 * <br>
 * 
 * {{true@1/2, false@1/2}@1}<br>
 * <br>
 * 
 * describing the fact that, with probability one, she knows the secret to be
 * equally likely to be true or false. And following her interaction with the
 * informant, her KnowledgeDistribution is:<br>
 * <br>
 * 
 * {{true@1/4, false@3/4}@1/2, {true@3/4, false@1/4}@1/2}<br>
 * <br>
 * 
 * If she now interacts with another informant, one that communicates with her
 * over two-coin channel TwoCoinChannel(1/3, 1/2) if and only if her knowledge
 * state is {true@3/4, false@1/4}, then her resulting KnowledgeDistribution will
 * be:<br>
 * <br>
 * 
 * {{true@1/4, false@3/4}@1/2, {true@2/3, false@1/3}@3/16, {true@4/5,
 * false@1/5}@5/16}
 * 
 */

public class KnowledgeDistribution implements Iterable<BigFraction> {

    // An ordered map storing (knowledge-state, probability) pairs for
    // knowledge-states in the support of the distribution.
    private TreeMap<BigFraction, BigFraction> dist;

    /*
     * invariant:
     * 
     * dist != null &&
     * 
     * the keys (representing knowledge-states) in dist are probabilities &&
     * 
     * the values in dist are non-zero probabilities (and so they cannot be
     * null) &&
     * 
     * the sum of values in dist is a probability.
     */

    /**
     * Creates a new empty KnowledgeDistribution with zero weight.
     */
    public KnowledgeDistribution() {
	dist = new TreeMap<BigFraction, BigFraction>();
    }

    /**
     * Creates a new point distribution on s, i.e. a new KnowledgeDistribution
     * in which s has probability one.
     * 
     * @param s
     *            The knowledge-state from which the point distribution will be
     *            created.
     * 
     * @throws NullPointerException
     *             If parameter s is null.
     * 
     * @throws InvalidProbabilityException
     *             If s is not a probability.
     */
    public KnowledgeDistribution(BigFraction s) {
	if (s == null) {
	    throw new NullPointerException(
		    "Cannot create point distribution of a null knowledge-state.");
	}
	if (!s.isAProbability()) {
	    throw new InvalidProbabilityException(
		    "Parameter s must be a probability.");
	}
	dist = new TreeMap<BigFraction, BigFraction>();
	dist.put(s, BigFraction.ONE);
    }

    /**
     * Creates the KnowledgeDistribution of a spy that initially knows that the
     * secret is true with probability aPriori (and false with probability
     * aPriori.complement()), and subsequently encounters the informants
     * specified by parameter informants, in the order in which they appear in
     * that list.
     * 
     * @param aPriori
     *            The probability that the secret is true before the spy
     *            encounters any informants.
     * 
     * @param informants
     *            A list of informants that the spy meets, one at a time, in the
     *            order in which they are given in this list.
     * 
     * @throws NullPointerException
     *             If either parameter is null or informants contains null
     *             values.
     * 
     * @throws InvalidProbabilityException
     *             If aPriori is not a probability.
     */
    public KnowledgeDistribution(BigFraction aPriori,
	    List<ConditionalTwoCoinChannel> informants) {
	if (aPriori == null || informants == null || informants.contains(null)) {
	    throw new NullPointerException(
		    "Parameters cannnot be null or contain null values.");
	}
	if (!aPriori.isAProbability()) {
	    throw new InvalidProbabilityException(
		    "Parameter aPriori must be a probability.");
	}
	dist = new TreeMap<BigFraction, BigFraction>();
	dist.put(aPriori, BigFraction.ONE);
	this.update(informants);
    }

    /**
     * Creates a new KnowledgeDistribution with the same knowledge-states and
     * corresponding weights as KnowledgeDistribution k. Parameter k is
     * unmodified by the operation, and future operations on this. Similarly,
     * this is unmodified by future modifications to k.
     * 
     * @param k
     *            The KnowledgeDistribution from which our new
     *            KnowledgeDistribution will be created.
     * 
     * @throws NullPointerException
     *             If parameter k is null;
     */
    public KnowledgeDistribution(KnowledgeDistribution k) {
	if (k == null) {
	    throw new NullPointerException("Paramter k cannot be null.");
	}
	dist = new TreeMap<BigFraction, BigFraction>(k.dist);
    }

    /**
     * Returns the probability of knowledge-state s in this.
     * 
     * @param s
     *            The knowledge-state for which the probability is retrieved.
     * @throws NullPointerException
     *             If parameter s is null.
     * @return The probability of knowledge-state s.
     */
    public BigFraction weight(BigFraction s) {
	return (dist.containsKey(s) ? dist.get(s) : BigFraction.ZERO);
    }

    /**
     * Returns the combined probability of all the knowledge-states in the
     * support of the KnowledgeDistribution.
     * 
     * @return The probability of all the knowledge-states in the support of
     *         this added together.
     */
    public BigFraction weight() {
	BigFraction weight = BigFraction.ZERO; // weight of the distribution
	for (BigFraction w : dist.values()) {
	    weight = weight.add(w);
	}
	return weight;
    }

    /**
     * Returns an iterator over the knowledge-states in the <i>support</i> of
     * this distribution. The support of this distribution is defined as the
     * knowledge-states with non-zero probability in this. <br>
     * <br>
     * 
     * The knowledge-states returned are represented by BigFractions, describing
     * the probability that the secret is true in that knowledge-state.<br>
     * <br>
     * 
     * The knowledge-states, represented by BigFractions, are returned in
     * (ascending) sorted order according to the natural ordering of
     * BigFraction: that is, knowledge-states are returned ordered by their
     * likelihood that the secret is true, with the knowledge-state with the
     * smallest probability that the secret is true being returned first.<br>
     * <br>
     * 
     * The iterator's behaviour is not defined if the KnowledgeDistribution is
     * modified after it has been created. (That is, don't use this method to
     * get an iterator, modify the KnowledgeDistribution, and then try to use
     * the iterator.)
     * 
     */
    @Override
    public Iterator<BigFraction> iterator() {
	return new KnowledgeDistributionIterator();
    }

    /**
     * Iterator over support of this distribution. Essentially a wrapper around
     * another iterator that disallows removal of elements. (Since we haven't
     * explicitly said in the specification of iterator() that removal is
     * allowed.)
     */
    private class KnowledgeDistributionIterator implements
	    Iterator<BigFraction> {

	// iterator from keySet of the TreeMap that does allow removal of
	// elements
	Iterator<BigFraction> it;

	public KnowledgeDistributionIterator() {
	    it = dist.keySet().iterator();
	}

	@Override
	public boolean hasNext() {
	    return it.hasNext();
	}

	@Override
	public BigFraction next() {
	    return it.next();
	}

	@Override
	public void remove() {
	    throw new UnsupportedOperationException();
	}
    }

    /**
     * Returns the string <br>
     * <br>
     * 
     * "{KS1@W1, ... , KSN@WN}",<br>
     * <br>
     * 
     * where N is the number of knowledge-states in the support of this (i.e.
     * those states with a non-zero probability of occurrence), and for i in
     * 1..N, KSi is a string representation -- described below -- of the
     * knowledge-state in the support of this with the ith-smallest probability
     * that the secret is true, and Wi is its corresponding probability of
     * occurrence.<br>
     * <br>
     * 
     * The string representation of a knowledge-state in which the secret is
     * true with probability P and false with the complementary probability P'
     * is given by:<br>
     * <br>
     * 
     * "{true@P, false@P'}"<br>
     * <br>
     * 
     * where P and P' are given by the toString representation of BigFraction.<br>
     * <br>
     * 
     * For example, the string:<br>
     * <br>
     * 
     * "{{true@1/4, false@3/4}@1/2, {true@2/3, false@1/3}@3/16, {true@4/5,
     * false@1/5}@5/16}"<br>
     * <br>
     * 
     * is used to represent a KnowledgeDistribution with a three-element support
     * in which knowledge-state 1/4 occurs with probability 1/2, 2/3 occurs with
     * probability 3/16, and 4/5 occurs with probability 5/16.
     */
    @Override
    public String toString() {
	// the string representation being constructed
	StringBuilder sb = new StringBuilder("{");
	// an iterator over the elements in the support of this
	Iterator<BigFraction> it = dist.keySet().iterator();
	while (it.hasNext()) {
	    BigFraction s = it.next(); // knowledge-state
	    BigFraction p = dist.get(s); // probability of state
	    sb.append(knowledgeStateToString(s) + "@" + p);
	    if (it.hasNext()) {
		sb.append(", ");
	    }
	}
	sb.append("}");
	return sb.toString();
    }

    /**
     * Returns the string representation of a knowledge-state s.
     */
    private String knowledgeStateToString(BigFraction s) {
	return "{true@" + s + ", false@" + s.complement() + "}";
    }

    /**
     * Increase the likelihood of knowledge-state s in this by probability p.
     * 
     * @param s
     *            The knowledge-state that will have its likelihood increased.
     * @param p
     *            The probability by which the likelihood of s will be
     *            increased.
     * 
     * @throws NullPointerException
     *             If either s or p are null.
     * @throws InvalidProbabilityException
     *             If either p or s is not a probability.
     * @throws InvalidKnowledgeDistributionException
     *             If, as a result of this operation, the weight of the
     *             KnowledgeDistribution would exceed the value one.
     * 
     */
    public void add(BigFraction s, BigFraction p) {
	// check validity of parameters
	if (s == null || p == null) {
	    throw new NullPointerException("Parameters cannot be null.");
	}
	if (!p.isAProbability() || !s.isAProbability()) {
	    throw new InvalidProbabilityException(
		    "Parameters must be probabilities.");
	}
	// check that overall weight would not exceed one if this operation were
	// to be carried out
	if (!(weight().add(p)).isAProbability()) {
	    throw new InvalidKnowledgeDistributionException(
		    "Distribution weight cannot exceed one.");
	}
	// perform addition
	BigFraction w = weight(s).add(p); // the new likelihood of s
	if (!w.equals(BigFraction.ZERO)) {
	    dist.put(s, w);
	}
    }

    /**
     * Decreases the likelihood of knowledge-state s in this by probability p.
     * 
     * @param s
     *            The knowledge-state that will have its likelihood decreased.
     * @param p
     *            The probability by which the likelihood of s will be
     *            decreased.
     * 
     * @throws NullPointerException
     *             If either s or p are null.
     * @throws InvalidProbabilityException
     *             If either s or p is not a probability.
     * @throws InvalidKnowledgeDistributionException
     *             If, as a result of this operation, the likelihood of s would
     *             fall below zero.
     * 
     */
    public void subtract(BigFraction s, BigFraction p) {
	// check validity of parameters
	if (s == null || p == null) {
	    throw new NullPointerException("Parameters cannot be null.");
	}
	if (!p.isAProbability() || !s.isAProbability()) {
	    throw new InvalidProbabilityException(
		    "Parameters must be probabilities.");
	}
	// check that resulting distribution would be valid
	BigFraction w = weight(s).subtract(p); // the new likelihood of s
	if (w.compareTo(BigFraction.ZERO) < 0) {
	    throw new InvalidKnowledgeDistributionException(
		    "Distribution weight cannot be negative.");
	}
	// perform subtraction
	if (w.equals(BigFraction.ZERO)) {
	    // remove s from dist if its new weight is zero
	    dist.remove(s);
	} else {
	    // update likelihood of s in dist if new weight is non-zero
	    dist.put(s, w);
	}
    }

    /**
     * 
     * Recall that this KnowledgeDistributin may be used to describe the
     * likelihood of the spy being in each possible knowledge-state, following a
     * sequence of of interactions with informants using conditional two-coin
     * channels. <br>
     * <br>
     * 
     * This operation updates this KnowledgeDistribution to reflect the change
     * of knowledge that would result from an interaction with an informant
     * using conditional two-coin channel c.<br>
     * <br>
     * 
     * Recall that such an informant first checks to see if the knowledge-state
     * of the spy is c.getCondition(). If it is, then the informant reveals the
     * bias of both coins in c.getTwoCoinChannel() to the spy. Then, in private,
     * the informant flips coin1 if the secret is true, and coin2 if the secret
     * is false, and reveals the outcome of the coin flip to the spy (but not
     * which coin has been flipped).<br>
     * <br>
     * 
     * If the knowledge-state of the spy is not c.getCondition(), then the
     * informant reveals nothing and vanishes.<br>
     * <br>
     * 
     * Assume, for example that this knowledge distribution is currently:<br>
     * <br>
     * 
     * {{true@1/2, false@1/2}@1/4, {true@4/5, false@1/5}@3/4}<br>
     * <br>
     * 
     * Following an interaction with an informant using the conditional two-coin
     * channel <i>if true@1/2 then (3/4, 1/4)</i>, then the updated
     * KnowledgeDistribution will be:<br>
     * <br>
     * 
     * {{true@1/4, false@3/4}@1/8, {true@3/4, false@1/4}@1/8, {true@4/5,
     * false@1/5}@3/4}
     * 
     * @param c
     *            The conditional two-coin channel that will be used to update
     *            this KnowledgeDistribution
     * 
     * @throws NullPointerException
     *             If c is null.
     */
    public void update(ConditionalTwoCoinChannel c) {
	// the knowledge-state that this update is conditional on
	BigFraction aPriori = c.getCondition();
	// the probability that the spy is in knowledge-state aPriori
	BigFraction p = weight(aPriori);
	if (p.equals(BigFraction.ZERO)) {
	    // this revelation won't affect the knowledge distribution
	    return;
	} else {
	    // split knowledge-state using revelation
	    this.subtract(aPriori, p);
	    BigFraction heads = c.outcomeProbability(true); // prob. heads
	    BigFraction tails = c.outcomeProbability(false); // prob. tails
	    if (!heads.equals(BigFraction.ZERO)) {
		this.add(c.aPosteriori(true), p.multiply(heads));
	    }
	    if (!tails.equals(BigFraction.ZERO)) {
		this.add(c.aPosteriori(false), p.multiply(tails));
	    }
	}
    }

    /**
     * This operation updates this KnowledgeDistribution to reflect the change
     * of knowledge that would result from interactions with each informant in
     * the list informants, in the order in which they appear in that list.
     * 
     * @param informants
     *            The list of conditional two-coin channels that will be used to
     *            update this KnowledgeDistribution
     * 
     * @throws NullPointerException
     *             If informants is null or contains null TwoCoinChannels.
     */
    public void update(List<ConditionalTwoCoinChannel> informants) {
	if (informants == null || informants.contains(null)) {
	    throw new NullPointerException(
		    "Parameter cannot be null or contain null elements");
	}
	for (ConditionalTwoCoinChannel ch : informants) {
	    update(ch);
	}
    }

    /**
     * Determines whether this knowledge sub-distribution is internally
     * consistent.
     * 
     * @return true if this KnowledgeDistribution is internally consistent, and
     *         false otherwise
     */
    public boolean checkInv() {
	// check for null values
	if (dist == null || dist.values().contains(null)) {
	    return false;
	}
	// check that each of the knowledge-states are probabilities
	for (BigFraction w : dist.keySet()) {
	    if (!w.isAProbability()) {
		return false;
	    }
	}
	// check that weights for each knowledge-state are non-zero
	// probabilities
	for (BigFraction w : dist.values()) {
	    if (!w.isAProbability() || w.equals(BigFraction.ZERO)) {
		return false;
	    }
	}
	// check that the sum of the values in dist is a probability
	BigFraction weight = BigFraction.ZERO; // calculated weight
	for (BigFraction w : dist.values()) {
	    weight = weight.add(w);
	}
	if (!weight.isAProbability()) {
	    return false;
	}
	// everything is OK
	return true;
    }

}
