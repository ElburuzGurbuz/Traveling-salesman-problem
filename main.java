package algooooo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class main {
	static ArrayList<Integer> orders = new ArrayList<Integer>();
	static ArrayList<City> cities = new ArrayList<City>();
	static ArrayList<City> route = new ArrayList<City>();
	static int totalDistance = 0;

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		int count = 0;
		int totalX = 0;
		int totalY = 0;

		try {
			Scanner scanner = new Scanner(new File("example-input-1.txt"));
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
		int averageX = totalX / count;
		int averageY = totalY / count;

		City midPoint = new City(averageX, averageY, -1);

		ArrayList<City> notVisitedCities = (ArrayList<City>) cities.clone();

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
		
		
		PrintWriter writer = new PrintWriter("file-3.txt", "UTF-8");///////////////////
		

		//System.out.println(totalDistance);
		writer.println(totalDistance);

		route.clear();
		for (int i = 0; i < orders.size(); i++) {
			route.add(cities.get(orders.get(i)));
			//Repository.addCity(route.get(route.size()-1));
			// System.out.println(route.get(i).getOrder());
		}
		
		int distance = 0;
		System.out.println("first " + totalDistance);
		for (int i = 0; i < 1000000; i++) {
			optimize();
			int dis = totalDistanceCalculator(route);
			
			if (distance != dis) {
				
				if (dis < totalDistance) {
					distance = dis;
					totalDistance = dis;
					System.out.print(i + " -> " + dis + " ");
					//writer.print(i + " -> " + dis + " ");
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
					LocalDateTime now = LocalDateTime.now();
					System.out.println(dtf.format(now));
					//writer.println(dtf.format(now));
					for (int j = 0; j < orders.size(); j++) {
						//writer.println(route.get(j).getOrder());
						//System.out.println(route.get(j).getOrder());
					}
					//System.out.println("------------");
					//writer.println("------------");
				}
			} else {
				break;
				//swap4Route();
				// Collections.shuffle(route);
			}

		}
		
		for (int i = 0; i < orders.size(); i++) {
			//route.add(cities.get(orders.get(i)));
			Repository.addCity(route.get(i));
			// System.out.println(route.get(i).getOrder());
		}

		
		SimulatedAnnealing annealing = new SimulatedAnnealing();
		annealing.simulation();
		System.out.println("Final approximated solution's distance is: " + annealing.getBest().getDistance());
		
		for (int i = 0; i < Repository.getCities().size(); i++) {
			System.out.println(Repository.getCities().get(i).getOrder());
		}
		
		writer.close();
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

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static int totalDistanceCalculator(ArrayList<City> cities) {
		int distance = 0;

		for (int i = 1; i < cities.size(); i++) {
			distance += cities.get(i - 1).distanceTo(cities.get(i));
		}

		distance += cities.get(cities.size() - 1).distanceTo(cities.get(0));

		return distance;
	}

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
