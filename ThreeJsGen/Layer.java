//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from:
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
//package threejsFileGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Represents one piece of text, containing the semantic concepts (nodes),
// communities of concepts, and links between concepts (edges) therein
public class Layer {
	private int date;
	private HashMap<Integer, Community> communities;
	private ArrayList<Edge> edges;

	public Layer(int date) {
		communities = new HashMap<Integer, Community>();
		edges = new ArrayList<Edge>();
		this.date = date;
	}

	// community getter/setter/add
	public Community getCommunity(int communityId) {
		return communities.get(communityId);
	}

	public HashMap<Integer, Community> getCommunities() {
		return communities;
	}

	public void setCommunities(HashMap<Integer, Community> communities) {
		this.communities = communities;
	}

	public void addCommunity(Community community, int communityId) {
		communities.put(communityId, community);
	}

	// date getter/setter
	public int getDate() {
		return date;
	}

	public void setDate(int dateIn) {
		date = dateIn;
	}

	// EdgeList getter/setter/add
	public ArrayList<Edge> getEdgeList() {
		return edges;
	}

	public void setEdgeList(ArrayList<Edge> edgeListIn) {
		edges = edgeListIn;
	}

	public void addEdge(Edge edgeIn) {
		edges.add(edgeIn);
	}

	// Checks if a modularity class / community id already exists
	public boolean checkCommunityIdExists(int communityId) {
		return communities.containsKey(communityId);
	}

	// Print Layer - mainly a debug function
	public void printLayer() {
		System.out.println("Layer - Date: " + date);
		for (Map.Entry<Integer, Community> entry : communities.entrySet()) {
			Integer key = entry.getKey();
			Community value = entry.getValue();
			System.out.println("Community: " + key);
			System.out.println("CentX: " + value.getTrueCenter().getX() + " CentY: " + value.getTrueCenter().getY());
			for (Node n : value.getNodes()) {
				String label = n.getLabel();
				System.out.println("Label: " + label);
			}
		}
	}

	// clone method
	@Override
	public Layer clone() {
		try {
			Layer newLayer = new Layer(date);
			newLayer.setCommunities(new HashMap<Integer, Community>(communities));
			newLayer.setEdgeList(edges);
			return newLayer;
		} catch (Exception e) {
			System.err.println("Couldn't clone layer.");
			System.err.println(e);
			return null;
		}
	}
}
