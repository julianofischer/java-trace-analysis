package com.company;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static final int log_step = 1;

    //TIME, INDEGREE
    private static TreeMap<Integer, Double> avgInDegree = new TreeMap();
    private static TreeMap<Integer, Double> avgOutDegree = new TreeMap();
    private static String filename;

    private static void extract_info(DefaultDirectedGraph graph, int currentTime){
        int totalInDegree = 0;
        int totalOutDegree = 0;

        Set<Integer> vertexSet = graph.vertexSet();
        for (Integer v : vertexSet){
            totalInDegree = totalInDegree + graph.inDegreeOf(v);
            totalOutDegree = totalOutDegree + graph.outDegreeOf(v);
        }

        int nrOfVertex = vertexSet.size();
        double averageInDegree = (double)totalInDegree/nrOfVertex;
        double averageOutDegree = (double)totalOutDegree/nrOfVertex;

        avgInDegree.put(currentTime, averageInDegree);
        avgOutDegree.put(currentTime, averageOutDegree);

        /*
            Connected Sets:
            class ConnectivityInspector
            method connectedSets: Returns a list of Set s, where each set contains all vertices that are in the
             same maximally connected component.
         */
    }

    private static void log_info() throws IOException {
        String newline = System.getProperty("line.separator");

        Set<Integer> keySet = avgInDegree.keySet();
        File avgInOutput = new File(filename+".avgin");
        BufferedWriter fwIn = new BufferedWriter(new FileWriter(avgInOutput));

        File avgOutOutput = new File(filename+".avgout");
        BufferedWriter fwOut = new BufferedWriter(new FileWriter(avgOutOutput));

        for (Integer key : keySet){
            fwIn.write(String.format("%d\t%f", key, avgInDegree.get(key)));
            fwIn.newLine();

            fwOut.write(String.format("%d\t%f", key, avgOutDegree.get(key)));
            fwOut.newLine();
        }
    }

    public static void main(String[] args) {
	    filename = args[1];
	    ArrayList<Event> events = Event.getEventsFromFile(new File(filename));

	    // finding the nodes in order to create the graph
	    HashSet<Integer> nodes = new HashSet<Integer>();
        for (Event e : events){
            nodes.add(e.getFrom());
            nodes.add(e.getTo());
        }

        DefaultDirectedGraph<Integer, DefaultEdge> graph = new DefaultDirectedGraph<Integer, DefaultEdge>
                (DefaultEdge.class);

        // initializing graphs
        for (Integer vertex : nodes) {
            graph.addVertex(vertex);
        }

        // sorting events by the time using lambda expression
        events.sort((Event e1, Event e2)->e1.getTime().compareTo(e2.getTime()));

        int current_time = log_step;

        while(events.size() > 0){
            while(events.size() > 0 && events.get(0).getTime() <= current_time){
                Event e = events.remove(0);
                if(e.isOpening()){
                    graph.addEdge(e.getFrom(), e.getTo());
                } else{
                    graph.removeEdge(e.getFrom(), e.getTo());
                }
                extract_info(graph, current_time);
                current_time = current_time + log_step;
            }
        }

        log_info();

    }
}
