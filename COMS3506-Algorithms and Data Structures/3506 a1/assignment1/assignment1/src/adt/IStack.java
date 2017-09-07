package adt;

/**
 * Interface for a stack: a collection of objects that are inserted and removed
 * according to the last-in first-out principle.
 * 
 * - taken and adapted from Goodrich, Tamassia and Goldwasser (6th Ed)
 * 
 * @param <E>, the type of the objects to be stored on the stack.
 */

public interface IStack<E> {
	/**
	 * Returns the number of elements in the stack.
	 * 
	 * @return number of elements in the stack.
	 */
	int size();

	/**
	 * Returns whether the stack is empty.
	 * 
	 * @return true if the stack is empty; false otherwise.
	 */
	boolean isEmpty();

	/**
	 * If the stack is empty then this method returns the value null, otherwise
	 * it returns the value on top of the stack. The stack itself is unmodified
	 * by this operation.
	 * 
	 * @return the top element of the stack, if there is one, or null otherwise.
	 */
	E top();

	/**
	 * Places the passed element on the top of the stack.
	 * 
	 * @param element
	 *            element to be inserted.
	 */
	void push(E element);

	/**
	 * If the stack is empty then this method returns the value null and does
	 * not modify the stack, otherwise it removes and returns the top element of
	 * the stack.
	 * 
	 * @return the element removed from the top of the stack, if there was one,
	 *         or null otherwise.
	 */
	E pop();

}
