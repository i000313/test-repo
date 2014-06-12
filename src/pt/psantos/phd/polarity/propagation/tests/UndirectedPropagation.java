
package pt.psantos.phd.polarity.propagation.tests;

import java.util.List;
import java.util.Set;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.SimpleGraph;
import pt.psantos.phd.polarity.propagation.examples.ExampleTinyDirectedGraph;
import pt.psantos.phd.polarity.propagation.algorithm.LexicalRelation;
import pt.psantos.phd.polarity.propagation.algorithm.PolarityPropagation;
import pt.psantos.phd.polarity.propagation.algorithm.PolarityUtils;
import pt.psantos.phd.polarity.propagation.algorithm.Word;

/**
 *
 * @author PSantos
 */
public class UndirectedPropagation {

     public static void main(String[] args) {         
        ExampleTinyDirectedGraph epiaExample = new ExampleTinyDirectedGraph();
        DirectedPseudograph<Word, LexicalRelation> graph = epiaExample.createDirectedGraph();
        List<Word> seedWords = epiaExample.getSeedWords();
        
        boolean cont = graph.containsVertex(seedWords.get(0));
        Set<LexicalRelation> outt = graph.edgesOf(seedWords.get(0));
        DirectedPseudograph<Word, LexicalRelation> g2 
                = PolarityPropagation.propagate(graph, seedWords);
         PolarityUtils.printGraphNodes(g2);
     }
     
     
}
