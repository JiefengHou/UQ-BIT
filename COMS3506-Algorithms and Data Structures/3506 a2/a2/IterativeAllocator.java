package a2;

import java.util.*;

public class IterativeAllocator {

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
		
		List<Donation> ds = findProjectsDonation(donations,projects);
		donateToNotFullProject(ds,projects);
		
		List<Donation> unSpentDonations = findUnSpentDonation(ds);
		List<Project> neededFundsProjects = findNeededFundsProject(projects);

		if(neededFundsProjects.size()>0 && unSpentDonations.size()==0) {
			resetAllocations(projects);
			return false;
		}
		
		for(Project project : neededFundsProjects){
			// start to find donations until this project is fully funded
			do {
				unSpentDonations = findUnSpentDonation(ds);
				List<Project> path = new ArrayList<Project>();
				List<Donation> donationRecord = new ArrayList<Donation>();
				int getMoney = findPath(ds,project,unSpentDonations,path,
						donationRecord);
				// if donation amount is > 0
				if(getMoney>0) {
					int neededFunds = project.neededFunds();
					if(getMoney<=neededFunds) {
						for(int i = 0; i<path.size()-1;i++) {
							path.get(i).transfer(getMoney, path.get(i+1));
							if(i==path.size()-2) {
								path.get(i+1).allocate(donationRecord.get(0), 
										getMoney);
							}
						}
						if(getMoney==neededFunds) break;
						
					} else {
						for(int i = 0; i<path.size()-1;i++) {
							path.get(i).transfer(neededFunds, path.get(i+1));
							if(i==path.size()-2) {
								path.get(i+1).allocate(donationRecord.get(0), 
										neededFunds);
							}
						}
					}
					
				} else {
					resetAllocations(projects);
					return false;
				}
			} while(project.neededFunds()!=0);
		}
		clearAllocation(projects);
		return true;
	}
	
	//find path using DFS
	private static int findPath(List<Donation> ds, Project project, 
			List<Donation> unSpentDonations,
			List<Project> path,List<Donation> donationRecord) {
		int getMoney = 0;
		List<Donation> projectDonations = findDonations(project,ds);
		List<Project> nextProjects = findProjects(project,projectDonations,path);
		if(nextProjects.size()==0) return 0;
		path.add(project);
		for(Project p : nextProjects) {
			path.add(p);
			for(Donation d : unSpentDonations) {
				if(d.canBeUsedFor(p)) {
					if(d.getUnspent()>0) {
						donationRecord.add(d);
						return d.getUnspent();
					}
				}
			}
			getMoney = findPath(ds,p,unSpentDonations,path,donationRecord);
			path.remove(p);
		}
		return getMoney;
	}
	
	// return list of Project which in a same donation
	private static List<Project> findProjects(Project project, 
			List<Donation> donations, List<Project> path) {
		List<Project> result = new ArrayList<Project>();
		for(Donation d : donations) {
			for(Project p : d.getProjects()) {
				if(!p.equals(project)&&!result.contains(p)&&!path.contains(p)) {
					result.add(p);
				}
			}
		}	
		return result;
	}

	// return list of Donation which can donate to a specific project
	private static List<Donation> findDonations(Project project, 
			List<Donation> donations) {
		List<Donation> result = new ArrayList<Donation>();
		for(Donation donation : donations) {
			if(donation.canBeUsedFor(project)) {
				result.add(donation);
			}
		}
		return result;
	}
		
	// return list of donation which can donate to more than one project, and 
	// find donation which only can donate to one project and then donate.
	private static List<Donation> findProjectsDonation(List<Donation> donations,
			Set<Project> projects) {
		List<Donation> result = new ArrayList<Donation>();
		for(Donation donation : donations) {
			Set<Project> Projects = donation.getProjects();
			if(Projects.size()==1) {
				for(Project p : Projects) {
					int neededFunds = p.neededFunds();
					if(neededFunds == 0) break;
					if(neededFunds > 0) {
						int Unspent = donation.getUnspent();
						if(Unspent <= neededFunds) {
							p.allocate(donation, Unspent);
							if(Unspent == neededFunds) break;
						} else {
							p.allocate(donation, neededFunds);
						}
					}
				}
			} else {
				result.add(donation);
			}
		}
		return result;
	}
	
	// donate to project which is needed funds
	private static void donateToNotFullProject(List<Donation> donations,
			Set<Project> projects) {
		for(Donation donation : donations) {
			if(donation.getUnspent()==0) continue;
			Set<Project> ps = donation.getProjects();
			for(Project p : ps) {
				int Unspent = donation.getUnspent();				
				int neededFunds = p.neededFunds();
				if(neededFunds==0) continue;
				if(Unspent<=neededFunds) {
					p.allocate(donation, Unspent);
					if(Unspent == neededFunds) break;
				} else {
					p.allocate(donation, neededFunds);
				}
			}
		}
	}
	
	// return list of Donation that has unspent donation.
	private static List<Donation> findUnSpentDonation(List<Donation> donations) {
		List<Donation> result = new ArrayList<Donation>();
		for(Donation donation : donations) {
			if(donation.getUnspent()>0) {
				result.add(donation);
			}
		}
		return result;
	}
	
	// return list of Project that need funds
	private static List<Project> findNeededFundsProject(Set<Project> projects) {
		List<Project> result = new ArrayList<Project>();
		for(Project project : projects) {
			if(!project.fullyFunded()) {
				result.add(project);
			}
		}
		return result;
	}
	
	// clear all allocations
	private static void resetAllocations(Set<Project> projects) {
		for(Project project : projects){
			project.deallocateAll();
		}
	}
	
	// clear the allocation which value is 0
	private static void clearAllocation(Set<Project> projects) {
		for(Project project : projects){
			for (Map.Entry<Donation, Integer> allocation:project.getAllocations()
					.entrySet()) {
				Donation d = allocation.getKey();
				int amount = allocation.getValue();
				if(amount==0) {
					project.deallocate(d, 0);
				}
			}
		}
	}
}
