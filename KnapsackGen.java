/*
* Name: Justin Senia
* E-Number: E00851822
* Date: 11/11/2017
* Class: COSC 461
* Project: #3
*/

import java.io.*;
import java.util.*;

/***********************************************************************************/

//This program solves integer knapsack problem using genetic algorithm
public class KnapsackGen
{
	private int[][] table;			//weights and values of item
	private int numberItems;		//number of items
	private int maximumWeight;		//max weight
	private PrintWriter pW;
	
	private int populationSize;		//population size
	private int stringLength;		//string length
	private int numberIterations; 	//number of iterations
	private double crossoverRate;	//crossover rate
	private double mutationRate;	//mutation rate
	private Random random;			//random number generator
	
	private int[][] population;		//population of strings
	private double[] fitnessValues;	//fitness values of strings

	/***********************************************************************************/

	//constructor of knapsack class
	public KnapsackGen(int[][] table, int numberItems, int maximumWeight, PrintWriter pWriter)
	{
		//set knapsack data
		this.table = table;
		this.numberItems = numberItems;
		this.maximumWeight = maximumWeight;
		this.pW = pWriter;
	}

	/***********************************************************************************/

	//method sets the parameters of genetic algorithm
	public void setParameters(int populationSize, int stringLength,
	int numberIterations, double crossoverRate, double mutationRate, int seed)
	{
		//set genetic algorithm parameters
		this.populationSize = populationSize;
		this.stringLength = stringLength;
		this.numberIterations = numberIterations;
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
		this.random = new Random(seed);
		
		//create a population and fitness arrays
		this.population = new int[populationSize][stringLength];
		this.fitnessValues = new double[populationSize];
	}

	/***********************************************************************************/

	//method executes genetic algorithm
	public void solve()
	{
		//initialize population
		initialize();
		
		//create generations
		for (int i = 0; i < numberIterations; i++)
		{
			//crossover strings
			crossover();
			
			//mutate strings
			mutate();
			
			//reproduce strings
			reproduce();
		}
		
		//choose best string
		solution();
	}

	/***********************************************************************************/

	//method initializes population
	private void initialize()
	{
		//initialize strings with random 0/1
		for (int i = 0; i < populationSize; i++)
		{
			for (int j = 0; j < stringLength; j++)
			{
				population[i][j] = random.nextInt(2);
			}
		}
		
		//intial fitness values are zero
		for (int i = 0; i < populationSize; i++)
		{		
			fitnessValues[i] = 0;
		}
	}

	/***********************************************************************************/

	//Method performs crossover operation
	private void crossover()
	{
		//go thru each string
		for (int i = 0; i < populationSize; i++)
		{
			//if crossover is performed
			if (random.nextDouble() < crossoverRate)
			{
				//choose another string
				int j = random.nextInt(populationSize);
				
				//choose crossover point
				int cut = random.nextInt(stringLength);
				
				//crossover the bits of the two strings
				for (int k = cut; k < stringLength; k++)
				{
					int temp = population[i][k];
					population[i][k] = population[j][k];
					population[j][k] = temp;
				}
			}
		}
	}

	/***********************************************************************************/

	//method performs mutation operation
	private void mutate()
	{
		//go through each bit of the string
		for (int i = 0; i < populationSize; i++)
		{
			for (int j = 0; j < stringLength; j++)
			{
				//if mutation is performed
				if (random.nextDouble() < mutationRate)
				{
					//flip bit from 0 to 1 or 1 to 0
					if (population[i][j] == 0)
						population[i][j] = 1;
					else
						population[i][j] = 0;
				}
			}
		}
	}

	/***********************************************************************************/

	//method performs reproduction operation
	private void reproduce()
	{
		//find the fitness value of all strings
		computeFitnessValues();
		
		//create and array for next generation
		int [][] nextGeneration = new int[populationSize][stringLength];
		
		//repeat population number of times
		for (int i = 0; i < populationSize; i++)
		{
			//select a string from current generation based on fitness
			int j = select();
			
			//copy selected string to next generation
			for (int k = 0; k < stringLength; k++)
			{	
				nextGeneration[i][k] = population[j-1][k];
			}
		}
		
		//next generation becomes current generation
		for (int i = 0; i < populationSize; i++)
		{
			for (int j = 0; j < stringLength; j++)
			{
				population[i][j] = nextGeneration[i][j];
			}
		}
	}

	/***********************************************************************************/

	//method computes fitness values of all strings
	private void computeFitnessValues()
	{
		//compute fitness values of strings
		for (int i = 0; i < populationSize; i++)
		{
			fitnessValues[i] = fitness(population[i]);
		}
		
		//accumulate fitness values
		for (int i = 1; i < populationSize; i++)
		{
			fitnessValues[i] = fitnessValues[i] + fitnessValues[i-1];
		}
		
		//normalize accumulated fitness values
		for (int i = 0; i < populationSize; i++)
		{
			fitnessValues[i] = fitnessValues[i]/fitnessValues[populationSize-1];
		}
	}

	/***********************************************************************************/

	//method selects a string based on fitness values
	private int select()
	{
		//create a random number between 1 and 0
		double value = random.nextDouble();
		
		//determine on which normalized accumulated fitness value it falls
		int i;
		for (i = 0; i < populationSize; i++)
		{
			if (value <= fitnessValues[i])
			{
				break;
			}
		}
		
		//return the string where the random number fell
		return i;
	}

	/***********************************************************************************/

	//Method find the best solution
	private void solution()
	{
		//compute fitness values of strings
		for (int i = 0; i < populationSize; i++)
		{
			fitnessValues[i] = fitness(population[i]);
		}
		
		//find the string with best fitness value
		int best = 0;
		for (int i = 0; i < populationSize; i++)
		{
			if (fitnessValues[i] > fitnessValues[best])
			{
				best = i;
			}
		}
		
		//display the best string
		display(population[best]);
	}

	/***********************************************************************************/

	//method computes the fitness value of a string (application specific)
	private double fitness(int[] string)
	{
		//initialize weight and value
		int weight = 0;
		int value = 0;
		
		//go through string
		for (int i = 0; i < stringLength; i++)
		{
			//adds up values and weights on selected items
			if (string[i] == 1)
			{
				weight += table[i][0];
				value += table[i][1];
			}
		}
		
		//if total weight does not exceed maximum weight fitness is total value
		if (weight <= maximumWeight)
		{
			return value;
		}
		//if total weight exceeds maximum weight fitness is zero
		else
		{
			return 0;
		}
	}

	/***********************************************************************************/

	//method displays a string (application specific)
	private void display(int[] string)
	{
		//Print item number
		System.out.print("Items: ");
		pW.print("Items: ");
		
		//go thru string
		for (int i = 0; i < stringLength; i++)
		{
			//if item is selected print its weight and value
			if (string[i] ==1)
			{
				//incrementing number by 1 to compensate for index/item number difference
				System.out.print((i+1) + " ");
				pW.print((i+1) + " ");
			}
		}
		System.out.println("");
		pW.println("");
		
		
		//prints out total configuration's weight
		System.out.print("Weight: ");
		pW.print("Weight: ");
		
		int tempTotWeight = 0;	//created to hold accumulated weight value
		
		for (int i = 0; i < stringLength; i++)
		{
			//if item is selected print its weight and value
			if (string[i] ==1)
			{
				tempTotWeight = tempTotWeight + table[i][0]; 
			}
		}
		System.out.println(tempTotWeight);
		pW.println(tempTotWeight);
		
		
		//prints out total configuration's value
		System.out.print("Value: ");
		pW.print("Value: ");
		
		int tempTotValue = 0;	//created to hold accumulated price value
		for (int i = 0; i < stringLength; i++)
		{
			//if item is selected print its weight and value
			if (string[i] ==1)
			{
				tempTotValue = tempTotValue + table[i][1];
			}
		}
		
		System.out.println(tempTotValue);
		pW.println(tempTotValue);
		
		System.out.println("");
		pW.println("");
	}

	/***********************************************************************************/
}


