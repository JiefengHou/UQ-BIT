package adt;

/**
 * Interface for a position.
 * 
 * - taken and adapted from Goodrich, Tamassia and Goldwasser (6th Ed)
 * 
 * @param <E>, the type of the element stored in a Position.
 */

public interface Position<E> {
	/** Return the element stored at this position. */
	E getElement();
}
