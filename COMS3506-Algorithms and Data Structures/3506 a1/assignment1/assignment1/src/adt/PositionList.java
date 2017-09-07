package adt;

/**
 * Interface for a position list.
 * 
 * - taken and adapted from Goodrich, Tamassia and Goldwasser (6th Ed)
 * 
 * @param <E>, the type of the objects to be stored in the position list.
 */

public interface PositionList<E> {

	/** Returns the number of elements in this list. */
	int size();

	/** Returns whether or not the list is empty. */
	boolean isEmpty();

	/**
	 * Returns the first position in the list, if any, or null otherwise.
	 */
	Position<E> first();

	/**
	 * Returns the last position in the list, if any, or null otherwise.
	 */
	Position<E> last();

	/**
	 * Returns true if p has a next position in the list, and false otherwise
	 * 
	 * @throws IllegalArgumentException
	 *             if p is not a valid position of the list
	 */
	boolean hasNext(Position<E> p) throws IllegalArgumentException;

	/**
	 * Returns true if p has a previous position in the list, and false
	 * otherwise
	 * 
	 * @throws IllegalArgumentException
	 *             if p is not a valid position of the list
	 */
	boolean hasPrev(Position<E> p) throws IllegalArgumentException;

	/**
	 * Returns the node after position p in the list, if there is one, or null
	 * otherwise.
	 * 
	 * @throws IllegalArgumentException
	 *             if p is not a valid position of the list
	 */
	Position<E> next(Position<E> p) throws IllegalArgumentException;

	/**
	 * Returns the position before p in the list, if there is one, or null
	 * otherwise.
	 * 
	 * @throws IllegalArgumentException
	 *             if p is not a valid position of the list
	 */
	Position<E> prev(Position<E> p) throws IllegalArgumentException;

	/** Inserts element e at the front of the list */
	void addFirst(E e);

	/** Inserts element e at the back of the list */
	void addLast(E e);

	/**
	 * Inserts element e after position p in the list.
	 * 
	 * @throws IllegalArgumentException
	 *             if p is not a valid position of the list
	 */
	void addAfter(Position<E> p, E e) throws IllegalArgumentException;

	/**
	 * Inserts element e before position p in the list.
	 * 
	 * @throws IllegalArgumentException
	 *             if p is not a valid position of the list
	 */
	void addBefore(Position<E> p, E e) throws IllegalArgumentException;

	/**
	 * Removes position p from the list, returning the element stored there.
	 * 
	 * @throws IllegalArgumentException
	 *             if p is not a valid position of the list
	 */
	E remove(Position<E> p) throws IllegalArgumentException;

	/**
	 * Replaces the element stored at the position p with e, returning old
	 * element.
	 * 
	 * @throws IllegalArgumentException
	 *             if p is not a valid position of the list
	 * 
	 * */
	E set(Position<E> p, E e) throws IllegalArgumentException;

}
