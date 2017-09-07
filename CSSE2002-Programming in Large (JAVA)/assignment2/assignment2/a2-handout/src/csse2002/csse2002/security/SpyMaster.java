package csse2002.security;

import java.util.*;
import java.io.*;
import csse2002.math.*;

public class SpyMaster {

	/**
	 * @require fileName != null
	 * 
	 * @ensure This method reads a text file from fileName with zero or more
	 *         lines, each of which contains data for a
	 *         ConditionalTwoCoinChannel. Each line of the file should contain
	 *         three probabilities separated by one or more whitespaces. The
	 *         first probability represents the condition of the
	 *         ConditionalTwoCoinChannel, the second the heads-bias of the first
	 *         coin of the channel (thrown if the secret is true), and the third
	 *         the heads-bias of the second coin of the channel (thrown if the
	 *         secret is false). Each probability is denoted either by a single
	 *         integer, or by an expression of the form X/Y, where X is an
	 *         integer and Y is an integer.
	 * 
	 *         The method throws IOException if there is an input error with the
	 *         input file; otherwise it throws FileFormatException if there is
	 *         an error with the input format, otherwise it returns a list of of
	 *         informants containing each ConditionalTwoCoinChannel from the
	 *         file, in the order in which they appear in the input file.
	 */
	public static List<ConditionalTwoCoinChannel> readInformants(String fileName)
			throws FileFormatException, IOException {
		return null; // REMOVE THIS LINE AND WRITE THIS METHOD
	}

	/**
	 * @require Parameter aPriori is a probability. Parameter informants is a
	 *          list containing two (possibly empty) lists of non-null
	 *          ConditionalTwoCoinChannels. (As such, neither parameter is null
	 *          or contains null-values.)
	 * 
	 * @ensure This method extends each list \old(informants).get(0) and
	 *         \old(informants).get(1) with zero or more informants such that
	 * 
	 *         kd0 "is equivalent to" kd1
	 * 
	 *         for kd0 = new KnowledgeDistribution(aPriori, informants.get(0))
	 *         and kd1 = new KnowledgeDistribution(aPriori, informants.get(1))
	 * 
	 *         and
	 * 
	 *         for any alternative extension of these lists informants' such
	 *         that
	 * 
	 *         kd0' "is equivalent to" kd1'
	 * 
	 *         for kd0' = new KnowledgeDistribution(aPriori, informants'.get(0))
	 *         and kd1' = new KnowledgeDistribution(aPriori, informants'.get(1))
	 * 
	 *         we have that
	 * 
	 *         kd0 is "at least as secure as" kd0'.
	 * 
	 *         Any two KnowledgeDistributions kd and kd' are "equivalent to" one
	 *         another if for each knowledge-state ks, the likelihood of ks in
	 *         kd is equal to the likelihood of ks in kd'.
	 * 
	 *         A KnowledgeDistirbution kd is "at least as secure as" kd' if
	 *         there exists a possibly empty list of informants x such that
	 * 
	 *         kd.update(x) "is equivalent to" kd'.
	 * 
	 *         HINT: Use the algorithm from the assignment sheet!
	 * 
	 */
	public static void findAdditionalInformants(BigFraction aPriori,
			List<List<ConditionalTwoCoinChannel>> informants) {
		// REMOVE THIS LINE AND WRITE THIS METHOD
	}

}
