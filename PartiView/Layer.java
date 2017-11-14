//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
package threejsFileGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Layer {
	private HashMap<Integer, Community> communities;
	private int date;
	private ArrayList<Edge> edgeList;

	public Layer(int dateIn){
		communities = new HashMap<Integer, Community>();
		edgeList = new ArrayList<Edge>();
		date = dateIn;
	}

	// community getter/setter/add
	public Community getCommunity(int key){
		return communities.get(key);
	}
	public HashMap<Integer, Community> getCommunities(){
		return communities;
	}
	public void setCommunities(HashMap<Integer, Community> communityIn){
		communities = communityIn;
	}
	public void addCommunity(Community communityIn, int modularityClassIn){
		communities.put(modularityClassIn, communityIn);
	}

	// date getter/setter
	public int getDate(){
		return date;
	}
	public void setDate(int dateIn){
		date = dateIn;
	}
	
	// EdgeList getter/setter/add
	public ArrayList<Edge> getEdgeList(){
		return edgeList;
	}
	public void setEdgeList(ArrayList<Edge> edgeListIn){
		edgeList = edgeListIn;
	}
	public void addEdge(Edge edgeIn){
		edgeList.add(edgeIn);
	}

	// Checks if a modularity class already exists
	public boolean checkModClassExist(int modularityClassIn){
		return communities.containsKey(modularityClassIn);
	}
	
	// Print Layer
	public void printLayer(){
		//System.out.println("Date: "+date);
		for(Map.Entry<Integer, Community> entry: communities.entrySet()){
			Integer key = entry.getKey();
			Community value = entry.getValue();
			//System.out.println("Community: "+key);
			//System.out.println("CentX: "+value.getCentroid().getX()+" CentY: "+value.getCentroid().getY());
			for(Map.Entry<String, Node> nodeHMEntry: value.getNodes().entrySet()){
				String label = nodeHMEntry.getKey();
				//System.out.println("Label: "+label);
			}
		}
	}

	// clone method
	public Layer clone(){
		try{
			Layer newLayer = new Layer(date);
			newLayer.setCommunities(new HashMap<Integer, Community>(communities));
			newLayer.setEdgeList(edgeList);
			return newLayer;
		}
		catch(Exception e){
			System.out.println(e);
			return null;
		}
	}
}
