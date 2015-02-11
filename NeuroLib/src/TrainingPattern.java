import java.util.ArrayList;


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
	
	public static ArrayList<TrainingPattern> getOrTraining(){
		ArrayList<TrainingPattern> pattern = new ArrayList<>();
		{
			double[] inp = {0,0};
			double[] result = {0};
			pattern.add(new TrainingPattern(inp, result));
		}
		{
			double[] inp = {1,0};
			double[] result = {1};
			pattern.add(new TrainingPattern(inp, result));
		}
		{
			double[] inp = {0,1};
			double[] result = {1};
			pattern.add(new TrainingPattern(inp, result));
		}
		{
			double[] inp = {1,1};
			double[] result = {1};
			pattern.add(new TrainingPattern(inp, result));
		}
		return pattern;
	}
	
	public static ArrayList<TrainingPattern> getAndTraining(){
		ArrayList<TrainingPattern> pattern = new ArrayList<>();
		{
			double[] inp = {0,0};
			double[] result = {0};
			pattern.add(new TrainingPattern(inp, result));
		}
		{
			double[] inp = {1,0};
			double[] result = {0};
			pattern.add(new TrainingPattern(inp, result));
		}
		{
			double[] inp = {0,1};
			double[] result = {0};
			pattern.add(new TrainingPattern(inp, result));
		}
		{
			double[] inp = {1,1};
			double[] result = {1};
			pattern.add(new TrainingPattern(inp, result));
		}
		return pattern;
	}
	
	public static ArrayList<TrainingPattern> getEqTraining(){
		ArrayList<TrainingPattern> pattern = new ArrayList<>();
		{
			double[] inp = {0,0};
			double[] result = {1};
			pattern.add(new TrainingPattern(inp, result));
		}
		{
			double[] inp = {1,0};
			double[] result = {0};
			pattern.add(new TrainingPattern(inp, result));
		}
		{
			double[] inp = {0,1};
			double[] result = {0};
			pattern.add(new TrainingPattern(inp, result));
		}
		{
			double[] inp = {1,1};
			double[] result = {1};
			pattern.add(new TrainingPattern(inp, result));
		}
		return pattern;
	}
}
