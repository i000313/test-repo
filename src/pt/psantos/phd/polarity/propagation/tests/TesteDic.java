package pt.psantos.phd.polarity.propagation.tests;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.jgrapht.graph.*;

/**
 * A simple example for dealing with labeling edges. Each edge has one of two
 * labels - "friend" and "enemy". A custom RelationshipEdge class takes care of
 * the nitty gritty. Comments/suggestions are welcome.
 *
 * Source: https://github.com/jgrapht/jgrapht/wiki/LabeledEdges
 */
public class TesteDic {

    private static final String synonym = "syn";
    private static final String antonym = "ant";

    public static void main(String[] args) {
        // I changed from DirectedMultigraph to DirectedPseudograph. This was
        // required because the former does not support loops. This means does not
        // support a vertex from a edge to itself. 
        DirectedPseudograph<Vertex, RelationshipEdge> graph
                = new DirectedPseudograph<Vertex, RelationshipEdge>(
                        new ClassBasedEdgeFactory<Vertex, RelationshipEdge>(RelationshipEdge.class));

        List<Vertex> words = new ArrayList<Vertex>();
        words.add(new Vertex("A")); // 0
        words.add(new Vertex("B")); // 1
        words.add(new Vertex("C")); // 2
        words.add(new Vertex("D")); // 3
        words.add(new Vertex("E")); // 4
        words.add(new Vertex("F")); // 5
        words.add(new Vertex("G")); // 6
        words.add(new Vertex("H")); // 7
        words.add(new Vertex("I")); // 8
                
        // John is everyone's friend
        for (Vertex wrd : words) {
            graph.addVertex(wrd);
        }
        
        graph.addEdge(words.get(0), words.get(2), new RelationshipEdge<Vertex>(words.get(0), words.get(2), synonym));
        graph.addEdge(words.get(0), words.get(3), new RelationshipEdge<Vertex>(words.get(0), words.get(3), synonym));
        graph.addEdge(words.get(1), words.get(3), new RelationshipEdge<Vertex>(words.get(1), words.get(3), synonym));
        graph.addEdge(words.get(1), words.get(4), new RelationshipEdge<Vertex>(words.get(1), words.get(4), synonym));
        graph.addEdge(words.get(2), words.get(5), new RelationshipEdge<Vertex>(words.get(2), words.get(5), synonym));
        graph.addEdge(words.get(3), words.get(5), new RelationshipEdge<Vertex>(words.get(3), words.get(5), synonym));
        graph.addEdge(words.get(5), words.get(6), new RelationshipEdge<Vertex>(words.get(5), words.get(6), synonym));
        graph.addEdge(words.get(4), words.get(6), new RelationshipEdge<Vertex>(words.get(4), words.get(6), synonym));
        graph.addEdge(words.get(4), words.get(7), new RelationshipEdge<Vertex>(words.get(4), words.get(7), synonym));
        graph.addEdge(words.get(7), words.get(4), new RelationshipEdge<Vertex>(words.get(7), words.get(4), synonym)); 
        graph.addEdge(words.get(5), words.get(8), new RelationshipEdge<Vertex>(words.get(5), words.get(8), antonym));     
          
//        for (RelationshipEdge edge : graph.edgeSet()) {
//            if (edge.toString().equals("enemy")) {
//                System.out.printf(edge.getV1() + "is an enemy of " + edge.getV2() + "\n");
//            } else if (edge.toString().equals("friend")) {
//                System.out.printf(edge.getV1() + " is a friend of " + edge.getV2() + "\n");
//            }
//        }
        
        
//       GraphIterator<Vertex, RelationshipEdge> iterator = 
//                new BreadthFirstIterator<Vertex, RelationshipEdge>(graph);
//        while (iterator.hasNext()) {
//            System.out.println( iterator.next() );
//        }
        
        List<Vertex> nodesToVisit = new ArrayList<Vertex>();
        nodesToVisit.add(words.get(0).increasePosCounter());
        nodesToVisit.add(words.get(1).increaseNegCounter());
 
        Deque<Vertex> queueNodesVisited = new ArrayDeque<Vertex>();
        
        // 2. Retrieve the1rst word w1 from the queue Q
        for(int i=0; i<nodesToVisit.size(); i++) {
            Vertex node = nodesToVisit.get(i);
            System.out.println("Vertex: " + node);
            // Get all their neighbors
            for(RelationshipEdge ed : graph.outgoingEdgesOf(node)) {
               
                Vertex neighborNode = ((Vertex)ed.getV2());
                
                // For each neighbor visited for the 1rst time, set the iteration 
                // counter to value of iteration counter of currentWrd + 1.
                if(neighborNode.getIteration()<=0)
                    neighborNode.setIteration(node.getIteration()+1);
                
                if(node.isPositive()) {
                    if(ed.label.equals(synonym)){
                      neighborNode.increasePosCounter();
                    } else if(ed.label.equals(antonym)) {
                      neighborNode.increaseNegCounter();      
                    }  
                } else if(node.isNegative()) {
                    if(ed.label.equals(synonym)){
                      neighborNode.increaseNegCounter();
                    } else if(ed.label.equals(antonym)) {
                      neighborNode.increasePosCounter();      
                    } 
                }
                 
                // (b) If nbi does not exists on queue Q nor list V, add him to the end of Q.
                if(!nodesToVisit.contains(neighborNode) && 
                        !queueNodesVisited.contains(neighborNode)) {
                    nodesToVisit.add(neighborNode);
                }
                System.out.println("edge: " + ed + " of " + ed.getV2());
            }
            queueNodesVisited.add(node);
        }
               
    }

    public static class RelationshipEdge<V> extends DefaultEdge {

        private V v1;
        private V v2;
        private String label;

        public RelationshipEdge(V v1, V v2, String label) {
            this.v1 = v1;
            this.v2 = v2;
            this.label = label;
        }
        
        public V getV1() {
            return v1;
        }

        public V getV2() {
            return v2;
        }

        public String toString() {
            return label;
        }
    }
    
    
    public static class Vertex {
        private int pos;
        private int neg;
        private int iteration;
        private String label;

        public Vertex(String label) {
            this.label = label;
            this.pos = 0;
            this.neg = 0;
            this.iteration = 0;
        }
        
        public Vertex increasePosCounter() {
            pos++;
            return this;
        }
         
        public Vertex increaseNegCounter() {
            neg++;
            return this;
        }

        public boolean isPositive(){
            return (this.pos - this.neg) > 0;
        }
         
        public boolean isNegative(){
            return (this.neg - this.pos) > 0;
        }
        
        public int getIteration() {
            return iteration;
        }

        public void setIteration(int iteration) {
            this.iteration = iteration;
        } 
        
        public String toString() {
            return label + " [+:" + pos + " -:" + neg + " I:" + iteration + "]";
        }
    }    
    
}
