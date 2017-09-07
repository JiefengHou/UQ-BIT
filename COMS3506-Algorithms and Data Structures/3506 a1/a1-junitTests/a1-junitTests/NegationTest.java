package part2.test;

import org.junit.*;

import part2.ExpenditureTrees;
import adt.*;

import java.util.*;

/**
 * Tests for the part2.ExpenditureTrees.negation method
 */
public class NegationTest {

	/**
	 * A basic test for the negation method.
	 */
	@Test(timeout = 5000)
	public void testHandoutExample() throws Exception {
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

	/**
	 * Test that the negation method works for a tree of size one.
	 */
	@Test(timeout = 5000)
	public void testSingletonTree() throws Exception {

		// test for empty list of types
		Tree<String> tree = getSingletonTree();
		List<String> types = new ArrayList<String>();
		List<String> expectedNegation = new ArrayList<String>(
				Arrays.asList("0"));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);

		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));

		// test for singleton list of types (root node)
		tree = getSingletonTree();
		types = new ArrayList<String>(Arrays.asList("0"));
		expectedNegation = new ArrayList<String>();
		actualNegation = ExpenditureTrees.negation(tree, types);

		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}

	/**
	 * Test that the negation method works for an empty list of types, on large
	 * tree.
	 */
	@Test(timeout = 5000)
	public void testEmptyList() throws Exception {
		Tree<String> tree = getLargeExampleTree();
		List<String> types = new ArrayList<String>();
		List<String> expectedNegation = new ArrayList<String>(
				Arrays.asList("0"));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);

		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}

	/**
	 * Test that the negation method works for an singleton list of types, on
	 * large tree.
	 */
	@Test(timeout = 5000)
	public void testSingletonList() throws Exception {
		// check when types has root node only
		Tree<String> tree = getLargeExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList("0"));
		List<String> expectedNegation = new ArrayList<String>();
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);

		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));

		// check when types has a leaf node only

		tree = getLargeExampleTree();
		types = new ArrayList<String>(Arrays.asList("34"));
		expectedNegation = new ArrayList<String>(Arrays.asList("1", "2", "3",
				"22", "35", "11"));
		actualNegation = ExpenditureTrees.negation(tree, types);

		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));

		// check when types has an internal node (other than root) only

		tree = getLargeExampleTree();
		types = new ArrayList<String>(Arrays.asList("9"));
		expectedNegation = new ArrayList<String>(Arrays.asList("1", "8", "3",
				"4"));
		actualNegation = ExpenditureTrees.negation(tree, types);

		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}

	/**
	 * Test that the summary method works for list of many types, on large tree,
	 * where no given type is a subtype of something else.
	 */
	@Test(timeout = 5000)
	public void testLargeList() throws Exception {
		Tree<String> tree = getLargeExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList("13", "24",
				"7", "29", "3", "10"));
		List<String> expectedNegation = new ArrayList<String>(Arrays.asList(
				"25", "26", "27", "28", "15", "16", "2", "11"));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);

		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}

	/**
	 * Test that the summary method works for list of many types, on large tree,
	 * where some types are sub-types of others.
	 */
	@Test(timeout = 5000)
	public void testLargeListWithSubtypes() throws Exception {
		// test where one sub-type is an immediate child, later descendant,
		// leaf, internal (does not include root node)

		Tree<String> tree = getLargeExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList("25", "22",
				"15", "28", "19", "14", "37", "5", "36", "11"));
		List<String> expectedNegation = new ArrayList<String>(Arrays.asList(
				"16", "7", "20", "21", "8", "3", "23"));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);

		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));

		// test where one sub-type is an immediate child, later descendant,
		// leaf, internal -- includes root node

	}

	// helper methods

	/**
	 * Creates and returns a singleton tree.
	 */
	private Tree<String> getSingletonTree() {
		LinkedTree<String> tree = new LinkedTree<String>();
		ArrayList<Position<String>> nodes = new ArrayList<>();

		nodes.add(tree.addRoot("0"));

		return tree;
	}

	/**
	 * Creates and returns a large example tree.
	 */
	private Tree<String> getLargeExampleTree() {
		LinkedTree<String> tree = new LinkedTree<String>();
		ArrayList<Position<String>> nodes = new ArrayList<>();

		nodes.add(tree.addRoot("0"));
		nodes.add(tree.insertChild(nodes.get(0), "1"));
		nodes.add(tree.insertChild(nodes.get(0), "2"));
		nodes.add(tree.insertChild(nodes.get(0), "3"));
		nodes.add(tree.insertChild(nodes.get(0), "4"));
		nodes.add(tree.insertChild(nodes.get(1), "5"));
		nodes.add(tree.insertChild(nodes.get(1), "6"));
		nodes.add(tree.insertChild(nodes.get(1), "7"));
		nodes.add(tree.insertChild(nodes.get(2), "8"));
		nodes.add(tree.insertChild(nodes.get(2), "9"));
		nodes.add(tree.insertChild(nodes.get(4), "10"));
		nodes.add(tree.insertChild(nodes.get(4), "11"));
		nodes.add(tree.insertChild(nodes.get(5), "12"));
		nodes.add(tree.insertChild(nodes.get(5), "13"));
		nodes.add(tree.insertChild(nodes.get(6), "14"));
		nodes.add(tree.insertChild(nodes.get(6), "15"));
		nodes.add(tree.insertChild(nodes.get(6), "16"));
		nodes.add(tree.insertChild(nodes.get(7), "17"));
		nodes.add(tree.insertChild(nodes.get(7), "18"));
		nodes.add(tree.insertChild(nodes.get(9), "19"));
		nodes.add(tree.insertChild(nodes.get(9), "20"));
		nodes.add(tree.insertChild(nodes.get(9), "21"));
		nodes.add(tree.insertChild(nodes.get(10), "22"));
		nodes.add(tree.insertChild(nodes.get(10), "23"));
		nodes.add(tree.insertChild(nodes.get(12), "24"));
		nodes.add(tree.insertChild(nodes.get(12), "25"));
		nodes.add(tree.insertChild(nodes.get(14), "26"));
		nodes.add(tree.insertChild(nodes.get(14), "27"));
		nodes.add(tree.insertChild(nodes.get(14), "28"));
		nodes.add(tree.insertChild(nodes.get(14), "29"));
		nodes.add(tree.insertChild(nodes.get(19), "30"));
		nodes.add(tree.insertChild(nodes.get(19), "31"));
		nodes.add(tree.insertChild(nodes.get(21), "32"));
		nodes.add(tree.insertChild(nodes.get(21), "33"));
		nodes.add(tree.insertChild(nodes.get(23), "34"));
		nodes.add(tree.insertChild(nodes.get(23), "35"));
		nodes.add(tree.insertChild(nodes.get(30), "36"));
		nodes.add(tree.insertChild(nodes.get(30), "37"));

		return tree;
	}

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
