//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from:
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
package threejsFileGen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ThreeWriter {

	// Writes file with # of layers and all color values
	// -- PROJECTNAME_meta-colors.three.txt --
	// layercount [#of layers]
	// colorcount [#of colors]
	// [color #1]
	// [color #2]
	// [color #3]
	public void writeMetaColors(int numLayers, ArrayList<NodeColor> colorList, String path, String projectName) {
		String fullpath = path + "/" + projectName + "_meta-colors.three.txt";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullpath));
			writer.write("layercount " + numLayers + "\n");
			writer.write("colorcount " + colorList.size() + "\n");

			for (NodeColor c : colorList) {
				writer.write(c.getR() / 255.0 + " " + c.getG() / 255.0 + " " + c.getB() / 255.0 + "\n");
			}

			writer.close();
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	// -- PROJECTNAME_layers.three.txt --
	// layer [layerIndex] [layerZ]
	// cluster [clusterIndex] [clusterName] [cluster X,Y] [colorIndex]
	// [conceptIndex(w/in layer)] [conceptName] [conceptRadius] [concept X,Y]
	public void writeLayers(ArrayList<Layer> layers, ArrayList<NodeColor> colorList, String path, String projectName,
			double xSkew, double ySkew, double zSkew, int lowestZ) {

		String fullpath = path + "/" + projectName + "_layers.three.txt";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullpath));

			int layerIndex = 0;
			for (Layer layer : layers) {
				double layerz = layer.getDate() - lowestZ;
				writer.write("layer " + layerIndex + " " + layerz + "\n");

				int clusterIndex = 0;
				for (Community community : layer.getCommunities().values()) {
					writer.write("cluster " + clusterIndex + " " + community.dominantConcept() + " "
							+ community.getWeightedCenter().getX() + " " + community.getWeightedCenter().getY() + " "
							+ findColorIndexNumber(colorList, community.getColor()) + "\n");

					int nodeIndex = 0;
					for (Node node : community.getNodes()) {
						writer.write(nodeIndex + " " + node.getLabel() + " " + node.getSize() + " " + node.getX() + " "
								+ node.getY() + "\n");
						nodeIndex++;
					}
					clusterIndex++;
				}
				layerIndex++;
			}
			writer.close();
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
	}

	// -- PROJECTNAME_edges.three.txt --
	// layer [layerIndex]
	// [nodeIndex] [nodeIndex]
	// [nodeIndex] [nodeIndex]
	public void writeEdges(ArrayList<Layer> layers, String path, String projectName) {
		String fullpath = path + "/" + projectName + "_edges.three.txt";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullpath));
			int layerIndex = 0;

			for (Layer layer : layers) {
				writer.write("layer " + layerIndex + "\n");
				for (Edge edge : layer.getEdgeList()) {
					// find the source -- this isn't efficient at all, but until I rewrite the data
					// structures, it'll do
					for (Community c : layer.getCommunities().values()) {
						if (c.containsKey(edge.getSource())) {
							for (Node node : c.getNodes().values()) {
								if (node.getLabel().equals(edge.getSource())) {
									writer.write(node.absoluteIndex + " ");
								}
							}
						}
					}
					// find the target
					for (Community c : layer.getCommunities().values()) {
						if (c.containsKey(edge.getTarget())) {
							for (Node node : c.getNodes().values()) {
								if (node.getLabel().equals(edge.getTarget())) {
									writer.write(node.absoluteIndex + "\n");
								}
							}
						}
					}
				}
				layerIndex++;
			}

			writer.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	// -- PROJECTNAME_noodles.three.txt
	// [layerIndex0] [clusterIndex0] [layerIndex1] [clusterIndex1]
	public void writeNoodles(ArrayList<Link> links, String path, String projectName) {
		String fullpath = path + "/" + projectName + "_noodles.three.txt";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullpath));

			for (Link link : links) {
				writer.write(link.layerOne + " " + link.commOne + " " + link.layerTwo + " " + link.commTwo + "\n");
			}

			writer.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private int findColorIndexNumber(ArrayList<NodeColor> clist, NodeColor c) {
		NodeColor tempNodeColor;
		for (int i = 0; i < clist.size(); i++) {
			tempNodeColor = clist.get(i);
			if (tempNodeColor.getR() == c.getR() && tempNodeColor.getG() == c.getG()
					&& tempNodeColor.getB() == c.getB()) {
				return i;
			}
		}
		return -1;
	}
}
