public class Neuron {
	protected PropagationFunction propFun;
	protected ActivationFunction actFun;
	protected OutputFunction outFun;
	private int index;
	
	public Neuron(PropagationFunction propFun, ActivationFunction actFun,
			OutputFunction outFun, int index) {
		this.propFun = propFun;
		this.actFun = actFun;
		this.outFun = outFun;
		this.index = index;
	}

	public double propagate(double[] outputs, double[] weights, double oldAct){
		double webInput = propFun.propagate(outputs, weights);
		double activation = actFun.calcActivation(webInput, oldAct);
		double output = outFun.calcOutput(activation);
		return output;
	}
	
	public int getIndex() {
		return index;
	}

	public void setPropFun(PropagationFunction propFun) {
		this.propFun = propFun;
	}

	public void setActFun(ActivationFunction actFun) {
		this.actFun = actFun;
	}

	public void setOutFun(OutputFunction outFun) {
		this.outFun = outFun;
	}
}
