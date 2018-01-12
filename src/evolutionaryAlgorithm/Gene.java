package evolutionaryAlgorithm;

import java.util.Random;

public class Gene
{
	private String bitString;
	private double value;
	private double goal;
	private double fitness;
	private int index;
	private String formula;

	private Random rand;

	public Gene (int size, double goal, int index)
	{
		this.goal = goal;
		this.index = index;
		rand = new Random();
		bitString = "";

		for (int i = 0; i < size; i++)
		{
			bitString += rand.nextInt(2);
		}

		updateValue();
		updateFitness();
	}

	public int getIndex ()
	{
		return index;
	}
	
	public void setIndex (int index)
	{
		this.index = index; 
	}

	public String toString ()
	{
		return bitString;
	}

	public String getFormula ()
	{
		return formula;
	}

	public void setBitString (String bitString)
	{
		this.bitString = bitString;

		// we have mutated, so update value
		updateValue();
		updateFitness();
	}

	public double getValue ()
	{
		return value;
	}

	public double getFitness ()
	{
		return fitness;
	}

	private void updateFitness ()
	{
		if (goal - value == 0)
		{
			fitness = 0;
			return;
		}
		// fitness is inversely proportional to the difference between the goal
		// and this gene's value
		fitness = Math.abs(1 / (goal - value));
	}

	private void updateValue ()
	{
		String valueString = formula = convertAndClean();

		// if not enough values to perform one operation, return first digit's
		// value
		if (valueString.length() == 0)
		{
			value = 0;
			return;
		}

		if (valueString.length() < 3)
		{
			value = Character.digit(valueString.charAt(0), 10);
			return;
		}

		// track current value, starting with the first digit
		double currentValue = Character.digit(valueString.charAt(0), 10);

		// operate on the value until the end of the String
		for (int i = 1; i < valueString.length() - 1; i += 2)
		{
			int nextDigit = Character.digit(valueString.charAt(i + 1), 10);
			switch (valueString.charAt(i))
			{
				case '+':
					currentValue = currentValue + nextDigit;
					break;
				case '-':
					currentValue = currentValue - nextDigit;
					break;
				case '*':
					currentValue = currentValue * nextDigit;
					break;
				case '/':
					currentValue = currentValue / nextDigit;
					break;
			}
		}
		// update value
		value = currentValue;
	}

	private String convertAndClean ()
	{
		String temp = bitString;
		int validEnd = temp.length() / 4;
		String bitString = temp.substring(0, validEnd * 4);

		String retString = "";
		boolean wantDigit = true;

		for (int i = 0; i < bitString.length() - 1; i += 4)
		{
			String character = bitString.substring(i, i + 4);
			if (wantDigit)
			{
				switch (character)
				{
					case "0000":
						break;
					case "0001":
						retString += "1";
						wantDigit = false;
						break;
					case "0010":
						retString += "2";
						wantDigit = false;
						break;
					case "0011":
						retString += "3";
						wantDigit = false;
						break;
					case "0100":
						retString += "4";
						wantDigit = false;
						break;
					case "0101":
						retString += "5";
						wantDigit = false;
						break;
					case "0110":
						retString += "6";
						wantDigit = false;
						break;
					case "0111":
						retString += "7";
						wantDigit = false;
						break;
					case "1000":
						retString += "8";
						wantDigit = false;
						break;
					case "1001":
						retString += "9";
						wantDigit = false;
						break;
					case "1010":
						break;
					case "1011":
						break;
					case "1100":
						break;
					case "1101":
						break;
					case "1110":
						break;
					case "1111":
						break;
				}
			}
			else
			{
				switch (character)
				{
					case "0000":
						break;
					case "0001":
						break;
					case "0010":
						break;
					case "0011":
						break;
					case "0100":
						break;
					case "0101":
						break;
					case "0110":
						break;
					case "0111":
						break;
					case "1000":
						break;
					case "1001":
						break;
					case "1010":
						retString += "+";
						wantDigit = true;
						break;
					case "1011":
						retString += "-";
						wantDigit = true;
						break;
					case "1100":
						retString += "*";
						wantDigit = true;
						break;
					case "1101":
						retString += "/";
						wantDigit = true;
						break;
					case "1110":
						break;
					case "1111":
						break;
				}
			}
		}
		return retString;
	}
}
