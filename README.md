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

### Example: Tiny Directed Graph 

This example shows how to propagate the `positive` and `negative` polarity of two 
seed "words" (A and B), over a tiny **directed graph** of synonyms and antonymys.

The graph is shown below on its *initial state*. 
After applying a propagation algorithm, we get the graph on its *final state*.

![tiny-directed-graph-of-letters-epia](/test-resources/figures/tiny-directed-graph-of-letters-epia.png)

The full code can be seen in the file [ExampleTinyDirectedGraph.java](src/pt/psantos/phd/polarity/propagation/examples/ExampleTinyDirectedGraph.java#L149).

```java
// Loads the example data
ExampleTinyDirectedGraph exampleData = new ExampleTinyDirectedGraph();
DirectedPseudograph<Word, LexicalRelation> initialGraph = exampleData.createDirectedGraph();
// Get word "A" and "B" classified as positive and negative respectively
List<Word> seedWords = exampleData.getSeedWords();

// Applies the propagation algorithm over the directed graph
DirectedPseudograph<Word, LexicalRelation> finalGraph 
		= PolarityPropagation.propagate(initialGraph, seedWords);
```

OUTPUT:

- Word A, C, F classified as positive;
- Word B, E, H, I classified as negative;
- Word D, G classified as ambiguous (they are simultaneously positive and negative).

### Example - Tiny undirected graph

This example shows how to propagate the `positive`, `negative` and `neutral` 
polarity of the words `0, 1 and 2` over a tiny **undirected graph** of synonyms.

![tiny-undirected-graph-of-numbers-propor](/test-resources/figures/tiny-undirected-graph-of-numbers-propor.png)

INPUT (*initial state* in the above figure):
- An *undirected graph* of synonyms;
- Word `0` classified as `positive`, `1` calssified as `negative`, and
word `2` classified as `neutral`.

OUTPUT (*final state* in the above figure):
- Word `0, 6, 8, 9, 10` classified as `positive`;
- Word `1, 4` classified as `negative`;
- Word `2, 3` classified as `neutral`;
- Word `5, 7` classified as `ambiguous` (they are simultaneously positive and negative);
- Word `11, 12` end up with `no polarity`.

The full code can be seen in the file [ExampleTinyUndirectedGraph](src/pt/psantos/phd/polarity/propagation/examples/ExampleTinyUndirectedGraph.java).

# References
