import java.util.ArrayList;


public class ConnectionWeights {
	private double[][] weights;
	
	public ConnectionWeights(int neuronCount){
		weights = new double[neuronCount][neuronCount];
	}
	
	/*
	 * Gibt die Gewichte aller Verbindungen der Neuronen aus from mit dem
	 * Neuron to zurueck.
	 * */
	public double[] getWeights(ArrayList<Neuron> from, Neuron to){
		double[] needetWeights = new double[from.size()];
		int toIndex = to.getIndex();
		for (int i = 0; i < needetWeights.length; i++) {
			needetWeights[i] = weights [from.get(i).getIndex()][toIndex];
		}
		return needetWeights;
	}
	
	public void addWeight(int source, int target, double value){
		if(source < 0 || source > weights.length)
			throw new RuntimeException("Falsche Dimension:"+source);
		if(target < 0 || target > weights.length)
			throw new RuntimeException("Falsche Dimension:"+target);
		weights[source][target] += value;
	}
	
	public void setConnection(int sourceIndex, int targetIndex, double weight){
		if(sourceIndex<0 || sourceIndex>=weights.length)
			throw new RuntimeException("Gewicht zu diesem Neuron existert nicht. Index:"+sourceIndex);
		if(targetIndex<0 || targetIndex>=weights.length)
			throw new RuntimeException("Gewicht zu diesem Neuron existert nicht. Index:"+targetIndex);
		weights[sourceIndex][targetIndex] = weight;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("   ");
		for (int i = 0; i < weights.length; i++) {
			sb.append("  "+i+"  ");
		}
		sb.append("\n");
		for (int i = 0; i < weights.length; i++) {
			sb.append(i+" : ");
			for (int j = 0; j < weights.length; j++) {
				sb.append(weights[i][j]+"  ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
