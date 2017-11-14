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

public class Writer {
	
	public void writeCF(String path, String filename) {
		String pathname = path + "/" + filename + ".cf";
		String name;
		if(filename.contains("/")){
			name = filename.substring(filename.lastIndexOf('/')+1);
		}
		else{
			name = filename;
		}
		try{
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
		}catch(IOException e){System.out.println(e);}
	}

	public void writeCmap(ArrayList<NodeColor> colorList, String path, String filename){
		String fullpath = path + "/" + filename + ".cmap";
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullpath));
			writer.write(colorList.size() + "\n");
			for(NodeColor c : colorList){
				writer.write((double) (c.getR() / 255.0) + " " + (double) (c.getG() / 255.0) + " "
						+ (double) (c.getB() / 255.0) + " 1\n");
			}
			writer.close();
		}catch(IOException e){System.out.println(e);}
	}

	public void writeNodes(ArrayList<Layer> layers, ArrayList<NodeColor> colorList, String path, String filename, double xSkew, double ySkew, double zSkew, int lowestZ) {
		String fullPath = path + "/" + filename + "_nodes.speck";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));
			double size;
			double maxRadius = 0;
			
			writer.write("textcolor 1\n");
			if(filename.contains("/")){
				writer.write("eval cmap ./data/" + filename + "/" + filename.substring(filename.lastIndexOf('/')+1) + ".cmap\n");
			}
			else{
				writer.write("eval cmap ./data/" + filename + "/" + filename + ".cmap\n");
			}
			
			for(int i = 0; i < layers.size(); i++){
				HashMap<Integer, Community> tempCommunities = layers.get(i).getCommunities();
				for(Map.Entry<Integer, Community> communities : tempCommunities.entrySet()){
					Community tempCommunity = communities.getValue();
					Centroid adjCentroid = tempCommunity.getAdjCentroid();
					HashMap<String, Node> tempNodes = tempCommunity.getNodes();
					if(adjCentroid == null){
						adjCentroid = tempCommunity.getCentroid();
					}
					for(Map.Entry<String, Node> nodes : tempNodes.entrySet()){
						Node tempNode = nodes.getValue();
						size = tempNode.getSize();
						
						if (size > maxRadius){
							maxRadius = size;
						}
						writer.write(((tempNode.getX()+adjCentroid.getX())*xSkew) + " " + ((tempNode.getY()+adjCentroid.getY())*ySkew) + " " + ((tempNode.getZ()-lowestZ)*zSkew) + " ellipsoid -r " + size + "," + size + ","
								+ size + " -c " + findColorIndexNumber(colorList, tempNode.getColor()) + "\n");
					}
				}
			}
			writer.close();
		}catch(IOException e){System.out.println(e);}
	}
	
	public void writeLabels(ArrayList<Layer> layers, String path, String filename, double xSkew, double ySkew, double zSkew, int lowestZ) {
		String fullPath = path + "/" + filename + ".label";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));
			double size;
			double maxRadius = 0;
			
			writer.write("textcolor 1\n");
			if(filename.contains("/")){
				writer.write("eval cmap ./data/" + filename + "/" + filename.substring(filename.lastIndexOf('/')+1) + ".cmap\n");
			}
			else{
				writer.write("eval cmap ./data/" + filename + "/" + filename + ".cmap\n");
			}
			
			for(int i = 0; i < layers.size(); i++){
				HashMap<Integer, Community> tempCommunities = layers.get(i).getCommunities();
				for(Map.Entry<Integer, Community> communities : tempCommunities.entrySet()){
					Community tempCommunity = communities.getValue();
					Centroid adjCentroid = tempCommunity.getAdjCentroid();
					HashMap<String, Node> tempNodes = tempCommunity.getNodes();
					double largestNode = 0;
					String largestNodeLabel = "";
					int date = layers.get(i).getDate();
					if(adjCentroid == null){
						adjCentroid = tempCommunity.getCentroid();
					}
					for(Map.Entry<String, Node> nodes : tempNodes.entrySet()){
						Node tempNode = nodes.getValue();

						size = tempNode.getSize();
						if(largestNode < size){
							largestNode = size;
							largestNodeLabel = tempNode.getLabel();
						}
						if (size > maxRadius){
							maxRadius = size;
						}
						writer.write(((tempNode.getX()+adjCentroid.getX())*xSkew) + " " + ((tempNode.getY()+adjCentroid.getY())*ySkew) + " " + ((tempNode.getZ()-lowestZ)*zSkew) + " text -size " + (tempNode.getSize() / maxRadius)*2
								+ " " + tempNode.getLabel() + '\n');
					}
					writer.write((adjCentroid.getX()*xSkew) + " " + (adjCentroid.getY()*ySkew) + " " + ((date-lowestZ)*zSkew) + " text -size " + (largestNode / maxRadius)*4
							+ " " + largestNodeLabel + '\n');
				}
			}
			writer.close();
		}catch(IOException e){System.out.println(e);}
	}

	private int findColorIndexNumber(ArrayList<NodeColor> clist, NodeColor c) {
		NodeColor tempNodeColor;
		for (int i = 0; i < clist.size(); i++) {
			tempNodeColor = clist.get(i);
			if (tempNodeColor.getR() == c.getR() && tempNodeColor.getG() == c.getG() && tempNodeColor.getB() == c.getB()) {
				return i;
			}
		}
		return -1;
	}

	public void writeMesh(Community tempCommunity1, Community tempCommunity2, int modClass, boolean newFile, String shape, String path, String fileName, double xSkew, double ySkew, double zSkew, int lowestZ, int numCom){
		HashMap<String, Node> nodeList1 = tempCommunity1.getNodes();
		HashMap<String, Node> nodeList2 = tempCommunity2.getNodes();
		ArrayList<Node> community1 = communityHashMapToArray(nodeList1);
		ArrayList<Node> community2 = communityHashMapToArray(nodeList2);
		Centroid adjCentroid1 = tempCommunity1.getAdjCentroid();
		Centroid adjCentroid2 = tempCommunity2.getAdjCentroid();
		
		int endCount = 1;
		boolean first = true;
		String firstX = "";
		String firstY = "";
		String firstZ = "";
		
		
		// Gap Filler
		if(community1.size() > community2.size()){
			community2 = fillGaps(community1, community2);
		}
		else if(community2.size() > community1.size()){
			community1 = fillGaps(community2, community1);
		}

		if(shape.equals("hybrid")){
			endCount = 2;
		}
		for(int shapeCount = 0; shapeCount < endCount; shapeCount++){
			if(shapeCount == 0 && endCount == 2){
				shape = "wire";
			}
			else if(shapeCount == 1 && endCount == 2){
				shape = "solid";
			}
			// Sets the path and file name
			String pathName = path + "/" + fileName + "_mesh.speck";
			String x,y,z;
			double alteredX, alteredY, alteredZ;
			try{
				BufferedWriter writer;
				if(newFile){
					writer = new BufferedWriter(new FileWriter(pathName));
				}
				else{
					writer = new BufferedWriter(new FileWriter(pathName,true));
				}
				if(fileName.contains("/")){
					writer.write("eval cmap ./data/" + fileName + "/" + fileName.substring(fileName.lastIndexOf('/')+1) + ".cmap\n");
				}
				else{
					writer.write("eval cmap ./data/" + fileName + "/" + fileName + ".cmap\n");
				}
			    writer.write("mesh -s "+shape+" -c " + (modClass%numCom) + " {\n");
				writer.write((community1.size() + 1) + " " + 2 + "\n");

				for(int i = 0; i < community1.size(); i++){
					alteredX = (community1.get(i).getX()+adjCentroid1.getX()) * xSkew;
					alteredY = (community1.get(i).getY()+adjCentroid1.getY())* ySkew;
					System.out.println("Z: "+community1.get(i).getZ());
					alteredZ = (community1.get(i).getZ()-lowestZ) * zSkew;
					x = Double.toString(alteredX);
					y = Double.toString(alteredY);
	                z = Double.toString(alteredZ);
	                
	                if(first){
	                	firstX = x;
	                	firstY = y;
	                	firstZ = z;
	                	first = false;
	                }
					writer.write(x + " " + y + " " + z + "\n");
				}
				writer.write(firstX + " " + firstY + " " + firstZ + "\n");
					
				first = true;
				for(int i = 0; i < community2.size(); i++){
					if(adjCentroid2 == null){
						adjCentroid2 = tempCommunity2.getCentroid();
					}
					alteredX = (community2.get(i).getX()+adjCentroid2.getX()) * xSkew;
					alteredY = (community2.get(i).getY()+adjCentroid2.getY()) * ySkew;
					alteredZ = (community2.get(i).getZ()-lowestZ) * zSkew;
					x = Double.toString(alteredX);
					y = Double.toString(alteredY);
	                z = Double.toString(alteredZ);

	                if(first){
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
			} catch(IOException e){
				System.out.println(e);
			}
		}
	}
	
	public void writeEdges(ArrayList<Layer> layerList, ArrayList<NodeColor> colorList, String path, String filename, double xSkew, double ySkew, double zSkew, int lowestZ) {
		String fullpath = path + "/" + filename + "_edges.speck";
		String mesh = "mesh -s wire -c 0 {\n1 2\n";
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullpath));
			for(int i = 0; i < layerList.size(); i++){
				System.out.println("Layer #: "+ i);
				ArrayList<Edge> edgeList = layerList.get(i).getEdgeList();
				System.out.println("edgeList Size: "+edgeList.size());
				Layer currentLayer = layerList.get(i);
				for (Edge e : edgeList){
					String sourceKey = e.getSource();
					String targetKey = e.getTarget();
					HashMap<Integer, Community> communities = currentLayer.getCommunities();
					boolean sourceFound = false;
					boolean targetFound = false;
					Node s = null;
					Node t = null;
					Centroid sAdjCentroid = null;
					Centroid tAdjCentroid = null;
					
					for(Map.Entry<Integer, Community> entry: communities.entrySet()){
						Community community = entry.getValue();
						if(community.containsKey(sourceKey)){
							s = community.getNodes().get(sourceKey);
							if(community.getAdjCentroid() != null){
								sAdjCentroid = community.getAdjCentroid();
							}
							else{
								sAdjCentroid = community.getCentroid();
							}
							sourceFound = true;
						}
						if(community.containsKey(targetKey)){
							t = community.getNodes().get(targetKey);
							if(community.getAdjCentroid() != null){
								tAdjCentroid = community.getAdjCentroid();
							}
							else{
								tAdjCentroid = community.getCentroid();
							}
							targetFound = true;
						}
						if(sourceFound && targetFound){
							break;
						}
					}

					if(s != null && t != null){
						writer.write(mesh);
						writer.write(((s.getX()+sAdjCentroid.getX())*xSkew) + " " + ((s.getY()+sAdjCentroid.getY())*ySkew) + " " + ((s.getZ()-lowestZ)*zSkew) + "\n");
						writer.write(((t.getX()+tAdjCentroid.getX())*xSkew) + " " + ((t.getY()+tAdjCentroid.getY())*ySkew) + " " + ((t.getZ()-lowestZ)*zSkew) + "\n}\n");
					}
				}
			}
			writer.close();
		} catch (IOException e) {
		}
	}

	private ArrayList<Node> fillGaps(ArrayList<Node> largerCommunity, ArrayList<Node> smallerCommunity){
		MeshLogic logic = new MeshLogic();
		ArrayList<Node> smallerArray;
		ArrayList<Node> largerArray;

		smallerArray = logic.xyStackSort(smallerCommunity);
		largerArray = logic.xyStackSort(largerCommunity);

		if(smallerArray.size()!=largerArray.size()){
			for(int i = smallerArray.size(); i < largerArray.size(); i++){
				//System.out.println("biggerLayer: " + largerArray.size());
				//System.out.println("smallerLayer: " + smallerArray.size());
				double shortDist = distBetweenNodes(largerArray.get(i),smallerArray.get(0));
				int shortDistArrayPos = 0; //Position in the array that has the Shortest Distance from smallerLayer[j] to biggerLayer[i]
				for(int j = 0; j < smallerArray.size(); j++){
					double currentDist = distBetweenNodes(largerArray.get(i),smallerArray.get(j));
					if(currentDist<shortDist){
						shortDist = currentDist;
						shortDistArrayPos = j;
					}
				}
				Node node = new Node();
				node.setX(smallerArray.get(shortDistArrayPos).getX());
				node.setY(smallerArray.get(shortDistArrayPos).getY());
				node.setZ(smallerArray.get(shortDistArrayPos).getZ());
				smallerArray.add(node);
			}
		}
		//System.out.println("SmallerArray: "+smallerArray.size());
		return smallerArray;
	}

	// Distance between two nodes
	private double distBetweenNodes(Node node1, Node node2){
		double x = node1.getX() - node2.getX();
		double y = node1.getY() - node2.getY();
		x = Math.abs(x);
		y = Math.abs(y);
		double c = Math.sqrt((x*x)+(y*y)); 
		return c;
	}

	public ArrayList<Node> communityHashMapToArray(HashMap<String, Node> hmCommunity){
		ArrayList<Node> communityArray = new ArrayList<Node>();
		for(Map.Entry<String, Node> entry: hmCommunity.entrySet()){
			communityArray.add(entry.getValue());
		}
		return communityArray;
	}
}
