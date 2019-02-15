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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

// class containing various utilities, such as linking communities between layers,
// sorting layers, etc
public class LayerUtilities {

	// give an arraylist of links - representing everytime a concept links from one
	// layer to the next
	public ArrayList<Link> linkLayers(ArrayList<Layer> layers, int range) {
		ArrayList<Link> links = new ArrayList<Link>();

		for (int layerIndex = 0; layerIndex < layers.size() - 1; layerIndex++) {

			for (Community c : layers.get(layerIndex).getCommunities().values()) {
				for (Community nextC : layers.get(layerIndex + 1).getCommunities().values()) {
					Link link = null;

					for (Node n : c.getNodes()) {
						for (Node nextN : nextC.getNodes()) {

							if (n.getLabel().equals(nextN.getLabel())) {
								if (link == null) {
									link = new Link(layers.get(layerIndex).getDate(),
											layers.get(layerIndex + 1).getDate(), c.getCommunityId(),
											nextC.getCommunityId());
								} else {
									link.addToWeight();
								}
							}
						}
					}

					if (link != null) {
						links.add(link);
					}
				}
			}
		}
		return links;
	}

	public int lowestZ(ArrayList<Layer> layers) {
		int lowestZ = Integer.MAX_VALUE;
		for (int i = 0; i < layers.size(); i++) {
			int z = layers.get(i).getDate();
			if (lowestZ > z) {
				lowestZ = z;
			}
		}
		return lowestZ;
	}

	public void verifyLayerDates(ArrayList<Layer> layers) {
		Set<Integer> dates = new HashSet<Integer>();
		for (Layer layer : layers) {
			if (!dates.add(layer.getDate()))
				System.err.println("Found a duplicate date: " + layer.getDate());
		}
	}

	// gets a list of all the colors within a layer
	public ArrayList<NodeColor> getColorList(ArrayList<Layer> layers) {
		ArrayList<NodeColor> colors = new ArrayList<NodeColor>();

		for (Layer layer : layers) {
			for (Community community : layer.getCommunities().values()) {
				colors.add(community.getColor());
			}
		}
		return colors;
	}

	// Sort Layers by date
	public ArrayList<Layer> sortLayers(ArrayList<Layer> layers) {
		for (int i = 0; i < layers.size() - 1; i++) {
			for (int j = i + 1; j < layers.size(); j++) {
				if (layers.get(i).getDate() > layers.get(j).getDate()) {
					Layer tempLayer = layers.get(i).clone();
					layers.set(i, layers.get(j));
					layers.set(j, tempLayer);
				}
			}
		}
		return layers;
	}

	public ArrayList<Node> xyStackSort(ArrayList<Node> nodeList) {
		Stack<Node> xstack = new Stack<Node>();
		ArrayList<Node> sortedList = new ArrayList<Node>();

		// sort by x into stack
		nodeList = sortByX(nodeList);
		for (int i = 0; i < nodeList.size(); i++) {
			xstack.push(nodeList.get(i));
		}
		// spit out xstack into list by y value. place at head if above mid and tail if
		// below.
		sortedList.add(xstack.pop());
		double midpoint = sortedList.get(0).getY();
		while (!xstack.isEmpty()) {
			Node currentNode = xstack.pop();
			if (currentNode.getY() > midpoint) {
				sortedList.add(0, currentNode);
			} else {
				sortedList.add(currentNode);
			}
		}
		return sortedList;
	}

	// Sorts the nodesOfCompleteGraph array by z
	private ArrayList<Node> sortByX(ArrayList<Node> nodeList) {
		for (int i = 0; i < nodeList.size(); i++) {
			for (int j = 0; j < i; j++) {
				if (nodeList.get(i).getX() < nodeList.get(j).getX()) {
					Collections.swap(nodeList, i, j);
				}
			}
		}
		return nodeList;
	}

	public int numberOfCommunities(ArrayList<Layer> layers) {
		int counter = 0;
		for (int i = 0; i < layers.size(); i++) {
			HashMap<Integer, Community> communitites = layers.get(i).getCommunities();
			for (int j = 0; j < communitites.size(); j++) {
				counter++;
			}
		}
		return counter;
	}

	public void printLayers(ArrayList<Layer> layers) {
		for (int i = 0; i < layers.size(); i++) {
			layers.get(i).printLayer();
		}
	}
}
