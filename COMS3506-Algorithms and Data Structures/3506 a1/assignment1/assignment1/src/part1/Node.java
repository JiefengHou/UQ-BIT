package part1;

import adt.Position;

/* 
 * This implementation of a Position is used by LinkedList.
 * DO NOT MODIFY THIS FILE IN ANY WAY.
 */

/**
 * A linked implementation of a Position.
 * 
 * - taken and adapted from Goodrich, Tamassia and Goldwasser (6th Ed)
 * 
 * @param <E>, the type of the objects to be stored in the position.
 */

public class Node<E> implements Position<E> {

	// References to the nodes before and after
	private Node<E> prev, next;
	// Element stored in this position
	private E element;

	/** Construct a new node with next and previous nodes and element elem */
	public Node(Node<E> prev, Node<E> next, E elem) {
		this.prev = prev;
		this.next = next;
		this.element = elem;
	}

	@Override
	public E getElement() {
		return element;
	}

	public Node<E> getPrev() {
		return prev;
	}

	public Node<E> getNext() {
		return next;
	}

	public void setElement(E newElement) {
		element = newElement;
	}

	public void setNext(Node<E> newNext) {
		next = newNext;
	}

	public void setPrev(Node<E> newPrev) {
		prev = newPrev;
	}

}
