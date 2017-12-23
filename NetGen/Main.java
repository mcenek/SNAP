//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from:
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
package netgen;

import java.util.ArrayList;

public class Main {

    public static void textToWeightedEdgelist(String inputFileName, String stopwordFileName, String outputFileName, int windowSize, int frequency, double freqMin, double freqMax) {

        //Read input file
        String text = IO.readFileAsString(inputFileName);
    	//String text = IO.importDirectory(inputFileName);
        System.out.println(text);

        //Clean POS
        String cleanPOS = Filter.removePOS(text);
        System.out.println("cleanPOS: "+cleanPOS);

        //Get Stop word List
        ArrayList<String> stopList = Filter.getStopWords(stopwordFileName);

        //Remove stop words
        String cleanStopWords = Filter.filterStopWords(stopList, cleanPOS);
        System.out.println("cleanStopWords: "+cleanStopWords);

        //Clean text
        String cleanText = Filter.removeNonpermittedCharacters(cleanStopWords);
        System.out.println("cleanText: "+cleanText);

        //Tokenize
        ArrayList<String> tokens = Tokenizer.split(cleanText, "\\s+");
        System.out.println("tokens: "+tokens.toString());

        //Frequency
        if(frequency == 1){
        	tokens = Filter.setFrequency(tokens, freqMin, freqMax);
        	System.out.println("Frequency: "+tokens.toString());
	    }

        //Generate network
        Network network = Network.generateNetworkBySlidingWindow(tokens, windowSize);

        //Write network to .dl output file
        network.writeEdgelist(outputFileName);
    }

    public static void main(String[] args)
    {
        //-----------------------Import Single file----------------------//
		String input = args[0].toString();
		String stopword = args[1];
		System.out.println("input: " + input);
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
