package NeuralNetwork;

public class HiddenNeuron extends Neuron {

protected double lastValueReturned = number;
	
	protected Neuron[] input;
	
	public HiddenNeuron(Neuron[] nodes, int indexOfHiddenNode) {
		this.input = nodes;
		this.index = indexOfHiddenNode;
	}
	
	//Sigmoid activation function
	public double sigmoid(double x) {
		return (1.0 / (1 + Math.pow(Math.E, -x)));
	}
		
	public double deriveSigmoid(double d){
		return d * (1.0 - d);
	}

	// Hyperbolic Tan Activation Function
	public double hyperTanFunction(double x) {
		return (Math.pow(Math.E, x) - Math.pow(Math.E, -x)) 
				/ (Math.pow(Math.E, x) + Math.pow(Math.E, -x));
		
	}
	
	public double deriveHyperTanFunction(double d){
		return (1.0 - Math.pow(hyperTanFunction(d), 2.0));
	}
	
	//Output method for the HiddenNode class which will sum up all the input nodes for
	//a specific hidden node
	public double output(double[][] weights){
		
		/*Here we will be implementing the following function
		 *  Sigma(x[i] * weights[i]) = net
		 *  Pass net into the hyberbolic tangent function to get an output between -1 to 1
		 *  We will pass net into the activation function in the train method of Neural Network
		 */
//		double[] tempArray = new double[input.length];
//		for (int j = 0; j < input.length; j++){
//			tempArray[j] = input[j].output(weights) * weights[input[j].getIndex()][index];
//			//System.out.println(tempArray[j]); //Test Code
//		}
		//Setup a double sum variable to represent net
		lastValueReturned = 0;
		//Setup for loop to loop over input nodes array for a hidden node
		for (int i = 0; i < input.length; i++){
			lastValueReturned += input[i].output(weights) * weights[input[i].getIndex()][index];
		}
		
		lastValueReturned = hyperTanFunction(lastValueReturned);
		//Change output from hyperTanFunction to 1 or -1
		if (lastValueReturned > 0){
			lastValueReturned = 1;
		}
		else {
			lastValueReturned = -1;
		}
		//System.out.println(lastValueReturned); //Test Code
		return lastValueReturned;
	}
	
	
}
