/*
* Name: Justin Senia
* E-Number: E00851822
* Date: 11/11/2017
* Class: COSC 461
* Project: #3
*/

import java.io.*;
import java.util.*;

public class KnapsackGenTester
{

	//Main method for testing
	public static void main(String[] args) throws IOException
	{
		//creating buffered reader for getting user input
		java.io.BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));

		//message welcoming to the program/giving instructions
		System.out.println("*****************************************");
		System.out.println("*     Knapsack Genetic Algo program     *");
		System.out.println("*****************************************");
		System.out.println("*  Please enter in a filename to start  *");
		System.out.println("* or type quit to terminate the program *");
		System.out.println("*****************************************");

		//start a loop that continues querying for input as long as user
		//does not enter "quit" (without the quotes)
		while (true)
		{

			String userIn = "";			//used for file entry or quitting

			System.out.print("Please enter a filename for data input: ");
			userIn = keyIn.readLine(); //reading user input

			

			if (userIn.equalsIgnoreCase("quit")) //if user typed quit, stops program
				break;
			else
			{
				try
				{
					//creating parameters to be passed to genetic algorithm
					int popSize = 0;			//population size parameter to be passed
					int strLength = 0;			//string length parameter to be passed
					int numIterations = 0;		//number of iterations parameter to be passed
					double crssoverRate = 0;	//crossover rate parameter to be passed
					double mutRate = 0;			//mutation rate parameter to be passed
					int seedVal = 0;			//seed value parameter to be passed
					
					
					//establishing working directory for file I/O
					String currentDir = System.getProperty("user.dir");
					File fIn = new File(currentDir + '\\' + userIn);

					//using scanner with new input file based on user input
					Scanner scanIn = new Scanner(fIn);

					//creating printwriter for file output
					File fOut = new File("output_" + userIn);
					PrintWriter PWOut = new PrintWriter(fOut, "UTF-8");


					//scanning external file for number of items in list
					int numOfItems = scanIn.nextInt();
					
					
					//reading in parameters to be passed into genetic algorithm
					System.out.print("Please enter an int value for population size (for genetic parameter passing): ");
					popSize = Integer.parseInt(keyIn.readLine()); //reading user input

					//assigning scanned in number of items as string length, due to string length being reliant
					//the number of items available to choose from
					strLength = numOfItems;

					System.out.print("Please enter an int value to indicate number of iterations (for genetic parameter passing): ");
					numIterations = Integer.parseInt(keyIn.readLine()); //reading user input

					System.out.print("Please enter a double value between 0 and 1 for crossover rate (for genetic parameter passing): ");
					crssoverRate = Double.parseDouble(keyIn.readLine()); //reading user input

					System.out.print("Please enter a double value between 0 and 1 for mutation rate(for genetic parameter passing): ");
					mutRate = Double.parseDouble(keyIn.readLine()); //reading user input

					System.out.print("Please enter an int value for seedvalue (for genetic parameter passing): ");
					seedVal = Integer.parseInt(keyIn.readLine()); //reading user input

					//testing statement to see if data was parsed from file correctly
					//System.out.println("test: " + popSize + " " + strLength + " " + numIterations + " " + crssoverRate + " " + mutRate + " " + seedVal);

					

					//declaring multidimensional array based on number of items designated by external file
					int[][] knapItems = new int[numOfItems][2];

					//importing item information from external file
					for (int i = 0; i < numOfItems; i++)
					{
						//skips item number, will just use index+1 of array as item number since its all in order
						scanIn.nextInt(); //skips item number
						knapItems[i][0] = scanIn.nextInt();	//gets item weight
						knapItems[i][1] = scanIn.nextInt();	//gets item cost

						//System.out.println(knapItems[i][0] + " " + knapItems[i][1]);
					}

					//declaring variable to hold max weight
					int maxWeight = scanIn.nextInt();


					//printing related info to console
					System.out.println("***************************************************************");
					System.out.println("          Knapsack Parameters for File: " + userIn              );
					System.out.println("***************************************************************");
					System.out.println("*                         Parameters                          *");
					System.out.println("***************************************************************");
					System.out.println(" Population Size:      " + popSize); 
					System.out.println(" String Length:        " + strLength);
					System.out.println(" Number of Iterations: " + numIterations); 
					System.out.println(" Crossover Rate:       " + crssoverRate);
					System.out.println(" Mutation Rate:        " + mutRate);
					System.out.println(" Seed Value:           " + seedVal);
					System.out.println(" Number of Items:      " + numOfItems);
					System.out.println(" Maximum Weight:       " + maxWeight);
					System.out.println("***************************************************************");
					System.out.println("*                            Output                           *");
					System.out.println("***************************************************************");

					//printing related info to file
					PWOut.println("***************************************************************");
					PWOut.println("          Knapsack Parameters for File: " + userIn              );
					PWOut.println("***************************************************************");
					PWOut.println("*                         Parameters                          *");
					PWOut.println("***************************************************************");
					PWOut.println(" Population Size:      " + popSize); 
					PWOut.println(" String Length:        " + strLength);
					PWOut.println(" Number of Iterations: " + numIterations); 
					PWOut.println(" Crossover Rate:       " + crssoverRate);
					PWOut.println(" Mutation Rate:        " + mutRate);
					PWOut.println(" Seed Value:           " + seedVal);
					PWOut.println(" Number of Items:      " + numOfItems);
					PWOut.println(" Maximum Weight:       " + maxWeight);
					PWOut.println("***************************************************************");
					PWOut.println("*                            Output                           *");
					PWOut.println("***************************************************************");

					//create knapsack solver
					KnapsackGen k = new KnapsackGen(knapItems, numOfItems, maxWeight, PWOut);

					//set parameters of genetic algorithm
					k.setParameters(popSize, strLength, numIterations, crssoverRate, mutRate, seedVal);

					//find the optimal solution 
					k.solve();

					//closing printwriter and scanner objects to maintain file integrity
					scanIn.close();
					PWOut.close();
				}
				catch (IOException e) //catches if there were any fileIO exceptions
				{
					;
				}
			}
		}
	}
}
