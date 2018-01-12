package evolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GenePool
{
	// A number, representing the 'goal' of the genes
	private double			goal;
	
	// The number of bits in a gene
	private int				geneSize;
	
	// Holds the agents
	private ArrayList<Gene>	poolOfGenes;

	// The probability of two selected genes performing crossover
	private double			crossOverRate;
	
	// The chance that any bit will randomly flip in a gene after crossover
	private double			mutationRate;

	Random					rand;

	// Track the number of crossovers and mutations
	public int				crossCount;
	public int				mutateCount;

	public static void main (String args[])
	{
		
		// GenePool(poolSize, goal, geneSize, crossOverRate, mutationRate)
		GenePool genePool = new GenePool(100, 146, 100, 0.7, 0.001);

		System.out.println("Running Evolutionary Algorithm to Find: " + genePool.goal);
		System.out.println("---------------------------------------------- \n");

		int correctGene = genePool.runSimulation();

		System.out.println(" \n" + "After " + genePool.getCrossCount() + " Crossovers and " 
							+ genePool.getMutateCount() + " Mutations " + "The Winner Is: \n");
		System.out.println(genePool.poolOfGenes.get(correctGene).toString() + "\n");
		System.out.println("Representing the Formula: \n");
		System.out.println(genePool.poolOfGenes.get(correctGene).getFormula() + "\n");
		System.out.println("With a Value Of: \n");
		System.out.println(genePool.poolOfGenes.get(correctGene).getValue());

	}

	public GenePool (int poolSize, double goal, int geneSize, double crossOverRate,
			double mutationRate)
	{
		this.goal = goal;
		this.geneSize = geneSize;
		this.crossOverRate = crossOverRate;
		this.mutationRate = mutationRate;
		this.crossCount = 0;
		this.mutateCount = 0;

		rand = new Random();
		poolOfGenes = new ArrayList<Gene>();

		for (int i = 0; i < poolSize; i++)
		{
			poolOfGenes.add(new Gene(geneSize, goal, i));
		}
	}

	public int getCrossCount ()
	{
		return crossCount;
	}

	public int getMutateCount ()
	{
		return mutateCount;
	}

	public int runSimulation ()
	{
		boolean answerFound = false;
		int index = -1;

		while (!answerFound)
		{

			Gene geneOne = select();
			Gene geneTwo = select();

			// Ensure the same gene has not been selected twice
			while (geneOne == geneTwo)
			{
				geneTwo = select();
			}

			// Perform crossover
			crossOver(geneOne, geneTwo);

			// Check for a solution
			if (geneOne.getFitness() == 0)
			{
				answerFound = true;
				index = geneOne.getIndex();
			}
			else if (geneTwo.getFitness() == 0)
			{
				answerFound = true;
				index = geneTwo.getIndex();
			}
		}
		return index;
	}

	public void crossOver (Gene geneOne, Gene geneTwo)
	{
		// Check if crossover should happen
		if (rand.nextDouble() < crossOverRate)
		{
			// print out when a crossover happens
			System.out.println("Gene " + geneOne.getIndex() + " with fitness " + geneOne.getFitness()
					+ " and Gene " + geneTwo.getIndex() + " with fitness " + geneTwo.getFitness()
					+ " are performing crossover!");
			
			crossCount++;
			char[] tempOne = geneOne.toString().toCharArray();
			char[] tempTwo = geneTwo.toString().toCharArray();

			// Identify a split point for the crossover
			int swapPoint = rand.nextInt(geneSize);
			int fromSide = rand.nextInt(2);

			for (int i = swapPoint; i < geneSize; i++)
			{
				char tempBit = tempOne[i];
				tempOne[i] = tempTwo[i];
				tempTwo[i] = tempBit;
			}
			
//			if (fromSide == 0)
//			{
//				// swap bits from the swap point to the end of the gene
//				for (int i = swapPoint; i < geneSize; i++)
//				{
//					char tempBit = tempOne[i];
//					tempOne[i] = tempTwo[i];
//					tempTwo[i] = tempBit;
//				}
//			}
//			else
//			{
//				// swap bits from the beginning of the gene to the swap point
//				for (int i = 0; i <= swapPoint; i++)
//				{
//					char tempBit = tempOne[i];
//					tempOne[i] = tempTwo[i];
//					tempTwo[i] = tempBit;
//				}
//			}

			geneOne.setBitString(new String(tempOne));
			geneTwo.setBitString(new String(tempTwo));
		}

		mutate(geneOne);
		mutate(geneTwo);
	}

	private void mutate (Gene gene)
	{
		char[] temp = gene.toString().toCharArray();

		for (int i = 0; i < temp.length; i++)
		{
			double prob = rand.nextDouble();
			if (prob < mutationRate)
			{
				mutateCount++;
				if (temp[i] == '0')
					temp[i] = '1';
				else
					temp[i] = '0';

				// print out when there is a mutation
				System.out.println("Mutation in Gene " + gene.getIndex() + "!");
			}
		}

		gene.setBitString(new String(temp));
	}

	public Gene select ()
	{
		double[] totalFitnesses = new double[poolOfGenes.size()];
		totalFitnesses[0] = poolOfGenes.get(0).getFitness();

		for (int i = 1; i < poolOfGenes.size(); i++)
		{
			totalFitnesses[i] = totalFitnesses[i - 1] + poolOfGenes.get(i).getFitness();
		}

		double randomFitness = rand.nextDouble() * totalFitnesses[poolOfGenes.size() - 1];
		int index = Arrays.binarySearch(totalFitnesses, randomFitness);
		if (index < 0)
		{
			index = Math.abs(index + 1);
		}

		return poolOfGenes.get(index);
	}
}
