package csse2002.security;

import csse2002.math.*;

/**
 * Informants are privy to the value of a Boolean-valued secret. An informant
 * may reveal some information about the secret to a spy using a two-coin
 * channel. <br>
 * <br>
 * 
 * Having chosen biased two coins, the first, coin1, with heads-bias c1 and the
 * second, coin2, with heads-bias c2, the informant reveals the bias of both
 * coin1 and coin2 to the spy. Then, in private, the informant flips coin1 if
 * the secret is true, and coin2 if the secret is false, and reveals the outcome
 * of the coin flip to the spy (but not which coin has been flipped).<br>
 * <br>
 * 
 * Before the spy learns information from the informant she knows the likelihood
 * that the secret is true, and the likelihood that it is false.<br>
 * <br>
 * 
 * After the informant has revealed to the spy the outcome of the coin flip, the
 * spy can update her knowledge of the distribution of the secret, given the
 * outcome of the coin flip.<br>
 * <br>
 * 
 * The immutable class TwoCoinChannel can be used to keep track of the coins
 * used by an informant, and to calculate, given a spy's <i>a priori</i>
 * knowledge of the secret (i.e. the initial distribution of the secret):<br>
 * <br>
 * 
 * (a) The likelihood that the outcome of the coin flip will be heads or tails,
 * and<br>
 * <br>
 * 
 * (b) The <i>a posteriori</i> distribution of the secret given the outcome is
 * heads (or tails). (i.e. the likelihood that the secret is true or false given
 * that the outcome is heads (or tails)).<br>
 * <br>
 * 
 * For example, if coin1 has heads-bias 3/4 and coin2 has head-bias 1/4 and the
 * spy initially knows that the secret is true with probability 1/2 and false
 * with probability 1/2, then:<br>
 * 
 * 1. The probability that the outcome of the private-coin-flip will be heads
 * is: 1/2*3/4 + 1/2*1/4 = 1/2<br>
 * 
 * 2. The probability that the outcome of the private-coin-flip will be tails
 * is: 1/2*1/4 + 1/2*3/4 = 1/2<br>
 * 
 * 3. The probability that the secret is true given that the outcome is heads
 * is: 1/2*3/4 divided by 1/2 = 3/4<br>
 * 
 * 4. The probability that the secret is true given that the outcome is tails
 * is: 1/2*1/4 divided by 1/2 = 1/4<br>
 */

public class TwoCoinChannel {
	
	private BigFraction c1; //the bias of the first coin towards outcome heads
	private BigFraction c2; //The bias of the second coin towards outcome heads

	/**
	 * Creates a new channel with a coin with heads-bias c1 for secret true and
	 * c2 for secret false.
	 * 
	 * @param c1
	 *            The bias of the first coin towards outcome heads.
	 * @param c2
	 *            The bias of the second coin towards outcome heads.
	 * 
	 * @throws NullPointerException
	 *             If either c1 or c2 is null
	 * 
	 * @throws InvalidProbabilityException
	 *             If c1 or c2 are not probabilities.
	 */
	public TwoCoinChannel(BigFraction c1, BigFraction c2) 
			throws NullPointerException, InvalidProbabilityException {
				
		//when c1 or c2 is null, throw exception
		if (c1 == null || c2 == null) {
			throw new NullPointerException("The parameter is null");
		}
		
		//when c1 or c2 is not a probability, throw exception
		if (!c1.isAProbability() || !c2.isAProbability() ) {
			throw new InvalidProbabilityException("The parameter is not a probability");
		}
		
		//create new c1 and c2
		this.c1 = c1;
		this.c2 = c2;
	}

	/**
	 * Returns the probability that the outcome of the private coin flip will be
	 * (outcome? heads: tails) given that the secret is initially known to be
	 * true with probability aPriori (and false with the complement of aPriori).
	 * 
	 * @throws NullPointerException
	 *             If aPriori is null.
	 * 
	 * @throws InvalidProbabilityException
	 *             If aPriori is not a probability.
	 */
	public BigFraction outcomeProbability(BigFraction aPriori, boolean outcome) 
			throws NullPointerException, InvalidProbabilityException {
		
		//when aPriori is null, throw exception
		if (aPriori == null) {
			throw new NullPointerException("This parameter is null");
		}
		
		//when aPriori is not a probability, throw exception
		if (!aPriori.isAProbability()) {
			throw new InvalidProbabilityException("The parameter is not a probability");
		}
		
		/*calculate the probability of outcome of the private coin flip will be 
		  (outcome? heads: tails)*/ 
		else {
			if (outcome == true) {
				//The probability that the outcome of the private-coin-flip will be heads
				//c1 * aPriori + c2 * (1 - aPriori)
				return (c1.multiply(aPriori)).add(c2.multiply(aPriori.complement()));
			}
			else {
				//The probability that the outcome of the private-coin-flip will be tails
				//(1 - c1) * aPriori + (1 - c2) * (1 - aPriori);
				return (c1.complement().multiply(aPriori))
						.add(c2.complement().multiply(aPriori.complement()));
			}
		}
	}
	

	/**
	 * Given that the secret is initially known to be true with probability
	 * aPriori (and false with the remaining probability), this method returns: <br>
	 * <br>
	 * 
	 * 1. BigFraction.ZEROBigFraction.ZERO, if the probability of the outcome (outcome? heads:
	 * tails) is zero, and <br>
	 * 
	 * 2. the probability that the secret is true, given that the outcome of the
	 * private coin flip is (outcome? heads: tails), otherwise.
	 * 
	 * @throws NullPointerException
	 *             If aPriori is null.
	 * 
	 * @throws InvalidProbabilityException
	 *             If aPriori is not a probability.
	 */
	public BigFraction aPosteriori(BigFraction aPriori, boolean outcome) 
			throws NullPointerException, InvalidProbabilityException {
		
		//when aPriori is null, throw exception
		if (aPriori == null) {
			throw new NullPointerException("This parameter is null");
		}
		
		//when aPriori is not a probability, throw exception
		if (!aPriori.isAProbability()) {
			throw new InvalidProbabilityException("The parameter is not a probability");
		}
		
		//if the probability is zero, return BigFraction.ZERO
		if ((outcomeProbability(aPriori, outcome)).compareTo(BigFraction.ZERO) == 0) {
			return BigFraction.ZERO;
		}
		else {
			if (outcome == true) {
				//The probability that the secret is true given that the outcome is heads
				//c1 * aPriori / outcomeProbability(aPoriori, outcome)
				return (c1.multiply(aPriori)).divide(outcomeProbability(aPriori, outcome));
			}
			else {
				//The probability that the secret is true given that the outcome is tails
				//(1 - c1) * aPriori / outcomeProbability(aPoriori, outcome);
				return (c1.complement().multiply(aPriori))
						.divide(outcomeProbability(aPriori, outcome));
			}	
		}
	}

	/**
	 * Returns the bias (towards heads) of the coin that will be flipped if the
	 * secret has value s.
	 * 
	 * @param s
	 *            The secret for which the coin bias will be returned.
	 * @return The bias (towards heads) of the coin that will be flipped if the
	 *         secret has value s.
	 */
	public BigFraction getCoinBias(boolean s) {
		//when secret is true, return c1
		if (s == true) {
			return c1;
		}
		//when secret is false, return c2
		else {
			return c2;
		}
	}

	/**
	 * Returns the String:
	 * 
	 * "(C1, C2)"
	 * 
	 * where C1 is the heads-bias of the coin for secret true, and C2 is the
	 * heads-bias of the coin for secret false. (The biases are represented
	 * using the toString-representation of BigFraction.)
	 */
	@Override
	public String toString() {
		return "(" + c1 + ", " + c2 +")";
	}

	/**
	 * Returns true iff o is a TwoCoinChannel with the same coin bias as this
	 * for secret true, and the same coin bias as this for secret false. Two
	 * coin biases are considered to be the same if they represent the same
	 * rational value.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TwoCoinChannel)) {
			return false;
		}
		//two channels with the same c1 and c2 are considered equal 
		TwoCoinChannel c = (TwoCoinChannel) o;
		return c1.equals(c.c1) && c2.equals(c.c2);
	}
		
	@Override
	public int hashCode() {
		//calculate hashCode
		int result=10;
		result=10000*result+c1.hashCode();
		result=10000*result+c2.hashCode();
		return result;
	}
	
	/**
	 * Determines whether this class is internally consistent.
	 * 
	 * @return true if this class is internally consistent, and false otherwise
	 */
	public boolean checkInv() {
		
		//when c1 or c2 is null, return false
		if (c1 == null || c2 == null) {
			return false;
		}
		
		//when c1 or c2 is not a probability, return fasle
		if (!c1.isAProbability() || !c2.isAProbability()) {
			return false;
		}
		return true;
	}
}
