package tsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TSP_GA {

    public static void main(String[] args) {
    	
    	int count = 0;
    	
    	try {
    		
    		ArrayList<City> cities = new ArrayList<City>();
			Scanner scanner = new Scanner(new File("example-input-1.txt"));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] list = line.split("\\s+");
				City city = new City(Integer.parseInt(list[1]), Integer.parseInt(list[2]));
				TourManager.addCity(city);
				cities.add(city);
				count++;
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	Population pop = new Population(count * 10000, true);
    	
    	 System.out.println("Initial distance: " + pop.getFittest().getDistance());

         // Evolve population for 100 generations
         pop = GA.evolvePopulation(pop);
         for (int i = 0; i < 100; i++) {
             pop = GA.evolvePopulation(pop);
         }

         // Print final results
         System.out.println("Finished");
         System.out.println("Final distance: " + pop.getFittest().getDistance());
         System.out.println("Solution:");
         System.out.println(pop.getFittest());
    }
}