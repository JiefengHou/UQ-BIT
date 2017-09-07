package a2;

import java.util.*;

public class NaiveAllocator {

	/**
	 * @precondition: Neither of the inputs are null or contain null elements.
	 *                The parameter donations is a list of distinct donations
	 *                such that for each d in donations, d.getTotal() equals
	 *                d.getUnspent(); and for each p in projects
	 *                p.allocatedFunding() equals 0.
	 * @postcondition: returns false if there no way to completely fund all of
	 *                 the given projects using the donations, leaving both the
	 *                 input list of donations and set of projects unmodified;
	 *                 otherwise returns true and allocates to each project
	 *                 funding from the donations. The allocation to each
	 *                 project must be complete and may not violate the
	 *                 conditions of the donations.
	 */
	public static boolean canAllocate(List<Donation> donations,
			Set<Project> projects) {
		return canAllocateHelper(donations, projects, 0); 
	}
		
	private static boolean canAllocateHelper(List<Donation> donations, 
			Set<Project> projects, int index) {
		
		if(allProjectsfullyFunded(projects)) {
			return true;
		} else {
			if(index == donations.size()) {
				return false;
			}
		}
		
		Donation donation = donations.get(index);
		Set<Project> neededFundsProjects = 
				neededFundsProjects(donation.getProjects());
		
		if(donation.getUnspent() == 0 || neededFundsProjects.size() == 0) {
			return canAllocateHelper(donations, projects, index+1); 
		}
		
		for(Project project : neededFundsProjects) {
			project.allocate(donation, 1);
			if(canAllocateHelper(donations, projects, index)) {
				return true;
			} else {
				project.deallocate(donation, 1);
			}
		}	
		return false;
	}
	
	private static Set<Project> neededFundsProjects(Set<Project> projects) {
		Set<Project> result = new HashSet<Project>();
		for(Project project : projects) {
			if(!project.fullyFunded()) {
				result.add(project);
			}
		}
		return result;
	}
	
	private static boolean allProjectsfullyFunded(Set<Project> projects) {
		for(Project project : projects) {
			if(!project.fullyFunded()) {
				return false;
			}
		}
		return true;
	}
}
