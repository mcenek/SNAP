

package histgen;

import java.io.*;
import java.util.*;

public class Main {

    public static void textToWeightedEdgelist(String inputFileName, String stopwordFileName, String outputFileName, int windowSize, int frequency, double freqMin, double freqMax) {
        
        //Read input file
        String text = IO.readFileAsString(inputFileName);
    	//String text = IO.importDirectory(inputFileName);
        
        //Clean POS
        String cleanPOS = Filter.removePOS(text);
        
        //Get Stop word List
        ArrayList<String> stopList = Filter.getStopWords(stopwordFileName);
        
        //Remove stop words
        String cleanStopWords = Filter.filterStopWords(stopList, cleanPOS);
        
        //Clean text
        String cleanText = Filter.removeNonpermittedCharacters(cleanStopWords);
        
        //Tokenize
        ArrayList<String> tokens = Tokenizer.split(cleanText, "\\s+");
        
        //Frequency
        	tokens = Filter.setFrequency(tokens);     
        
        try {
            File file = new File(outputFileName + "Histogram.txt");
            FileWriter writer = null;
            writer = new FileWriter(file);
    	    		
     	    for(int i = 0; i < tokens.size() -1; i++)
     	    {
            		writer.write(tokens.get(i) + "\n");
     	    }

            writer.close();

        } catch (Exception e) 
        {
            e.printStackTrace();
            //System.out.println("Failed to complete output file. Exiting.");
            System.exit(-1);
        }
    }
    
    public static void main(String[] args) 
    {
        
        //-----------------------Import Single file----------------------//
		String input = args[0].toString();
		String stopword = args[1];
		String[] tokens = input.split("\\.(?=[^\\.]+$)");///Eliminate input extension for use in output name.
		String output = tokens[0];
		int frequency = 0; // Set to 1 to use frequency
		double freqMin = 35; // Percentage value - Defaults to 35% : less than 4 words
		double freqMax = 5; // Percentage value - Defaults to 5% : more than 20 words
		int windowSize = 3;
		
		if(args.length >= 3){
			frequency = Integer.parseInt(args[2]);
		}
		if(args.length >= 4){
			freqMin = Double.parseDouble(args[3]);
		}
		if(args.length >= 5){
			freqMax = Double.parseDouble(args[4]);
		}
		if(args.length >= 6){
			windowSize = Integer.parseInt(args[5]);
		}
		//Input file name & suffix, output file name, window size, use Frequency, Frequency min, Frequency max
		Main.textToWeightedEdgelist(input, stopword, output, windowSize, frequency, freqMin, freqMax);
    }
}
