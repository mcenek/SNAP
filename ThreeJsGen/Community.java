package threejsFileGen;

import java.util.HashMap;

public class Community{
	private HashMap<String, Node> nodesHM;
	private int modularityValue; // Also known as modularity class or community number
	private NodeColor color;
	private Centroid centroid;
	private Centroid adjustedCentroid;
	
	// Constructor
	public Community(){
		nodesHM = new HashMap<String, Node>();
	}
	public Community(int modularityValueIn){
		nodesHM = new HashMap<String, Node>();
		modularityValue = modularityValueIn;
	}

	// nodesHM getter/setter
	public HashMap<String, Node> getNodes(){
		return nodesHM;
	}
	public void setNodes(HashMap<String, Node> nodesHMIn){
		nodesHM = nodesHMIn;
	}

	// modularityValue getter/setter
	public int getModValue(){
		return modularityValue;
	}
	public void setModValue(int modValueIn){
		modularityValue = modValueIn;
	}

	// color getter/setter
	public NodeColor getColor(){
		return color;
	}
	public void setColor(NodeColor colorIn){
		color = colorIn;
	}
	
	// centroid getter/setter
	public Centroid getCentroid(){
		return centroid;
	}
	public void setCentroid(Centroid centroidIn){
		centroid = centroidIn;
	}
	
	// adjustedCentroid getter/setter
	public Centroid getAdjCentroid(){
		return adjustedCentroid;
	}
	public void setAdjCentroid(Centroid centroidIn){
		adjustedCentroid = centroidIn;
	}
	
	public boolean containsKey(String token){
		return nodesHM.containsKey(token);
	}
	
	// add Node to HashMap
	public void addNode(Node nodeIn){
		if(!nodesHM.containsKey(nodeIn.getLabel())){
			nodesHM.put(nodeIn.getLabel(), nodeIn);
		}
	}
	
	public String dominantConcept() {
		double max = -1;
		String dominant = "";
		
		for( Node node : nodesHM.values() ) {
			if( node.getSize() > max ) {
				max = node.getSize();
				dominant = node.getLabel();
			}
		}
		
		return dominant;
	}
}
