
public class TrainingPattern {
	private double[] tPattern;
	private double[] teachingInput;
	
	
	public TrainingPattern(double[] tPattern, double[] teachingInput) {
		this.tPattern = tPattern;
		this.teachingInput = teachingInput;
	}


	public double[] gettPattern() {
		return tPattern;
	}


	public double[] getTeachingInput() {
		return teachingInput;
	}
	
	public double[] getDelta(double[] webOutput){
		if(webOutput.length!=teachingInput.length)
			throw new RuntimeException("webOutput hat eine andere Dimension wie teachingInput");
		double[] delta = new double[webOutput.length];
		for (int i = 0; i < delta.length; i++) {
			delta[i] = teachingInput[i] - webOutput[i];
		}
		return delta;
	}
}
