package algooooo;

public class City {
	int x;
	int y;
	int order;

	public City() {
		this.x = (int) (Math.random() * 200);
		this.y = (int) (Math.random() * 200);
	}

	public City(int x, int y, int order) {
		this.x = x;
		this.y = y;
		this.order = order;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int distanceTo(City city) {
		int xDistance = Math.abs(getX() - city.getX());
		int yDistance = Math.abs(getY() - city.getY());
		int distance = (int) Math.round(Math.sqrt((xDistance * xDistance) + (yDistance * yDistance)));

		return distance;
	}

	@Override
	public String toString() {
		return getX() + ", " + getY();
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}
}