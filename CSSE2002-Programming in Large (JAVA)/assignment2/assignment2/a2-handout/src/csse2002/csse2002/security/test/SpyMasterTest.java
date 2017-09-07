package csse2002.security.test;

import org.junit.Assert;
import org.junit.Test;

import csse2002.math.*;
import csse2002.security.*;
import java.util.*;
import java.io.*;

/**
 * Basic tests for the {@link SpyMaster} implementation class. A much more
 * extensive test suite will be performed for assessment of your code, but this
 * should get you started.
 */
public class SpyMasterTest {

	// TESTS for readInformants

	/**
	 * Test reading from a typical, well-formatted file of informants.
	 */
	@Test
	public void testBasicReadInformants() throws FileFormatException,
			IOException {
		List<ConditionalTwoCoinChannel> expectedInformants =
				new ArrayList<ConditionalTwoCoinChannel>();
		expectedInformants
				.add(new ConditionalTwoCoinChannel(new BigFraction(1, 2),
						new TwoCoinChannel(BigFraction.ZERO, new BigFraction(1,
								6))));
		expectedInformants.add(new ConditionalTwoCoinChannel(new BigFraction(6,
				11), new TwoCoinChannel(new BigFraction(1, 6), new BigFraction(
				2, 10))));

		List<ConditionalTwoCoinChannel> actualInformants =
				SpyMaster.readInformants("input1.txt");
		Assert.assertEquals(expectedInformants, actualInformants);
	}

	/**
	 * Test reading from a file that does not exist.
	 */
	@Test(expected = IOException.class)
	public void testReadInformantsIOError() throws FileFormatException,
			IOException {
		List<ConditionalTwoCoinChannel> actualInformants =
				SpyMaster.readInformants("doesNotExist.txt");
	}

	/**
	 * Test reading from a poorly-formatted file of informants: missing
	 * probability on a line.
	 */
	@Test(expected = FileFormatException.class)
	public void testReadInformantsFormatError() throws FileFormatException,
			IOException {
		List<ConditionalTwoCoinChannel> actualInformants =
				SpyMaster.readInformants("input2.txt");
	}

	/**
	 * Test reading from a poorly-formatted file of informants: contains an
	 * invalid probability.
	 */
	@Test(expected = FileFormatException.class)
	public void testReadInformantsFormatProbabilityError()
			throws FileFormatException, IOException {
		List<ConditionalTwoCoinChannel> actualInformants =
				SpyMaster.readInformants("input3.txt");
	}

	// TESTS for findInformants

	/**
	 * Test finding informants in special case that no new informants are
	 * needed, and the spies have point distributions.
	 */
	@Test
	public void testInformantsNotRequiredSpecialCase() {
		// initial secret probability
		BigFraction aPriori = new BigFraction(1, 3);
		// create empty lists of informants
		List<List<ConditionalTwoCoinChannel>> informants =
				getEmptyInformantsList();
		// add informants so that spy0 = {{true@1/3, false@2/3}@1}
		// add informants so that spy1 = {{true@1/3, false@2/3}@1}

		SpyMaster.findAdditionalInformants(aPriori, informants);
		// Calculate the actual distribution of both spies after all initial and
		// additional informants have visited.
		KnowledgeDistribution actualSpy0 =
				new KnowledgeDistribution(aPriori, informants.get(0));
		KnowledgeDistribution actualSpy1 =
				new KnowledgeDistribution(aPriori, informants.get(1));
		String expectedSpy = "{{true@1/3, false@2/3}@1}";
		Assert.assertEquals(expectedSpy, actualSpy0.toString());
		Assert.assertEquals(expectedSpy, actualSpy1.toString());
	}

	/**
	 * Test finding informants in special case that no new informants are
	 * needed, but the spies have distributions with support > 1.
	 */
	@Test
	public void testInformantsNotRequired() {
		// initial secret probability
		BigFraction aPriori = new BigFraction(1, 2);
		// create empty lists of informants
		List<List<ConditionalTwoCoinChannel>> informants =
				getEmptyInformantsList();

		// add informants so that
		// kd0 =
		// {{{true@1/3, false@2/3}@1/4,
		// {true@1/2, false@1/2}@1/6,
		// {true@4/7, false@3/7}@7/12}
		informants.get(0).add(
				new ConditionalTwoCoinChannel(aPriori, new TwoCoinChannel(
						new BigFraction(1, 6), new BigFraction(1, 3))));
		informants.get(0).add(
				new ConditionalTwoCoinChannel(new BigFraction(5, 9),
						new TwoCoinChannel(new BigFraction(1, 5),
								new BigFraction(1, 4))));
		// add informants so that
		// kd1 =
		// {{{true@1/3, false@2/3}@1/4,
		// {true@1/2, false@1/2}@1/6,
		// {true@4/7, false@3/7}@7/12}
		informants.get(1).add(
				new ConditionalTwoCoinChannel(aPriori, new TwoCoinChannel(
						new BigFraction(1, 3), new BigFraction(1, 2))));
		informants.get(1).add(
				new ConditionalTwoCoinChannel(new BigFraction(2, 5),
						new TwoCoinChannel(new BigFraction(1, 2),
								new BigFraction(2, 3))));

		SpyMaster.findAdditionalInformants(aPriori, informants);
		// Calculate the actual distribution of both spies after all initial and
		// additional informants have visited.
		KnowledgeDistribution actualSpy0 =
				new KnowledgeDistribution(aPriori, informants.get(0));
		KnowledgeDistribution actualSpy1 =
				new KnowledgeDistribution(aPriori, informants.get(1));
		String expectedSpy =
				"{{true@1/3, false@2/3}@1/4, {true@1/2, false@1/2}@1/6,"
						+ " {true@4/7, false@3/7}@7/12}";
		Assert.assertEquals(expectedSpy, actualSpy0.toString());
		Assert.assertEquals(expectedSpy, actualSpy1.toString());
	}

	/**
	 * Test finding informants in special case that new informants are only
	 * required for the second spy.
	 */
	@Test
	public void testInformantsRequiredSecondSpyOnly() {
		// initial secret probability
		BigFraction aPriori = new BigFraction(1, 2);
		// create empty lists of informants
		List<List<ConditionalTwoCoinChannel>> informants =
				getEmptyInformantsList();

		// add informants so that
		// kd0 =
		// {{{true@1/3, false@2/3}@1/4,
		// {true@1/2, false@1/2}@1/6,
		// {true@4/7, false@3/7}@7/12}
		informants.get(0).add(
				new ConditionalTwoCoinChannel(aPriori, new TwoCoinChannel(
						new BigFraction(1, 6), new BigFraction(1, 3))));
		informants.get(0).add(
				new ConditionalTwoCoinChannel(new BigFraction(5, 9),
						new TwoCoinChannel(new BigFraction(1, 5),
								new BigFraction(1, 4))));
		// add informants so that
		// kd1 = {{true@2/5, false@3/5}@5/12, {true@4/7, false@3/7}@7/12}
		informants.get(1).add(
				new ConditionalTwoCoinChannel(aPriori, new TwoCoinChannel(
						new BigFraction(1, 3), new BigFraction(1, 2))));

		SpyMaster.findAdditionalInformants(aPriori, informants);
		// Calculate the actual distribution of both spies after all initial and
		// additional informants have visited.
		KnowledgeDistribution actualSpy0 =
				new KnowledgeDistribution(aPriori, informants.get(0));
		KnowledgeDistribution actualSpy1 =
				new KnowledgeDistribution(aPriori, informants.get(1));
		String expectedSpy =
				"{{true@1/3, false@2/3}@1/4, {true@1/2, false@1/2}@1/6, "
						+ "{true@4/7, false@3/7}@7/12}";
		Assert.assertEquals(expectedSpy, actualSpy0.toString());
		Assert.assertEquals(expectedSpy, actualSpy1.toString());
	}

	/**
	 * Test finding informants in typical case that new informants are required
	 * for both spies.
	 */
	@Test
	public void testInformantsRequiredBothSpies() {
		// initial secret probability
		BigFraction aPriori = new BigFraction(1, 2);
		// create empty lists of informants
		List<List<ConditionalTwoCoinChannel>> informants =
				getEmptyInformantsList();

		// add informants so that
		// kd0 =
		// {{true@0, false@1}@1/12,
		// {true@1/2, false@1/2}@1/6,
		// {true@5/9,false@4/9}@3/4}
		informants.get(0).add(
				new ConditionalTwoCoinChannel(aPriori, new TwoCoinChannel(
						BigFraction.ZERO, new BigFraction(1, 6))));
		informants.get(0).add(
				new ConditionalTwoCoinChannel(new BigFraction(6, 11),
						new TwoCoinChannel(new BigFraction(1, 6),
								new BigFraction(1, 5))));

		// add informants so that
		// kd1 =
		// {{true@1/3, false@2/3}@1/4,
		// {true@1/2,false@1/2}@1/6,
		// {true@4/7, false@3/7}@7/12}
		informants.get(1).add(
				new ConditionalTwoCoinChannel(aPriori, new TwoCoinChannel(
						new BigFraction(1, 6), new BigFraction(1, 3))));
		informants.get(1).add(
				new ConditionalTwoCoinChannel(new BigFraction(5, 9),
						new TwoCoinChannel(new BigFraction(1, 5),
								new BigFraction(1, 4))));

		SpyMaster.findAdditionalInformants(aPriori, informants);
		// Calculate the actual distribution of both spies after all initial and
		// additional informants have visited.
		KnowledgeDistribution actualSpy0 =
				new KnowledgeDistribution(aPriori, informants.get(0));
		KnowledgeDistribution actualSpy1 =
				new KnowledgeDistribution(aPriori, informants.get(1));

		// check that final distribution of both spies is as expected
		String expectedSpy =
				"{{true@0, false@1}@1/12, {true@1/2, false@1/2}@1/3,"
						+ " {true@4/7, false@3/7}@7/12}";
		Assert.assertEquals(expectedSpy, actualSpy0.toString());
		Assert.assertEquals(expectedSpy, actualSpy1.toString());
	}

	// HELPER METHODS

	/**
	 * @return An empty list of two lists of informants.
	 */
	public List<List<ConditionalTwoCoinChannel>> getEmptyInformantsList() {
		List<List<ConditionalTwoCoinChannel>> informants =
				new ArrayList<List<ConditionalTwoCoinChannel>>();
		informants.add(new ArrayList<ConditionalTwoCoinChannel>());
		informants.add(new ArrayList<ConditionalTwoCoinChannel>());
		return informants;
	}

}
