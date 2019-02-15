//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from:
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
//package threejsFileGen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Generator {
	// TODO fileCounter shouldn't be a magic number, or this poorly named
	private static int fileCounter = 100;
	public static boolean logging = true;
	private static boolean development = true;

	private static void generateFiles(String projectName, String iGEXFPath, GenType genType, int range, double xSkew,
			double ySkew, double zSkew, String shape) throws IOException {

		GexfReader reader = new GexfReader();
		LayerUtilities utils = new LayerUtilities();
		ArrayList<Layer> layers = new ArrayList<Layer>();
		ArrayList<Link> links;
		ArrayList<NodeColor> colorList;
		int lowestZ;

		if (logging)
			System.out.println("PATH " + iGEXFPath);

		// read files and create layers from them
		ArrayList<Layer> initialLayers = new ArrayList<Layer>();
		try (Stream<Path> paths = Files.walk(Paths.get(iGEXFPath))) {
			paths.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					if (filePath.toString().endsWith(".gexf")) {
						initialLayers.add(reader.createLayerFromFile(filePath, fileCounter));
						fileCounter += 100;
					}
				}
			});
		} catch (Exception e) {
			System.out.println("A problem occurred reading the files into layers.");
			System.out.println(e.getMessage());
		}
		layers = new ArrayList<Layer>(initialLayers);

		colorList = utils.getColorList(layers);
		lowestZ = utils.lowestZ(layers);
		layers = utils.sortLayers(layers);
		links = utils.linkLayers(layers, range);

		if (genType == GenType.Threejs) {
			ThreeWriter writer = new ThreeWriter();

			writer.writeMetaColors(layers.size(), colorList, iGEXFPath, projectName);
			writer.writeLayers(layers, colorList, iGEXFPath, projectName, xSkew, ySkew, zSkew, lowestZ);
			writer.writeEdges(layers, iGEXFPath, projectName);
			writer.writeNoodles(links, iGEXFPath, projectName);

		} else if (genType == GenType.Partiview) {
			PartiWriter writer = new PartiWriter();

//			for (Map.Entry<Integer, Link> entry : links.entrySet()) {
//				Integer modClass = entry.getKey();
//				Link tempLink = entry.getValue();
//				for (int j = 0; j < layers.size() - 1; j++) {
//					for (int i = 0; i < tempLink.getLinkedClass().size(); i++) {
//						if (layers.get(j).checkCommunityIdExists(tempLink.getLinkedClass().get(i))) {
//							Community tempLayer1 = layers.get(j + 1).getCommunity(modClass);
//							Community tempLayer2 = layers.get(j).getCommunity(tempLink.getLinkedClass().get(i));
//							int numCom = utils.numberOfCommunities(layers);
//							writer.writeMesh(tempLayer1, tempLayer2, modClass, true, shape, iGEXFPath, projectName,
//									xSkew, ySkew, zSkew, lowestZ, numCom);
//						} else {
//							break;
//						}
//					}
//				}
//			}
//			writer.writeNodes(layers, colorList, iGEXFPath, projectName, xSkew, ySkew, zSkew, lowestZ);
//			writer.writeLabels(layers, iGEXFPath, projectName, xSkew, ySkew, zSkew, lowestZ);
//			writer.writeEdges(layers, colorList, iGEXFPath, projectName, xSkew, ySkew, zSkew, lowestZ);
		}
	}

	public static void main(String[] args) throws IOException {
		// quick run helper for development
		if (args.length == 0 && development) {
			generateFiles("test", "gexfs", GenType.Threejs, 91, 1, 1, 1, "hybrid");
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

		// TODO should be moved to mandatory args -- before the optional ones
		String individualGEXFFolder = null; // Path to the folder containing individual GEXFs

		if (args.length >= 2) {
			range = Integer.valueOf(args[1]); // Set Argument for range [IntervalSize(int)]
		}
		if (args.length >= 3) {
			xSkew = Double.valueOf(args[2]); // Set Argument for xSkew
		}
		if (args.length >= 4) {
			ySkew = Double.valueOf(args[3]); // Set Argument for ySkew
		}
		if (args.length >= 5) {
			zSkew = Double.valueOf(args[4]); // Set Argument for zSkew
		}
		if (args.length >= 6) {
			shape = args[5].toLowerCase(); // Set Argument for Shape
		}
		if (args.length >= 7) {
			individualGEXFFolder = args[6];
		}

		generateFiles(projectName, individualGEXFFolder, GenType.Threejs, range, xSkew, ySkew, zSkew, shape);
	}
}
