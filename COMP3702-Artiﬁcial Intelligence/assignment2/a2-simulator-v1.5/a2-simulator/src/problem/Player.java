package problem;

/**
 * Immutable representation of a player
 * @author Joshua Song
 *
 */
public class Player  implements Actor {
	private String id;
	private Cycle cycle;
	private GridCell position;
	private double damageCost;	// Damage cost gained this turn
	private boolean isObstacle;
	
	public Player(String id, Cycle cycle, GridCell position) {
		this.id = id;
		this.cycle = cycle;
		this.position = position;
		damageCost = 0;
		isObstacle = false;
	}
	
	public Player(String id, Cycle cycle, GridCell position, double damageCost,
			boolean isObstacle) {
		this.id = id;
		this.cycle = cycle;
		this.position = position;
		this.damageCost = damageCost;
		this.isObstacle = isObstacle;
	}
	
	public String getId() {
		return id;
	}
	
	public GridCell getPosition() {
		return position;
	}
	
	public Cycle getCycle() {
		return cycle;
	}
	
	/**
	 * Returns the damage cost received this turn. I.e. if a player received
	 * damage several turns ago but not in this turn, 0 is returned.
	 * @return
	 */
	public double getDamageCost() {
		return damageCost;
	}
	
	public boolean isObstacle() {
		return isObstacle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cycle == null) ? 0 : cycle.hashCode());
		long temp;
		temp = Double.doubleToLongBits(damageCost);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (isObstacle ? 1231 : 1237);
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
		Player other = (Player) obj;
		if (cycle == null) {
			if (other.cycle != null)
				return false;
		} else if (!cycle.equals(other.cycle))
			return false;
		if (Double.doubleToLongBits(damageCost) != Double
				.doubleToLongBits(other.damageCost))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isObstacle != other.isObstacle)
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}
}
