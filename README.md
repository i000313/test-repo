# Polarity Propagation Algorithm - Overview

This packages contains the implementation of two algorithms to propagate the 
polarity of an initial set of words to a set of unlabeled words.

For applying this algorithms it is required:
- A set of unlabeled words represented as a directed or undirected graph;
- A set of words labeled as positive, negative or even neutral.


Example 1:

Suppose that:
- We already have the <b>directed graph<b> shown in the next figure at your 
left hand side (the graph is on its <i>initial state</i>).
- We label "A" as a <font color="green">positive</font> word and B is labeled a 
<font color="red">negative</font> word. 
- We want to apply a propagation algorithm over the mentioned graph to get the graph 
on your right hand side (the graph on its <i>final state</i>). 
<br/>On this final graph the words were labeled automatically based on the different relations
(synonyms and antonyms) represented on the graph.

![tiny-directed-graph-of-letters-epia](/test-resources/figures/tiny-directed-graph-of-letters-epia.png)

The above tasks can be done:

```java
// Loads the example data data.
ExampleTinyDirectedGraph exampleData = new ExampleTinyDirectedGraph();
DirectedPseudograph<Word, LexicalRelation> initialGraph = exampleData.createDirectedGraph();
List<Word> seedWords = exampleData.getSeedWords();

// Applies the propagation algorithm over the directed graph
DirectedPseudograph<Word, LexicalRelation> finalGraph 
		= PolarityPropagation.propagate(initialGraph, seedWords);

// Compute and show a few statistics about the final graph
PolarityStats stats = new PolarityStats(finalGraph);
System.out.println(stats);
```

# Examples
## Example 1
