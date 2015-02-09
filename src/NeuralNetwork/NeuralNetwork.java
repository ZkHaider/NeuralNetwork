package NeuralNetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Random;
import java.util.Scanner;

public class NeuralNetwork {

	/*
	 * Setup an 2-d array of doubles to store values from a .data file This will
	 * be used in the NeuralNetwork constructor
	 */
	protected double[][] data;

	/*
	 * Declare a double[][] array, and randomize the weights of the double array
	 * in constructor This will be used in the train(), randomizeWeights(),
	 * getWeights() methods
	 */
	public double[][] weights;

	/*
	 * We set a double field named learningRate equal to 0.01. Used in the train() method
	 */
	final protected double learningRate = 0.00001;

	/*
	 * Declare a protected error value represented by a double value
	 */
	private double errorOfNetwork;
	
	
	/*
	 * Setup conditional for the train method
	 */
	final private double conditional = 0.5;

	/*
	 * Setup variables for the errorOn() method
	 */
	protected double actual;
	protected double theoretical;
	protected double theoreticalMinusActual;
	protected double sumOfSquaredMinus;
	protected double avgOfSumOfSquaredMinus;
	protected double lengthOfData;
	
	/*
	 * Setup a counter for the epochs that the neural network has to go through
	 */
	public int epochs;

	/*
	 * These are values for the NeuralNetwork Constructor
	 */
	private final String comma = ",";
	private final String qMarks = "?";
	private final String positiveHeartCondition = "1";
	private final String negativeHeartCondition = "0";
	private Neuron[] input; // Input Nodes
	private Neuron[] hiddenNodeLayer; // Array of HiddenNode to hold input in each
									// HiddenNode
	private Neuron outputNode; // Take the input of hiddenNodeLayer

	// We initialize a constructor which only takes a parameter int n.
	public NeuralNetwork(File f) {

		int noOfNodes = 0;

		try {
			@SuppressWarnings("resource")
			Scanner inFile = new Scanner(f);

			int noOfRowsInData = 0;

			LineNumberReader lnr = new LineNumberReader(new FileReader(f));
			try {
				lnr.skip(Long.MAX_VALUE);
				noOfRowsInData = lnr.getLineNumber();
				lnr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String line = inFile.nextLine();

			String[] numbers = line.split(comma);

			data = new double[noOfRowsInData][numbers.length];

			noOfNodes = numbers.length - 1;

			int i = 0;
			// While there is another line in inFile.
			inFile = new Scanner(f);

			while (inFile.hasNextLine()) {
				// Store that line into String line
				line = inFile.nextLine();
				// System.out.println(line);//Test code to see what the file
				// looks like
				// Parition values separated by a comma
				numbers = line.split(comma);
				// System.out.println(Arrays.toString(columns)); //Test code to
				// see length of each column
				/*
				 * code works and prints out row
				 */
				/*
				 * initialize noOfNodes to length of numbers - 1
				 */

				// This will count the number of rows in the .data file

				// System.out.println(data[0].length); //Test code works
				// properly, and prints numbers.length

				// Copy over data form the .data file
				for (int j = 0; j < data[i].length; j++) {
					// For each row...
					if (!numbers[j].equals(qMarks)) {
						// If the values in each row do not equal "?"
						// Set rows[i] to the values in column[i]
						if (j != 13) {
							data[i][j] = Double.parseDouble(numbers[j]);
						} else {
							if (j == 13
									&& numbers[j]
											.equals(positiveHeartCondition)) {
								data[i][j] = 1;
							} else if (j == 13
									&& numbers[j]
											.equals(negativeHeartCondition)) {
								data[i][j] = -1;
							}
						}
						// System.out.println(data[i][j]); //Test code to see
						// what's printed here
					} else {
						data[i][j] = 0;
						// System.out.println(data[i][j]); //Test code to see
						// what's printed here
					}
				}
				i++;
			}

			// System.out.println(data.length); //See what the length of the
			// data double array is. works
			// Test code to print out the 2-d double array and see what is being
			// stored
			// for (int k = 0; k < data.length; k++) {
			// System.out.println("For Row[" + k + "] of file:"); // Works
			// for (int j = 0; j < data[k].length; j++) {
			// System.out.println("   data[" + k + "][" + j + "] = "
			// + data[k][j]); // Works
			// }
			// }

			/*
			 * Setup the Neural Network architecture.
			 */
			input = new InputNeuron[noOfNodes];
			// System.out.println(noOfNodes);
			// Creating InputNodes based on noOfNodes
			for (int m = 0; m < noOfNodes; m++) {
				input[m] = new InputNeuron(0.0, m);
				// System.out.println(m);
			}
			// Create 8 HiddenNode objects
			int noOfHiddenNodes = (int) Math.floor(2.0 * (noOfNodes / 3.0));
			hiddenNodeLayer = new HiddenNeuron[noOfHiddenNodes];
			// Initialize each created HiddenNode object to have the parameters
			// "input"
			for (int p = 0; p < hiddenNodeLayer.length; p++) {
				hiddenNodeLayer[p] = new HiddenNeuron(input, noOfNodes + p);
			}

			/*
			 * Create an output node which is actually a HiddenNode, Feed into
			 * it the hidden node layer.
			 */
			outputNode = new HiddenNeuron(hiddenNodeLayer, noOfNodes
					+ noOfHiddenNodes);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// This method will take the hidden nodes, do a quick verification check on
	// it and
	// sum up the weights using the simple threshold function described in class
	// to return
	// either a 1 or -1. 1 meaning fire, and -1 not firing.
	// public int run() {
	//
	// }

	// Take 10% of data, to prevent overfitting.
	public void test() {

	}

	// This will be a train method which will train the nodes to update.
	public void train() {

		// output values of hidden nodes and output node
		double outputOfOutputNode = 0; // Input value * weight
		
		//Initialize epochs to 0 before beginning any loops
		epochs = 0;
		
//		//While the error is greater than 25%
		while (errorOn() > conditional) {
			// Create a loop over the entire data set.
			
			for (int i = 0; i < data.length; i++) {
				// Loop over the input Node array
				for (int j = 0; j < input.length; j++) {
					((InputNeuron) input[j]).set(data[i][j]);// Set the jth
															// InputNode
															// in the input node
															// array to
															// data[i][j]
				}

				outputOfOutputNode = outputNode.output(weights);
//				System.out.println("The output for row " + i + " is "
//						+ outputOfOutputNode); // Test code works correctly

				/*
				 * If the resuling output of the outputNode does equal the same
				 * value as the last number of the indice of the row in the.data
				 * file then update the weights using the following code
				 */
				if (outputOfOutputNode != data[i][13]) {

					// System.out.println("NEURAL NETWORK UPDATED FOR ROW " +
					// i);

					// Update weights from input nodes to hidden nodes
					for (int k = 0; k < input.length; k++) {
						for (int m = 13; m < 21; m++) {
							weights[k][m] = weights[k][m]
									+ ((learningRate
											* (data[i][13] - outputOfOutputNode) * input[k].number));
//							 System.out.println("    New weight from InputNode "
//							 + k + " to HiddenNode " + m + " is: ");
//							 System.out.println("       " + + weights[k][m]);
						}
					}

					// Update weights from HiddenNodes to outputnode
					for (int k = 0; k < hiddenNodeLayer.length; k++) {
						weights[k][21] = weights[k][21]
								- (learningRate
										* (data[i][13] - outputOfOutputNode) * hiddenNodeLayer[k].number);
//						 System.out.println("    New weight from HiddenNode "
//						 + k + " to OutputNode is: ");
//						 System.out.println("       " + + weights[k][21]);
					}
				}
				epochs++;
			}
			
		}
	}

	public double errorOn() {

		/*
		 * The root mean squared error function is used here For simplicity
		 * purposes I picked this, it can be changed to something else later to
		 * optimize the system The function looks like: squareRT((1/length of
		 * data)(sum of (theoretical - observed)^2)
		 */

		// Setup length of data inversed variable
		lengthOfData = data.length;
		
		sumOfSquaredMinus = 0;

		// Implement the root mean squared error function on the network.

		for (int i = 0; i < data.length; i++) {
			// Loop over the input Node array
			for (int j = 0; j < input.length; j++) {
				((InputNeuron) input[j]).set(data[i][j]);// Set the jth InputNode
														// in the input node
														// array to data[i][j
			}
			actual = outputNode.output(weights);
			//System.out.println(actual); //Test code to see if actual is
			// printing

			theoretical = data[i][13];
			// System.out.println(theoretical); //Test code to see if the
			// theoretical is printing. Works

			// Find difference of theoretical minus actual
			theoreticalMinusActual = (theoretical - actual);
			// System.out.println(theoreticalMinusActual); //Test code, works.

			sumOfSquaredMinus += Math.pow(theoreticalMinusActual, 2);
			// System.out.println(squaredOfMinus); //Test code, works
			
			//sumOfSquaredMinus += Math.pow(Math.atan(theoreticalMinusActual), 2); //For aTan error

			// System.out.println(lengthOfData); //Test code

			// System.out.println(1 / lengthOfData); //Test Code
			

		}
		
		
		avgOfSumOfSquaredMinus = sumOfSquaredMinus / lengthOfData;
		
		// System.out.println(squaredOfMinus); //Working properly and sums up
		// System.out.println(data.length); //Test code works
		//System.out.println(avgOfSumOfSquaredMinus);

		errorOfNetwork = Math.sqrt(avgOfSumOfSquaredMinus); //This is for RMSE
		//errorOfNetwork = sumOfSquaredMinus/lengthOfData; //For the arcTan error
		

		System.out.println(errorOfNetwork);

		return errorOfNetwork;
	}

	// Method which will create randomized weights
	public double[][] randomizeWeights() {

		/*
		 * Initialize weights array to [#of input nodes + #of hidden nodes][#of
		 * input nodes + #of hidden nodes]
		 */
		weights = new double[(input.length) + hiddenNodeLayer.length + 1][(input.length)
				+ hiddenNodeLayer.length + 1];
		Random random = new Random(0);

		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				weights[i][j] = (random.nextDouble() * 2) - 1; // Create random
																// weights
																// between
																// -1 and 1
				// System.out.println(weights[i][j]); //Test code to see if
				// there are correct
				// number of weights
			}
		}

		return weights;

	}

	// Method to grab a weight
	public double getWeights(int i, int j) {
		return weights[i][j];
	}

	public static void main(String[] args) {

		// Call constructor method to create a network from a .data file
		NeuralNetwork network1 = new NeuralNetwork(new File(
				"C:\\Heart Disease Machine Learning\\processed.hungarian.data"));

		// Randomize weights between -1 and 1.
		network1.randomizeWeights();

		// Implement the training method which will update the weights of the
		// neural network.
		//network1.train();//Not working properly

		// Implement the error method to see if it is working correctly.
//		network1.errorOn(); //Not working properly
//		network1.errorOn();

	}

	
}
