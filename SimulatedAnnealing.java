
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SimulatedAnnealing {

	private SingleTour best;

	/*
	 * This function has a representative energy level. The energy level represents
	 * the distance value of the tour. In addition, this function basically makes a
	 * virtual cooling process. It cools this temperature every iteration, starting
	 * from a certain temperature to the cooling rate. This creates a loop in the
	 * function and determines how much the loop will repeat. A new while loop runs
	 * when the temperature is more than 1. In this cycle, the locations of 4 cities
	 * are changed randomly. If the newly obtained values are lower, these values
	 * are considered to be the best solution ever.
	 */
	public void simulation() throws FileNotFoundException, UnsupportedEncodingException {

		double temperature = 1000;
		double coolingRate = 0.001;

		SingleTour currentSolution = new SingleTour();
		currentSolution.generateIndividual();

		best = new SingleTour(currentSolution.getTour());

		while (temperature > 1) {

			SingleTour newSolution = new SingleTour(currentSolution.getTour());

			int randomIndex1 = (int) ((newSolution.getTourSize() - 1) / 2 * Math.random());
			int randomIndex2 = (int) (randomIndex1 + 1);

			int randomIndex3 = (int) (((newSolution.getTourSize() - 1) / 2 * Math.random())
					+ (newSolution.getTourSize() / 2));
			int randomIndex4 = (int) (randomIndex3 + 1);

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

			if (acceptanceProbability(currentEnergy, neighbourEnergy, temperature) > Math.random())
				currentSolution = new SingleTour(newSolution.getTour());

			if (currentSolution.getDistance() < best.getDistance()) {
				best = new SingleTour(currentSolution.getTour());

				for (int i = 0; i < getBest().getTour().size(); i++) {
					System.out.println(Repository.getCities().get(i).getOrder());

				}
			}

			temperature *= 1 - coolingRate;
		}

	}

	public SingleTour getBest() {
		return best;
	}

	// This function checks and compares the energies if neighbourEnergy is less
	// than currentEnergy returns 1
	private double acceptanceProbability(double currentEnergy, double neighbourEnergy, double temperature) {

		if (neighbourEnergy < currentEnergy)
			return 1;

		return Math.exp((currentEnergy - neighbourEnergy) / temperature);

	}

	// This function checks all all route and calculates total distance up to end
	// with calling distanceTo funtion.
	public static int totalDistanceCalculator(ArrayList<City> cities) {
		int distance = 0;

		for (int i = 1; i < cities.size(); i++) {
			distance += cities.get(i - 1).distanceTo(cities.get(i));
		}

		distance += cities.get(cities.size() - 1).distanceTo(cities.get(0));

		return distance;
	}
}