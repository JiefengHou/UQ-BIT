package a2.test;

import org.junit.*;
import java.util.*;
import a2.*;

/**
 * Some tests for the part2.IterativeAllocator.canAllocate method. A much more
 * extensive test suite will be used to mark your code, but this should get you
 * started writing your own tests to help you to debug your implementation.
 */
public class IterativeAllocatorTest {

	@Test
	public void basicTestTrue() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 100));
		projects.add(new Project("P1", 100));
		projects.add(new Project("P2", 100));
		donations.add(new Donation("D0", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D1", 100, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(2)))));
		donations.add(new Donation("D2", 50, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D3", 50, new HashSet<Project>(Arrays
				.asList(projects.get(2)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}

	@Test
	public void basicTestFalse() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 100));
		projects.add(new Project("P1", 100));
		projects.add(new Project("P2", 100));
		donations.add(new Donation("D0", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2)))));
		donations.add(new Donation("D1", 200, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// no allocations should have been made
		checkEmptyAllocation(actualDonations, actualProjects);
	}

	// helper methods

	/**
	 * Helper method to check that each project has been completely allocated by
	 * the given donations, and that the total spent on each donation is equal
	 * to that spent on the given projects.
	 **/
	private void checkCompleteAllocation(List<Donation> donations,
			Set<Project> projects) {

		// the amount spent from each donation by all of the combined projects
		Map<Donation, Integer> totalSpent = new HashMap<>();

		// check that each project has been completely (and properly) allocated
		// and calculate totalSpent
		for (Project p : projects) {
			Assert.assertTrue(p.fullyFunded());
			for (Map.Entry<Donation, Integer> allocation : p.getAllocations()
					.entrySet()) {
				Donation d = allocation.getKey();
				int amount = allocation.getValue();
				Assert.assertTrue(amount > 0);
				Assert.assertTrue(d.canBeUsedFor(p));
				Assert.assertTrue(donations.contains(d));
				if (totalSpent.containsKey(d)) {
					totalSpent.put(d, totalSpent.get(d) + amount);
				} else {
					totalSpent.put(d, amount);
				}
			}
		}

		// check that the remaining funds in each donation are correct, assuming
		// that no funds were spent from each donation to begin with.
		for (Donation d : donations) {
			if (totalSpent.containsKey(d)) {
				Assert.assertTrue(d.getUnspent() >= 0);
				Assert.assertEquals(d.getUnspent(),
						d.getTotal() - totalSpent.get(d));
			} else {
				Assert.assertEquals(d.getUnspent(), d.getTotal());
			}
		}
	}

	/**
	 * Helper method to check that no allocations have been made for any project
	 * in projects and that all donations have not been spent at all.
	 **/
	private void checkEmptyAllocation(List<Donation> donations,
			Set<Project> projects) {
		for (Project p : projects) {
			Assert.assertEquals(p.getCost(), p.neededFunds());
		}
		for (Donation d : donations) {
			Assert.assertEquals(d.getUnspent(), d.getTotal());
		}
	}
}
