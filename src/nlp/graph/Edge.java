/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.graph;

/**
 *
 * @author Manh Tien
 */
public class Edge {

    public final Vertex target;
    public double weight;
    public int frequency;

    public Edge(Vertex argTarget, double argWeight) {
        target = argTarget;
        weight = argWeight;
    }
}
