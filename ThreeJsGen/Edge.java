package threejsFileGen;

public class Edge{
	private String source;
	private String target;
	private double weight;
	private int start;
	private int end;
	
	// constructor
	Edge(){ }
	Edge(String targetIn, String sourceIn, int startIn, int endIn){
		target = targetIn;
		source = sourceIn;
		start = startIn;
		end = endIn;
	}
	
	// source getter/setter
	public String getSource(){
		return source;
	}
	public void setSource(String sourceIn){
		source = sourceIn;
	}
	
	// target getter/setter
	public String getTarget(){
		return target;
	}
	public void setTarget(String targetIn){
		target = targetIn;
	}
	
	// weight getter/setter
	public double getWeight(){
		return weight;
	}
	public void setWeight(double weightIn){
		weight = weightIn;
	}
	
	// start getter/setter
	public int getStart(){
		return start;
	}
	public void setStart(int startIn){
		start = startIn;
	}
	
	// end getter/setter
	public int getEnd(){
		return end;
	}
	public void setEnd(int endIn){
		end = endIn;
	}
}
