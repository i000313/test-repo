# Polarity Propagation Algorithm - Introduction

This Java code implements two algorithms to propagate the «positive», «negative»
and «neutral» polarity of words over a graph of *synonyms* and *antonyms*.

The application can be run from the command line or through the Java API.
In both cases it is required:

- A set of words classified as «positive» and «negative» (and eventually as «neutral»);
- A graph of unclassified words. In this graph, a node represents a word and
a edge represents a sysnonym or an antonymy between two words.

As output one gets the graph nodes classified as «positive», «negative», «neutral»,
«ambiguous», and «unknown».

**EXAMPLE 1:**

Suppose that we have the following graph of unclassified words (words without polarity).
(This kind of graph can be build from a dictionary, a thesaurus, a wordnet, etc.

[tiny-undirected-graph-of-english-words](/test-resources/figures/ex01-undirected-graph-state-01.png)

Them we classify a small set of these words as «positive», «negative» and «neutral».
We call these words, seed words. In this case, we cassify `good` as **positive**, 
`bad` as **negative**, and `common` as neutral.

[tiny-undirected-graph-of-english-words](/test-resources/figures/ex01-undirected-graph-state-02.png)

Finally, we apply a propagation algorithm for propagating the polarity of the seed 
words to the unclassified words.

# Calling the Application from the Command Line

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

**EXAMPLE:**

The example 1 shown above, can be reproduced by running:

```
java -jar <path-to-the-application-jar> -s "test-resources/example01-tiny-graph-english/seed-words-utf8.csv"
 -g "test-resources/example01-tiny-graph-english/graph-edges-utf8.txt" -e "utf-8"
```

The seed words file has the following content:

```
good,1
bad,-1
common,0
```

A sample of the graph file content, is the following:

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

# Calling the Application through the Java API

The example 1 shown above, can be reproduced by calling:

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

## Example - Propagating the «positive» and «negative» polarity over a tiny directed graph 

![tiny-directed-graph-of-letters-epia](/test-resources/figures/tiny-directed-graph-of-letters-epia.png)

This example shows how to call the method `propagate(...)` of the class `PolarityPropagation`,
to propagate the «positive» and «negative» polarity of two words (A and B), over 
a <b>directed graph</b> of synonyms and antonymys (see the above graph).

STEP 1:
- First we create a <b>directed graph</b> of synonyms and antonyms by calling the 
method `createDirectedGraph()`. The created graph is shown in the above figure 
on its <i>initial state</i>.

STEP 2:
- Then we label the word "A" as <i>positive</i> and the word "B" as <i>negative</i> 
by calling the method `getSeedWords()`.

```java
// Loads the example data
ExampleTinyDirectedGraph exampleData = new ExampleTinyDirectedGraph();
DirectedPseudograph<Word, LexicalRelation> initialGraph = exampleData.createDirectedGraph();
List<Word> seedWords = exampleData.getSeedWords();
```

STEP 3:
- We call the method `propagate(...)` of the class `PolarityPropagation`, passing 
as parameters the graph `initialGraph` and the seed words `seedWords`.

```
// Applies the propagation algorithm over the directed graph
DirectedPseudograph<Word, LexicalRelation> finalGraph 
		= PolarityPropagation.propagate(initialGraph, seedWords);
```

OUTPUT:
- The graph shown in the above figure at the right hand side (the graph on the
<i>final state</i>). On this final graph the words were labeled automatically 
based on the different relations (synonyms and antonyms) represented on the graph. 
<br/>In this graph, the words:
- A, C, F end up labeled as positive;
- B, E, H, I end up labeled as negative;
- D, G end up with an ambiguous polarity (they are simultaneously positive and negative).

The full code can be seen in the file ExampleTinyDirectedGraph.java .


## Example - Propagating the «positive», «negative» and «neutral» polarity over a tiny undirected graph

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

## Example 3 - Propagating the «positive», «negative» and «neutral» polarity over an undirected graph

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