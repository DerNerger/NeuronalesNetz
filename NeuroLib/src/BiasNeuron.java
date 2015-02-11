
public class BiasNeuron extends Neuron{

	public BiasNeuron(int index) {
		super(null, null, null, index);
	}

	public double propagate(double[] outputs, double[] weights, double oldAct){
		return 1;
	}
}
