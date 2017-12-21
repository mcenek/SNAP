package threejsFileGen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class MeshLogic {
	private ArrayList<Layer> layers;
	private HashMap<Integer, Link> links = new HashMap<Integer, Link>();
	
	// Constructor
	MeshLogic(){}

	MeshLogic(ArrayList<Layer> layersIn){
		layers = sortLayers(layersIn);
		//System.out.println("\nIN MESH LOGIC");
		for(int i = 0; i < layers.size(); i++){
			//System.out.println(i+": "+layers.get(i).getEdgeList().size());
		}
		linkCommunities();
		for(Map.Entry<Integer, Link> entry: links.entrySet()){
			Integer modClass = entry.getKey();
			//System.out.println("ModClass: "+modClass);
			Link links = entry.getValue();
			ArrayList<Integer> linkedClass = links.getLinkedClass();
			for(int i = 0; i < linkedClass.size(); i++){
				//System.out.println(linkedClass.get(i).toString());
			}
		}
	}

	public HashMap<Integer, Link> getLinks(){
		return links;
	}
	
	// give an arraylist of links - representing everytime a concept links from one layer to the next
	// these links only store two numbers - modclass of the first community, and modclass of the second
	public ArrayList<Link> linkAndGet() {
		ArrayList<Link> l = new ArrayList<Link>(); // could potentially assign a weight here...
		for(int i = 0; i < layers.size()-1; i++) {
			for(Community c : layers.get(i).getCommunities().values()) {
				for(Community nextC : layers.get(i+1).getCommunities().values()) {
					boolean foundLink = false;
					for(Node n : c.getNodes().values()) { // there is a better way to do this - possibly need
						for(Node nextN : nextC.getNodes().values()) { // a list of concept labels for each community
							if(n.getLabel().equals(nextN.getLabel()) && !foundLink) { // this just use a .contains()
								l.add(new Link(c.dominantConcept(), nextC.dominantConcept(), i, i+1));
								foundLink = true;
							}
						}
					}
				}
			}
		}
		return l;
	}

	// TODO not currently used
	private void linkCommunities(){
		for(int i = 0; i < layers.size()-1; i++){
			HashMap<Integer, Community> communities2 = layers.get(i).getCommunities();
			HashMap<Integer, Community> communities1 = layers.get(i+1).getCommunities();
			for(Map.Entry<Integer, Community> entry1: communities1.entrySet()){
				Integer modClass1 = entry1.getKey();
				Community community1 = entry1.getValue();
				if(i == -1){
					//System.out.println("=== DATE: " + layers.get(i).getDate());
					community1.setAdjCentroid(community1.getCentroid());
				}
				else{
					for(Map.Entry<String, Node> nodeHMEntry: community1.getNodes().entrySet()){
						String label = nodeHMEntry.getKey();
						for(Map.Entry<Integer, Community> entry2: communities2.entrySet()){
							Integer modClass2 = entry2.getKey();
							Community community2 = entry2.getValue();
							if(community2.getNodes().containsKey(label)){
								if(links.isEmpty() || !links.containsKey(modClass1)){
									Link tempLinks = new Link(modClass1, modClass2);
									links.put(modClass1, tempLinks);
								}
								else{
									links.get(modClass1).addLink(modClass2);
								}
							}
						}
					}
				}
			}
		}
	}

	// Sort Layers by date
	public ArrayList<Layer> sortLayers(ArrayList<Layer> layers){
		for(int i = 0; i < layers.size()-1; i++){
			for(int j = i+1; j < layers.size(); j++){
				if(layers.get(i).getDate() > layers.get(j).getDate()){
					Layer tempLayer = layers.get(i).clone();
					layers.set(i, layers.get(j));
					layers.set(j, tempLayer);
				}
			}
		}
		return layers;
	}

	public int lowestZ(ArrayList<Layer> layers){
		int lowestZ = Integer.MAX_VALUE;
		for(int i = 0; i < layers.size(); i++){
			int z = layers.get(i).getDate();
			if(lowestZ > z){
				lowestZ = z;
			}
		}
		return lowestZ;
	}
	
	public void verifyLayerDates(ArrayList<Layer> layers) {
		Set<Integer> dates = new HashSet<Integer>();
		for( Layer layer : layers ) {
			if( !dates.add( layer.getDate() ) )
				System.out.println( "Found a duplicate date: "+layer.getDate() );
		}
	}

	public ArrayList<NodeColor> getColorList(ArrayList<Layer> layers){
		ArrayList<NodeColor> tempColorList = new ArrayList<NodeColor>();
		
		for(int i = 0; i < layers.size(); i++){
			HashMap<Integer, Community>tempCom = layers.get(i).getCommunities();
			for(Map.Entry<Integer, Community> entry: tempCom.entrySet()){
				tempColorList.add(entry.getValue().getColor());
			}
		}
		return tempColorList;
	}

	public ArrayList<Node> xyStackSort(ArrayList<Node> nodeList){
        Stack<Node> xstack = new Stack<Node>();
        ArrayList<Node> sortedList = new ArrayList<Node>();

		// sort by x into stack
		nodeList = sortByX(nodeList);
		for(int i = 0; i < nodeList.size(); i++){
			xstack.push(nodeList.get(i));
		}
		// spit out xstack into list by y value. place at head if above mid and tail if below.
		sortedList.add(xstack.pop());
		double midpoint = sortedList.get(0).getY();
		while(!xstack.isEmpty()){
			Node currentNode = xstack.pop();
			if(currentNode.getY() > midpoint){
				sortedList.add(0, currentNode);
			}
			else{
				sortedList.add(currentNode);
			}
		}
		return sortedList;
	}

	// Sorts the nodesOfCompleteGraph array by z
	private ArrayList<Node> sortByX(ArrayList<Node> nodeList){
		for(int i = 0; i < nodeList.size(); i++){
			for(int j = 0; j < i; j++){
				if(nodeList.get(i).getX() < nodeList.get(j).getX()){
					Collections.swap(nodeList, i, j);
				}
			}
		}
		return nodeList;
	}

	public ArrayList<Layer> setCentroids(ArrayList<Layer> layers){
		for(int i = 0; i < layers.size(); i++){
			HashMap<Integer, Community> communityHM = layers.get(i).getCommunities();
			for(Map.Entry<Integer, Community> entry: communityHM.entrySet()){
				Community tempCom = entry.getValue();
				tempCom.setCentroid(createCentroid(tempCom.getNodes()));
				//System.out.println("CentroidX: "+tempCom.getCentroid().getX()+" Y: "+tempCom.getCentroid().getY());
			}
		}
		return layers;
	}

	public Centroid createCentroid(HashMap<String, Node> community){
		double totalX = 0;
		double totalY = 0;
		double centroidX;
		double centroidY;
		int numberOfNodes = community.size();
		
		for(Map.Entry<String, Node> entry: community.entrySet()){
			Node node = entry.getValue();
			totalX += node.getX();
			totalY += node.getY();
		}
		centroidX = totalX/numberOfNodes;
		centroidY = totalY/numberOfNodes;
		return new Centroid(centroidX, centroidY);
	}

	public ArrayList<Layer> resetNodeValuesToCentroid(ArrayList<Layer> layers){
		for(int i = 0; i < layers.size(); i++){
			HashMap<Integer, Community> communityHM = layers.get(i).getCommunities();
			for(Map.Entry<Integer, Community> comEntry: communityHM.entrySet()){
				Community tempCom = comEntry.getValue();
				HashMap<String, Node> nodes = tempCom.getNodes();
				for(Map.Entry<String, Node> nodeEntry: nodes.entrySet()){
					Node node = nodeEntry.getValue();
					node.setX(node.getX()-tempCom.getCentroid().getX());
					node.setY(node.getY()-tempCom.getCentroid().getY());
				}
			}
		}
		return layers;
	}

	public ArrayList<Layer> createAdjCentroid(HashMap<Integer, Link> links, ArrayList<Layer> layers){
		//System.out.println("SETADJCENTROID~~~~~~~");
		//layers = sortLayers(layers);
		for(Map.Entry<Integer, Link> entry: links.entrySet()){
			Centroid adjCentroid = new Centroid((double)0, (double)0);
			Integer modClass = entry.getKey();
			Link tempLink = entry.getValue();
			//System.out.println("MODCLASS: "+modClass);
			for(int j = 0; j < layers.size(); j++){
				for(int i = 0; i < tempLink.getLinkedClass().size(); i++){
					//System.out.println(j);
					if(layers.get(j).checkModClassExist(tempLink.getLinkedClass().get(i))){
						//System.out.println("\tCreate ADJ CENTROID "+j);
						//System.out.println("Link: "+tempLink.getLinkedClass().get(i));
						Centroid tempCentroid = findAdjCentroid(tempLink, layers.get(j));
						adjCentroid.addX(tempCentroid.getX());
						adjCentroid.addY(tempCentroid.getY());
						if(i == tempLink.getLinkedClass().size()-1){
							adjCentroid.setX(adjCentroid.getX()/(i+1));
							adjCentroid.setY(adjCentroid.getY()/(i+1));
							
							//System.out.println(" ## i: "+ i + " size: "+tempLink.getLinkedClass().size());
							//System.out.println("\t\t MODCLASS: "+modClass+" ADJVCENT X: "+(adjCentroid.getX()+" Y: "+(adjCentroid.getY())));
							//System.out.println("Date: "+layers.get(j+1).getDate());//+layers.get(j+1).getCommunity(modClass).getColor());
							//System.out.println("Test: "+layers.get(j+1).getCommunities().containsKey(modClass));
							layers.get(j+1).getCommunity(modClass).setAdjCentroid(adjCentroid);
							
							//System.out.println("SETTING ADJ X: "+layers.get(j).getCommunity(modClass).getAdjCentroid().getX()+" Y: "+layers.get(j).getCommunity(modClass).getAdjCentroid().getY());
						}
					}
					//System.out.println("i: "+ i + " size: "+tempLink.getLinkedClass().size());
				}
			}
		}
		return layers;
	}

	public Centroid findAdjCentroid(Link tempLink, Layer layer){
		//System.out.println("FIND ADJ CENTROID");
		int numberOfLinks = tempLink.getLinkedClass().size();
		double totalX = 0;
		double totalY = 0;
		for(int i = 0; i < numberOfLinks; i++){
			int linkedClass = tempLink.getLinkedClass().get(i);
			//System.out.println("LinkedClass: "+ linkedClass);
			totalX += layer.getCommunity(linkedClass).getCentroid().getX();
			totalY += layer.getCommunity(linkedClass).getCentroid().getY();
		}
		Centroid retCentroid = new Centroid((totalX/numberOfLinks), (totalY/numberOfLinks));
		//System.out.println("\tretCentroid: "+retCentroid.getX()+" "+retCentroid.getY());
		return retCentroid;
	}

	public int numberOfCommunities(ArrayList<Layer> layers){
		int counter = 0;
		for(int i = 0; i < layers.size(); i++){
			HashMap<Integer, Community> communitites = layers.get(i).getCommunities();
			for(int j = 0; j < communitites.size(); j++){
				counter++;
			}
		}
		return counter;
	}

	public void printLayers(ArrayList<Layer> layers){
		for(int i = 0; i < layers.size(); i++){
			layers.get(i).printLayer();
		}
	}
}