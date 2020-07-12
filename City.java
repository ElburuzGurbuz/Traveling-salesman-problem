
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

	// Calculates the distance between this city to argument city
	public int distanceTo(City city) {

		double x1 = getX() * 1.0;
		double y1 = getY() * 1.0;
		double x2 = city.getX() * 1.0;
		double y2 = city.getY() * 1.0;

		double ac = Math.abs(y2 - y1);
		double cb = Math.abs(x2 - x1);

		int ret = (int) Math.round(Math.hypot(ac, cb));
		return ret;

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