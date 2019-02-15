//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
//package netgen;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Neal
 */
public class IO {
    
    //Reads a file & returns each line in the file as a string
    public static String readFileAsString(String fileName) 
    {
        return concatenateAll(readFileAsLines(fileName));
    }
    
    //Reads a file and returns its lines in an arraylist
    public static ArrayList<String> readFileAsLines(String fileName) {
        ArrayList<String> lines = new ArrayList<>();
        Scanner inFile = null;

        try {
            //Debugging print statements
           // System.out.println(new File("").getAbsolutePath());
           // System.out.println(fileName);
            inFile = new Scanner(new FileReader(fileName));
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Failed to open input file. Exiting.");
            System.exit(-1);
        }

        while (inFile.hasNextLine()) {
            lines.add(inFile.nextLine());
        }
        return lines;
    }
    
    public static String importDirectory(String dirName)
    {
        File[] files = new File("../"+dirName).listFiles();
        System.out.println(dirName);
        //System.out.println(files.length);
        String returnString = "";
        
        for(int i=0;i<files.length-1; i++)
        { 
        	if(files[i].toString().endsWith(".txt")){
	            try {
	                //importGraph(files[i].getName());
	                returnString.concat(concatenateAll(readFileAsLines(files[i].getName())));
	            } catch (Exception e) {
	                System.out.println(files[i].toString() + " is not a graph file");
	            }
        	}
        }
        return returnString;
    } 
    
    //Combines the lines of the lines of the arraylist into a single String, separated by newline characters
    //TODO: throw exception if too many chars in strings?
    //Must have a total of less than about 2^30 characters
    public static String concatenateAll(ArrayList<String> lines) {
        String condensed = "";
        for (String line : lines) {
            condensed += ("\n" + line);
        }
        return condensed;
    }

    //Takes the lines of a .dl file and returns a weighted edgelist
    public static HashMap<SemanticPair, Double> importWeightedEdgelist(ArrayList<String> input) {
        
        HashMap<SemanticPair, Double> edges = new HashMap<>();
        
        for(int i = 4; i < input.size(); i++) {
            String[] line = input.get(i).split("\\s");
            edges.put(new SemanticPair(line[0], line[1]), Double.parseDouble(line[2]));
        }
        return edges;
    }
    
}
