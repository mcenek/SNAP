//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from:
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
//package netgen;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

public class Filter {
	//Remove POS tagging
    public static String removePOS(String input) {
    	String output = "";
    	ArrayList<String> stringList = new ArrayList<>();
    	String[] split = input.split("\\s+");
    	boolean inNE = false;
    	String namedEntity = "";

    	for(String s : split) {
    		for(int i = s.length()-1; i > 0; i--) {
    			//System.out.println(s);
    			if(s.startsWith("(NE")) {
    				inNE = true;
    				namedEntity = "";
					//System.out.println("Starts with NE: "+namedEntity);
    				break;
    			}
    			else if(s.startsWith("<") || s.endsWith(">")){}
    			else if(s.startsWith("(S")){}
    			else if(inNE) {
    				if(s.endsWith(")")) {
						//System.out.println("end: "+namedEntity);
    					if(s.charAt(i) == '/') {
        					inNE = false;
        					String firstChar = String.valueOf(s.charAt(0));
        					namedEntity += firstChar.toUpperCase();
        					if(s.length()>1){
        						namedEntity += s.substring(1, i);
        					}
    						//System.out.println("ends with NE: "+namedEntity);
    						stringList.add(namedEntity);
    						break;
    					}
    				}
    				else if(s.charAt(i) == '/') {
    					String firstChar = String.valueOf(s.charAt(0));
    					namedEntity += firstChar.toUpperCase();
    					if(s.length()>1){
    						namedEntity += s.substring(1, i);
    					}
						//System.out.println("Concate NE: "+namedEntity);
    					break;
    				}
    			}
    			else if(s.charAt(i) == '/') {
    				stringList.add(s.substring(0, i).toLowerCase());
    				break;
    			}
    			else if(i == 1) {
    				stringList.add(s);
    			}
    		}
    	}
        for(String s : stringList) {
        	output += s + " ";
        }

    	//System.out.println(IO.concatenateAll(output));
		return output;
    }

    //Filters out everything but spaces, letters
    //Trims and converts letters to lower-case
    public static String removeNonpermittedCharacters(String input) {
        return input.replaceAll("[^a-zA-Z\\.!? ]", "").trim();
    }

    //Get stopword list
    public static ArrayList<String> getStopWords(String fileName) {
    	ArrayList<String> stopList = new ArrayList<>();
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
            stopList.add(inFile.nextLine());
        }
        //System.out.println("stoplist: "+stopList.toString());
        return stopList;
    }

    //Filters out the stopwords from the input using the stopword arraylist
    public static String filterStopWords(ArrayList<String> stopList, String input) {
    	String output = "";
    	ArrayList<String> stringList = new ArrayList<>();
    	String[] split = input.split("\\s+");
    	for(String s : split) {
    		if(!stopList.contains(s)) {
    			stringList.add(s.trim());
    		}
    	}
        for(String s : stringList) {
        	output += s + " ";
        }

    	//System.out.println(IO.concatenateAll(output));
		return output;
    }

    //Filters out everything but spaces, letters
    //Trims and converts to lower-case
    public static ArrayList<String> removeNonpermittedCharacters(ArrayList<String> input) {
        ArrayList<String> output = new ArrayList<>();
        for (String line : input) {
            output.add(Filter.removeNonpermittedCharacters(line));
        }
        return output;
    }

    // Sorts tokens by frequency WITHIN A SENTENCE(?), filters out those above max or below min frequency threshold
    public static ArrayList<String> setFrequency(ArrayList<String> tokens, double min, double max) {
    	HashMap<String, Integer> hm = new HashMap<String, Integer>();
    	LinkedHashMap<String, Integer> lhm = new LinkedHashMap<String, Integer>();
    	ArrayList<ArrayList<String>> sentences;
    	sentences = filterSentence(tokens);

    	// retrieve a count of the number of times each token is seen
    	// as well as a min count, and a max count
    	int minCount = 0x7fffffff;
    	int maxCount = 0;
        int numWords = 0;
    	for(ArrayList<String> sentence: sentences) {
	        for(String s : sentence) {
        		hm.put(s, hm.getOrDefault(s, 0)+1);
        		int currCount = hm.get(s);
                numWords +=1;
        		if(currCount < minCount)
        			minCount = currCount;
        		else if(currCount > maxCount)
        			maxCount = currCount;
	        }
    	}

    	// put everything into a linked hm...
    	for(ArrayList<String> sentence: sentences) {
    		sentence = sortByFreq(sentence, hm);
    		for(String s: sentence) {
    			lhm.put(s, hm.get(s));
    		}
    	}

    	// remove those tokens outside the min and max bounds
        removeMinMaxHm(lhm, min, max, minCount, maxCount,numWords);
        return hmToArrayList(lhm);
    }

    public static ArrayList<String> sortByFreq(ArrayList<String> sentence, HashMap<String, Integer> hm){
    	for(int i = 0; i < sentence.size(); i++){
    		for(int j = i+1; j < sentence.size(); j++){
    			String token1 = sentence.get(i);
    			String token2 = sentence.get(j);
    			if(hm.get(token1)<hm.get(token2)){
    				String temp = token1;
    				sentence.set(i, token2);
    				sentence.set(j, temp);
    			}
    		}
    	}
    	return sentence;
    }

    // filterSentence
    public static ArrayList<ArrayList<String>> filterSentence(ArrayList<String> tokens){
    	ArrayList<ArrayList<String>> sentences = new ArrayList<ArrayList<String>>();
		ArrayList<String> sentence = new ArrayList<String>();
    	for(String s: tokens){
    		if(s.equals("#/#")){
    			ArrayList<String> temp = (ArrayList<String>) sentence.clone();
    			sentences.add(temp);
    			sentence.clear();
    		}
    		else{
    			sentence.add(s);
    		}
    	}
    	sentences.add(sentence);
		return sentences;
    }

    // Normalize the counts w/ respect to the article length
    // Remove the frequecies from the hash map that are not within the min max threshhold
    private static void removeMinMaxHm(HashMap<String, Integer> hm, double min, double max, int minCount, int maxCount, int numWords) {
    	Iterator<Map.Entry<String, Integer>> i = hm.entrySet().iterator();
    	int tokenCount = 0, removeCount = 0;

        // System.out.println("Min: "+min+" Max: "+max+" minCount: "+minCount+" maxCount: "+maxCount);
        // min /= (double)maxCount;
        // max /= (double)maxCount;
        // System.out.println("Min: "+min+" Max: "+max+" minCount: "+minCount+" maxCount: "+maxCount);

    	while(i.hasNext()) {
    		Map.Entry<String, Integer> m = i.next();
			double normalCount = (double)m.getValue() / maxCount; //minCount is always 1 so don't do: ((double)m.getValue() - minCount / ((double) maxCount - minCount); 
            double freqValue = m.getValue();
			if(normalCount < min || normalCount > max) {
				//System.out.println("Removing:\ttoken="+m.getKey()+"\trawCount="+m.getValue()+" minCount="+minCount+" maxCount="+maxCount+" normalCount="+normalCount);
				i.remove();
				removeCount++;
			} else {
				//System.out.println("Within Bounds:\ttoken="+m.getKey()+"\trawCount="+m.getValue()+" minCount="+minCount+" maxCount="+maxCount+" normalCount="+normalCount);
			}
			tokenCount++;
		};
		System.out.println("Token Count="+tokenCount+" Removed="+removeCount);
	}

    private static ArrayList<String> hmToArrayList(LinkedHashMap<String, Integer> hmap) {
    	Set<String> keySet = hmap.keySet();
    	ArrayList<String> tokens = new ArrayList<String>(keySet);
    	return tokens;
    }
}
