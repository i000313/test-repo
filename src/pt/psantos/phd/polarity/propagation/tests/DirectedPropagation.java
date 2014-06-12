
package pt.psantos.phd.polarity.propagation.tests;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;
import pt.psantos.phd.polarity.propagation.tests.TesteDic.RelationshipEdge;
import pt.psantos.phd.polarity.propagation.tests.TesteDic.Vertex;

/**
 *
 * @author PSantos
 */
public class DirectedPropagation {

    /** A dictionary represented as Graph */
    private DirectedPseudograph<Vertex, RelationshipEdge> graph = null;
    
    /** The initial list of positive and negative words */
    private List<Vertex> seedWords = null;
    
    public enum RelationType {
        SYNONYM, ANTONYM
    }
    
    public DirectedPropagation(DirectedPseudograph<Vertex, RelationshipEdge> graph
            , List<Vertex> seedWords) {
        this.graph = graph;
        this.seedWords = seedWords;
    }
   
    
    public DirectedPseudograph<Vertex, RelationshipEdge> propagate() {

        List<Vertex> nodesToVisit = new ArrayList<Vertex>(this.seedWords);
        Deque<Vertex> queueNodesVisited = new ArrayDeque<Vertex>();
        
        // 2. Retrieve the1rst word w1 from the queue Q
        for(int i=0; i<nodesToVisit.size(); i++) {
            Vertex node = nodesToVisit.get(i);
            System.out.println("Vertex: " + node);
            // Get all their neighbors
            for(RelationshipEdge ed : graph.outgoingEdgesOf(node)) {
               
                Vertex neighborNode = (Vertex)ed.getV2();
                
                // For each neighbor visited for the 1rst time, set the iteration 
                // counter to value of iteration counter of currentWrd + 1.
                if(neighborNode.getIteration()<=0)
                    neighborNode.setIteration(node.getIteration()+1);
                
                
                if (ed.getRelationType().equals(RelationType.SYNONYM)) {
                    if (node.isPositive()) {
                        neighborNode.increasePosCounter();
                    } else if (node.isNegative()) {
                        neighborNode.increaseNegCounter();
                    }
                } else if (ed.getRelationType().equals(RelationType.ANTONYM)) {
                    if (node.isPositive()) {
                        neighborNode.increaseNegCounter();
                    } else if (node.isNegative()) {
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
        return graph;
    }
    
    public void printGraphNodes() {
       GraphIterator<Vertex, RelationshipEdge> iterator = 
                new BreadthFirstIterator<Vertex, RelationshipEdge>(graph);
        while (iterator.hasNext()) {
            System.out.println( iterator.next() );
        }        
    }
    
    public static void main(String[] args) {
        List<Vertex> nodes = getGraphNodes();
        DirectedPseudograph graph = creategraph(nodes);
        List<Vertex> seedWords = getSeedWords(nodes);
        
        DirectedPropagation g = new DirectedPropagation(graph, seedWords);
        g.printGraphNodes();
        g.propagate();
        g.printGraphNodes();
    }
    
    
    private static List<Vertex> getGraphNodes() {
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
        return words;
    }
            
    private static DirectedPseudograph creategraph(List<Vertex> words) {
        // I changed from DirectedMultigraph to DirectedPseudograph. This was
        // required because the former does not support loops. This means does not
        // support a vertex from a edge to itself. 
        DirectedPseudograph<Vertex, RelationshipEdge> graph
                = new DirectedPseudograph<Vertex, RelationshipEdge>(
                        new ClassBasedEdgeFactory<Vertex, RelationshipEdge>(RelationshipEdge.class));

        
                
        // John is everyone's friend
        for (Vertex wrd : words) {
            graph.addVertex(wrd);
        }
        
        graph.addEdge(words.get(0), words.get(2), new RelationshipEdge<Vertex>(words.get(0), words.get(2), RelationType.SYNONYM));
        graph.addEdge(words.get(0), words.get(3), new RelationshipEdge<Vertex>(words.get(0), words.get(3), RelationType.SYNONYM));
        graph.addEdge(words.get(1), words.get(3), new RelationshipEdge<Vertex>(words.get(1), words.get(3), RelationType.SYNONYM));
        graph.addEdge(words.get(1), words.get(4), new RelationshipEdge<Vertex>(words.get(1), words.get(4), RelationType.SYNONYM));
        graph.addEdge(words.get(2), words.get(5), new RelationshipEdge<Vertex>(words.get(2), words.get(5), RelationType.SYNONYM));
        graph.addEdge(words.get(3), words.get(5), new RelationshipEdge<Vertex>(words.get(3), words.get(5), RelationType.SYNONYM));
        graph.addEdge(words.get(5), words.get(6), new RelationshipEdge<Vertex>(words.get(5), words.get(6), RelationType.SYNONYM));
        graph.addEdge(words.get(4), words.get(6), new RelationshipEdge<Vertex>(words.get(4), words.get(6), RelationType.SYNONYM));
        graph.addEdge(words.get(4), words.get(7), new RelationshipEdge<Vertex>(words.get(4), words.get(7), RelationType.SYNONYM));
        graph.addEdge(words.get(7), words.get(4), new RelationshipEdge<Vertex>(words.get(7), words.get(4), RelationType.SYNONYM)); 
        graph.addEdge(words.get(5), words.get(8), new RelationshipEdge<Vertex>(words.get(5), words.get(8), RelationType.ANTONYM));     
            
        return graph;
    }
    
    private static List<Vertex> getSeedWords(List<Vertex> words) {
        List<Vertex> seedWords = new ArrayList<Vertex>();
        seedWords.add(words.get(0).increasePosCounter());
        seedWords.add(words.get(1).increaseNegCounter());
        
        return seedWords;
    }
    
    
    
    
    public static class RelationshipEdge<V> extends DefaultEdge {

        private V v1;
        private V v2;
        private RelationType rt;

        public RelationshipEdge(V v1, V v2, RelationType rt) {
            this.v1 = v1;
            this.v2 = v2;
            this.rt = rt;
        }
        
        public V getV1() {
            return v1;
        }

        public V getV2() {
            return v2;
        }

        public RelationType getRelationType() {
            return rt;
        }
        
        public String toString() {
            return rt.toString();
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
