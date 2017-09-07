package part1.test;

import org.junit.*;
import adt.*;
import part1.ArrayStack;

/**
 * Tests for the ArrayStack class.
 */
public class ArrayStackTest {

	/**
	 * Test that the accessor methods behave as expected on a newly constructed
	 * stack.
	 */
	@Test
	public void testStackConstructor() {
		IStack<String> stack = new ArrayStack<String>();

		Assert.assertEquals("Top of an empty stack should be null", null,
				stack.top());
		Assert.assertEquals("Incorrect stack size", 0, stack.size());
		Assert.assertEquals("List should be empty", true, stack.isEmpty());
	}

	/**
	 * Tests that we can use the push method to push one element onto the stack.
	 */
	@Test
	public void testPushOneElement() {
		IStack<String> stack = new ArrayStack<String>();
		stack.push("A");

		Assert.assertEquals("Incorrect stack size", 1, stack.size());
		Assert.assertEquals("List is not empty", false, stack.isEmpty());
		Assert.assertEquals("Top of stack is incorrect", "A", stack.top());
	}

	/**
	 * Tests that we can push many elements onto the stack.
	 */
	@Test
	public void testPushManyElements() {
		IStack<String> stack = new ArrayStack<String>();
		stack.push("A");
		stack.push("B");
		stack.push("C");
		stack.push("D");
		stack.push("E");

		Assert.assertEquals("Incorrect stack size", 5, stack.size());
		Assert.assertEquals("List is not empty", false, stack.isEmpty());
		Assert.assertEquals("Top of stack is incorrect", "E", stack.top());
	}

	/**
	 * Tests method pop on an empty stack (newly initialised).
	 */
	@Test
	public void testPopEmptyStack() {
		IStack<Object> stack = new ArrayStack<Object>();
		Assert.assertEquals("Top of an empty stack should be null", null,
				stack.pop());

		Assert.assertEquals("Top of an empty stack should be null", null,
				stack.top());
		Assert.assertEquals("Incorrect stack size", 0, stack.size());
		Assert.assertEquals("List should be empty", true, stack.isEmpty());
	}

	/**
	 * Tests popping elements off the stack.
	 */
	@Test
	public void testPoppingElements() {
		String[] testInputs = { "A", "B", "C", "D", "E", "F", "G", "H", "I" };

		IStack<String> stack = new ArrayStack<String>();
		for (int i = 0; i < testInputs.length; i++) {
			stack.push(testInputs[i]);
		}

		for (int i = testInputs.length - 1; i > 0; i--) {
			Assert.assertEquals("Popped element is not correct", testInputs[i],
					stack.pop());
			Assert.assertEquals("Incorrect stack size", i, stack.size());
			Assert.assertEquals("List is not empty", false, stack.isEmpty());
			Assert.assertEquals("Top of stack is incorrect", testInputs[i - 1],
					stack.top());
		}

		Assert.assertEquals("Popped element is not correct", testInputs[0],
				stack.pop());
		Assert.assertEquals("Top of an empty stack should be null", null,
				stack.top());
		Assert.assertEquals("Incorrect stack size", 0, stack.size());
		Assert.assertEquals("List should be empty", true, stack.isEmpty());
	}

	/**
	 * Test that the stack behaves correctly with interleaved push and pop
	 * operations.
	 */
	@Test
	public void testInterleavedPushPop() {
		IStack<String> stack = new ArrayStack<String>();
		stack.push("A");
		stack.pop();
		stack.push("B");
		stack.push("C");
		stack.push("D");
		stack.pop();
		stack.pop();
		stack.push("E");
		stack.push("F");
		stack.pop();
		stack.push("G");

		Assert.assertEquals("Popped element is not correct", "G", stack.pop());
		Assert.assertEquals("Incorrect stack size", 2, stack.size());
		Assert.assertEquals("List is not empty", false, stack.isEmpty());
		Assert.assertEquals("Top of stack is incorrect", "E", stack.top());

		Assert.assertEquals("Popped element is not correct", "E", stack.pop());
		Assert.assertEquals("Incorrect stack size", 1, stack.size());
		Assert.assertEquals("List is not empty", false, stack.isEmpty());
		Assert.assertEquals("Top of stack is incorrect", "B", stack.top());

		Assert.assertEquals("Popped element is not correct", "B", stack.pop());
		Assert.assertEquals("Top of an empty stack should be null", null,
				stack.top());
		Assert.assertEquals("Incorrect stack size", 0, stack.size());
		Assert.assertEquals("List should be empty", true, stack.isEmpty());
	}
}
