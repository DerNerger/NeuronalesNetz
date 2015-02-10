import java.util.ArrayList;


public class Neuron {
	//functions to calculate the output
	private PropagationFunction propagationFunction;
	private ActivationFunction activationFunction;
	private OutputFunction outputFunction;
	
	//the index of the neuron
	private int index;
	
	//the current state of the neuron
	private double activationState;
	private double output;
	
	//All neurons in the list are connected with this neuron
	private ArrayList<Neuron> connectedNeurons;
	
	//constructor
	public Neuron(int index, double activationState) {
		this.index = index;
		this.activationState = activationState;
		connectedNeurons = new ArrayList<>();
	}

	public double propagate(ConnectionWeights weights){
		double[] needetWeights = weights.getWeights(connectedNeurons, this);
		double[] outputs = getOutput(connectedNeurons);
		double webInput = propagationFunction.propagate(outputs, needetWeights);
		double nextActivationState = activationFunction.calcActivation(webInput, activationState);
		activationState = nextActivationState;
		output = outputFunction.calcOutput(activationState);
		return output;
	}

	private double[] getOutput(ArrayList<Neuron> connectedNeurons){
		double [] output = new double[connectedNeurons.size()];
		for (int i = 0; i < output.length; i++) {
			output[i] = connectedNeurons.get(i).getOutput();
		}
		return output;
	}
	
	public void addPredecessor(Neuron n){
		connectedNeurons.add(n);
	}

	//getters and setters------------------------------------------------------
	public void setPropagationFunction(PropagationFunction propagationFunction) {
		this.propagationFunction = propagationFunction;
	}

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	public void setOutputFunction(OutputFunction outputFunction) {
		this.outputFunction = outputFunction;
	}

	public void setActivationState(double activationState) {
		this.activationState = activationState;
	}
	
	public int getIndex() {
		return index;
	}

	public double getOutput() {
		if(index==0) return -1;
		return output;
	}

	public ArrayList<Neuron> getConnectedNeurons() {
		return connectedNeurons;
	}
	
	public String toString(){
		return index+"";
	}

	public void setOutput(int output) {
		this.output = output;
	}
}
