package threejsFileGen;

import java.util.ArrayList;

public class Link {
	private int communityId;
	private ArrayList<Integer> linkedClass;

	// Constructor
	public Link(int communityId, int linkedClassIn){
		this.communityId = communityId;
		linkedClass = new ArrayList<Integer>();
		linkedClass.add(linkedClassIn);
	}

	// modClass getter/setter
	public int getModClass(){
		return communityId;
	}
	public void setModClass(int modClassIn){
		communityId = modClassIn;
	}

	// linkedClass getter/setter/add
	public ArrayList<Integer> getLinkedClass(){
		return linkedClass;
	}
	public void setLinkedClass(ArrayList<Integer> linkedClassIn){
		linkedClass = linkedClassIn;
	}
	public void addLink(int link){
		linkedClass.add(link);
	}

	// new imp
	public int layerOne;
	public int layerTwo;
	public String commOne;
	public String commTwo;
	
	public Link(String commOne, String commTwo, int layerOne, int layerTwo) {
		// not at all ideal, but until I rewrite the rest of the app, I don't want to break this class' functionality
		this.commOne = commOne;
		this.commTwo = commTwo;
		this.layerOne = layerOne;
		this.layerTwo = layerTwo;
	}
}
