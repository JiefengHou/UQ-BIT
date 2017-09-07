package adt;

import java.util.*;

/**
 * An implementation of the Tree interface by means of a linked structure.
 * 
 * - taken and adapted from Goodrich, Tamassia and Goldwasser (6th Ed)
 * 
 * @param <E>, the type of the objects to be stored in the tree.
 */
public class LinkedTree<E> implements Tree<E> {

	protected Node<E> root; // reference to the root
	protected int size; // number of nodes in the tree

	/* invariant: (root == null) iff (size == 0) */

	/** Creates an empty tree. */
	public LinkedTree() {
		root = null;
		size = 0;
	}

	// access methods

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return (size == 0);
	}

	@Override
	public Position<E> root() {
		return root;
	}

	@Override
	public Position<E> parent(Position<E> p) throws IllegalArgumentException {
		Node<E> v = validate(p);
		return v.getParent();
	}

	@Override
	public List<Position<E>> children(Position<E> p)
			throws IllegalArgumentException {
		Node<E> v = validate(p);
		return v.getChildren();
	}

	@Override
	public int numChildren(Position<E> p) throws IllegalArgumentException {
		Node<E> v = validate(p);
		return v.getChildren().size();
	}

	@Override
	public boolean isRoot(Position<E> p) {
		return (root != null && root == p);
	}

	@Override
	public boolean isInternal(Position<E> p) throws IllegalArgumentException {
		return (numChildren(p) != 0);
	}

	@Override
	public boolean isExternal(Position<E> p) throws IllegalArgumentException {
		return (numChildren(p) == 0);
	}

	// update methods

	/**
	 * Adds a root node with element e to an empty tree.
	 * 
	 * @return the position representing the new root node
	 * @throws IllegalStateException
	 *             if the tree is not empty
	 */
	public Position<E> addRoot(E e) throws IllegalStateException {
		if (!isEmpty()) {
			throw new IllegalStateException("Tree is not empty.");
		}
		root = new Node<E>(e, null);
		size = 1;
		return root;
	}

	/**
	 * Inserts a child with element e at node v.
	 * 
	 * @throws IllegalArgumentException
	 *             if v is not a valid position of the tree
	 * 
	 * @return the position of the newly constructed child.
	 */
	public Position<E> insertChild(Position<E> p, E e)
			throws IllegalArgumentException {
		Node<E> parent = validate(p);
		Node<E> child = new Node<E>(e, parent);
		parent.addChild(child);
		size++;
		return child;
	}

	/**
	 * Sets the children of p to be the trees in the list of childTrees. The
	 * child trees are set to be empty after this operation, as they are
	 * subsumed by the tree to which they are added (this one). An exception is
	 * thrown if p is either invalid or internal.
	 * 
	 * @throws InvalidPositionException
	 *             if position v is either invalid or internal
	 * */
	public void attach(Position<E> p, List<LinkedTree<E>> childTrees)
			throws IllegalArgumentException {
		Node<E> node = validate(p);
		if (isInternal(p))
			throw new IllegalArgumentException("p must be a leaf");
		for (LinkedTree<E> childTree : childTrees) {
			size += childTree.size();
			childTree.root.setParent(node);
			node.addChild(childTree.root);
			// the old child tree is set to be empty
			childTree.root = null;
			childTree.size = 0;
		}
	}

	// helper methods

	/**
	 * Verifies that a Position belongs to the appropriate class, and is not one
	 * that has been previously removed. Note that our current implementation
	 * does not actually verify that the position belongs to this particular
	 * tree instance.
	 *
	 * @param p
	 *            a Position (that should belong to this tree)
	 * @return the underlying Node instance for the position
	 * @throws IllegalArgumentException
	 *             if an invalid position is detected
	 */
	protected Node<E> validate(Position<E> p) throws IllegalArgumentException {
		if (!(p instanceof Node)) {
			throw new IllegalArgumentException("Not valid position type");
		}
		Node<E> node = (Node<E>) p; // safe cast
		// check our convention for defunct node
		if (node.getParent() == node) {
			throw new IllegalArgumentException("p is no longer in the tree");
		}
		return node;
	}

	// ---------------- nested Node class ----------------
	/** Nested static class for a binary tree node. */
	protected static class Node<E> implements Position<E> {
		// an element stored at this node
		private E element;
		// a reference to the parent node (if any), null otherwise
		private Node<E> parent;
		// the children of the node
		private List<Position<E>> children;

		/* invariant: children != null */

		/** Constructs a node with the given element, parent and no children */
		public Node(E element, Node<E> parent) {
			this.element = element;
			this.parent = parent;
			this.children = new LinkedList<Position<E>>();
		}

		public E getElement() {
			return element;
		}

		public Node<E> getParent() {
			return parent;
		}

		public List<Position<E>> getChildren() {
			return children;
		}

		public void setElement(E element) {
			this.element = element;
		}

		public void setParent(Node<E> parent) {
			this.parent = parent;
		}

		public void addChild(Node<E> child) {
			children.add(child);
		}
	} // ----------- end of nested Node class -----------

}
