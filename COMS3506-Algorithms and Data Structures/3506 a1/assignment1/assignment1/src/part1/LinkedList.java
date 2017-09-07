package part1;

import adt.Position;
import adt.PositionList;

/*
 * PART 1 (QUESTION 2): Using the JUnit tests in part1.test.LinkedListTest
 * discover and correct the 4 ERRORS in the following implementation of
 * PositionList. When you find an error, insert a brief one-line "//" comment at
 * that location, indicating where the error had been found, and why it
 * occurred.
 *
 * You may not modify the code other than to fix the 4 ERRORS and insert the
 * required comments.
 */

/**
 * A doubly linked list implementation of a PositionList without header and
 * trailer sentinels.
 * 
 * - taken and adapted from Goodrich, Tamassia and Goldwasser (6th Ed)
 * 
 * @param <E>, the type of the objects to be stored in the list.
 */

public class LinkedList<E> implements PositionList<E> {

	// Number of elements in the list
	private int size;
	// head of the list.
	private Node<E> head;
	// tail of the list
	private Node<E> tail;

	/*
	 * Invariant: When size == 0 then head == null and tail == null; and head ==
	 * this.first() and tail == this.last().
	 */

	/** Creates an empty list */
	public LinkedList() {
		size = 0;
		head = null;
		tail = null;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return (size == 0);
	}

	@Override
	public Position<E> first() {
		return head;
	}

	@Override
	public Position<E> last() {
		return tail;
	}

	@Override
	public boolean hasNext(Position<E> p) throws IllegalArgumentException {
		Node<E> n = checkPosition(p);
		return (n.getNext() != null);
	}

	@Override
	public boolean hasPrev(Position<E> p) throws IllegalArgumentException {
		Node<E> n = checkPosition(p);
		return (n.getPrev() != null);
	}

	@Override
	public Position<E> next(Position<E> p) throws IllegalArgumentException {
		Node<E> n = this.checkPosition(p);
		return n.getNext();
	}

	@Override
	public Position<E> prev(Position<E> p) throws IllegalArgumentException {
		Node<E> n = checkPosition(p);
		return n.getPrev();
	}

	@Override
	public void addFirst(E e) {
		Node<E> n = new Node<E>(null, head, e);
		if (head != null) {
			head.setPrev(n);
		}
		head = n;
		size++;
	}

	@Override
	public void addLast(E e) {
		Node<E> n = new Node<E>(tail, null, e);
		if (tail != null) {
			tail.setNext(n);
		}
		tail = n;
		size++;
	}

	@Override
	public void addAfter(Position<E> p, E e) throws IllegalArgumentException {
		Node<E> b = checkPosition(p);
		Node<E> a = b.getNext();
		Node<E> n = new Node<E>(b, a, e);
		if (a == null) {
			tail = n;
		}
		b.setNext(n);
		size++;
	}

	@Override
	public void addBefore(Position<E> p, E e) throws IllegalArgumentException {
		Node<E> a = checkPosition(p);
		Node<E> b = a.getPrev();
		Node<E> n = new Node<E>(b, a, e);
		if (b != null) {
			b.setNext(n);
		} else {
			head = n;
		}
		size++;
	}

	@Override
	public E remove(Position<E> p) throws IllegalArgumentException {
		Node<E> n = checkPosition(p);
		Node<E> before = n.getPrev();
		Node<E> after = n.getNext();
		if (before != null) {
			before.setNext(after);
		}
		if (after != null) {
			after.setPrev(before);
		}
		if (n == head) {
			head = after;
		}
		if (n == tail) {
			tail = before;
		}
		n.setNext(null);
		n.setPrev(null);
		size--;
		return n.getElement();
	}

	@Override
	public E set(Position<E> p, E e) throws IllegalArgumentException {
		Node<E> n = checkPosition(p);
		E oldElem = n.getElement();
		n.setElement(e);
		return oldElem;
	}

	/**
	 * If Position p is not null, cast it to a Node if possible, otherwise throw
	 * an exception.
	 * 
	 * @return p cast to a Node
	 * @throws IllegalArgumentException
	 *             if either p is null or it is not an instance of Node.
	 */
	private Node<E> checkPosition(Position<E> p)
			throws IllegalArgumentException {
		if (p == null || !(p instanceof Node))
			throw new IllegalArgumentException("The position is invalid");
		return (Node<E>) p;
	}

}
