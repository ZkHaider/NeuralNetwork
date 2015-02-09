package NeuralNetwork;


public class InputNeuron extends Neuron {

	public InputNeuron(double number, int inputIndex) {
		this.number = number;
		this.index = inputIndex;
	}

	//Create method to initialize input nodes
	public void set(double tempValue){
		this.number = tempValue;
	}
	
	public double get(){
		return number;
	}
	
	//Override method from Node class
	//This method will grab the sum of all input node values.
	public double output(double[][] weights){
		return number;
		
	}
	
}
