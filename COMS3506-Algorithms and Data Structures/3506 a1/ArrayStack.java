package part1;

import adt.IStack;

/*
 * PART 1 (QUESTION 1): Using the JUnit tests in part1.test.ArrayStackTest
 * discover and correct the 2 ERRORS in the following implementation of
 * ArrayStack. When you find an error, insert a brief one-line "//" comment at
 * that location, indicating where the error had been found, and why it
 * occurred.
 * 
 * You may not modify the code other than to fix the 2 ERRORS and insert the
 * required comments.
 */

/**
 * Implementation of the stack ADT using an array-based implementation.
 * 
 * - taken and adapted from Goodrich, Tamassia and Goldwasser (6th Ed)
 * 
 * @param <E>, the type of the objects to be stored on the stack.
 */
public class ArrayStack<E> implements IStack<E> {

	// Default initial capacity of the stack.
	public static final int INITIAL_CAPACITY = 1;
	// Array used to implement the stack.
	private E stackArray[];
	// Index of the top element of the stack in the array.
	private int top;

	/* invariant: stackArray.length >= 1 && -1 <= top < stackArray.length */

	/**
	 * Initializes an empty stack.
	 */
	public ArrayStack() {
		stackArray = (E[]) new Object[INITIAL_CAPACITY];
		top = -1;
	}

	@Override
	public int size() {
		return (top + 1);
	}

	@Override
	public boolean isEmpty() {
		return (top < 0);
	}

	@Override
	public E top() {
		//Reason: when stackArray is empty, top is -1, so the index is out of 
		//		  bounds(top should be 0).
		//Modification : check the stackArray is empty or not, if it is empty,
		//               index should be top+1, else should be top.
		//Before: return stackArray[top];
		return isEmpty()?stackArray[top+1]:stackArray[top];
	}

	@Override
	public void push(E element) throws IllegalStateException {
		if (size() == stackArray.length) {
			// double the capacity of the array
			E tmpArray[] = (E[]) new Object[stackArray.length * 2];
			//Reason: when the stackArray is not empty(has 1 element), the 
			//        condition i<top is wrong, because the top is 0 when stackArray
			//        has 1 element, so the for loop cannot use.
			//Modification : the condition should be i<top+1
			//Before: for (int i = 0; i < top; i++) {
			for (int i = 0; i < top+1; i++) {
				tmpArray[i] = stackArray[i];
			}
			stackArray = tmpArray;
		}
		top++;
		stackArray[top] = element;
	}

	@Override
	public E pop() {
		if (isEmpty()) {
			return null;
		}
		E element = stackArray[top];
		// halve the array capacity if the size will fall below a quarter
		if (size() <= stackArray.length / 4) {
			E tmpArray[] = (E[]) new Object[stackArray.length / 2];
			//same reason as before, wrong condition
			for (int i = 0; i < top+1; i++) {
				tmpArray[i] = stackArray[i];
			}
			stackArray = tmpArray;
		}
		stackArray[top--] = null;
		return element;
	}
}
