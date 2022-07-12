package coursework;

import java.util.ArrayList;

import model.Fitness;
import model.Individual;
import model.LunarParameters.DataSet;
import model.NeuralNetwork;

/**
 * Implements a basic Evolutionary Algorithm to train a Neural Network
 * 
 * You Can Use This Class to implement your EA or implement your own class that
 * extends {@link NeuralNetwork}
 * 
 */
public class ExampleEvolutionaryAlgorithm extends NeuralNetwork {

	/**
	 * The Main Evolutionary Loop
	 */
	@Override
	public void run() {
		// Initialise a population of Individuals with random weights
		population = initialise();

		// Record a copy of the best Individual in the population
		best = getBest();
		System.out.println("Best From Initialisation " + best);

		/**
		 * main EA processing loop
		 */

		while (evaluations < Parameters.maxEvaluations) {

			/**
			 * this is a skeleton EA - you need to add the methods. You can also change the
			 * EA if you want You must set the best Individual at the end of a run
			 * 
			 */

			// Select 2 Individuals from the current population. Currently returns random
			// Individual
			Individual parent1 = select();
			Individual parent2 = select();
			Individual parent3 = select();

			// Generate a child by crossover. Not Implemented
			ArrayList<Individual> children = reproduce(parent1, parent2,parent3);

			// mutate the offspring
			mutate(children);

			// Evaluate the children
			evaluateIndividuals(children);

			// Replace children in population
			replace(children);

			// check to see if the best has improved
			best = getBest();

			// Implemented in NN class.
			outputStats();

			// Increment number of completed generations
		}

		// save the trained network to disk
		saveNeuralNetwork();
	}

	/**
	 * Sets the fitness of the individuals passed as parameters (whole population)
	 * 
	 */
	private void evaluateIndividuals(ArrayList<Individual> individuals) {
		for (Individual individual : individuals) {
			individual.fitness = Fitness.evaluate(individual, this);
		}
	}

	/**
	 * Returns a copy of the best individual in the population
	 * 
	 */
	private Individual getBest() {
		best = null;
		;
		for (Individual individual : population) {
			if (best == null) {
				best = individual.copy();
			} else if (individual.fitness < best.fitness) {
				best = individual.copy();
			}
		}
		return best;
	}

	/**
	 * Generates a randomly initialised population
	 * 
	 */
	private ArrayList<Individual> initialise() {
		population = new ArrayList<>();
		for (int i = 0; i < Parameters.popSize; ++i) {
			// chromosome weights are initialised randomly in the constructor
			Individual individual = new Individual();
			population.add(individual);
		}
		evaluateIndividuals(population);
		return population;
	}

	/**
	 * Selection --
	 * 
	 * NEEDS REPLACED with proper selection this just returns a copy of a random
	 * member of the population
	 */
	private Individual select() {

		int fit;
		int i;
		int picked;
		int bestIndex;
		Individual bestFit;

		picked = Parameters.random.nextInt(Parameters.popSize);
		bestFit = population.get(picked);
		bestIndex = picked;

		for (i = 0; i < Parameters.tournamentSize - 1; i++) {
			picked = Parameters.random.nextInt(Parameters.popSize);
			if (population.get(picked).fitness < bestFit.fitness) {
				bestFit = population.get(picked);
				bestIndex = picked;
			}
		}

		return bestFit.copy();
	}

	// tournament selection picks x random chromosomes,
	// and returns the fittest one

//    int i(tournament index), picked(Pcik a randonm individual from population), bestIndex;
//    double bestFit;
//    
//    // pick one
//    picked = randomNum.Next(populationSize);
//    bestFit = fitness[picked];
//    bestIndex = picked;
//
//    // pick tnSize-1 more and then see which is the best
//    for (i = 0; i < tnSize - 1; i++)
//    {
//        picked = randomNum.Next(populationSize);
//        if (fitness[picked] > bestFit)
//        {
//            bestFit = fitness[picked];
//            bestIndex = picked;
//        }
//    }
//    return (picked);	// return index of best one

	/**
	 * Crossover / Reproduction
	 * 
	 * NEEDS REPLACED with proper method this code just returns exact copies of the
	 * parents.
	 */
	private ArrayList<Individual> reproduce(Individual parent1, Individual parent2, Individual parent3) {
		ArrayList<Individual> children = new ArrayList<>();
		Individual child = new Individual();
		Individual child2 = new Individual();
		Individual child3= new Individual();
		int p = Parameters.random.nextInt(child.chromosome.length);
		for (int i = 0; i < p; i++) {
			child.chromosome[i] = parent1.chromosome[i];
		}
		for (int i = p; i < child.chromosome.length; i++) {
			child.chromosome[i] = parent2.chromosome[i];
		}
		for (int i = 0; i < p; i++) {
			child2.chromosome[i] = parent1.chromosome[i];
		}
		for (int i = p; i < child2.chromosome.length; i++) {
			child2.chromosome[i] = parent2.chromosome[i];
		}
		for (int i = 0; i < p; i++) {
			child3.chromosome[i] = parent3.chromosome[i];
		}
		for (int i = p; i < child2.chromosome.length; i++) {
			child3.chromosome[i] = parent3.chromosome[i];
		}
		children.add(child3.copy());
//		children.add(parent2.copy());
		return children;
	}

	/**
	 * Mutation
	 * 
	 * 
	 */
	private void mutate(ArrayList<Individual> individuals) {
		for (Individual individual : individuals) {
			for (int i = 0; i < individual.chromosome.length; i++) {
				if (Parameters.random.nextDouble() < Parameters.mutateRate) {
					if (Parameters.random.nextBoolean()) {
						individual.chromosome[i] += (Parameters.mutateChange);
					} else {
						individual.chromosome[i] -= (Parameters.mutateChange);
					}
				}
			}
		}
	}

	/**
	 * 
	 * Replaces the worst member of the population (regardless of fitness)
	 * 
	 */
	private void replace(ArrayList<Individual> individuals) {
		int ran = Parameters.random.nextInt(Parameters.popSize);
		Individual bestFit;
		int picked = 0;
		
		bestFit = population.get(picked);
		for (Individual individual : individuals) {
			Individual potentialParent = population.get(ran);
			bestFit = population.get(picked);
			int idx = getWorstIndex();
			for(int i = 0; i < Parameters.tournamentSize - 1; i++);{
				if(ran<idx) {
					bestFit= potentialParent;
					int i = 0;
					individual.chromosome[i] = potentialParent.chromosome[i];
				}
				
			}
			population.set(idx, individual);
		}
	}

//	// tournament selection picks x random chromosomes,
//    // and returns the fittest one
//
//    int i, picked, bestIndex;
//    double bestFit;
//    
//    // pick one
//    picked = randomNum.Next(populationSize);
//    bestFit = fitness[picked];
//    bestIndex = picked;
//
//    // pick tnSize-1 more and then see which is the best
//    for (i = 0; i < tnSize - 1; i++)
//    {
//        picked = randomNum.Next(populationSize);
//        if (fitness[picked] > bestFit)
//        {
//            bestFit = fitness[picked];
//            bestIndex = picked;
//        }
//    }
//    return (picked);	// return index of best one
	
	/**
	 * Returns the index of the worst member of the population
	 * 
	 * @return
	 */
	private int getWorstIndex() {
		Individual worst = null;
		int idx = -1;
		for (int i = 0; i < population.size(); i++) {
			Individual individual = population.get(i);
			if (worst == null) {
				worst = individual;
				idx = i;
			} else if (individual.fitness > worst.fitness) {
				worst = individual;
				idx = i;
			}
		}
		return idx;
	}

	@Override
	public double activationFunction(double x) {
		if (x < -20.0) {
			return -1.0;
		} else if (x > 20.0) {
			return 1.0;
		}
		return Math.tanh(x);
	}
}
