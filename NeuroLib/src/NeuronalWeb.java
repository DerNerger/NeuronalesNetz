import java.util.ArrayList;


public class NeuronalWeb {
	private ArrayList<Neuron> neurons;
	private ArrayList<InputNeuron> startNeurons;
	private ArrayList<Neuron> endNeurons;
	private double[][] weights;
	private double[] outputs;
	private PropagationFunction propFun;
	private ActivationFunction actFun;
	private OutputFunction outFun;
	private ActivationOrder order;
	
	public NeuronalWeb(int neuronCount){
		neurons = new ArrayList<>(neuronCount+1);
		startNeurons = new ArrayList<>(neuronCount+1);
		endNeurons = new ArrayList<>(neuronCount+1);
		weights = new double[neuronCount+1][neuronCount+1];
		outputs = new double[neuronCount+1];
		initDefaultFunctions();
		neurons.add(new BiasNeuron(0));
		for (int i = 1; i < neuronCount+1; i++) {
			Neuron n = new Neuron(propFun,actFun,outFun,i);
			neurons.add(n);
		}
	}
	
	public void addToStart(int neuron){
		InputNeuron sNeuron = new InputNeuron(propFun,actFun,outFun, neuron);
		neurons.set(neuron, sNeuron);
		startNeurons.add(sNeuron);
	}
	
	public void addToEnd(int neuron){
		endNeurons.add(neurons.get(neuron));
	}
	
	public double[] propagate(double[] values){
		for (int i = 0; i < values.length; i++) {
			startNeurons.get(i).setStartValue(values[i]);
		}
		
		ArrayList<Neuron> ord = order.getActivationOrder(this);
		for (int i = 0; i < ord.size(); i++) {
			double[] w = getWeightsFor(i);
			double o = ord.get(i).propagate(outputs, w, outputs[i]);
			outputs[i] = o;
		}
		
		double[] out = new double[endNeurons.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = outputs[endNeurons.get(i).getIndex()];
		}
		return out;
	}
	
	public void learn(ArrayList<TrainingPattern> pattern, int runs, double learnFac){
		for (int i = 0; i < runs; i++) {
			for (TrainingPattern p : pattern) {
				double [] tPattern = p.gettPattern();
				double[] result = propagate(tPattern);
				backpropagate(p.getDelta(result), learnFac);
				System.out.println(this);
			}
		}
	}
	
	private void backpropagate(double[] delta, double learnFac) {
		for (int i = 0; i < delta.length; i++) {
			int omega_index = endNeurons.get(i).getIndex();
			for (int j = 0; j < outputs.length; j++) {
				double o = outputs[j];
				double d = delta[i];
				double change = learnFac*o*d;
				weights[j][omega_index] += change;
			}
		}
	}

	private double[] getWeightsFor(int neuron){
		double[] w = new double[weights.length];
		for (int i = 0; i < w.length; i++) {
			w[i] = weights[i][neuron];
		}
		return w;
	}

	private void initDefaultFunctions() {
		propFun = new PropagationFunction(){
			@Override
			public double propagate(double[] outputs, double[] weights) {
				double sum = 0;
				for (int i = 0; i < weights.length; i++) {
					sum+=outputs[i]*weights[i];
				}
				return sum;
			}
		};
		actFun = new ActivationFunction(){
			@Override
			public double calcActivation(double webInput, double oldActivation) {
				if(webInput>0) return 1;
				else return 0;
			}
		};
		outFun = new OutputFunction(){
			@Override
			public double calcOutput(double activationState) {
				return activationState;
			}
		};
		order = new ActivationOrder(){
			@Override
			public ArrayList<Neuron> getActivationOrder(NeuronalWeb web) {
				return web.neurons;
			}
		};
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Gewichte:\n");
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights.length; j++) {
				sb.append(weights[i][j]+"  ");
			}
			sb.append("\n");
		}
		sb.append("Outputs:\n");
		for (int i = 0; i < outputs.length; i++) {
			sb.append("Neuron "+i+": "+outputs[i]+"\n");
		}
		return sb.toString();
	}
	
	public static void main(String[] args){
		NeuronalWeb web = new NeuronalWeb(3);
		System.out.println(web);
		web.addToStart(1);
		web.addToStart(2);
		web.addToEnd(3);
		ArrayList<TrainingPattern> pattern = TrainingPattern.getOrTraining();
		web.learn(pattern, 100, 0.1);
	}
}
