package csse2002.security.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import csse2002.math.*;
import csse2002.security.*;

/**
 * Basic tests for the {@link KnowledgeDistribution} implementation class. A
 * much much more extensive test suite will be performed for assessment of your
 * code, but this should get you started.
 */
public class KnowledgeDistributionTest {

	/**
	 * A basic test of the first two constructors.
	 */
	@Test
	public void basicConstructorTests() {

		// check constructor with no parameters

		KnowledgeDistribution k = new KnowledgeDistribution();
		// check that the weight of the distribution is zero.
		Assert.assertEquals(BigFraction.ZERO, k.weight());
		// check that the weight of some knowledge-state is zero.
		Assert.assertEquals(BigFraction.ZERO, k.weight(new BigFraction(1, 2)));
		// check toString method
		Assert.assertEquals("{}", k.toString());
		// test iteration over elements in the support of the distribution
		Iterator<BigFraction> it = k.iterator();
		Assert.assertFalse(it.hasNext());
		Assert.assertTrue(k.checkInv());

		// check point-distribution constructor

		BigFraction p = new BigFraction(1, 3);
		k = new KnowledgeDistribution(p);
		// check that the weight of the distribution is one.
		Assert.assertEquals(BigFraction.ONE, k.weight());
		// check that the weight of knowledge-state p is one.
		Assert.assertEquals(BigFraction.ONE, k.weight(p));
		// check that the weight of some other knowledge-state is zero.
		Assert.assertEquals(BigFraction.ZERO, k.weight(new BigFraction(1, 2)));
		// check toString method
		Assert.assertEquals("{{true@1/3, false@2/3}@1}", k.toString());
		// test iteration over elements in the support of the distribution
		it = k.iterator();
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(p, it.next());
		Assert.assertFalse(it.hasNext());
		Assert.assertTrue(k.checkInv());

		// test copy distribution constructor
		// ...

	}

	/**
	 * Test that one of the exceptions for the point-distribution constructor is
	 * correctly thrown.
	 */
	@Test(expected = InvalidProbabilityException.class)
	public void constructorInvalidProbabilityTest() {
		BigFraction p = new BigFraction(4, 3);
		KnowledgeDistribution k = new KnowledgeDistribution(p);
	}

	/**
	 * A basic test of the addition method.
	 */
	@Test
	public void basicAdditionTest() {
		BigFraction p1 = new BigFraction(1, 4);
		BigFraction w1 = new BigFraction(1, 2);

		BigFraction p2 = new BigFraction(1, 8);
		BigFraction w2 = new BigFraction(1, 3);

		KnowledgeDistribution k = new KnowledgeDistribution();
		k.add(p1, w1);
		k.add(p2, w2);
		// check the weight of the distribution
		Assert.assertEquals(new BigFraction(5, 6), k.weight());
		// check the weight of knowledge-state p1.
		Assert.assertEquals(w1, k.weight(p1));
		// check the weight of knowledge-state p2.
		Assert.assertEquals(w2, k.weight(p2));
		// check that the weight of some other knowledge-state is zero.
		Assert.assertEquals(BigFraction.ZERO, k.weight(new BigFraction(1, 2)));
		// check toString method
		Assert.assertEquals(
				"{{true@1/8, false@7/8}@1/3, {true@1/4, false@3/4}@1/2}",
				k.toString());
		// test iteration over elements in the support of the distribution
		Iterator<BigFraction> it = k.iterator();
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(p2, it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(p1, it.next());
		Assert.assertFalse(it.hasNext());
		Assert.assertTrue(k.checkInv());
	}

	/**
	 * Test that one of the exceptions for the addition method is correctly
	 * thrown.
	 */
	@Test(expected = InvalidKnowledgeDistributionException.class)
	public void addTooMuchWeightTest() {
		BigFraction p1 = new BigFraction(1, 4);
		BigFraction w1 = new BigFraction(3, 4);

		BigFraction p2 = new BigFraction(1, 8);
		BigFraction w2 = new BigFraction(1, 3);

		KnowledgeDistribution k = new KnowledgeDistribution();
		k.add(p1, w1);
		k.add(p2, w2);
	}

	/**
	 * A basic test of the update method.
	 */
	@Test
	public void basicUpdateTest() {
		BigFraction p = new BigFraction(1, 2);
		KnowledgeDistribution k = new KnowledgeDistribution(p);

		ConditionalTwoCoinChannel c1 = new ConditionalTwoCoinChannel(
				p,
				new TwoCoinChannel(new BigFraction(3, 4), new BigFraction(1, 4)));
		ConditionalTwoCoinChannel c2 = new ConditionalTwoCoinChannel(
				new BigFraction(3, 4), new TwoCoinChannel(
						new BigFraction(1, 3), new BigFraction(1, 2)));

		k.update(c1);
		// check toString method
		Assert.assertEquals(
				"{{true@1/4, false@3/4}@1/2, {true@3/4, false@1/4}@1/2}",
				k.toString());
		Assert.assertTrue(k.checkInv());

		k.update(c2);
		// check toString method
		Assert.assertEquals(
				"{{true@1/4, false@3/4}@1/2, {true@2/3, false@1/3}@3/16, {true@4/5, false@1/5}@5/16}",
				k.toString());
		Assert.assertTrue(k.checkInv());
	}

}
