import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

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
		neurons = new ArrayList<>(neuronCount+1);
		weights = new ConnectionWeights(neuronCount+1);
		for (int i = 0; i < neuronCount+1; i++) {
			Neuron newNeuron = new Neuron(i, 0);
			newNeuron.setActivationFunction(activationFunction);
			newNeuron.setOutputFunction(outputFunction);
			newNeuron.setPropagationFunction(propagationFunction);
			neurons.add(newNeuron);
		}
		//add the bias neuron connection
		Neuron bias = neurons.get(0);
		bias.setOutput(1);
		for (int i = 1; i < neurons.size(); i++) {
			weights.setConnection(0, i, 1);
			neurons.get(i).addPredecessor(bias);
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
	
	//solve the problem input
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
	
	//teaching the web
	public void backproagation(LinkedList<TrainingPattern> setOfTrainingPattern){
		for (int aa = 0; aa < 20; aa++) {
			double teachingRate = 0.08;
			for(TrainingPattern pattern : setOfTrainingPattern){
				double[] tPattern = pattern.gettPattern();
				double[] result = forwardPropagation(tPattern);
				double[] delta = pattern.getDelta(result);
				//teaching the web
				for (int i = 0; i < delta.length; i++) { //all output neuron
					Neuron outNeuron = endNeurons.get(i);
					ArrayList<Neuron> connectedNeurons = outNeuron.getConnectedNeurons();
					//double [] weightsToOutNeuron = weights.getWeights(connectedNeurons, outNeuron);
					for (int j = 0; j < connectedNeurons.size(); j++) { //for all connections to outNeuron
						double d = delta[i];
						double o = connectedNeurons.get(j).getOutput();
						double deltaWeight = teachingRate * d * o;
						weights.addWeight(connectedNeurons.get(j).getIndex(),outNeuron.getIndex(),deltaWeight);
					}
				}
			}
			System.out.println(this);
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Gewichte:\n");
		sb.append(weights.toString());
		sb.append("StartNeurons:\n");
		sb.append(startNeurons.toString());
		sb.append("\nEndNeurons:\n");
		sb.append(endNeurons.toString());
		sb.append("\nOutput:\n");
		for (int i = 0; i < neurons.size(); i++) {
			Neuron n = neurons.get(i);
			sb.append(n.getIndex()+":  "+n.getOutput()+"\n");
		}
		return sb.toString();
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
			/*
			//Tangens Hyperbolicus
			@Override
			public double calcActivation(double webInput, double oldActivation) {
				double input = webInput+oldActivation; //TODO schwachsinn
				return Math.tanh(input);
			}*/
			@Override
			public double calcActivation(double webInput, double oldActivation) {
				if(webInput>0) return 1;
				else return 0;
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
		web.setConnection(1, 3, 1);
		web.setConnection(2, 3, 1);
		web.addToStartNeurons(1);
		web.addToStartNeurons(2);
		web.addToEndNeurons(3);
		
		System.out.println(web);
		
		LinkedList<TrainingPattern> pattern = new LinkedList<>();
		{ //F or T = T
			double[] input = {0,1};
			double[] output = {1};
			pattern.add(new TrainingPattern(input,output));
		}
		{ //T or F = T
			double[] input = {1,0};
			double[] output = {1};
			pattern.add(new TrainingPattern(input,output));
		}
		{ //T or T = T
			double[] input = {1,1};
			double[] output = {1};
			pattern.add(new TrainingPattern(input,output));
		}
		{ //F or F = F
			double[] input = {0,0};
			double[] output = {0};
 			pattern.add(new TrainingPattern(input,output));
		}
		
		web.backproagation(pattern);
	}
}
