
public class InputNeuron extends Neuron{
	
	double startValue;

	public InputNeuron(PropagationFunction propFun, ActivationFunction actFun,
			OutputFunction outFun, int index) {
		super(propFun, actFun, outFun, index);
	}

	public void setStartValue(double startValue){
		this.startValue = startValue;
	}
	
	public double propagate(double[] outputs, double[] weights, double oldAct){
		double webInput = startValue;
		double activation = actFun.calcActivation(webInput, oldAct);
		double output = outFun.calcOutput(activation);
		return output;
	}
}
