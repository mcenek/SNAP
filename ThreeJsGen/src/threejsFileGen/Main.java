package threejsFileGen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

public class Main {
	private static int fileCounter = 100;
	public static boolean logging = true;

	private static void partiviewGen(
			String projectName,
			int range,
			double xSkew,
			double ySkew,
			double zSkew,
			String shape,
			String iGEXFPath) throws IOException {

		GexfReader reader = new GexfReader();
		ArrayList<Layer> tempLayers = new ArrayList<Layer>();
		ArrayList<Layer> layers;
		HashMap<Integer, Link> links;
		ThreeWriter writer = new ThreeWriter();
		ArrayList<NodeColor> colorList;
		MeshLogic logic;
		int lowestZ;

		// read files into layers
		if (logging)
			System.out.println("PATH " + iGEXFPath);

		try (Stream<Path> paths = Files.walk(Paths.get(iGEXFPath))) {
			paths.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					if (filePath.toString().endsWith(".gexf")){
						tempLayers.add(reader.createLayerFromFile(filePath, fileCounter));
						fileCounter += 100;
					}
				}
			});
		} catch (Exception e) {
			System.out.println("A problem occurred reading the files into layers.");
			System.out.println(e.getMessage());
		}

		logic = new MeshLogic(tempLayers);
		colorList = logic.getColorList(tempLayers);
		lowestZ = logic.lowestZ(tempLayers);
		layers = logic.setCentroids(tempLayers);
		//logic.printLayers(layers);
		layers = logic.resetNodeValuesToCentroid(layers);

		// TODO adapt for prod
		System.out.println("Testing templayer dates");
		logic.verifyLayerDates(tempLayers);
		System.out.println("Testing layer dates");
		logic.verifyLayerDates(layers);

		writer.writeMetaColors(layers.size(), colorList, iGEXFPath, projectName);

		ArrayList<Layer> newLayers = logic.sortLayers(layers);
		links = logic.getLinks();
		newLayers = logic.createAdjCentroid(links, newLayers);

		// TODO adapt for prod
		System.out.println("Testing newLayer dates");
		logic.verifyLayerDates(newLayers);

		writer.writeLayers(newLayers, colorList, iGEXFPath, projectName, xSkew, ySkew, zSkew, lowestZ);
		writer.writeEdges(newLayers, iGEXFPath, projectName);
		writer.writeNoodles(logic.linkAndGet(), iGEXFPath, projectName);

//		for(Map.Entry<Integer, Link> entry: links.entrySet()){
//			Integer modClass = entry.getKey();
//			Link tempLink = entry.getValue();
//			for(int j = 0; j < newLayers.size()-1; j++){
//				for(int i = 0; i < tempLink.getLinkedClass().size(); i++){
//					if(newLayers.get(j).checkModClassExist(tempLink.getLinkedClass().get(i))){
//						Community tempLayer1 = newLayers.get(j+1).getCommunity(modClass);
//						Community tempLayer2 = newLayers.get(j).getCommunity(tempLink.getLinkedClass().get(i));
//						int numCom = logic.numberOfCommunities(newLayers);
//						//writer.writeMesh(tempLayer1, tempLayer2, modClass, newFile, shape, iGEXFPath, projectName, xSkew, ySkew, zSkew, lowestZ, numCom);
//						newFile = false;
//					}
//					else{
//						break;
//					}
//				}
//			}
//		}
		//writer.writeNodes(newLayers, colorList, iGEXFPath, projectName, xSkew, ySkew, zSkew, lowestZ);
		//writer.writeLabels(newLayers, iGEXFPath, projectName, xSkew, ySkew, zSkew, lowestZ);
		//writer.writeEdges(newLayers, colorList, iGEXFPath, projectName, xSkew, ySkew, zSkew, lowestZ);
	}

	public static void main(String[] args) throws IOException{
		if(args.length == 0) {
			partiviewGen("test", 91, 1, 1, 1, "hybrid", "gexfs");
			System.exit(0);
		}

		// Mandatory Arguments
		String projectName = args[0];

		// Default Arguments
		int range = 91; // The number of days the layers have to be within to be connected
		double xSkew = 1; // Shorten or stretch the x axis
		double ySkew = 1; // Shorten or stretch the y axis
		double zSkew = 1; // Shorten or stretch the z axis
		String shape = "hybrid"; // Shape of the structure of the 3D objects.(wire/solid/point/hybrid)
		String individualGEXFFolder = null; // Path to the folder containing individual GEXFs

		if(args.length >= 2){
			range = Integer.valueOf(args[1]); // Set Argument for range [IntervalSize(int)]
		}
		if(args.length >= 3){
			xSkew = Double.valueOf(args[2]); // Set Argument for xSkew
		}
		if(args.length >= 4){
			ySkew = Double.valueOf(args[3]); // Set Argument for ySkew
		}
		if(args.length >= 5){
			zSkew = Double.valueOf(args[4]); // Set Argument for zSkew
		}
		if(args.length >= 6){
			shape = args[5].toLowerCase(); // Set Argument for Shape
		}
		if(args.length >= 7){
			individualGEXFFolder = args[6];
		}
		partiviewGen(projectName, range, xSkew, ySkew, zSkew, shape, individualGEXFFolder);
	}
}
