//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from:
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
//package threejsFileGen;

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
		String fullpath;
		if(path == null)
			fullpath = projectName + "_meta-colors.three.txt";
		else
			fullpath = path + "/" + projectName + "_meta-colors.three.txt";
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullpath));
			writer.write("layercount " + numLayers + "\n");
			writer.write("colorcount " + colorList.size() + "\n");

			for (NodeColor c : colorList) {
				writer.write(c.getR() / 255.0 + " " + c.getG() / 255.0 + " " + c.getB() / 255.0 + "\n");
			}

			writer.close();
		} catch (IOException e) {
			System.err.println("Error writing metaColors.");
			System.err.println(e);
			System.exit(1);
		}
	}

	// -- PROJECTNAME_layers.three.txt --
	// layer [layerIndex] [layerDate] [layerZ]
	// cluster [clusterIndex] [communityId] [clusterName] [cluster X,Y] [colorIndex]
	// [conceptIndex(w/in cluster)] [conceptName] [conceptRadius] [concept X,Y]
	public void writeLayers(ArrayList<Layer> layers, ArrayList<NodeColor> colorList, String path, String projectName,
			double xSkew, double ySkew, double zSkew, int lowestZ) {

		String fullpath;
		if(path == null)
			fullpath = projectName + "_layers.three.txt";
		else
			fullpath = path + "/" + projectName + "_layers.three.txt";

		//String fullpath = path + "/" + projectName + "_layers.three.txt";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullpath));

			int layerIndex = 0;
			for (Layer layer : layers) {
				double layerz = layer.getDate() - lowestZ;
				writer.write("layer " + layerIndex + " " + layer.getDate() + " " + layerz + "\n");

				int clusterIndex = 0;
				for (Community community : layer.getCommunities().values()) {
					//community.calculateTrueCenter();
					writer.write("cluster " + clusterIndex + " " + community.getCommunityId() + " "
							+ community.dominantConcept() + " " + community.getTrueCenter().getX() + " "
							+ community.getTrueCenter().getY() + " "
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
			System.err.println("Error writing layers.");
			System.err.println(e);
			System.exit(1);
		}
	}

	// -- PROJECTNAME_edges.three.txt --
	// layer [layerDate]
	// [communityId] [nodeLabel] [communityId] [nodeLabel]
	// [communityId] [nodeLabel] [communityId] [nodeLabel]
	public void writeEdges(ArrayList<Layer> layers, String path, String projectName) {
		String fullpath;
		if(path == null)
			fullpath = projectName + "_edges.three.txt";
		else
			fullpath = path + "/" + projectName + "_edges.three.txt";

		//String fullpath = path + "/" + projectName + "_edges.three.txt";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullpath));

			for (Layer layer : layers) {
				writer.write("layer "+layer.getIndex() + " " + layer.getDate() + "\n");

				for (Edge edge : layer.getEdgeList()) {
					//print edge source x y dest x y
					Node src = edge.getSource();
					Node trg = edge.getTarget();

					writer.write(src.getX()+" "+src.getY()+" "+src.getCommunity()+" "+src.getLabel()+" "+trg.getX()+" "+trg.getY()+" "+trg.getCommunity()+" "+trg.getLabel()+"\n");

				// 	for (Community c : layer.getCommunities().values()) {
				// 		if (c.containsNodeWithLabel(edge.getSource())) {
				// 			for (Node node : c.getNodes()) {
				// 				if (node.getLabel().equals(edge.getSource())) {
				// 					writer.write(c.getCommunityId() + " " + node.getLabel() + " ");
				// 				}
				// 			}
				// 			break;
				// 		}
				// 	}
				// 	for (Community c : layer.getCommunities().values()) {
				// 		if (c.containsNodeWithLabel(edge.getTarget())) {
				// 			for (Node node : c.getNodes()) {
				// 				if (node.getLabel().equals(edge.getTarget())) {
				// 					writer.write(c.getCommunityId() + " " + node.getLabel() + "\n");
				// 				}
				// 			}
				// 			break;
				// 		}
				}
			}
			writer.close();
		} catch (IOException e) {
			System.err.println("Error writing edges.");
			System.err.println(e);
			System.exit(1);
		}
	}

	// -- PROJECTNAME_noodles.three.txt
	// [srcLayerDate] [srcCommunityId] [tarLayerDate] [tarCommunityId] [weight]
	public void writeNoodles(ArrayList<Link> links, String path, String projectName) {
		String fullpath;
		if(path == null)
			fullpath = projectName + "_noodles.three.txt";
		else
			fullpath = path + "/" + projectName + "_noodles.three.txt";

		//String fullpath = path + "/" + projectName + "_noodles.three.txt";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullpath));

			for (Link link : links) {
				writer.write(link.sourceLayerDate + " " + link.sourceCommunityId + " " + link.targetLayerDate + " "
						+ link.targetCommunityId + " " + link.weight + "\n");
			}

			writer.close();
		} catch (IOException e) {
			System.err.println("Error writing links/noodles.");
			System.out.println(e);
			System.exit(1);
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
