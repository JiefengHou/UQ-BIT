package part2.test;

import org.junit.*;

import part2.ExpenditureTrees;
import adt.*;

import java.util.*;

/**
 * Basic tests for the methods summary, contains and negation from class
 * ExpenditureTrees. A much more extensive test suite will be performed for
 * assessment of your code, but this should get you started.
 */
public class ExpenditureTreeTest {

	/**
	 * A basic test for the summary method.
	 */
	@Test
	public void testSummary() {
		Tree<String> tree = getExampleTree();

		List<String> types = new ArrayList<String>(Arrays.asList(
				"Disadvantaged Student Scholarships", "Scholarships",
				"Athletics Facilities", "Sporting Facilities",
				"Parking Facilities", "Other Facilities",
				"Research Infrastructure", "Teaching Infrastructure"));

		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				"Scholarships", "Grounds and Infrastructure"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);

		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));

	}

	/**
	 * A basic test for the contains method.
	 */
	@Test
	public void testContains() {
		Tree<String> tree = getExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"Scholarships", "Grounds and Infrastructure"));

		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"Salaries and Wages"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"University Expenses"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"Parking Facilities"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"Other Student Scholarships"));
		Assert.assertTrue(ExpenditureTrees
				.contains(tree, types, "Scholarships"));

	}

	/**
	 * A basic test for the negation method.
	 */
	@Test
	public void testNegation() {
		Tree<String> tree = getExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"Parking Facilities", "Scholarships"));

		List<String> expectedNegation = new ArrayList<String>(Arrays.asList(
				"Salaries and Wages", "Sporting Facilities",
				"Other Facilities", "Research Infrastructure",
				"Teaching Infrastructure"));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);

		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));

	}

	// helper methods

	/**
	 * Creates and returns example tree from handout.
	 */
	private Tree<String> getExampleTree() {
		LinkedTree<String> tree = new LinkedTree<String>();
		Position<String> n0 = tree.addRoot("University Expenses");
		Position<String> n1 = tree.insertChild(n0, "Scholarships");
		Position<String> n2 = tree.insertChild(n0, "Salaries and Wages");
		Position<String> n3 = tree
				.insertChild(n0, "Grounds and Infrastructure");
		Position<String> n4 = tree
				.insertChild(n1, "Other Student Scholarships");
		Position<String> n5 = tree.insertChild(n1,
				"Disadvantaged Student Scholarships");
		Position<String> n6 = tree.insertChild(n3, "Grounds");
		Position<String> n7 = tree.insertChild(n3, "Research Infrastructure");
		Position<String> n8 = tree.insertChild(n3, "Teaching Infrastructure");
		Position<String> n9 = tree.insertChild(n6, "Sporting Facilities");
		Position<String> n10 = tree.insertChild(n6, "Parking Facilities");
		Position<String> n11 = tree.insertChild(n6, "Other Facilities");
		Position<String> n12 = tree.insertChild(n9, "Tennis Facilities");
		Position<String> n13 = tree.insertChild(n9, "Swimming Facilities");
		Position<String> n14 = tree.insertChild(n9, "Athletics Facilities");
		Position<String> n15 = tree
				.insertChild(n9, "Other Sporting Facilities");

		return tree;
	}

}
