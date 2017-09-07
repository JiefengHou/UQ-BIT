package part2.test;

import org.junit.*;

import part2.ExpenditureTrees;
import adt.*;

import java.util.*;

/**
 * Tests for the part2.ExpenditureTrees.contains method.
 */
public class ContainsTest {

	/**
	 * A basic test for the contains method.
	 */
	@Test(timeout = 5000)
	public void testHandoutExample() throws Exception {
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
	 * Test that the contains method works for a tree of size one.
	 */
	@Test(timeout = 5000)
	public void testSingletonTree() throws Exception {

		// test for empty list of types
		Tree<String> tree = getSingletonTree();
		List<String> types = new ArrayList<String>();

		Assert.assertFalse(ExpenditureTrees.contains(tree, types, "0"));

		// test for singleton list of types (root node)
		tree = getSingletonTree();
		types = new ArrayList<String>(Arrays.asList("0"));

		Assert.assertTrue(ExpenditureTrees.contains(tree, types, "0"));

	}

	/**
	 * Test that the contains method works for an empty list of types, on large
	 * tree.
	 */
	@Test(timeout = 5000)
	public void testEmptyList() throws Exception {
		Tree<String> tree = getLargeExampleTree();
		List<String> types = new ArrayList<String>();

		Assert.assertFalse(ExpenditureTrees.contains(tree, types, "0"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types, "18"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types, "6"));
	}

	/**
	 * Test that the summary method works for an singleton list of an internal
	 * node type, on large tree.
	 */
	@Test(timeout = 5000)
	public void testSingletonListInternalNode() throws Exception {
		// check when types has root node only
		Tree<String> tree = getLargeExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList("0"));

		Assert.assertTrue(ExpenditureTrees.contains(tree, types, "0"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types, "36"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types, "10"));

		// check when types has an internal node, other than root, only.
		tree = getLargeExampleTree();
		types = new ArrayList<String>(Arrays.asList("9"));

		Assert.assertFalse(ExpenditureTrees.contains(tree, types, "0")); // root
		Assert.assertFalse(ExpenditureTrees.contains(tree, types, "22")); // leaf
		Assert.assertFalse(ExpenditureTrees.contains(tree, types, "14")); // non-leaf
		Assert.assertTrue(ExpenditureTrees.contains(tree, types, "21")); // child
		Assert.assertTrue(ExpenditureTrees.contains(tree, types, "37")); // other
																			// descendant
		Assert.assertTrue(ExpenditureTrees.contains(tree, types, "9")); // self
	}

	/**
	 * Test that the summary method works for an singleton list of types, on
	 * large tree.
	 */
	@Test(timeout = 5000)
	public void testSingletonListExternalNode() throws Exception {
		// check when types has a leaf node, other than root, only.
		Tree<String> tree = getLargeExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList("8"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types, "0")); // root
		Assert.assertFalse(ExpenditureTrees.contains(tree, types, "29")); // leaf
		Assert.assertFalse(ExpenditureTrees.contains(tree, types, "4")); // non-leaf
		Assert.assertTrue(ExpenditureTrees.contains(tree, types, "8")); // self
	}

	/**
	 * Test that the summary method works for a concise list of many types, on
	 * large tree.
	 */
	@Test(timeout = 5000)
	public void testArbitraryList() throws Exception {
		Tree<String> tree = getLargeExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList("19", "21",
				"10", "8", "1"));

		Assert.assertFalse(ExpenditureTrees.contains(tree, types, "0")); // root
		Assert.assertFalse(ExpenditureTrees.contains(tree, types, "9")); // parent
		Assert.assertFalse(ExpenditureTrees.contains(tree, types, "11")); // leaf
		Assert.assertTrue(ExpenditureTrees.contains(tree, types, "10")); // element
																			// non-leaf
		Assert.assertTrue(ExpenditureTrees.contains(tree, types, "8")); // element
																		// leaf
		Assert.assertTrue(ExpenditureTrees.contains(tree, types, "30")); // child
		Assert.assertTrue(ExpenditureTrees.contains(tree, types, "26")); // descendant

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
