
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*
 * This code was prepared by Muhammet Kürþat Açýkgöz and Ahmet Elburuz Gürbüz
 * for traveling salesman problem solution. The purpose of this code is to
 * combine 3 different methods and try to approach the best situation. In
 * np-hard problems, the aim is not to find the exact result, but to converge to
 * the exact result as much as possible. In this code, the closest neighbor
 * method, 2-opt method and simulated annealing method are used together.
 * 
 * Muhammet Kürþat AÇIKGÖZ 
 * Ahmet Elburuz GÜRBÜZ 
 * 
 */

public class main {
	// Global variables initialized
	static ArrayList<Integer> orders = new ArrayList<Integer>();
	static ArrayList<Integer> ordersTemp = new ArrayList<Integer>();
	static ArrayList<City> cities = new ArrayList<City>();
	static ArrayList<City> route = new ArrayList<City>();
	static int totalDistance = 0;

	public static void main(String[] args) throws IOException {

		int count = 0;
		int totalX = 0;
		int totalY = 0;

		long startTime = System.currentTimeMillis();

		// In this try catch file is read and added to global "cities" array list as
		// city objects
		try {
			Scanner scanner = new Scanner(new File("example-input-3.txt"));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] list = line.split(" ");
				if (list.length > 3) {
					int j = 0;
					for (int i = 0; i < list.length; i++) {
						if (isNumeric(list[i])) {
							list[j] = list[i];
							j++;
						}
					}
				}
				City city = new City(Integer.parseInt(list[1]), Integer.parseInt(list[2]), count);
				cities.add(city);
				totalX += Integer.parseInt(list[1]);
				totalY += Integer.parseInt(list[2]);
				count++;
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Average amount of x an y's calculated
		int averageX = totalX / count;
		int averageY = totalY / count;

		// Closest city to (average x, average y) found
		City midPoint = new City(averageX, averageY, -1);

		@SuppressWarnings("unchecked")
		ArrayList<City> notVisitedCities = (ArrayList<City>) cities.clone();

		// This if condition for small inputs to find exact solution with brute force
		if (notVisitedCities.size() < 15) {
			route.add(notVisitedCities.get(0));
			notVisitedCities.remove(0);

			bruteForceTSP(notVisitedCities);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			System.out.println("total distance " + totalDistance + " " + dtf.format(now));

			for (int i = 0; i < orders.size(); i++) {
				System.out.println(orders.get(i));
			}

			System.out.print(orders.get(0));

		} else { // if input size is not small to use brute force gonna use 2-opt and simulated
					// annealling because tsp is np hard problem
			City currentPoint = findNearestPoint(notVisitedCities, midPoint);
			City startPoint = currentPoint;
			orders.add(currentPoint.getOrder());
			notVisitedCities.remove(currentPoint);

			currentPoint = findNearestPoint(notVisitedCities, midPoint);
			City secondStartPoint = currentPoint;
			orders.add(currentPoint.getOrder());
			notVisitedCities.remove(currentPoint);

			totalDistance = startPoint.distanceTo(secondStartPoint);

			int size = notVisitedCities.size();
			for (int i = 0; i < size / 2; i++) {
				currentPoint = findNearestPoint(notVisitedCities, currentPoint);
				orders.add(currentPoint.getOrder());
				notVisitedCities.remove(currentPoint);
			}

			Collections.reverse(orders);
			currentPoint = startPoint;

			while (notVisitedCities.size() != 0) {
				currentPoint = findNearestPoint(notVisitedCities, currentPoint);
				orders.add(currentPoint.getOrder());
				notVisitedCities.remove(currentPoint);
			}

			totalDistance += cities.get(orders.get(0)).distanceTo(cities.get(orders.get(orders.size() - 1)));

			route.clear();
			for (int i = 0; i < orders.size(); i++) {
				route.add(cities.get(orders.get(i)));
			}
			int distance;

			distance = 0;
			for (int i = 0; i < 1000000; i++) {
				optimize();
				int dis = totalDistanceCalculator(route);
				if (distance != dis) {
					if (dis < totalDistance) {
						distance = dis;
						totalDistance = dis;
					}
				} else {
					break;
				}
			}

			for (int i = 0; i < orders.size(); i++) {
				Repository.addCity(route.get(i));
			}

			SimulatedAnnealing annealing = new SimulatedAnnealing();
			annealing.simulation();

			distance = 0;
			for (int i = 0; i < 1000000; i++) {
				optimize();
				int dis = totalDistanceCalculator(route);
				if (distance != dis) {
					if (dis < totalDistance || distance == 0) {
						distance = dis;
						totalDistance = dis;
					}
				} else {
					break;
				}
			}

			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;

			// System.out.println(elapsedTime);

			PrintWriter out = new PrintWriter("output.txt", "UTF-8");

			// System.out.println(annealing.getBest().getDistance());
			out.println(annealing.getBest().getDistance());

			for (int i = 0; i < Repository.getCities().size() - 1; i++) {
				out.println(Repository.getCities().get(i).getOrder());
				// System.out.println(Repository.getCities().get(i).getOrder());
			}
			out.print(Repository.getCities().get(Repository.getCities().size() - 1).getOrder());
			out.close();
		}

	}

	// This is brute force TSP solver. this function's time complexity is n! and
	// just works for small inputs recursively this is not usable for large inputs
	private static void bruteForceTSP(ArrayList<City> notVisitedCities) {
		@SuppressWarnings("unchecked")
		ArrayList<City> notVisitedCitiesTemp = (ArrayList<City>) notVisitedCities.clone();
		int n = notVisitedCitiesTemp.size();
		for (int i = 0; i < n; i++) {
			City tempCity = notVisitedCitiesTemp.get(i);

			if (tempCity != null) {
				route.add(tempCity);
				notVisitedCitiesTemp.remove(i);
			}

			if (notVisitedCitiesTemp.size() == 0) {
				int tot = totalDistanceCalculator(route);
				if (tot < totalDistance || totalDistance == 0) {
					totalDistance = tot;
					orders.clear();
					for (int j = 0; j < route.size(); j++) {
						orders.add(route.get(j).getOrder());
					}
				}
			} else {
				bruteForceTSP(notVisitedCitiesTemp);
			}

			notVisitedCitiesTemp.add(i, tempCity);
			route.remove(route.size() - 1);
		}

	}

	public static void optimize() {
		City t, t2, t3, t4;
		double distance, distance2;
		double maxdist = 0;
		int ind1 = -1;
		int ind2 = -1;
		for (int i = 0; i < route.size() - 1; i++) {
			ind1 = -1;
			ind2 = -1;
			maxdist = 0;
			t = (City) route.get(i);
			t2 = (City) route.get(i + 1);
			for (int j = i + 1; j < route.size(); j++) {
				t3 = (City) route.get(j);
				t4 = (City) route.get(0);
				if (j < route.size() - 1) {
					t4 = (City) route.get(j + 1);
				}
				distance = t.distanceTo(t2);
				distance += t3.distanceTo(t4);
				distance2 = t.distanceTo(t3);
				distance2 += t2.distanceTo(t4);

				if ((distance - distance2) > maxdist) {
					maxdist = distance - distance2;
					ind1 = i;
					ind2 = j;
				}
			}
			if (ind1 != -1) {
				int j = ind2;
				for (int k = ind1 + 1; j >= (k + 1); k++) {
					t = (City) route.get(k);
					t2 = (City) route.get(j);
					if ((j % route.size()) > (k % route.size())) {
						route.remove(j % route.size());
						route.remove(k % (route.size() + 1));
						route.add(k % (route.size() + 2), t2);
						route.add(j % (route.size() + 1), t);
					} else {
						route.remove(k % route.size());
						route.remove(j % route.size() + 1);
						route.add(j % (route.size() + 2), t);
						route.add(k % (route.size() + 1), t2);
					}
					j--;
				}
			}
		}
	}

	// this function finds the closest city to given current point from cities array
	// list
	private static City findNearestPoint(ArrayList<City> cities, City currentPoint) {
		int distance = Integer.MAX_VALUE;
		City mid = null;
		for (int i = 0; i < cities.size(); i++) {
			if (currentPoint.distanceTo(cities.get(i)) < distance) {
				distance = currentPoint.distanceTo(cities.get(i));
				mid = cities.get(i);
			}
		}

		totalDistance += distance;
		return mid;
	}

	// this function returns the string is numeric or not
	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// This function returns the total distance of the tour
	public static int totalDistanceCalculator(ArrayList<City> cities) {
		int distance = 0;

		for (int i = 1; i < cities.size(); i++) {
			distance += cities.get(i - 1).distanceTo(cities.get(i));
		}

		distance += cities.get(cities.size() - 1).distanceTo(cities.get(0));

		return distance;
	}

	// This function swap 4 city on route to try find a short distance and create
	// new chances
	public static void swap4Route() {
		int size = route.size();

		for (int i = 0; i < 5; i++) {
			int number1 = (int) (Math.random() * (size / 2) - 1);
			int number2 = number1 + 1;
			int number3 = (int) (Math.random() * size / 2 + size / 2 - 1);
			int number4 = number3 + 1;

			City temp;

			temp = route.get(number1);
			route.set(number1, route.get(number3));
			route.set(number3, temp);

			temp = route.get(number2);
			route.set(number2, route.get(number4));
			route.set(number4, temp);

		}
	}
}

