//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
package threejsFileGen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Main {
	private static int fileCounter = 100;
	
	private static void partiviewGen(String gexf, String datefile, int range, double xSkew, double ySkew, double zSkew, String shape, String iGEXFPath) throws IOException{
		GexfReader reader = new GexfReader();
		ArrayList<Layer> tempLayers = new ArrayList<Layer>();
		ArrayList<Layer> layers;
		HashMap<Integer, Link> links;
		Writer writer = new Writer();
		ArrayList<NodeColor> colorList;
		MeshLogic logic;
		int lowestZ;
		boolean newFile = true;
		
		if(iGEXFPath != null){
			try(Stream<Path> paths = Files.walk(Paths.get("./"+iGEXFPath))){
			    paths.forEach(filePath -> {
			        if (Files.isRegularFile(filePath)){
			        	if(filePath.toString().endsWith(".gexf")){
			        		tempLayers.add(reader.createLayer(filePath, fileCounter));
			        		System.out.println("export successful\n");
			        		fileCounter += 100;
			        	}
			        }
			    });
			}
		}
		
		logic = new MeshLogic(tempLayers);
		colorList = logic.getColorList(tempLayers);
		lowestZ = logic.lowestZ(tempLayers);
		layers = logic.setCentroids(tempLayers);
		logic.printLayers(layers);
		layers = logic.resetNodeValuesToCentroid(layers);
		
		writer.writeCF(iGEXFPath, "fileName");
		writer.writeCmap(colorList, iGEXFPath, "fileName");
		
		ArrayList<Layer> newLayers = logic.sortLayers(layers);
		links = logic.getLinks();
		newLayers = logic.createAdjCentroid(links, newLayers);
		
		System.out.println();
		for(int i = 0; i < newLayers.size(); i++){
			System.out.println("I: "+i+" date: "+newLayers.get(i).getDate());
			if(layers.get(i).getCommunities() == null){
				System.out.println("null com");
			}
		}
		
		for(Map.Entry<Integer, Link> entry: links.entrySet()){
			Integer modClass = entry.getKey();
			Link tempLink = entry.getValue();
			for(int j = 0; j < newLayers.size()-1; j++){
				for(int i = 0; i < tempLink.getLinkedClass().size(); i++){
					if(newLayers.get(j).checkModClassExist(tempLink.getLinkedClass().get(i))){
						Community tempLayer1 = newLayers.get(j+1).getCommunity(modClass);
						Community tempLayer2 = newLayers.get(j).getCommunity(tempLink.getLinkedClass().get(i));
						int numCom = logic.numberOfCommunities(newLayers);
						writer.writeMesh(tempLayer1, tempLayer2, modClass, newFile, shape, iGEXFPath, "fileName", xSkew, ySkew, zSkew, lowestZ, numCom);
						newFile = false;
					}
					else{
						break;
					}
				}
			}
		}
		writer.writeNodes(newLayers, colorList, iGEXFPath, "fileName", xSkew, ySkew, zSkew, lowestZ);
		writer.writeLabels(newLayers, iGEXFPath, "fileName", xSkew, ySkew, zSkew, lowestZ);
		writer.writeEdges(newLayers, colorList, iGEXFPath, "fileName", xSkew, ySkew, zSkew, lowestZ);
	}
	
	public static void main(String[] args) throws IOException{
		// Mandatory Arguments
		String gexf = args[0];
		String datefile = args[1];

		// Default Arguments
		int range = 91; // The number of days the layers have to be within to be connected
		double xSkew = 1; // Shorten or stretch the x axis
		double ySkew = 1; // Shorten or stretch the y axis
		double zSkew = 1; // Shorten or stretch the z axis
		String shape = "hybrid"; // Shape of the structure of the 3D objects.(wire/solid/point/hybrid)
		String individualGEXFFolder = null; // Path to the folder containing individual GEXFs
		
		if(args.length >= 3){
			range = Integer.valueOf(args[2]); // Set Argument for range [IntervalSize(int)]
		}
		if(args.length >= 4){
			xSkew = Double.valueOf(args[3]); // Set Argument for xSkew
		}
		if(args.length >= 5){
			ySkew = Double.valueOf(args[4]); // Set Argument for xSkew
		}
		if(args.length >= 6){
			zSkew = Double.valueOf(args[5]); // Set Argument for xSkew
		}
		if(args.length >= 7){
			shape = args[6].toLowerCase(); // Set Argument for Shape
		}
		if(args.length >= 8){
			individualGEXFFolder = args[7]; // Set Argument for xSkew
		}
		partiviewGen(gexf, datefile, range, xSkew, ySkew, zSkew, shape, individualGEXFFolder);
	}
}
