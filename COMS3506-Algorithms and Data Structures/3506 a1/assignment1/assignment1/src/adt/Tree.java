package adt;

import java.util.*;

/**
 * An interface for a tree. A tree may contain zero or more nodes.
 * 
 * - taken and adapted from Goodrich, Tamassia and Goldwasser (6th Ed)
 * 
 * @param <E>, the type of the objects to be stored in the tree.
 * 
 */
public interface Tree<E> {

	/**
	 * Returns the number of nodes in the tree.
	 * 
	 * @return number of nodes in the tree
	 */
	int size();

	/**
	 * Tests whether the tree is empty.
	 * 
	 * @return true if the tree is empty, false otherwise
	 */
	boolean isEmpty();

	/**
	 * Returns the root Position of the tree (or null if tree is empty).
	 * 
	 * @return root Position of the tree (or null if tree is empty)
	 */
	Position<E> root();

	/**
	 * Returns the Position of p's parent (or null if p is root).
	 *
	 * @param p
	 *            A valid Position within the tree
	 * @return Position of p's parent (or null if p is root)
	 * @throws IllegalArgumentException
	 *             if p is not a valid Position for this tree.
	 */
	Position<E> parent(Position<E> p) throws IllegalArgumentException;

	/**
	 * Returns an iterable collection of the Positions representing p's
	 * children.
	 * 
	 * NOTE: For efficiency reasons, the list of returned children may be part
	 * of the internal state of the tree, and so must not be modified. The
	 * internal consistency of the tree, and its future behaviour are not
	 * defined if the list of children returned by this method is modified.
	 *
	 * @param p
	 *            A valid Position within the tree
	 * @return iterable collection of the Positions of p's children
	 * @throws IllegalArgumentException
	 *             if p is not a valid Position for this tree.
	 */
	List<Position<E>> children(Position<E> p) throws IllegalArgumentException;

	/**
	 * Returns the number of children of Position p.
	 *
	 * @param p
	 *            A valid Position within the tree
	 * @return number of children of Position p
	 * @throws IllegalArgumentException
	 *             if p is not a valid Position for this tree.
	 */
	int numChildren(Position<E> p) throws IllegalArgumentException;

	/**
	 * Returns true if Position p represents the root of the tree.
	 *
	 * @param p
	 *            A valid Position within the tree
	 * @return true if p is the root of the tree, false otherwise
	 */
	boolean isRoot(Position<E> p);

	/**
	 * Returns true if Position p has one or more children.
	 *
	 * @param p
	 *            A valid Position within the tree
	 * @return true if p has at least one child, false otherwise
	 * @throws IllegalArgumentException
	 *             if p is not a valid Position for this tree.
	 */
	boolean isInternal(Position<E> p) throws IllegalArgumentException;

	/**
	 * Returns true if Position p does not have any children.
	 *
	 * @param p
	 *            A valid Position within the tree
	 * @return true if p has zero children, false otherwise
	 * @throws IllegalArgumentException
	 *             if p is not a valid Position for this tree.
	 */
	boolean isExternal(Position<E> p) throws IllegalArgumentException;

}
