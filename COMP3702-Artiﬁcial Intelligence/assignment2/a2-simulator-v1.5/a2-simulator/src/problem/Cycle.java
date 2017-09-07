package problem;

/**
 * Represents a cycle type.
 * @author Joshua Song
 *
 */
public class Cycle {
	
	public enum Speed { SLOW, MEDIUM, FAST }

	private String name;
	private Speed speed;
	private boolean reliable;
	private boolean wild;
	private double price;
	
	public Cycle(String name, Speed speed, boolean reliable, boolean wild,
			double price) {
		this.name = name;
		this.speed = speed;
		this.reliable = reliable;
		this.wild = wild;
		this.price = price;
	}

	public String getName() {
		return name;
	}
	
	public Speed getSpeed() {
		return speed;
	}
	
	public boolean isReliable() {
		return reliable;
	}
	
	public boolean isWild() {
		return wild;
	}
	
	public double getPrice() {
		return price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (reliable ? 1231 : 1237);
		result = prime * result + ((speed == null) ? 0 : speed.hashCode());
		result = prime * result + (wild ? 1231 : 1237);
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
		Cycle other = (Cycle) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price))
			return false;
		if (reliable != other.reliable)
			return false;
		if (speed != other.speed)
			return false;
		if (wild != other.wild)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name + ": " + speed.toString() + ", ");
		if (reliable) {
			sb.append("reliable, ");
		} else {
			sb.append("not reliable, ");
		}
		if (wild) {
			sb.append("wild, ");
		} else {
			sb.append("not wild, ");
		}
		sb.append("$" + price);
		return sb.toString();
	}
	
	
}
