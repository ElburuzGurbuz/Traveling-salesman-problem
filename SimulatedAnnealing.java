package algooooo;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SimulatedAnnealing {

	private SingleTour best;
	
	public void simulation() throws FileNotFoundException, UnsupportedEncodingException {

		double temperature = 1000000000;
		double coolingRate = 0.000000000000000001;
		
		SingleTour currentSolution = new SingleTour();
		currentSolution.generateIndividual();
		
		System.out.println("Initial solution distance: BENZETÝLMÝÞ TAVLAMA : -> !!! " + currentSolution.getDistance());
		
		best = new SingleTour(currentSolution.getTour());
		
		while ( temperature > 1 ) {
			
			SingleTour newSolution = new SingleTour(currentSolution.getTour());
			
			int randomIndex1 = (int) ((newSolution.getTourSize()-1)/2 * Math.random());
			int randomIndex2 = (int) (randomIndex1+1);
			
			int randomIndex3 = (int) (((newSolution.getTourSize()-1)/2 * Math.random()) + (newSolution.getTourSize()/2));
			int randomIndex4 = (int) (randomIndex3+1);
			
			City city1 = newSolution.getCity(randomIndex1);
			City city2 = newSolution.getCity(randomIndex2);
			City city3 = newSolution.getCity(randomIndex3);
			City city4 = newSolution.getCity(randomIndex4);
			
			newSolution.setCity(randomIndex1, city3);
			newSolution.setCity(randomIndex2, city4);
			newSolution.setCity(randomIndex3, city1);
			newSolution.setCity(randomIndex4, city2);
			
			double currentEnergy = currentSolution.getDistance();
			double neighbourEnergy = newSolution.getDistance();
			
			if( acceptanceProbability(currentEnergy, neighbourEnergy, temperature) > Math.random() )
				currentSolution = new SingleTour(newSolution.getTour());
			
			if( currentSolution.getDistance() < best.getDistance()) 
			{
				best = new SingleTour(currentSolution.getTour());
				System.out.println(best.getDistance());
			}
				
			
			temperature *= 1-coolingRate;
		}
		
		System.out.println(best.getDistance() +" optimizeden önce");
		// System.out.println(totalDistance);

					int distance = 0;

					for (int i = 0; i < 10000; i++) {
						int lastDist = best.getDistance();
						SingleTour s = optimize();
						int dis = s.getDistance();
						if (distance != dis && lastDist != dis ) {
							distance = dis;
							System.out.print(i + " -> " + dis + " ");
							DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
							LocalDateTime now = LocalDateTime.now();
							//System.out.println(dtf.format(now) + " 3");
							System.out.println(dtf.format(now));
							for (int j = 0; j < Repository.getNumberOfCities(); j++) {
								//System.out.println(newSolution.getTour().get(j).getOrder());
								//writer.println(newSolution.getTour().get(j).getOrder());
								//System.out.println(route.get(j).getOrder());
							}
							//System.out.println("------------");
							//writer.println("------------");
						}else {
							break;
						}
					}
		

	}

	private SingleTour optimize() {
		// TODO Auto-generated method stub
		SingleTour newTour = new SingleTour(best.getTour());
		newTour.setDistance(totalDistanceCalculator(newTour.getTour()));
		
		City t, t2, t3, t4;
		double distance, distance2;
		double maxdist = 0;
		int ind1 = -1;
		int ind2 = -1;
		for (int i = 0; i < newTour.getTourSize() - 1; i++) {
			ind1 = -1;
			ind2 = -1;
			maxdist = 0;
			t = (City) newTour.getCity(i);
			t2 = (City) newTour.getCity(i + 1);
			for (int j = i + 1; j < newTour.getTourSize(); j++) {
				t3 = (City) newTour.getCity(j);
				t4 = (City) newTour.getCity(0);
				if (j < newTour.getTourSize() - 1) {
					t4 = (City) newTour.getCity(j + 1);
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
					t = (City) newTour.getCity(k);
					t2 = (City) newTour.getCity(j);
					if ((j % newTour.getTourSize()) > (k % newTour.getTourSize())) {
						newTour.remove(j % newTour.getTourSize());
						newTour.remove(k % (newTour.getTourSize() + 1));
						newTour.addCityOn(k % (newTour.getTourSize() + 2), t2);
						newTour.addCityOn(j % (newTour.getTourSize() + 1), t);
					} else {
						newTour.remove(k % newTour.getTourSize());
						newTour.remove(j % newTour.getTourSize() + 1);
						newTour.addCityOn(j % (newTour.getTourSize() + 2), t);
						newTour.addCityOn(k % (newTour.getTourSize() + 1), t2);
					}
					j--;
				}
			}
		}
		best = new SingleTour(newTour.getTour());
		return best;
	}

	public SingleTour getBest() {
		return best;
	}

	private double acceptanceProbability(double currentEnergy, double neighbourEnergy, double temperature) {

		if (neighbourEnergy < currentEnergy)
			return 1;

		return Math.exp((currentEnergy - neighbourEnergy) / temperature);

	}
	
	public static int totalDistanceCalculator(ArrayList<City> cities) {
		int distance = 0;

		for (int i = 1; i < cities.size(); i++) {
			distance += cities.get(i - 1).distanceTo(cities.get(i));
		}

		distance += cities.get(cities.size() - 1).distanceTo(cities.get(0));

		return distance;
	}
}