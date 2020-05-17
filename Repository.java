package algooooo;

import java.util.ArrayList;
import java.util.List;

public class Repository {

	private static ArrayList<City> cities = new ArrayList<>();

	public static void addCity(City city) {
		cities.add(city);
	}

	public static City getCity(int index) {
		return cities.get(index);
	}

	public static int getNumberOfCities() {
		return cities.size();
	}

	/**
	 * @return the cities
	 */
	public static ArrayList<City> getCities() {
		return cities;
	}

	/**
	 * @param cities the cities to set
	 */
	public static void setCities(ArrayList<City> cities) {
		Repository.cities = cities;
	}
}