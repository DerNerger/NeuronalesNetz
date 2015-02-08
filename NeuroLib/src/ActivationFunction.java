
public interface ActivationFunction {
	//webInput= output of PropagationFunction
	//oldActivation = the activation before the recalculation
	//theta the threshold of the neuron
	double calcActivation(double webInput, double oldActivation, double theta);
}
