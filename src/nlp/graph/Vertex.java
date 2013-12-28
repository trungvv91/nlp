/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.graph;

import java.util.*;

/**
 *
 * @author Manh Tien
 */
public class Vertex implements Comparable<Vertex>{
    public String name;
    public String posTag;
    public double pageRank;
    public double score;
    public double tf;
    public double idf;
    public String chunk;
    public int phrase;
    public boolean stopWord;
    public int in;
    public int out;
    public boolean importance = false;
    public List<Edge> adjacencies = new ArrayList<Edge>();
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    public Vertex(String argName, String argLabel, double argScore, double argTf, double argIdf) 
    { 
        this.name = argName; 
        this.posTag = argLabel;
        this.score = argScore;
        this.tf = argTf;
        this.idf = argIdf;
//        this.chunk = argChunk;
//        this.phrase = argPhrase;
//        this.stopWord = argStopWord;
    }
    public String toString() { return name; }
    public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }
}
