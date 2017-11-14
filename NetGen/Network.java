//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
package netgen;


import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map.Entry;

import java.util.Random;

public class Network {

    public HashMap<SemanticPair, Double> edges;
    public Calendar calendar;

    //CONSTRUCTORS
    public Network() {
        edges = new HashMap<SemanticPair, Double>();
        calendar = new GregorianCalendar();
    }

    public Network(HashMap<SemanticPair, Double> edgeSet, Calendar date) {
        this.edges = edgeSet;
        this.calendar = date;
    }
    
    //Returns a network created from the tokens using a tokenwise sliding window method
    public static Network generateNetworkBySlidingWindow(ArrayList<String> tokens, int windowSize) {
        //Create network using sliding window
        Network network = new Network();
        for(int i = 0; i < tokens.size() - windowSize+1; i++) {
            for(int j = i + 1; j < i + windowSize; j++ ) {
                SemanticPair pair = new SemanticPair(tokens.get(i), tokens.get(j));
                if(network.edges.containsKey(pair)) {
                    network.edges.put(pair, network.edges.get(pair) + 1.0);
                } else {
                    network.edges.put(pair, 1.0);
                }
            }
        }
        return network;
    }

    //BASIC GRAPH METHODS
    //Returns a new graph which is sum of the two argument graphs
    public static HashMap<SemanticPair, Double> sum(HashMap<SemanticPair, Double> a, HashMap<SemanticPair, Double> b) {
        HashMap<SemanticPair, Double> sum = new HashMap<>();

        //Add all of network a
        for (Entry<SemanticPair, Double> entry : a.entrySet()) {
            if (!sum.containsKey((SemanticPair) entry.getKey())) {
                sum.put((SemanticPair) entry.getKey(), (double) entry.getValue());
            } else {
                sum.put((SemanticPair) entry.getKey(), (double) entry.getValue() + sum.get((SemanticPair) entry.getKey()));
            }
        }

        //Add all of network b
        for (Entry<SemanticPair, Double> entry : b.entrySet()) {
            if (!sum.containsKey((SemanticPair) entry.getKey())) {
                sum.put((SemanticPair)entry.getKey(), (double) entry.getValue());
            } else {
                sum.put((SemanticPair) entry.getKey(), (double) entry.getValue() + sum.get((SemanticPair) entry.getKey()));
            }
        }

        return sum;
    }

    //Scales the network by the specified scalar multiple
    public void scale(double scalar) {
        for (Entry<SemanticPair, Double> entry : edges.entrySet()) {
            entry.setValue((double) entry.getValue() * scalar);
        }
    }

    //OUTPUT METHODS
    //Writes the graph to an .dl file, weighted edge list format
    public void writeEdgelist(String fileName) {
//        System.out.println("Writing edgelist...");
        try {
            File file = new File(fileName + ".dl");
            FileWriter writer = null;
            writer = new FileWriter(file);
            writer.write("dl\nformat = edgelist1\t\nn=" + getEdgeset().size() + "\t\ndata:");
            System.out.println("DEBUG File Path: "+new File(".").getCanonicalPath());

            for (Entry<SemanticPair, Double> edge : getEdgeset().entrySet()) {
                writer.write("\n" + edge.getKey().getA() + " " + edge.getKey().getB() + " " + getEdgeset().get(edge.getKey()) + "\t");
                }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Failed to complete output file. Exiting.");
            System.exit(-1);
        }

    }

    //Writes the graph to an .dl file, weighted edge list format
    public void writeEdgelistWithAdditionalMetadata(String fileName) {

//        System.out.println("Writing edgelist...");
        try {
            File file = new File(fileName + ".dl");
            FileWriter writer = null;
            writer = new FileWriter(file);
            writer.write("dl\nYearMonthDate " + calendar.get(Calendar.YEAR) + " "
                    + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DATE) + "\nformat = edgelist1\t\nn=" + getEdgeset().size() + "\t\ndata:");

            for (Entry<SemanticPair, Double> edge : getEdgeset().entrySet()) {
                writer.write("\n" + edge.getKey().getA() + " " + edge.getKey().getB() + " " + getEdgeset().get(edge.getKey()) + "\t");
                //Debug
//                System.out.println(pair.getA().getSignature() + " " + pair.getB().getSignature() + " " + edges.get(pair) + "\t");
            }

            writer.close();

        } catch (Exception e) {
            System.out.println("Failed to complete output file. Exiting.");
            System.exit(-1);
        }

    }

    /**
     * @return the edgeSet
     */
    public HashMap<SemanticPair, Double> getEdgeset() {
        return edges;
    }

    public double meanEdge() {

        double mean = 0;

        for (Entry<SemanticPair, Double> entry : this.edges.entrySet()) {
            mean += (double) entry.getValue();
        }
        mean /= this.edges.size();
        return mean;
    }

    public double heaviestEdge() {
        double highest = 0;
        for (Entry<SemanticPair, Double> entry : this.edges.entrySet()) {
            if ((double) entry.getValue() > highest) {
                highest = (double) entry.getValue();
            }
        }
        return highest;
    }

    public void addNoise(double intensity) {
        Random random = new Random();

        for (Entry<SemanticPair, Double> entry : this.edges.entrySet()) {
            entry.setValue((double) entry.getValue() + random.nextDouble() * intensity);
        }
    }

    //Normalizes all edge weights on a (0,1.0] scaleFactor, with 1.0 being reserved for the heaviest edge
    //TODO: Check
    public void normalizeToHighestEdge() {

        double scaleFactor = 1.0 / this.heaviestEdge();
        this.scale(scaleFactor);

    }

    public void filterEdgesBelow(double minWeight) {

        HashMap<SemanticPair, Double> filteredEdges = new HashMap<>();
        
        for(SemanticPair edge : this.edges.keySet()) {
            if(edges.get(edge) >= minWeight) {
                filteredEdges.put(new SemanticPair(edge.getA(), edge.getB()), edges.get(edge));
            }
        }
        edges = filteredEdges;
    }

    
}
