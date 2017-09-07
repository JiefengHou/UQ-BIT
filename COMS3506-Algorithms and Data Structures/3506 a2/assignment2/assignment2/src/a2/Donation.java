package a2;

import java.util.*;

/**
 * A class representing a donation. A donation has a name, the total amount of
 * the donation, the unspent portion of the donation and a set of projects that
 * the donation may be spent on.
 * 
 * DO NOT MODIFY THIS FILE IN ANY WAY.
 */

public class Donation {

	// name of donation
	private String name;
	// total donation amount
	private int total;
	// amount of donation that has not yet been spent
	private int unspent;
	// projects that the funds from this donation could be spent on
	private Set<Project> projects;

	/*
	 * invariant: name != null && total > 0 && 0 <= unspent <= total &&
	 * projects!=null
	 */

	/**
	 * @precondition: name!= null && total > 0 && projects != null
	 * @postcondition: creates a new donation with given name, total donation
	 *                 amount and projects that this donation could be spent on.
	 *                 No funds from the donation have initially been spent.
	 */
	public Donation(String name, int total, Set<Project> projects) {
		assert name != null && projects != null && total > 0;

		this.name = name;
		this.total = total;
		this.projects = projects;
		this.unspent = total;
	}

	/**
	 * @postcondition: returns the total amount of this donation.
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @postcondition: returns the amount of this donation that hasn't been
	 *                 spent yet.
	 */
	public int getUnspent() {
		return unspent;
	}

	/**
	 * @postcondition: returns true iff this donation has been totally spent.
	 */
	public boolean spent() {
		return (unspent == 0);
	}

	/**
	 * @precondition: 0 <= cost <= getUnspent()
	 * @postcondition: removes cost from the amount of available funds for this
	 *                 donation. The only method that should call this one
	 *                 directly is the allocate method from the Project class.
	 *                 (That is, it should only be executed as part of an
	 *                 allocation of these funds to a particular project.)
	 */
	public void spend(int cost) {
		assert 0 <= cost && cost <= unspent;
		unspent = unspent - cost;
	}

	/**
	 * @precondition: 0 <= cost <= total - getUnspent()
	 * @postcondition: adds cost back to the available funds for this donation.
	 *                 The only method that should call this one directly is the
	 *                 deallocate method from the Project class. (That is, it
	 *                 should only be executed as part of the deallocation of
	 *                 these funds from a particular project.)
	 */
	public void unspend(int cost) {
		assert 0 <= cost && cost <= total - unspent;
		unspent = unspent + cost;
	}

	/**
	 * @postcondition: returns true iff this donation is allowed to be spent on
	 *                 the given project.
	 */
	public boolean canBeUsedFor(Project project) {
		return projects.contains(project);
	}

	/**
	 * @postcondition: returns a (shallow copy of) the set of the projects for
	 *                 this donation.
	 */
	public Set<Project> getProjects() {
		return new HashSet<>(projects);
	}
}
