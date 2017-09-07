package problem;

/**
 * Immutable representation of a distractor
 * @author Joshua Song
 *
 */
public class Distractor implements Actor {
	private String id;
	private double appearProbability;
	private boolean hasAppeared;
	private GridCell position;
	
	public Distractor(String id, double appearProbability, boolean hasAppeared,
			GridCell position) {
		this.id = id;
		this.appearProbability = appearProbability;
		this.hasAppeared = hasAppeared;
		this.position = position;
	}
	
	/**
	 * Returns a duplicated distractor, but with hasAppeared set to input
	 * @param val Whether the new distractor has appeared
	 * @return new Distractor with same properties but hasAppeared = val
	 */
	public Distractor getAppeared(boolean val) {
		return new Distractor(id, appearProbability, val, position);
	}
	
	public String getId() {
		return id;
	}
	
	public double getAppearProbability() {
		return appearProbability;
	}
	
	public boolean hasAppeared() {
		return hasAppeared;
	}
	
	public GridCell getPosition() {
		return position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(appearProbability);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (hasAppeared ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
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
		Distractor other = (Distractor) obj;
		if (Double.doubleToLongBits(appearProbability) != Double
				.doubleToLongBits(other.appearProbability))
			return false;
		if (hasAppeared != other.hasAppeared)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}	
	
	

}
