package a2.test;

import org.junit.*;

import java.util.*;

import a2.Donation;
import a2.Project;
import a2.IterativeAllocator;

/**
 * Tests for the part2.IterativeAllocator.canAllocate method.
 */
public class CompleteIterativeAllocatorTest {

	private static int REPEAT = 2;

	// -----------------------------------------------------------------------------

	@Test(timeout = 2000)
	public void handoutTests0Repeat() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			handoutTests0();
		}
	}

	// @Test(timeout = 1000)
	public void handoutTests0() throws Exception {
		handoutTestTrue();
		handoutTestFalse();
	}

	private void handoutTestTrue() throws Exception {
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

	private void handoutTestFalse() throws Exception {
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

	// -----------------------------------------------------------------------------

	@Test(timeout = 2000)
	public void boundaryTests1Repeat() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			boundaryTests1();
		}
	}

	// @Test(timeout = 1000)
	public void boundaryTests1() throws Exception {
		emptyDonations();
		emptyProjects();
		singletonProjectDonationTrue();
		singletonProjectDonationFalse();
		;
	}

	private void emptyDonations() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 100));
		projects.add(new Project("P1", 100));
		projects.add(new Project("P2", 100));

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

	private void emptyProjects() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		donations.add(new Donation("D0", 100, new HashSet<Project>()));
		donations.add(new Donation("D1", 100, new HashSet<Project>()));

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

	private void singletonProjectDonationTrue() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 8));
		donations.add(new Donation("D0", 19, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));

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

	private void singletonProjectDonationFalse() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 19));
		donations.add(new Donation("D0", 8, new HashSet<Project>(Arrays
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

	private void basicProjectDonations() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 88));
		projects.add(new Project("P1", 45));
		donations.add(new Donation("D0", 70, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D1", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));

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

	// ---------------------------------------------------------------------------

	@Test(timeout = 2000)
	public void testTrue2Repeat() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			testTrue2();
		}
	}

	// no money left over
	// @Test(timeout = 1000)
	public void testTrue2() throws Exception {
		int numProjs = 27; // must be greater than 5 and odd
		int[] value = { 133, 67 };
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();

		for (int i = 0; i < numProjs; i++) {
			projects.add(new Project("P" + i, value[i % 2]));
		}
		donations.add(new Donation("D0", 70, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
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
		donations.add(new Donation("D" + numProjs, 63, new HashSet<Project>(
				Arrays.asList(projects.get(numProjs - 2),
						projects.get(numProjs - 1)))));

		donations = new ArrayList<>(new HashSet<>(donations));
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

	// no money left over
	@Test(timeout = 12000)
	public void testTrueLarge7() throws Exception {
		int numProjs = 777; // must be greater than 5 and odd
		int[] value = { 133, 67 };
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();

		for (int i = 0; i < numProjs; i++) {
			projects.add(new Project("P" + i, value[i % 2]));
		}
		donations.add(new Donation("D0", 70, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
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
		donations.add(new Donation("D" + numProjs, 63, new HashSet<Project>(
				Arrays.asList(projects.get(numProjs - 2),
						projects.get(numProjs - 1)))));

		donations = new ArrayList<>(new HashSet<>(donations));
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

	@Test(timeout = 2000)
	public void testTrue3Repeat() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			testTrue3();
		}
	}

	// no money left over
	// @Test(timeout = 1000)
	public void testTrue3() throws Exception {
		Map<String, Project> projects = new HashMap<>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.put("Paa", new Project("Paa", 157));
		projects.put("Pab", new Project("Pab", 92));
		projects.put("Pac", new Project("Pac", 18));
		projects.put("Pba", new Project("Pba", 132));
		projects.put("Pbb", new Project("Pbb", 69));
		projects.put("Pbc", new Project("Pbc", 147));
		projects.put("Pbd", new Project("Pbd", 23));
		projects.put("Pbe", new Project("Pbe", 179));
		projects.put("Pca", new Project("Pca", 82));
		projects.put("Pcb", new Project("Pcb", 138));
		projects.put("Pcc", new Project("Pcc", 111));
		donations.add(new Donation("Daa", 115, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab")))));
		donations.add(new Donation("Dab", 129, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac")))));
		donations.add(new Donation("Dac", 177, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac"), projects.get("Pba"),
						projects.get("Pbe")))));
		donations.add(new Donation("Dad", 307, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbe"),
						projects.get("Pca"), projects.get("Pcb"),
						projects.get("Pcc")))));
		donations.add(new Donation("Dae", 15, new HashSet<Project>(Arrays
				.asList(projects.get("Pca"), projects.get("Pcb"),
						projects.get("Pcc")))));
		donations.add(new Donation("Daf", 55, new HashSet<Project>(Arrays
				.asList(projects.get("Pcb"), projects.get("Pcc")))));
		donations.add(new Donation("Dba", 20, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb")))));
		donations.add(new Donation("Dbb", 147, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb"),
						projects.get("Pbc")))));
		donations.add(new Donation("Dbc", 156, new HashSet<Project>(Arrays
				.asList(projects.get("Pbc"), projects.get("Pbd"),
						projects.get("Pbe")))));
		donations.add(new Donation("Dbd", 27, new HashSet<Project>(Arrays
				.asList(projects.get("Pbd"), projects.get("Pbe")))));

		donations = new ArrayList<>(new HashSet<>(donations));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects.values());
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects.values()), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}

	@Test(timeout = 2000)
	public void testTrue4Repeat() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			testTrue4();
		}
	}

	// no money left over
	// @Test(timeout = 1000)
	public void testTrue4() throws Exception {
		Map<String, Project> projects = new HashMap<>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.put("Paa", new Project("Paa", 157));
		projects.put("Pab", new Project("Pab", 92));
		projects.put("Pac", new Project("Pac", 18));
		projects.put("Pba", new Project("Pba", 132));
		projects.put("Pbb", new Project("Pbb", 69));
		projects.put("Pbca", new Project("Pbca", 80));
		projects.put("Pbcb", new Project("Pbcb", 85));
		projects.put("Pbcc", new Project("Pbcc", 30));
		projects.put("Pbcd", new Project("Pbcd", 35));
		projects.put("Pbce", new Project("Pbce", 20));
		projects.put("Pbd", new Project("Pbd", 23));
		projects.put("Pbe", new Project("Pbe", 179));
		projects.put("Pca", new Project("Pca", 82));
		projects.put("Pcb", new Project("Pcb", 138));
		projects.put("Pcc", new Project("Pcc", 111));
		donations.add(new Donation("Daa", 115, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab")))));
		donations.add(new Donation("Dab", 129, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac")))));
		donations.add(new Donation("Dac", 177, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac"), projects.get("Pba"),
						projects.get("Pbe")))));
		donations.add(new Donation("Dad", 307, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbe"),
						projects.get("Pca"), projects.get("Pcb"),
						projects.get("Pcc")))));
		donations.add(new Donation("Dae", 15, new HashSet<Project>(Arrays
				.asList(projects.get("Pca"), projects.get("Pcb"),
						projects.get("Pcc")))));
		donations.add(new Donation("Daf", 55, new HashSet<Project>(Arrays
				.asList(projects.get("Pcb"), projects.get("Pcc")))));

		donations.add(new Donation("Dba", 20, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb")))));
		donations.add(new Donation("Dbb", 147, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb"),
						projects.get("Pbca"), projects.get("Pbcb")))));
		donations.add(new Donation("Dbc", 156, new HashSet<Project>(Arrays
				.asList(projects.get("Pbca"), projects.get("Pbcb"),
						projects.get("Pbd"), projects.get("Pbe")))));
		donations.add(new Donation("Dbd", 27, new HashSet<Project>(Arrays
				.asList(projects.get("Pbd"), projects.get("Pbe")))));

		donations.add(new Donation("Dda", 5, new HashSet<Project>(Arrays
				.asList(projects.get("Pbca"), projects.get("Pbcb")))));
		donations.add(new Donation("Ddb", 33, new HashSet<Project>(Arrays
				.asList(projects.get("Pbca"), projects.get("Pbcb"),
						projects.get("Pbcc")))));
		donations.add(new Donation("Ddc", 25, new HashSet<Project>(Arrays
				.asList(projects.get("Pbcc"), projects.get("Pbcd"),
						projects.get("Pbce")))));
		donations.add(new Donation("Ddd", 40, new HashSet<Project>(Arrays
				.asList(projects.get("Pbcd"), projects.get("Pbce")))));

		donations = new ArrayList<>(new HashSet<>(donations));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects.values());
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects.values()), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}

	@Test(timeout = 2000)
	public void testTrue5Repeat() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			testTrue5();
		}
	}

	// money left over
	// @Test(timeout = 1000)
	public void testTrue5() throws Exception {
		Map<String, Project> projects = new HashMap<>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.put("Paa", new Project("Paa", 157));
		projects.put("Pab", new Project("Pab", 92));
		projects.put("Pac", new Project("Pac", 18));
		projects.put("Pba", new Project("Pba", 132));
		projects.put("Pbb", new Project("Pbb", 69));
		projects.put("Pbca", new Project("Pbca", 80));
		projects.put("Pbcb", new Project("Pbcb", 85));
		projects.put("Pbcc", new Project("Pbcc", 30));
		projects.put("Pbcd", new Project("Pbcd", 35));
		projects.put("Pbce", new Project("Pbce", 20));
		projects.put("Pbd", new Project("Pbd", 23));
		projects.put("Pbe", new Project("Pbe", 179));
		projects.put("Pca", new Project("Pca", 82));
		projects.put("Pcb", new Project("Pcb", 138));
		projects.put("Pcc", new Project("Pcc", 111));
		donations.add(new Donation("Daa", 115, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab")))));
		donations.add(new Donation("Dab", 129, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac")))));
		donations.add(new Donation("Dac", 177, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac"), projects.get("Pba"),
						projects.get("Pbe")))));
		donations.add(new Donation("Dad", 307, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbe"),
						projects.get("Pca"), projects.get("Pcb"),
						projects.get("Pcc")))));
		donations.add(new Donation("Dae", 15, new HashSet<Project>(Arrays
				.asList(projects.get("Pca"), projects.get("Pcb"),
						projects.get("Pcc")))));
		donations.add(new Donation("Daf", 55, new HashSet<Project>(Arrays
				.asList(projects.get("Pcb"), projects.get("Pcc")))));

		donations.add(new Donation("Dba", 20, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb")))));
		donations.add(new Donation("Dbb", 147, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb"),
						projects.get("Pbca"), projects.get("Pbcb")))));
		donations.add(new Donation("Dbc", 156, new HashSet<Project>(Arrays
				.asList(projects.get("Pbca"), projects.get("Pbcb"),
						projects.get("Pbd"), projects.get("Pbe")))));
		donations.add(new Donation("Dbd", 27, new HashSet<Project>(Arrays
				.asList(projects.get("Pbd"), projects.get("Pbe")))));

		donations.add(new Donation("Dda", 5, new HashSet<Project>(Arrays
				.asList(projects.get("Pbca"), projects.get("Pbcb")))));
		donations.add(new Donation("Ddb", 33, new HashSet<Project>(Arrays
				.asList(projects.get("Pbca"), projects.get("Pbcb"),
						projects.get("Pbcc")))));
		donations.add(new Donation("Ddc", 35, new HashSet<Project>(Arrays
				.asList(projects.get("Pbcc"), projects.get("Pbcd"),
						projects.get("Pbce")))));
		donations.add(new Donation("Ddd", 40, new HashSet<Project>(Arrays
				.asList(projects.get("Pbcd"), projects.get("Pbce")))));

		donations = new ArrayList<>(new HashSet<>(donations));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects.values());
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects.values()), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}

	@Test(timeout = 2000)
	public void testfalse6Repeat() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			testFalse6();
		}
	}

	// @Test(timeout = 1000)
	public void testFalse6() throws Exception {
		basicProjectDonations(); // we are not going to give marks for just
									// always returning false: it has to work on
									// at least a simple example for us to give
									// marks for this false result.

		Map<String, Project> projects = new HashMap<>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.put("Paa", new Project("Paa", 157));
		projects.put("Pab", new Project("Pab", 92));
		projects.put("Pac", new Project("Pac", 18));
		projects.put("Pba", new Project("Pba", 132));
		projects.put("Pbb", new Project("Pbb", 69));
		projects.put("Pbca", new Project("Pbca", 80));
		projects.put("Pbcb", new Project("Pbcb", 85));
		projects.put("Pbcc", new Project("Pbcc", 30));
		projects.put("Pbcd", new Project("Pbcd", 35));
		projects.put("Pbce", new Project("Pbce", 20));
		projects.put("Pbd", new Project("Pbd", 23));
		projects.put("Pbe", new Project("Pbe", 179));
		projects.put("Pca", new Project("Pca", 82));
		projects.put("Pcb", new Project("Pcb", 138));
		projects.put("Pcc", new Project("Pcc", 111));
		donations.add(new Donation("Daa", 115, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab")))));
		donations.add(new Donation("Dab", 129, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac")))));
		donations.add(new Donation("Dac", 177, new HashSet<Project>(Arrays
				.asList(projects.get("Paa"), projects.get("Pab"),
						projects.get("Pac"), projects.get("Pba"),
						projects.get("Pbe")))));
		donations.add(new Donation("Dad", 307, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbe"),
						projects.get("Pca"), projects.get("Pcb"),
						projects.get("Pcc")))));
		donations.add(new Donation("Dae", 15, new HashSet<Project>(Arrays
				.asList(projects.get("Pca"), projects.get("Pcb"),
						projects.get("Pcc")))));
		donations.add(new Donation("Daf", 55, new HashSet<Project>(Arrays
				.asList(projects.get("Pcb"), projects.get("Pcc")))));

		donations.add(new Donation("Dba", 20, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb")))));
		donations.add(new Donation("Dbb", 147, new HashSet<Project>(Arrays
				.asList(projects.get("Pba"), projects.get("Pbb"),
						projects.get("Pbca"), projects.get("Pbcb")))));
		donations.add(new Donation("Dbc", 156, new HashSet<Project>(Arrays
				.asList(projects.get("Pbca"), projects.get("Pbcb"),
						projects.get("Pbd"), projects.get("Pbe")))));
		donations.add(new Donation("Dbd", 27, new HashSet<Project>(Arrays
				.asList(projects.get("Pbd"), projects.get("Pbe")))));

		donations.add(new Donation("Dda", 33, new HashSet<Project>(Arrays
				.asList(projects.get("Pbca"), projects.get("Pbcb")))));
		donations.add(new Donation("Ddb", 5, new HashSet<Project>(Arrays
				.asList(projects.get("Pbca"), projects.get("Pbcb"),
						projects.get("Pbcc")))));
		donations.add(new Donation("Ddc", 35, new HashSet<Project>(Arrays
				.asList(projects.get("Pbcc"), projects.get("Pbcd"),
						projects.get("Pbce")))));
		donations.add(new Donation("Ddd", 40, new HashSet<Project>(Arrays
				.asList(projects.get("Pbcd"), projects.get("Pbce")))));

		donations = new ArrayList<>(new HashSet<>(donations));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects.values());
		Assert.assertFalse(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects.values()), actualProjects);
		// no allocations should have been made
		checkEmptyAllocation(actualDonations, actualProjects);
	}

	// -------------------------------------------------------------------------

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
