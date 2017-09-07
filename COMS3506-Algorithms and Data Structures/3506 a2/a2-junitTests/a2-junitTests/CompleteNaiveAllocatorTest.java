package a2.test;

import org.junit.*;

import java.util.*;

import a2.Donation;
import a2.Project;
import a2.NaiveAllocator;

/**
 * Tests for the part2.NaiveAllocator.canAllocate method.
 */
public class CompleteNaiveAllocatorTest {

	@Test(timeout = 10000)
	public void handoutTests0() throws Exception {
		handoutTestTrue();
		handoutTestFalse();
	}

	private void handoutTestTrue() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 10));
		projects.add(new Project("P1", 10));
		projects.add(new Project("P2", 10));
		donations.add(new Donation("D0", 10, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D1", 10, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(2)))));
		donations.add(new Donation("D2", 5, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D3", 5, new HashSet<Project>(Arrays
				.asList(projects.get(2)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(NaiveAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}

	private void handoutTestFalse() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 10));
		projects.add(new Project("P1", 10));
		projects.add(new Project("P2", 10));
		donations.add(new Donation("D0", 10, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2)))));
		donations.add(new Donation("D1", 20, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(NaiveAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// no allocations should have been made
		checkEmptyAllocation(actualDonations, actualProjects);
	}

	// -----------------------------------------------------------------------------

	@Test(timeout = 1000)
	public void boundaryTests1() throws Exception {
		emptyDonationsFalse();
		emptyProjectsTrue();
		singletonProjectDonationTrue();
		singletonProjectDonationFalse();
	}

	private void emptyDonationsFalse() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 3));
		projects.add(new Project("P1", 3));
		projects.add(new Project("P2", 3));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(NaiveAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// no allocations should have been made
		checkEmptyAllocation(actualDonations, actualProjects);
	}

	private void emptyProjectsTrue() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		donations.add(new Donation("D0", 5, new HashSet<Project>()));
		donations.add(new Donation("D1", 5, new HashSet<Project>()));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(NaiveAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}

	private void singletonProjectDonationTrue() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 8));
		donations.add(new Donation("D0", 19, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(NaiveAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}

	private void singletonProjectDonationFalse() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 19));
		donations.add(new Donation("D0", 8, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(NaiveAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// no allocations should have been made
		checkEmptyAllocation(actualDonations, actualProjects);
	}

	private void basicProjectDonationsTrue() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 8));
		projects.add(new Project("P1", 4));
		donations.add(new Donation("D0", 7, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D1", 10, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(NaiveAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}

	// ---------------------------------------------------------------------------


	// no money left over
	@Test(timeout = 10000)
	public void testTrue2() throws Exception {
		int numProjs = 7; // must be greater than 5 and odd
		int[] value = { 5, 2 };
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();

		for (int i = 0; i < numProjs; i++) {
			projects.add(new Project("P" + i, value[i % 2]));
		}
		donations.add(new Donation("D0", 3, new HashSet<Project>(Arrays.asList(
				projects.get(0), projects.get(1)))));
		donations.add(new Donation("D1", value[1], new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D2", value[0], new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2)))));
		for (int i = 3; i < numProjs - 2; i++) {
			donations.add(new Donation("D" + i, value[(i) % 2],
					new HashSet<Project>(Arrays.asList(projects.get(i - 1),
							projects.get(i)))));
		}
		donations.add(new Donation("D" + (numProjs - 2),
				value[(numProjs - 2) % 2], new HashSet<Project>(Arrays.asList(
						projects.get(numProjs - 3), projects.get(numProjs - 2),
						projects.get(numProjs - 1)))));
		donations.add(new Donation("D" + (numProjs - 1),
				value[(numProjs - 1) % 2],
				new HashSet<Project>(Arrays.asList(projects.get(numProjs - 2),
						projects.get(numProjs - 1)))));
		donations.add(new Donation("D" + numProjs, 2, new HashSet<Project>(
				Arrays.asList(projects.get(numProjs - 2),
						projects.get(numProjs - 1)))));

		donations = new ArrayList<>(new HashSet<>(donations));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(NaiveAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}

	// no money left over
	@Test(timeout = 10000)
	public void testTrue3() throws Exception {
		Map<String, Project> projects = new HashMap<>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.put("Paa", new Project("Paa", 1));
		projects.put("Pab", new Project("Pab", 2));
		projects.put("Pac", new Project("Pac", 2));
		projects.put("Pba", new Project("Pba", 3));
		projects.put("Pbb", new Project("Pbb", 2));
		projects.put("Pbc", new Project("Pbc", 2));
		projects.put("Pbd", new Project("Pbd", 3));
		projects.put("Pbe", new Project("Pbe", 2));
		projects.put("Pca", new Project("Pca", 2));
		projects.put("Pcb", new Project("Pcb", 1));
		projects.put("Pcc", new Project("Pcc", 1));
		donations.add(new Donation("Daa", 1, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab")))));
		donations.add(new Donation("Dab", 1, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac")))));
		donations.add(new Donation("Dac", 5, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac"), projects.get("Pba"),
						projects.get("Pbe")))));
		donations.add(new Donation("Dad", 3, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbe"),
						projects.get("Pca"), projects.get("Pcb"),
						projects.get("Pcc")))));
		donations.add(new Donation("Dae", 1, new HashSet<Project>(Arrays
				.asList(projects.get("Pca"), projects.get("Pcb"),
						projects.get("Pcc")))));
		donations.add(new Donation("Daf", 1, new HashSet<Project>(Arrays
				.asList(projects.get("Pcb"), projects.get("Pcc")))));
		donations.add(new Donation("Dba", 1, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb")))));
		donations.add(new Donation("Dbb", 3, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb"),
						projects.get("Pbc")))));
		donations.add(new Donation("Dbc", 3, new HashSet<Project>(Arrays
				.asList(projects.get("Pbc"), projects.get("Pbd"),
						projects.get("Pbe")))));
		donations.add(new Donation("Dbd", 2, new HashSet<Project>(Arrays
				.asList(projects.get("Pbd"), projects.get("Pbe")))));

		donations = new ArrayList<>(new HashSet<>(donations));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects.values());
		Assert.assertTrue(NaiveAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects.values()), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}

	// some money left over
	@Test(timeout = 10000)
	public void testTrue4() throws Exception {
		Map<String, Project> projects = new HashMap<>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.put("Paa", new Project("Paa", 1));
		projects.put("Pab", new Project("Pab", 2));
		projects.put("Pac", new Project("Pac", 2));
		projects.put("Pba", new Project("Pba", 3));
		projects.put("Pbb", new Project("Pbb", 2));
		projects.put("Pbc", new Project("Pbc", 2));
		projects.put("Pbd", new Project("Pbd", 3));
		projects.put("Pbe", new Project("Pbe", 2));
		projects.put("Pca", new Project("Pca", 2));
		projects.put("Pcb", new Project("Pcb", 1));
		projects.put("Pcc", new Project("Pcc", 1));
		donations.add(new Donation("Daa", 2, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab")))));
		donations.add(new Donation("Dab", 1, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac")))));
		donations.add(new Donation("Dac", 5, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac"), projects.get("Pba"),
						projects.get("Pbe")))));
		donations.add(new Donation("Dad", 3, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbe"),
						projects.get("Pca"), projects.get("Pcb"),
						projects.get("Pcc")))));
		donations.add(new Donation("Dae", 1, new HashSet<Project>(Arrays
				.asList(projects.get("Pca"), projects.get("Pcb"),
						projects.get("Pcc")))));
		donations.add(new Donation("Daf", 2, new HashSet<Project>(Arrays
				.asList(projects.get("Pcb"), projects.get("Pcc")))));
		donations.add(new Donation("Dba", 1, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb")))));
		donations.add(new Donation("Dbb", 3, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb"),
						projects.get("Pbc")))));
		donations.add(new Donation("Dbc", 3, new HashSet<Project>(Arrays
				.asList(projects.get("Pbc"), projects.get("Pbd"),
						projects.get("Pbe")))));
		donations.add(new Donation("Dbd", 2, new HashSet<Project>(Arrays
				.asList(projects.get("Pbd"), projects.get("Pbe")))));

		donations = new ArrayList<>(new HashSet<>(donations));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects.values());
		Assert.assertTrue(NaiveAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects.values()), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}

	@Test(timeout = 60000)
	public void testFalse4() throws Exception {
		basicProjectDonationsTrue(); // we are not going to give marks for just
		// always returning false: it has to work on
		// at least a simple example for us to give
		// marks for this false result.

		Map<String, Project> projects = new HashMap<>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.put("Paa", new Project("Paa", 1));
		projects.put("Pab", new Project("Pab", 2));
		projects.put("Pac", new Project("Pac", 2));
		projects.put("Pba", new Project("Pba", 3));
		projects.put("Pbb", new Project("Pbb", 2));
		projects.put("Pbc", new Project("Pbc", 2));
		projects.put("Pbd", new Project("Pbd", 3));
		projects.put("Pbe", new Project("Pbe", 2));
		projects.put("Pca", new Project("Pca", 2));
		projects.put("Pcb", new Project("Pcb", 1));
		projects.put("Pcc", new Project("Pcc", 1));
		donations.add(new Donation("Daa", 1, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab")))));
		donations.add(new Donation("Dab", 1, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac")))));
		donations.add(new Donation("Dac", 5, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac"), projects.get("Pbe")))));
		donations.add(new Donation("Dad", 3, new HashSet<Project>(Arrays
				.asList(projects.get("Pbe"), projects.get("Pca"),
						projects.get("Pcb"), projects.get("Pcc")))));
		donations.add(new Donation("Dae", 1, new HashSet<Project>(Arrays
				.asList(projects.get("Pca"), projects.get("Pcb"),
						projects.get("Pcc")))));
		donations.add(new Donation("Daf", 1, new HashSet<Project>(Arrays
				.asList(projects.get("Pcb"), projects.get("Pcc")))));
		donations.add(new Donation("Dba", 1, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb")))));
		donations.add(new Donation("Dbb", 3, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb"),
						projects.get("Pbc")))));
		donations.add(new Donation("Dbc", 3, new HashSet<Project>(Arrays
				.asList(projects.get("Pbc"), projects.get("Pbd"),
						projects.get("Pbe")))));
		donations.add(new Donation("Dbd", 2, new HashSet<Project>(Arrays
				.asList(projects.get("Pbd"), projects.get("Pbe")))));

		donations = new ArrayList<>(new HashSet<>(donations));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects.values());
		Assert.assertFalse(NaiveAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects.values()), actualProjects);
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
