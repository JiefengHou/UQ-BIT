package problem;

import java.util.Collections;
import java.util.List;

public class RaceState {
	
	public enum Status { RACING, WON, LOST }
	
	private List<Player> players;
	private List<Opponent> opponents;
	private List<Distractor> distractors;
	private Status status;
	private int turnNo;
	
	public RaceState(List<Player> players, List<Opponent> opponents,
			List<Distractor> distractors) {
		this.players = players;
		this.opponents = opponents;
		this.distractors = distractors;
		this.status = Status.RACING;
		this.turnNo = 0;
	}
	
	public RaceState(List<Player> players, List<Opponent> opponents,
			List<Distractor> distractors, Status status, int turnNo) {
		this.players = players;
		this.opponents = opponents;
		this.distractors = distractors;
		this.status = status;
		this.turnNo = turnNo;
	}

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}
	
	public List<Opponent> getOpponents() {
		return Collections.unmodifiableList(opponents);
	}
	
	public List<Distractor> getDistractors() {
		return Collections.unmodifiableList(distractors);
	}
	
	public Status getStatus() {
		return status;
	}
	
	public int getTurnNo() {
		return turnNo;
	}
	
	public double getTotalDamageCost() {
		double sum = 0;
		for (Player p : players) {
			sum += p.getDamageCost();
		}
		return sum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((distractors == null) ? 0 : distractors.hashCode());
		result = prime * result
				+ ((opponents == null) ? 0 : opponents.hashCode());
		result = prime * result + ((players == null) ? 0 : players.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RaceState other = (RaceState) obj;
		if (distractors == null) {
			if (other.distractors != null)
				return false;
		} else if (!distractors.equals(other.distractors))
			return false;
		if (opponents == null) {
			if (other.opponents != null)
				return false;
		} else if (!opponents.equals(other.opponents))
			return false;
		if (players == null) {
			if (other.players != null)
				return false;
		} else if (!players.equals(other.players))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
}
