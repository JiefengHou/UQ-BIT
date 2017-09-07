package part1.test;

import org.junit.*;
import part1.LinkedList;
import adt.*;

/**
 * Tests the LinkedList implementation of PositionList.
 */

public class LinkedListTest {

	@Test
	public void testConstructor() {
		Integer[] expectedElems = {};
		LinkedList<Integer> list = new LinkedList<Integer>();

		testIntegerListState(list, expectedElems);
	}

	@Test
	public void testAddFirstOnce() {
		Integer[] expectedElems = { 1 };
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.addFirst(1);

		testIntegerListState(list, expectedElems);
	}

	@Test
	public void testAddFirstMany() {
		Integer[] expectedElems = { 1, 2, 3 };
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int i = expectedElems.length - 1; i >= 0; i--) {
			list.addFirst(expectedElems[i]);
		}

		testIntegerListState(list, expectedElems);
	}

	@Test
	public void testAddLastOnce() {
		Integer[] expectedElems = { 1 };
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < expectedElems.length; i++) {
			list.addLast(expectedElems[i]);
		}

		testIntegerListState(list, expectedElems);
	}

	@Test
	public void testAddLastMany() {
		Integer[] expectedElems = { 1, 2, 3 };
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < expectedElems.length; i++) {
			list.addLast(expectedElems[i]);
		}

		testIntegerListState(list, expectedElems);
	}

	@Test
	public void testAddAfterMiddle() {
		Integer[] expectedElems = { 1, 2, 3, 4 };
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.addLast(expectedElems[0]);
		list.addLast(expectedElems[1]);
		list.addLast(expectedElems[3]);
		list.addAfter(list.next(list.first()), expectedElems[2]);

		testIntegerListState(list, expectedElems);

	}

	@Test
	public void testAddAfterLast() {
		Integer[] expectedElems = { 1, 2, 3 };
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.addLast(expectedElems[0]);
		list.addLast(expectedElems[1]);
		list.addAfter(list.last(), expectedElems[2]);

		testIntegerListState(list, expectedElems);

	}

	@Test
	public void testAddBeforeMiddle() {
		Integer[] expectedElems = { 1, 2, 3, 4 };
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.addLast(expectedElems[0]);
		list.addLast(expectedElems[2]);
		list.addLast(expectedElems[3]);
		list.addBefore(list.next(list.first()), expectedElems[1]);

		testIntegerListState(list, expectedElems);
	}

	@Test
	public void testAddBeforeFirst() {
		Integer[] expectedElems = { 1, 2, 3 };
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.addLast(expectedElems[1]);
		list.addLast(expectedElems[2]);
		list.addBefore(list.first(), expectedElems[0]);

		testIntegerListState(list, expectedElems);
	}

	@Test
	public void testRemoveOnlyElement() {
		Integer[] expectedElems = {};
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.addFirst(1);
		list.remove(list.first());

		testIntegerListState(list, expectedElems);
	}

	@Test
	public void testRemoveFirstElement() {
		Integer[] expectedElems = { 2 };
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.addLast(1);
		list.addLast(expectedElems[0]);
		list.remove(list.first());

		testIntegerListState(list, expectedElems);
	}

	@Test
	public void testRemoveLastElement() {
		Integer[] expectedElems = { 1 };
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.addLast(expectedElems[0]);
		list.addLast(2);
		list.remove(list.last());

		testIntegerListState(list, expectedElems);
	}

	@Test
	public void testRemoveMiddleElement() {
		Integer[] expectedElems = { 1, 3 };
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.addLast(expectedElems[0]);
		list.addLast(2);
		list.addLast(expectedElems[1]);
		list.remove(list.next(list.first()));

		testIntegerListState(list, expectedElems);
	}

	@Test
	public void testSetElementOnce() {
		Integer[] expectedElems = { 1, 5, 3 };
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.addLast(expectedElems[0]);
		list.addLast(2);
		list.addLast(expectedElems[2]);
		list.set(list.prev(list.last()), expectedElems[1]);

		testIntegerListState(list, expectedElems);
	}

	// Helper methods
	
	private void testIntegerListState(LinkedList<Integer> list,
			Integer[] expectedElems) {
		Assert.assertEquals("list.size() is incorrect", expectedElems.length,
				list.size());
		Assert.assertEquals("list.isEmpty() is incorrect",
				expectedElems.length == 0, list.isEmpty());

		Assert.assertArrayEquals("List elements are not as expected",
				expectedElems, getIntegerElements(list));

	}

	/**
	 * Helper method that returns an array of the elements in list; it
	 * additionally checks additional properties of list.first(), list.last(),
	 * list.hasNext() and list.hasPrev(), list.next() and list.prev().
	 */
	private Integer[] getIntegerElements(LinkedList<Integer> list) {
		Integer[] elems = new Integer[list.size()];
		// a position in the list, initialised to the first position if there is
		// one
		Position<Integer> n = list.first();
		// the position before n if it exists
		Position<Integer> p = null;

		if (list.isEmpty()) {
			Assert.assertEquals(
					"First element of an empty list should be null", null,
					list.first());
			Assert.assertEquals("Last element of an empty list should be null",
					null, list.last());
		} else {
			Assert.assertEquals("The element before the first should be null",
					null, list.prev(list.first()));
			Assert.assertEquals("The element after the last should be null",
					null, list.next(list.last()));
		}

		for (int i = 0; i < list.size(); i++) {
			// get the element at the ith position in the list
			elems[i] = n.getElement();
			// check properties of next and previous
			Assert.assertEquals(
					"list.hasNext(n) is incorrect for node at rank " + i,
					i < list.size() - 1, list.hasNext(n));
			Assert.assertEquals(
					"list.hasPrev(n) is incorrect for node at rank " + i,
					i > 0, list.hasPrev(n));

			if (i > 0) {
				Assert.assertEquals("list.prev(list.next(p)) != p", p,
						list.prev(n));
			}
			p = n;
			n = list.next(n);
		}

		if (!list.isEmpty()) {
			Assert.assertEquals(
					"The position at rank list.size()-1 is not list.last()", p,
					list.last());
		}

		return elems;
	}

}
