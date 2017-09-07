package a2;

import java.util.*;

/**
 * A class representing a project and its current allocation of funds.
 * 
 * DO NOT MODIFY THIS FILE IN ANY WAY.
 */

public class Project {

	private String name; // name of project
	private int cost; // total cost of the project
	private int allocatedFunding; // sum of the funds currently allocated
	private Map<Donation, Integer> allocations; // funds currently allocated

	/*
	 * invariant:
	 * 
	 * 	cost > 0 && allocatedFunding >= 0 && allocatedFunding <= cost &&
	 * 
	 * 	allocations != null && name != null &&
	 * 
	 * 	for each entry (d, x) in allocations, 
	 * 		d!=null && x>0 && d.canBeUsedFor(this) &&
	 * 
	 * 	allocatedFunding is the sum of values in the allocations map
	 */

	/**
	 * @precondition: name!= null && cost > 0
	 * @postcondition: creates a new project with given name and cost and an
	 *                 initially empty allocation of funds.
	 */
	public Project(String name, int cost) {
		assert (name != null && cost > 0);
		this.name = name;
		this.cost = cost;
		allocations = new HashMap<Donation, Integer>();
		allocatedFunding = 0;
	}

	/**
	 * @postcondition: returns the total cost of the project.
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * @postcondition: returns true if and only if the allocated funds are equal
	 *                 to the total cost of the project.
	 */
	public boolean fullyFunded() {
		return (cost == allocatedFunding);
	}

	/**
	 * @postcondition: returns the amount of money that is needed to completely
	 *                 fund the project.
	 */
	public int neededFunds() {
		return (cost - allocatedFunding);
	}

	/**
	 * @postcondition: returns the amount of money currently allocated to the
	 *                 project.
	 */
	public int allocatedFunding() {
		return allocatedFunding;
	}

	/**
	 * @postcondition: returns (a shallow copy of) the current allocations to
	 *                 the project. (Changing the returned map won't change the
	 *                 allocations of the project. To do that use the
	 *                 allocation, deallocation or transfer methods of this
	 *                 class.)
	 */
	public Map<Donation, Integer> getAllocations() {
		return new HashMap<>(allocations);
	}

	/**
	 * @precondition: donation!=null && 0 < amount <= donation.getUnspent() &&
	 *                amount <= neededFunds() donation.canBeUsedFor(this)
	 * @postcondition: spends the given amount of money from the donation by
	 *                 allocating it to this project.
	 */
	public void allocate(Donation donation, int amount) {
		assert donation != null;
		assert 0 < amount && amount <= donation.getUnspent()
				&& amount <= neededFunds() && donation.canBeUsedFor(this);

		addToAllocations(donation, amount);
		donation.spend(amount);
	}

	private void addToAllocations(Donation donation, int amount) {
		Integer existingAmount = allocations.get(donation);
		if (existingAmount == null) {
			existingAmount = 0;
		}
		allocations.put(donation, amount + existingAmount);
		allocatedFunding += amount;
	}

	/**
	 * @precondition: donation!=null && allocations.containsKey(donation) &&
	 *                allocations.get(donation) >= amount
	 * @postcondition: puts the given amount of money back into the unspent
	 *                 funds for the donation and removes it from the allocation
	 *                 to this project.
	 */
	public void deallocate(Donation donation, int amount) {
		assert donation != null;
		assert allocations.containsKey(donation);
		assert allocations.get(donation) >= amount;

		removeFromAllocations(donation, amount);
		donation.unspend(amount);
	}

	/**
	 * @postcondition: deallocates all allocations to this project.
	 */
	public void deallocateAll() {
		for (Map.Entry<Donation, Integer> entry : allocations.entrySet()) {
			Donation d = entry.getKey();
			int amount = entry.getValue();
			d.unspend(amount);
			allocatedFunding -= amount;
		}
		allocations.clear();
	}
	
	private void removeFromAllocations(Donation donation, int amount) {
		int existingAmount = allocations.get(donation);
		if (existingAmount > amount) {
			allocations.put(donation, existingAmount - amount);
		} else {
			allocations.remove(donation);
		}
		allocatedFunding -= amount;
	}

	/**
	 * @precondition: amount <= neededFunds() && the given amount of money may
	 *                be transferred from the source project to this project
	 * @postcondition: transfers $amount from source project to this project.
	 */
	public void transfer(int amount, Project source) {
		assert amount <= neededFunds();

		Iterator<Map.Entry<Donation, Integer>> it = source.allocations
				.entrySet().iterator();
		Map.Entry<Donation, Integer> entry;
		while (it.hasNext() && amount > 0) {
			entry = it.next();
			if (entry.getKey().canBeUsedFor(this)) {
				int transferAmount = Math.min(amount, entry.getValue());
				// deallocate transferAmount from source project
				entry.setValue(entry.getValue() - transferAmount);
				source.allocatedFunding -= transferAmount;
				if (entry.getValue() == 0) {
					it.remove();
				}
				// allocate transfer amount to this project
				this.addToAllocations(entry.getKey(), transferAmount);
				// update the amount that we have left to transfer
				amount = amount - transferAmount;
			}
		}
	}

}
