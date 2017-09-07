package csse2002.security;

import java.io.*;
import java.util.*;

import csse2002.math.*;

public class InformantsReader {

	/**
	 * @require fileName != null
	 * 
	 * @ensure This method reads a text file from fileName with zero or more
	 *         lines, each of which contains data for a TwoCoinChannel. Each
	 *         line of the file should contain two probabilities separated by
	 *         one or more whitespaces. The first probability represents the
	 *         heads-bias of the first coin of the channel (thrown if the secret
	 *         is true), and the second the heads-bias of the second coin of the
	 *         channel (thrown if the secret is false). Each probability is
	 *         denoted either by a single integer, or by an expression of the
	 *         form X/Y, where X is an integer and Y is an integer.
	 * 
	 *         The method throws IOException if there is an input error with the
	 *         input file; otherwise it throws FileFormatException if there is
	 *         an error with the input format, otherwise it returns a list of of
	 *         informants containing each ConditionalTwoCoinChannel from the
	 *         file, in the order in which they appear in the input file.
	 */
	public static List<TwoCoinChannel> readInformants(String fileName)
			throws FileFormatException, IOException {
		FileReader fr = new FileReader(fileName); // the file to read from
		Scanner in = new Scanner(fr); // scanner for reading the file
		int lineNumber = 0; // the line of the file being read
		// empty list to store read informants
		List<TwoCoinChannel> informants = new ArrayList<TwoCoinChannel>();

		// read each informant, one per line, from the file
		while (in.hasNextLine()) {
			lineNumber++;
			String line = in.nextLine();
			informants.add(readInformant(line, lineNumber));
		}
		return informants;
	}

	/**
	 * 
	 * @require s!=null
	 * @ensure Reads and returns the informant from line, throwing a
	 *         FileFormatException if s does not have the right format.
	 */
	private static TwoCoinChannel readInformant(String line, int lineNumber)
			throws FileFormatException {
		Scanner ls = new Scanner(line); // for reading line
		// heads-bias of first and second coins, respectively.
		BigFraction coin1 = readProbability(ls, 1, lineNumber);
		BigFraction coin2 = readProbability(ls, 2, lineNumber);
		// check that there is more input on line
		if (ls.hasNext()) {
			throw new FileFormatException("Line " + lineNumber
					+ ": extra data.");
		}
		// create and return channel with coins read
		return new TwoCoinChannel(coin1, coin2);
	}

	/**
	 * @require ls != null
	 * @ensure Throws FileFormatException is there is an issue reading the next
	 *         probability from ls, else reads and returns the next probability
	 *         from the Scanner ls.
	 */
	private static BigFraction readProbability(Scanner ls, int coinNo,
			int lineNumber) throws FileFormatException {
		BigFraction coin;
		if (ls.hasNext()) {
			// try to read the coin coinNo
			String probString = ls.next(); // string containing probability
			try {
				coin = readProbability(probString);
			} catch (InvalidProbabilityException e) {
				throw new FileFormatException("Line " + lineNumber
						+ ": invalid coin " + coinNo + " " + probString);
			}
		} else {
			// no coin to read
			throw new FileFormatException("Line " + lineNumber
					+ ": missing coin " + coinNo + ".");
		}
		return coin;
	}

	/**
	 * @require probString!=null
	 * @ensure Throws InvalidProbabilityException if probString cannot be parsed
	 *         to a probability, else returns probability parsed from
	 *         probString.
	 */
	private static BigFraction readProbability(String probString)
			throws InvalidProbabilityException {
		Scanner s = new Scanner(probString); // to parse probability
		s.useDelimiter("/");
		int numerator = getNumerator(s); // the numerator of probability
		int denominator = getDenominator(s); // the denominator of probability
		BigFraction p = new BigFraction(numerator, denominator); // probability
		if (!p.isAProbability()) {
			throw new InvalidProbabilityException();
		}
		return p;
	}

	/**
	 * @require ls != null
	 * @ensure Reads and returns numerator from Scanner ls. Throws an
	 *         InvalidProbabilityException if the next token in ls is not a
	 *         valid integer.
	 */
	private static int getNumerator(Scanner ls)
			throws InvalidProbabilityException {
		int numerator; // the numerator to be read
		if (ls.hasNextInt()) {
			numerator = ls.nextInt();
		} else {
			throw new InvalidProbabilityException("invalid numerator");
		}
		return numerator;
	}

	/**
	 * @require ls != null
	 * @ensure Reads denominator from Scanner ls if it is given. If no
	 *         denominator is given the method returns integer 1. Throws an
	 *         InvalidProbabilityException if denominator given is zero or it
	 *         isn't a valid integer.
	 */
	private static int getDenominator(Scanner ls)
			throws InvalidProbabilityException {
		int denominator; // the denominator to be read
		// read and check denominator
		if (ls.hasNext() && ls.hasNextInt()) {
			denominator = ls.nextInt();
			if (denominator == 0) {
				throw new InvalidProbabilityException("invalid denominator");
			}
		} else if (ls.hasNext() && !ls.hasNextInt()) {
			throw new InvalidProbabilityException("invalid denominator");
		} else {
			denominator = 1;
		}
		return denominator;
	}

}
