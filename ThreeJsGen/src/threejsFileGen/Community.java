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

public class Community {
	private static boolean logging = Generator.logging;

	private ArrayList<Node> nodes;
	private int communityId; // Also known as modularity class or community number
	private NodeColor color;
	private Centroid trueCenter;
	private Centroid weightedCenter;

	// Constructors
	public Community(int communityId) {
		nodes = new ArrayList<Node>();
		this.communityId = communityId;
	}

	// nodes getter/setter
	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
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
	public Centroid getTrueCenter() {
		return trueCenter;
	}

	public void setTrueCenter(Centroid trueCenter) {
		this.trueCenter = trueCenter;
	}

	// adjustedCentroid getter/setter
	public Centroid getWeightedCenter() {
		return weightedCenter;
	}

	public void setWeightedCenter(Centroid weightedCenter) {
		this.weightedCenter = weightedCenter;
	}

	public boolean containsNodeWithLabel(String label) {
		for (Node n : nodes) {
			if (n.getLabel().equals(label)) {
				return true;
			}
		}
		return false;
	}

	public Node getNodeWithLabel(String label) {
		for (Node n : nodes) {
			if (n.getLabel().equals(label)) {
				return n;
			}
		}
		return null;
	}

	// adds the node to the community if one with it's label doesn't already exist,
	// and adjusts the centroids to accommodate the new node
	public boolean addNode(Node node) {
		if (!containsNodeWithLabel(node.getLabel())) {
			nodes.add(node);
			calculateTrueCenter();
			calculateWeightedCenter();
		} else {
			if (logging)
				System.err.println("Cannot insert node, one already exists with same label.");
			return false;
		}
		return true;
	}

	private void calculateTrueCenter() {
		double x = 0, y = 0;

		for (Node n : nodes) {
			x += n.getX();
			y += n.getY();
		}
		x = x / nodes.size();
		y = y / nodes.size();

		trueCenter = new Centroid(x, y);
	}

	private void calculateWeightedCenter() {
		double x = 0, y = 0, totalSize = 0;
		for (Node n : nodes) {
			totalSize += n.getSize();
		}

		for (Node n : nodes) {
			x += n.getX() / totalSize;
			y += n.getY() / totalSize;
		}

		weightedCenter = new Centroid(x, y);
	}

	// get the largest node within this community
	public String dominantConcept() {
		double max = Double.MIN_VALUE;
		String dominant = "";

		for (Node node : nodes) {
			if (node.getSize() > max) {
				max = node.getSize();
				dominant = node.getLabel();
			}
		}

		return dominant;
	}

	// returns negative 1 when size of dominant concept
	// isn't found
	public double sizeOfDominantConcept() {
		String dominant = dominantConcept();

		for (Node node : nodes) {
			if (node.getLabel().equals(dominant)) {
				return node.getSize();
			}
		}
		return -1;
	}
}
