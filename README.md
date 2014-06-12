# Introduction

This is the implementation of two **polarity propagation algorithms** in Java. 
These algorithms allows us to classify a set of words as `positive`, `negative`
and `neutral`.

The application can be run from the **command line** and through a **Java API**.
In both cases it is required:

- A set of words classified as `positive` and `negative` (and optionally as `neutral`);
- A graph of unclassified words. In this graph, a node represents a word and
a edge represents a **sysnonym** or an **antonymy** between two words.

As output one gets the graph nodes classified as `positive`, `negative`, `neutral`,
`ambiguous`, and `unknown`.

### EXAMPLE 1

Suppose that we have collected from a dictionary, thesaurus or wordnet a bunch of 
words with their synonyms and antonyms. Then, we built the following graph. In this
initial graph words are not yet classified with a polarity.

![tiny-undirected-graph-of-english-words](/test-resources/figures/ex01-undirected-graph-state-01.png)

Them, we manually classified the word `good` as **positive**, `bad` as **negative**, 
and `common` as **neutral**.

![tiny-undirected-graph-of-english-words](/test-resources/figures/ex01-undirected-graph-state-02.png)

Finally, by applying a **polarity propagation algorithm**  we can automatically 
classify the remaining words.

![tiny-undirected-graph-of-english-words](/test-resources/figures/ex01-undirected-graph-state-03.png)

# Requirements

- Java JDK or JRE 1.6 or higher

# Command Line

The application can be run from the command line, by running:

```
java -jar <path-to-the-application-jar> {mandatory-options} [optional-options]
```

| Option         | Type 	  | Description
| -------------- | ---------- | -----------
| -h             | optional   | help
| -s <file_name> | mandatory  | Seed words file name
| -g <file_name> | mandatory  | Graph file name
| -o <file_name> | optional   | Output file name
| -e encoding    | optional   | Caracter encoding of all the files

### EXAMPLE

The above example, can be reproduced by running the following command line:

```
java -jar <path-to-the-application-jar> -s "test-resources/example01-tiny-graph-english/seed-words-utf8.csv" -g "test-resources/example01-tiny-graph-english/graph-edges-utf8.txt" -e "utf-8"
```

This command will:
- Read the `graph` from the file "test-resources/example01-tiny-graph-english/graph-edges-utf8.txt".
- Read an initial set of words classified as positive, negativa and neutral from
the file "test-resources/example01-tiny-graph-english/seed-words-utf8.csv".
- Build an undirected graph, apply a propagation algorithm, and output a list of
words with their polarity to a csv file.

The inputted seed words file has the following content:

```
good,1
bad,-1
common,0
```

A sample of the inputted graph file, is the following:

```
good SYN firm
good SYN hard
good SYN informed
good SYN just
good ANT bad
bad SYN bastard
bad SYN bush
bad SYN bush-league
bad SYN crummy
```

A sample of the **outputted csv file**, is the following:

```
words,polarity,negativeCounter,neutralCounter,positiveCounter,iteration
good,+,0,0,1,0
commonsense,+,1,0,0,1
commonsensible,+,0,0,1,1
commonsensical,+,0,0,1,1
firm,+,0,0,1,1
hard,+,0,0,1,1
(...)
well-founded,+,0,0,3,1
groundless,-,1,0,0,1
illogical,-,3,0,0,1
invalid,-,3,0,0,1
irrational,-,3,0,0,1
(...)
weak,-,2,0,0,2
fairish,+,0,0,1,2
serviceable,+,0,0,1,2
deficient,-,1,0,0,2
inadequate,-,1,0,0,2
insufficient,-,1,0,0,2
lacking,-,1,0,0,2
```

# Java API

The application can be called through a Java API. The previous example, can 
be reproduced by the following code:

```java
// Read the graph from a file
TriplesLoader loader = new TriplesLoader();
SimpleGraph<Word, LexicalRelation> initialGraph 
		= loader.load(
		 new File("test-resources/example01-tiny-graph-english/graph-edges-utf8.txt")
		, "utf-8");

// Read the seed words from a file
SeedWordsLoader seedWordsLoader = new SeedWordsLoader(); 
List<Word> seedWords = seedWordsLoader.load(
		 new File("test-resources/example01-tiny-graph-english/seed-words-utf8.csv")
		, "utf-8");

// Propagate the polarity from the seed words to the remaining words
SimpleGraph<Word, LexicalRelation> finalGraph = 
	PolarityPropagation.propagate(initialGraph, seedWords);

//	Write the list of words to a csv file
CsvOutput csv = new CsvOutput(new File("dic-output.csv"));
csv.write(finalGraph);          
```


# Other Examples

### Example: Propagating the «positive» and «negative» polarity over a tiny directed graph 

This example shows how to propagate the `positive` and `negative` polarity of two 
words (A and B), over a **directed graph** of synonyms and antonymys.

The graph is shown below on its initial state. 
After applying a propagation algorithm, we get the graph on its final state.

![tiny-directed-graph-of-letters-epia](/test-resources/figures/tiny-directed-graph-of-letters-epia.png)

The full code can be seen in the file [ExampleTinyDirectedGraph.java](src/pt/psantos/phd/polarity/propagation.examples/ExampleTinyDirectedGraph.java).

```java
// Loads the example data
ExampleTinyDirectedGraph exampleData = new ExampleTinyDirectedGraph();
DirectedPseudograph<Word, LexicalRelation> initialGraph = exampleData.createDirectedGraph();
// Get word "A" and word "B" labeled as positive and negative respectively
List<Word> seedWords = exampleData.getSeedWords();

// Applies the propagation algorithm over the directed graph
DirectedPseudograph<Word, LexicalRelation> finalGraph 
		= PolarityPropagation.propagate(initialGraph, seedWords);
```

OUTPUT:
<br/>The words:
- A, C, F end up labeled as positive;
- B, E, H, I end up labeled as negative;
- D, G end up with an ambiguous polarity (they are simultaneously positive and negative).


### Example - Propagating the «positive», «negative» and «neutral» polarity over a tiny undirected graph

![tiny-undirected-graph-of-numbers-propor](/test-resources/figures/tiny-undirected-graph-of-numbers-propor.png)

This example shows how to call the method `propagate(...)` of the class `PolarityPropagation`,
to propagate the «positive», «negative» and polarity of three words (the words 0, 
1 and 2), over a <b>undirected graph</b> of synonyms (see the above graph).

INPUT:
- The <b>undirected graph</b> of synonyms created by calling the method `createUndirectedGraph()`. 
This graph is shown in the above figure on its <i>initial state</i>;
- The word "0" labeled as <i>positive</i>, "1" labeled as <i>negative</i>, and
word "2" labeled as <i>neutral</i>.

ALGORITM
- We propagate the polarity of word 0, 1 and 2 to the unlabeled words, by appling a 
propagation algorithm. 

OUTPUT:
- The graph shown in the above figure on the <i>final state</i>). 
On this final graph the words were labeled automatically based on the different 
relations (synonyms and antonyms) represented on the graph. 
<br/>In this graph, the words:
 - 0, 6, 8, 9, 10 end up labeled as positive;
 - 1, 4 end up labeled as negative;
 - 2, 3 end up as neutral;
 - 5, 7 end up with an ambiguous polarity (they are simultaneously positive and negative);
 - 11, 12 end up with no polarity.

Example in Java (The full code can be seen in the file ExampleTinyUndirectedGraph.java):

```java
// Loads the sample data.
ExampleTinyUndirectedGraph exampleData = new ExampleTinyUndirectedGraph();
SimpleGraph<Word, LexicalRelation> initialGraph = exampleData.createUndirectedGraph();
List<Word> seedWords = exampleData.getSeedWords();

// Applies the propagation algorithm over the undirected graph
SimpleGraph<Word, LexicalRelation> finalGraph = PolarityPropagation.propagate(initialGraph, seedWords);

// Compute and show a few statistics about the final graph
PolarityStats stats = new PolarityStats(finalGraph);
System.out.println(stats);               
```

### Example 3 - Propagating the «positive», «negative» and «neutral» polarity over an undirected graph

Example in Java (The full code can be seen in the file ExamplePapel.java):

```java
// Get a graph from the PAPEL relations (a Portuguse resourse)
PAPELLoader papelLoader = new PAPELLoader();
SimpleGraph<Word, LexicalRelation> initialGraph 
		= papelLoader.load(
		 new File("test-resources/papel-2.0-relacoes_final_SINONIMIA-utf8.txt")
		, "utf-8"
		, POS.ADJECTIVE);

// Choose the seed words
List<Word> seedWords = new ArrayList<Word>(); 
seedWords.add((new Word("bonito")).setAsPositiveSeed());
seedWords.add((new Word("talento")).setAsPositiveSeed());
seedWords.add((new Word("interessante")).setAsPositiveSeed());

seedWords.add((new Word("feio")).setAsNegativeSeed());
seedWords.add((new Word("conspurcado")).setAsNegativeSeed());
seedWords.add((new Word("discriminação")).setAsNegativeSeed());
seedWords.add((new Word("ruinosamente")).setAsNegativeSeed()); 
seedWords.add((new Word("deploravelmente")).setAsNegativeSeed()); 
seedWords.add((new Word("falsamente")).setAsNegativeSeed());

// Propagate the polarity from the seed words to the remaining words
SimpleGraph<Word, LexicalRelation> finalGraph = 
	PolarityPropagation.propagate(initialGraph, seedWords);

// Show some statistics about the final graph
PolarityStats stats = new PolarityStats(finalGraph);
System.out.println(stats);             
```
