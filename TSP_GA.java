package tsp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TSP_GA {

	public static void main(String[] args) {

		int count = 0;
		int totalX=0;
		int totalY=0;

		try {

			ArrayList<City> cities = new ArrayList<City>();
			Scanner scanner = new Scanner(new File("example-input-1.txt"));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] list = line.split("\\s+");
				City city = new City(Integer.parseInt(list[1]), Integer.parseInt(list[2]));
				TourManager.addCity(city);
				cities.add(city);
				totalX+=Integer.parseInt(list[1]);
				totalY+=Integer.parseInt(list[2]);
				count++;
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int averageX = totalX/count;
		int averageY = totalY/count;
		Population pop = new Population(count*250, true);

		System.out.println("Initial distance: " + pop.getFittest().getDistance());

		// Evolve population for 100 generations
		pop = GA.evolvePopulation(pop);
		int min = 99999999;
		for (int i = 0; i < 1000000; i++) {
			pop = GA.evolvePopulation(pop);
			if(pop.getFittest().getDistance() < min) {
				min = pop.getFittest().getDistance();
				System.out.println(min);
			}
			
		}

		// Print final results
		/*
		System.out.println("Finished");
		System.out.println("Final distance: " + pop.getFittest().getDistance());
		System.out.println("Solution:");
		System.out.println(pop.getFittest());*/
	}
}