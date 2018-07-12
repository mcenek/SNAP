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
import java.util.HashMap;
import java.util.Map;

public class PartiWriter {

	public void writeCF(String path, String projectName) {
		String pathname = path + "/" + projectName + ".cf";
		String name;
		if (projectName.contains("/")) {
			name = projectName.substring(projectName.lastIndexOf('/') + 1);
		} else {
			name = projectName;
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(pathname));
			writer.write("eval clip 1 1000000\n");
			writer.write("object g1=noodles\n");
			writer.write("include " + name + "_mesh.speck\n");
			writer.write("cment 0 1 1 1\n");
			writer.write("eval alpha .1\n");

			writer.write("object g2=nodes\n");
			writer.write("include " + name + "_nodes.speck\n");

			writer.write("object g3=edges\n");
			writer.write("include " + name + "_edges.speck\n");
			writer.write("eval alpha .2\n");

			writer.write("include " + name + ".label\n");
			writer.write("textcment 1 1 0 0\n");
			writer.write("eval lsize 25\n");
			writer.write("eval laxes off\n");
			writer.write("eval label on\n");
			writer.close();
		} catch (IOException e) {
			System.err.println("Error writing cf.");
			System.err.println(e);
			System.exit(1);
		}
	}

	public void writeCmap(ArrayList<NodeColor> colorList, String path, String projectName) {
		String fullpath = path + "/" + projectName + ".cmap";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullpath));
			writer.write(colorList.size() + "\n");
			for (NodeColor c : colorList) {
				writer.write(c.getR() / 255.0 + " " + c.getG() / 255.0 + " " + c.getB() / 255.0 + " 1\n");
			}
			writer.close();
		} catch (IOException e) {
			System.err.println("Error writing Cmap.");
			System.err.println(e);
			System.exit(1);
		}
	}

	public void writeNodes(ArrayList<Layer> layers, ArrayList<NodeColor> colorList, String path, String projectName,
			double xSkew, double ySkew, double zSkew, int lowestZ) {
		String fullPath = path + "/" + projectName + "_nodes.speck";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));
			double size;

			writer.write("textcolor 1\n");
			if (projectName.contains("/")) {
				writer.write("eval cmap ./data/" + projectName + "/"
						+ projectName.substring(projectName.lastIndexOf('/') + 1) + ".cmap\n");
			} else {
				writer.write("eval cmap ./data/" + projectName + "/" + projectName + ".cmap\n");
			}

			for (int i = 0; i < layers.size(); i++) {
				HashMap<Integer, Community> tempCommunities = layers.get(i).getCommunities();
				for (Map.Entry<Integer, Community> communities : tempCommunities.entrySet()) {
					Community tempCommunity = communities.getValue();
					Centroid adjCentroid = tempCommunity.getWeightedCenter();
					ArrayList<Node> tempNodes = tempCommunity.getNodes();
					if (adjCentroid == null) {
						adjCentroid = tempCommunity.getTrueCenter();
					}
					for (Node node : tempNodes) {
						size = node.getSize();

						// TODO This may be a problem, if nodes used to store
						// coordinates relative to their centroid, and now store absolute coordinates
						writer.write(((node.getX() + adjCentroid.getX()) * xSkew) + " "
								+ ((node.getY() + adjCentroid.getY()) * ySkew) + " "
								+ ((layers.get(i).getDate() - lowestZ) * zSkew) + " ellipsoid -r " + size + "," + size
								+ "," + size + " -c " + findColorIndexNumber(colorList, node.getColor()) + "\n");
					}
				}
			}
			writer.close();
		} catch (IOException e) {
			System.err.println("Error writing nodes.");
			System.err.println(e);
			System.exit(1);
		}
	}

	public void writeLabels(ArrayList<Layer> layers, String path, String projectName, double xSkew, double ySkew,
			double zSkew, int lowestZ) {
		String fullPath = path + "/" + projectName + ".label";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));

			writer.write("textcolor 1\n");
			if (projectName.contains("/")) {
				writer.write("eval cmap ./data/" + projectName + "/"
						+ projectName.substring(projectName.lastIndexOf('/') + 1) + ".cmap\n");
			} else {
				writer.write("eval cmap ./data/" + projectName + "/" + projectName + ".cmap\n");
			}

			for (int i = 0; i < layers.size(); i++) {
				HashMap<Integer, Community> tempCommunities = layers.get(i).getCommunities();

				for (Map.Entry<Integer, Community> communities : tempCommunities.entrySet()) {
					Community tempCommunity = communities.getValue();
					Centroid adjCentroid = tempCommunity.getWeightedCenter();
					ArrayList<Node> tempNodes = tempCommunity.getNodes();
					int date = layers.get(i).getDate();
					if (adjCentroid == null) {
						adjCentroid = tempCommunity.getTrueCenter();
					}

					// Write the labels for every node
					// TODO this may suffer from the same node relative vs absolute coordinates
					// problem
//					for (Node node : tempNodes) {
//						writer.write(((node.getX() + adjCentroid.getX()) * xSkew) + " "
//								+ ((node.getY() + adjCentroid.getY()) * ySkew) + " "
//								+ ((layers.get(i).getDate() - lowestZ) * zSkew) + " text -size "
//								+ (node.getSize() / tempCommunity.sizeOfDominantConcept()) * 2 + " " + node.getLabel()
//								+ '\n');
//					}
					// write the label for the dominant concept
					writer.write((adjCentroid.getX() * xSkew) + " " + (adjCentroid.getY() * ySkew) + " "
							+ ((date - lowestZ) * zSkew) + " text -size " + tempCommunity.sizeOfDominantConcept() * 4
							+ " " + tempCommunity.dominantConcept() + "\n");
				}
			}
			writer.close();
		} catch (IOException e) {
			System.err.println("Error writing labels.");
			System.err.println(e);
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

	public void writeMesh(Community tempCommunity1, Community tempCommunity2, int modClass, boolean newFile,
			String shape, String path, String projectName, double xSkew, double ySkew, double zSkew, int lowestZ,
			int numCom, int layerDate1, int layerDate2) {
		ArrayList<Node> nodeList1 = tempCommunity1.getNodes();
		ArrayList<Node> nodeList2 = tempCommunity2.getNodes();
		Centroid adjCentroid1 = tempCommunity1.getWeightedCenter();
		Centroid adjCentroid2 = tempCommunity2.getWeightedCenter();

		int endCount = 1;
		boolean first = true;
		String firstX = "";
		String firstY = "";
		String firstZ = "";

		// Gap Filler
		if (nodeList1.size() > nodeList2.size()) {
			nodeList2 = fillGaps(nodeList1, nodeList2);
		} else if (nodeList2.size() > nodeList1.size()) {
			nodeList1 = fillGaps(nodeList2, nodeList1);
		}

		if (shape.equals("hybrid")) {
			endCount = 2;
		}
		for (int shapeCount = 0; shapeCount < endCount; shapeCount++) {
			if (shapeCount == 0 && endCount == 2) {
				shape = "wire";
			} else if (shapeCount == 1 && endCount == 2) {
				shape = "solid";
			}
			// Sets the path and file name
			String pathName = path + "/" + projectName + "_mesh.speck";
			String x, y, z;
			double alteredX, alteredY, alteredZ;
			try {
				BufferedWriter writer;
				if (newFile) {
					writer = new BufferedWriter(new FileWriter(pathName));
				} else {
					writer = new BufferedWriter(new FileWriter(pathName, true));
				}
				if (projectName.contains("/")) {
					writer.write("eval cmap ./data/" + projectName + "/"
							+ projectName.substring(projectName.lastIndexOf('/') + 1) + ".cmap\n");
				} else {
					writer.write("eval cmap ./data/" + projectName + "/" + projectName + ".cmap\n");
				}
				writer.write("mesh -s " + shape + " -c " + (modClass % numCom) + " {\n");
				writer.write((nodeList1.size() + 1) + " " + 2 + "\n");

				for (int i = 0; i < nodeList1.size(); i++) {
					alteredX = (nodeList1.get(i).getX() + adjCentroid1.getX()) * xSkew;
					alteredY = (nodeList1.get(i).getY() + adjCentroid1.getY()) * ySkew;
					alteredZ = (layerDate1 - lowestZ) * zSkew;
					x = Double.toString(alteredX);
					y = Double.toString(alteredY);
					z = Double.toString(alteredZ);

					if (first) {
						firstX = x;
						firstY = y;
						firstZ = z;
						first = false;
					}
					writer.write(x + " " + y + " " + z + "\n");
				}
				writer.write(firstX + " " + firstY + " " + firstZ + "\n");

				first = true;
				for (int i = 0; i < nodeList2.size(); i++) {
					alteredX = (nodeList2.get(i).getX() + adjCentroid2.getX()) * xSkew;
					alteredY = (nodeList2.get(i).getY() + adjCentroid2.getY()) * ySkew;
					alteredZ = (layerDate2 - lowestZ) * zSkew;
					x = Double.toString(alteredX);
					y = Double.toString(alteredY);
					z = Double.toString(alteredZ);

					if (first) {
						firstX = x;
						firstY = y;
						firstZ = z;
						first = false;
					}
					writer.write(x + " " + y + " " + z + "\n");
				}
				writer.write(firstX + " " + firstY + " " + firstZ + "\n");

				writer.write("}\n");
				writer.close();
			} catch (IOException e) {
				System.err.println("Error writing meshes.");
				System.err.println(e);
				System.exit(1);
			}
		}
	}

	public void writeEdges(ArrayList<Layer> layerList, ArrayList<NodeColor> colorList, String path, String projectName,
			double xSkew, double ySkew, double zSkew, int lowestZ) {

		String fullpath = path + "/" + projectName + "_edges.speck";
		String mesh = "mesh -s wire -c 0 {\n1 2\n";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullpath));

			for (int i = 0; i < layerList.size(); i++) {
				ArrayList<Edge> edgeList = layerList.get(i).getEdgeList();
				Layer currentLayer = layerList.get(i);

				for (Edge e : edgeList) {
					String sourceKey = e.getSource();
					String targetKey = e.getTarget();
					HashMap<Integer, Community> communities = currentLayer.getCommunities();
					boolean sourceFound = false;
					boolean targetFound = false;
					Node s = null;
					Node t = null;
					Centroid sAdjCentroid = null;
					Centroid tAdjCentroid = null;

					for (Map.Entry<Integer, Community> entry : communities.entrySet()) {
						Community community = entry.getValue();
						if (community.containsNodeWithLabel(sourceKey)) {
							s = community.getNodeWithLabel(sourceKey);
							sAdjCentroid = community.getWeightedCenter();
							sourceFound = true;
						}
						if (community.containsNodeWithLabel(targetKey)) {
							t = community.getNodeWithLabel(targetKey);
							tAdjCentroid = community.getWeightedCenter();
							targetFound = true;
						}
						if (sourceFound && targetFound) {
							break;
						}
					}

					if (s != null && t != null) {
						writer.write(mesh);
						writer.write(((s.getX() + sAdjCentroid.getX()) * xSkew) + " "
								+ ((s.getY() + sAdjCentroid.getY()) * ySkew) + " "
								+ ((currentLayer.getDate() - lowestZ) * zSkew) + "\n");
						writer.write(((t.getX() + tAdjCentroid.getX()) * xSkew) + " "
								+ ((t.getY() + tAdjCentroid.getY()) * ySkew) + " "
								+ ((currentLayer.getDate() - lowestZ) * zSkew) + "\n}\n");
					}
				}
			}
			writer.close();
		} catch (IOException e) {
			System.err.println("Error writing edges.");
			System.err.println(e);
			System.exit(1);
		}
	}

	private ArrayList<Node> fillGaps(ArrayList<Node> largerCommunity, ArrayList<Node> smallerCommunity) {
		LayerUtilities logic = new LayerUtilities();
		ArrayList<Node> smallerArray;
		ArrayList<Node> largerArray;

		smallerArray = logic.xyStackSort(smallerCommunity);
		largerArray = logic.xyStackSort(largerCommunity);

		if (smallerArray.size() != largerArray.size()) {
			for (int i = smallerArray.size(); i < largerArray.size(); i++) {
				double shortDist = distBetweenNodes(largerArray.get(i), smallerArray.get(0));
				int shortDistArrayPos = 0; // Position in the array that has the Shortest Distance from smallerLayer[j]
											// to biggerLayer[i]
				for (int j = 0; j < smallerArray.size(); j++) {
					double currentDist = distBetweenNodes(largerArray.get(i), smallerArray.get(j));
					if (currentDist < shortDist) {
						shortDist = currentDist;
						shortDistArrayPos = j;
					}
				}
				Node node = new Node();
				node.setX(smallerArray.get(shortDistArrayPos).getX());
				node.setY(smallerArray.get(shortDistArrayPos).getY());
				smallerArray.add(node);
			}
		}
		return smallerArray;
	}

	// Distance between two nodes
	private double distBetweenNodes(Node node1, Node node2) {
		double x = node1.getX() - node2.getX();
		double y = node1.getY() - node2.getY();
		x = Math.abs(x);
		y = Math.abs(y);
		double c = Math.sqrt((x * x) + (y * y));
		return c;
	}
}
