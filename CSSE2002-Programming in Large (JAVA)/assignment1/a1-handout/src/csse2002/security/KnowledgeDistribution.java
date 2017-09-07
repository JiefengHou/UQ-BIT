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

	/**
	 * Creates a new empty KnowledgeDistribution with zero weight.
	 */
	public KnowledgeDistribution() {
		// REMOVE THIS LINE AND WRITE THIS METHOD
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
		// REMOVE THIS LINE AND WRITE THIS METHOD
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
		// REMOVE THIS LINE AND WRITE THIS METHOD
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
		return null; // REMOVE THIS LINE AND WRITE THIS METHOD
	}

	/**
	 * Returns the combined probability of all the knowledge-states in the
	 * support of the KnowledgeDistribution.
	 * 
	 * @return The probability of all the knowledge-states in the support of
	 *         this added together.
	 */
	public BigFraction weight() {
		return null; // REMOVE THIS LINE AND WRITE THIS METHOD
	}

	/**
	 * Returns an iterator over the knowledge-states in the <i>support</i> of
	 * this distribution. The support of this distribution is defined as the
	 * knowledge-states with non-zero probability in this. <br>
	 * <br>
	 * 
	 * The knowledge-states returned are represented by BigIntegers, describing
	 * the probability that the secret is true in that knowledge-state.<br>
	 * <br>
	 * 
	 * The knowledge-states, represented by BigIntegers, are returned in
	 * (ascending) sorted order according to the natural ordering of BigInteger:
	 * that is, knowledge-states are returned ordered by their likelihood that
	 * the secret is true, with the knowledge-state with the smallest
	 * probability that the secret is true being returned first.<br>
	 * <br>
	 * 
	 * The iterator's behaviour is not defined if the KnowledgeDistribution is
	 * modified after it has been created. (That is, don't use this method to
	 * get an iterator, modify the KnowledgeDistribution, and then try to use
	 * the iterator.)
	 * 
	 */
	public Iterator<BigFraction> iterator() {
		return null; // REMOVE THIS LINE AND WRITE THIS METHOD
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
		return null; // REMOVE THIS LINE AND WRITE THIS METHOD
	}

	/**
	 * Increase the likelihood of knowledge-state s in this by probability p.
	 * 
	 * @param s
	 *            The knowledge-distribution that will have its likelihood
	 *            increased.
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
		// REMOVE THIS LINE AND WRITE THIS METHOD
	}

	/**
	 * Decreases the likelihood of knowledge-state s in this by probability p.
	 * 
	 * @param s
	 *            The knowledge-distribution that will have its likelihood
	 *            decreased.
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
		// REMOVE THIS LINE AND WRITE THIS METHOD
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
		// REMOVE THIS LINE AND WRITE THIS METHOD
	}

	/**
	 * Determines whether this knowledge sub-distribution is internally
	 * consistent.
	 * 
	 * @return true if this KnowledgeDistribution is internally consistent, and
	 *         false otherwise
	 */
	public boolean checkInv() {
		return true; // REMOVE THIS LINE AND WRITE THIS METHOD
	}

}
