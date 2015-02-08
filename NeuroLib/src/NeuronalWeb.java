import java.util.ArrayList;

public class NeuronalWeb {

	private ArrayList<Neuron> neurons;
	private ArrayList<Neuron> startNeurons;
	private ArrayList<Neuron> endNeurons;
	private ConnectionWeights weights;
	
	//functions to calculate the output
	private PropagationFunction propagationFunction;
	private ActivationFunction activationFunction;
	private OutputFunction outputFunction;
	
	//function for the order of activation
	private ActivationOrder activationOrder;
	
	//constructor
	public NeuronalWeb() {
		setDefaultFunctions();
		startNeurons = new ArrayList<>();
		endNeurons = new ArrayList<>();
	}

	//methods------------------------------------------------------------------
	public void generateNeuronalWeb(int neuronCount){
		neurons = new ArrayList<>(neuronCount);
		weights = new ConnectionWeights(neuronCount);
		for (int i = 0; i < neuronCount; i++) {
			Neuron newNeuron = new Neuron(i, 0, 0);
			newNeuron.setActivationFunction(activationFunction);
			newNeuron.setOutputFunction(outputFunction);
			newNeuron.setPropagationFunction(propagationFunction);
			neurons.add(newNeuron);
		}
	}
	
	//create new connection 
	public void setConnection(int sourceIndex, int targetIndex, double weight){
		if(weights==null || neurons==null) throw new RuntimeException
			("Noch nicht initialisiert, " +
			"vorher zuerst generateNeuronalWeb aufrufen");
		//throws exception if index out of correct range
		weights.setConnection(sourceIndex, targetIndex, weight); 
		neurons.get(targetIndex).addPredecessor(neurons.get(sourceIndex));
	}
	
	//set start neurons
	public void addToStartNeurons(int index){
		if(neurons == null) throw new RuntimeException("Netz nicht initialisiert" +
				", bitte zuerst generateNeuronalWeb aufrufen");
		/*if(!neurons.contains(index))
			throw new RuntimeException("Neuron mit index "+index+" existiert nicht.");*/
		startNeurons.add(neurons.get(index));
	}
	
	//set end neurons
	public void addToEndNeurons(int index){
		if(neurons == null) throw new RuntimeException("Netz nicht initialisiert" +
				", bitte zuerst generateNeuronalWeb aufrufen");
		/*if(!neurons.contains(index))
			throw new RuntimeException("Neuron mit index "+index+" existiert nicht.");*/
		endNeurons.add(neurons.get(index));
	}
	
	public double[] forwardPropagation(double[] input){
		if(input.length != startNeurons.size())
			throw new RuntimeException("Input Vektor hat die falsche Dimension");
		//set the activation
		for (int i = 0; i < startNeurons.size(); i++) {
			startNeurons.get(i).setActivationState(input[i]);
		}
		//propagate through the web
		ArrayList<Neuron> order = activationOrder.getActivationOrder(this);
		for (Neuron n : order) {
			n.propagate(weights);
		}
		//get the output
		double [] output = new double[endNeurons.size()];
		for (int i = 0; i < output.length; i++) {
			output[i] = endNeurons.get(i).getOutput();
		}
		return output;
	}

	//getters and setters -----------------------------------------------------
	public void setPropagationFunction(PropagationFunction propagationFunction) {
		this.propagationFunction = propagationFunction;
	}



	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}



	public void setOutputFunction(OutputFunction outputFunction) {
		this.outputFunction = outputFunction;
	}
	
	public ArrayList<Neuron> getNeurons(){
		return neurons;
	}
	
	//private functions--------------------------------------------------------
	private void setDefaultFunctions(){
		propagationFunction = new PropagationFunction() {
			//weighted sum
			@Override
			public double propagate(double[] outputs, double[] weights) {
				double toReturn = 0;
				for (int i = 0; i < weights.length; i++) {
					toReturn+=outputs[i]*weights[i];
				}
				return toReturn;
			}
		};
		activationFunction = new ActivationFunction() {
			//Tangens Hyperbolicus
			@Override
			public double calcActivation(double webInput, double oldActivation,
					double theta) {
				double input = webInput+oldActivation+theta; //TODO schwachsinn
				return Math.tanh(input);
			}
		};
		outputFunction = new OutputFunction() {
			//the identity
			@Override
			public double calcOutput(double activationState) {
				return activationState;
			}
		};
		activationOrder = new ActivationOrder() {
			@Override
			public ArrayList<Neuron> getActivationOrder(NeuronalWeb web) {
				return web.getNeurons();
			}
		};
	}
	
	public static void main(String[] args){
		NeuronalWeb web = new NeuronalWeb();
		web.generateNeuronalWeb(3);
		web.setConnection(0, 2, 1);
		web.setConnection(1, 2, 1);
		web.addToStartNeurons(0);
		web.addToStartNeurons(1);
		web.addToEndNeurons(2);
		
		double[] input = {0, -1};
		double[] output = web.forwardPropagation(input);
		System.out.println("Input:");
		for (int i = 0; i < input.length; i++) {
			System.out.print(input[i]+" ");
		}
		System.out.println();
		System.out.println("Output:");
		for (int i = 0; i < output.length; i++) {
			System.out.print(output[i]+" ");
		}
		System.out.println();
	}
}
