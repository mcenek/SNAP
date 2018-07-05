//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
package threejsFileGen;

import java.util.HashMap;

public class Community {
	private static boolean logging = Generator.logging;

	private HashMap<String, Node> nodes;
	private int communityId; // Also known as modularity class or community number
	private NodeColor color;
	private Centroid centroid;
	private Centroid adjustedCentroid;

	// Constructors
	public Community() {
		nodes = new HashMap<String, Node>();
	}
	public Community(int communityId) {
		nodes = new HashMap<String, Node>();
		this.communityId = communityId;
	}

	// nodes getter/setter
	public HashMap<String, Node> getNodes() {
		return nodes;
	}
	public void setNodes(HashMap<String, Node> nodes) {
		this.nodes = nodes;
	}

	// modularityValue getter/setter
	public int getCommunityId() {
		return communityId;
	}
	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}

	// color getter/setter
	public NodeColor getColor() {
		return color;
	}
	public void setColor(NodeColor color) {
		this.color = color;
	}

	// centroid getter/setter
	public Centroid getCentroid() {
		return centroid;
	}
	public void setCentroid(Centroid centroid) {
		this.centroid = centroid;
	}

	// adjustedCentroid getter/setter
	public Centroid getAdjCentroid() {
		return adjustedCentroid;
	}
	public void setAdjCentroid(Centroid adjustedCentroid) {
		this.adjustedCentroid = adjustedCentroid;
	}

	public boolean containsKey(String token) {
		return nodes.containsKey(token);
	}

	// add Node to HashMap
	public boolean addNode(Node nodeIn) {
		if (!nodes.containsKey(nodeIn.getLabel())) {
			nodes.put(nodeIn.getLabel(), nodeIn);
		} else {
			if (logging)
				System.err.println("Cannot insert node, one already exists with same key.");
			return false;
		}
		return true;
	}

	// get the largest node within this community
	public String dominantConcept() {
		double max = -1;
		String dominant = "";
		
		for ( Node node : nodes.values() ) {
			if (node.getSize() > max) {
				max = node.getSize();
				dominant = node.getLabel();
			}
		}
		
		return dominant;
	}
}
