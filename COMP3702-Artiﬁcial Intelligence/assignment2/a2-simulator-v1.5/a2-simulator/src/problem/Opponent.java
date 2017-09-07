package problem;

/**
 * Immutable representation of an opponent
 * @author Joshua Song
 *
 */
public class Opponent implements Actor {
	private String id;
	private RandomPolicy policy;
	private GridCell position;
	
	public Opponent(String id, RandomPolicy policy, GridCell position) {
		this.id = id;
		this.policy = policy;
		this.position = position;
	}
	
	public String getId() {
		return id;
	}
	
	public RandomPolicy getPolicy() {
		return policy;
	}
	
	public GridCell getPosition() {
		return position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((policy == null) ? 0 : policy.hashCode());
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
		Opponent other = (Opponent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (policy == null) {
			if (other.policy != null)
				return false;
		} else if (!policy.equals(other.policy))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}


}
