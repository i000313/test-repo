# Introduction

This is the implementation of two **polarity propagation algorithms** in Java. 
These algorithms allows us to classify a set of words as `positive`, `negative`
and `neutral` starting with a small set of words.

The application can be run from the **command line** and through a **Java API**.
In both cases it is required:

- A set of words classified as `positive` and `negative` (and optionally as `neutral`);
- A graph of **sysnonyms** and **antonymys** (optional) words, multiwords or synsets.

Then the application will output the words of the graph classified as `positive`, 
`negative`, `neutral`, `ambiguous`, and `unknown`.

### EXAMPLE 1

This example tries to explain the mentioned concepts. Suppose that our gool is 
to classify a set of words as `positive`, `negative` and `neutral` with a few effort.

First, we collect a bunch of words with their *synonyms* and *antonyms* from a 
dictionary, thesaurus or wordnet. Then, we built the following **undirect graph**
(and *disconnected graph*).
In this graph, a *dashed line* represents an **antonym** and a *solid line*
represents a **synonym**. 

![tiny-undirected-graph-of-english-words](/test-resources/figures/ex01-undirected-graph-state-01.png)

Them, we manually classified the words: `good` as **positive**, `bad` as **negative**, 
and `common` as **neutral**. This is shown in the following figure.

![tiny-undirected-graph-of-english-words](/test-resources/figures/ex01-undirected-graph-state-02.png)

Finally, by applying a **polarity propagation algorithm** we can automatically 
classify the remaining words.

![tiny-undirected-graph-of-english-words](/test-resources/figures/ex01-undirected-graph-state-03.png)

# Requirements

For running the application the following software must be installed:
- `JRE (Java Runtime Environmen) 1.6` or higher;
- Or `JDK (Java Development Kit)` 1.6 or higher.

# Command Line

The application can be run from the command line, by running:

```
java -jar <path-to-the-application-jar> {mandatory-options} [optional-options]
```

**Options:**

| Option         | Type 	  | Description
| -------------- | ---------- | -----------
| -h             | optional   | help
| -s &lt;file_name&gt; | mandatory  | Seed words file name
| -g &lt;file_name&gt; | mandatory  | Graph file name
| -o &lt;file_name&gt; | optional   | Output file name
| -e encoding    | optional   | Caracter encoding of all the files

### EXAMPLE

This example, shows how to reproduce the previous example in the command line, to
classify a set of words as `positive`, `negative` and `neutral`. 

- First, we create a file with the word **good** classified as `positive`, **bad** 
classified as `negative`, and the word **common** classified as `neutral`. 
This file is saved as [examples/01-tiny-graph-english/seed-words-utf8.csv](examples/01-tiny-graph-english/seed-words-utf8.csv).
File content:

```
good,1
bad,-1
common,0
```

- Then, we create a graph file and save it as [examples/01-tiny-graph-english/graph-edges-utf8.txt](examples/01-tiny-graph-english/graph-edges-utf8.txt). 
Sample of this file:

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
(...)
```

- Finally, we run the following command line:

```
java -jar polarity-propagation-0.6.0.jar -s "examples/01-tiny-graph-english/seed-words-utf8.csv" -g "examples/01-tiny-graph-english/graph-edges-utf8.txt" -e "utf-8" -o "examples/01-tiny-graph-english/dic-output.csv"
```

The application will output a list of words classified with their polarity, 
to the file [examples/01-tiny-graph-english/dic-output.csv](examples/01-tiny-graph-english/dic-output.csv). 
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

The application can be called through a Java API. The previous example and previous
command line can be reproduced by the following Java code:

```java
// Read the graph from a file
TriplesLoader loader = new TriplesLoader();
SimpleGraph<Word, LexicalRelation> initialGraph 
		= loader.load(
		 new File("examples/01-tiny-graph-english/graph-edges-utf8.txt")
		, "utf-8");

// Read the seed words from a file
SeedWordsLoader seedWordsLoader = new SeedWordsLoader(); 
List<Word> seedWords = seedWordsLoader.load(
		 new File("examples/01-tiny-graph-english/seed-words-utf8.csv")
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

This example shows how to propagate the `positive` and `negative` over a tiny 
**directed graph** of synonyms and antonymys. This graph is shown below. In this
graph **A** is classified as `positive` and **B** as `negative` (graph on the *initial state*).

![tiny-directed-graph-of-letters-epia](/test-resources/figures/tiny-directed-graph-of-letters-epia.png)

After applying a propagation algorithm, we get the graph shown on its *final state*.
In this graph:
- `A, C, F` are classified as `positive`;
- `B, E, H, I` are classified as `negative`;
- `D, G` are classified as `ambiguous` (they are simultaneously `positive` and `negative`).

The full Java code to reproduce this is example is available on
[ExampleTinyDirectedGraph.java](src/pt/psantos/phd/polarity/propagation/examples/ExampleTinyDirectedGraph.java#L149).

### Example - Tiny undirected graph

This example shows how to propagate the `positive`, `negative` and `neutral` 
polarity of the words `0, 1 and 2` over a tiny **undirected graph** of synonyms.

![tiny-undirected-graph-of-numbers-propor](/test-resources/figures/tiny-undirected-graph-of-numbers-propor.png)

**INPUT (*initial state* in the above figure):**
- An *undirected graph* of synonyms;
- Word `0` classified as `positive`, `1` calssified as `negative`, and
word `2` classified as `neutral`.

**OUTPUT (*final state* in the above figure):**
- Word `0, 6, 8, 9, 10` classified as `positive`;
- Word `1, 4` classified as `negative`;
- Word `2, 3` classified as `neutral`;
- Word `5, 7` classified as `ambiguous` (they are simultaneously positive and negative);
- Word `11, 12` end up with `no polarity`.

The full code can be seen in the file [ExampleTinyUndirectedGraph](src/pt/psantos/phd/polarity/propagation/examples/ExampleTinyUndirectedGraph.java).

# References