# Table of Contents
- [Introduction](#intro)
  - [Introductory Example](#intro-example)
- [Run the Application](#run-app")
 - [Requirements & Download](#requirements)
 - [Running in Command Line](#command-line)
  - [Example](#command-line-example)
 - [Running through the Java API](#java-api)
- [Other Examples](#other-examples)
 - [ExampleTinyDirectedGraph.java](#other-examples-example1)
 - [ExampleTinyUndirectedGraph.java](#other-examples-example2)
 - [ExamplePapel.java](#other-examples-example3)
- [References](#references)
- [License](#license)
 
# <a name="intro"></a>Introduction

This is an implementation in Java of the **polarity propagation algorithm** 
described on [[1]](#propor2012), section *"3. The Polarity Propagation Algorithm"*,
for classifying *words*, *multiwords* and *synsets* as `positive`, `negative` and 
`neutral` based on an **undirected graph**.

This implementation also allows running the algorithm described on [[2]](#epia2011),
section "*2.3 Simple and Intuitive Polarity Propagation - Algorithm*",
for classifying words as `positive` or `negative` based on a **direted graph**.


<br/>In both algorithms, the goal is to start with a small set of *words*, *multiwords*
or *synsets* manually classified, and automatically classify a bigger set of words as 
`positive`, `negative` or even `neutral`.
The set of words produced by these algorithms, may be useful, for example, 
for building a dictionary of sentiment words for [Sentiment Analysis / Opinion Mining](http://en.wikipedia.org/wiki/Sentiment_analysis).

### <a name="intro-example"></a>Introductory Example 

This example tries to explain the goal, the input, and output of the mentioned algorithms. 

Suppose that our goal is to classify a set of words as `positive`, `negative` and 
`neutral` with a few effort.

- **First**, we can collect the words that we want automatically classifying, 
  together with their *synonyms* and *antonyms*. We can collect this information from
  a *dictionary*, *thesaurus* or *wordnet*. 

  For this example, we collected a few words from online dictionaries. These words
  are saved in the file [examples/01-tiny-graph-english/graph-edges-utf8.txt](examples/01-tiny-graph-english/graph-edges-utf8.txt)
  and are shown on the following figure as an **undirect graph**.
  In this graph, a *dashed line* represents an **antonym** and a *solid line*
  represents a **synonym**. Note that this graph is a *disconnected graph*.

![tiny-undirected-graph-of-english-words](/docs/figures/ex01-undirected-graph-state-01.png)

- **Second**, we need to manually classify a set of words.

  For this example, we have classified the words **good** as `positive`, **bad** as `negative`,  
  and **common** as `neutral`. These words are saved on the file
  [examples/01-tiny-graph-english/seed-words-utf8.csv](examples/01-tiny-graph-english/seed-words-utf8.csv)
  and are shown in the following figure with a different color.

![tiny-undirected-graph-of-english-words](/docs/figures/ex01-undirected-graph-state-02.png)

- **Finally**, the **polarity propagation algorithm** can be applied to classify 
the words not yet classified. The result of this step is shown in the following graph.

![tiny-undirected-graph-of-english-words](/docs/figures/ex01-undirected-graph-state-03.png)

**As result**, we got a list of words classified as `positive` (green nodes), 
`negative` (red nodes), `neutral` (gray nodes), as shown on the above figure. 
This list of words is available in the file [examples/01-tiny-graph-english/dic-output.csv](examples/01-tiny-graph-english/dic-output.csv).

Some observations:
- In some cases we can end up with words classified also as `ambiguous` 
(words with more than one polarity at same time) and `unknown` (words with polarity not set). 
The latter happens with all nodes that are not connected to a classified node.
- Generally, the smaller the distance between a non seed words and the closest seed 
word greater the probability of that word being correctly classified.

# <a name="run-app"></a>Run the Application
 
This implementation can be run from the **command line** and through a **Java API**.
In both cases it is required as input:

- **A set of seed items**. This is an initial set of *words*, *multiwords*, or *synsets* 
classified as `positive` and `negative` (and optionally as `neutral`);
- **A set of relations between items**. This is set of **synonyms** and **antonymys** 
(optional) *words*, *multiwords* or *synsets* that we want to classify. These 
relations are used to build a graph.

The implementation will output:
- A list of items classified as `positive`, `negative`, `neutral`, `ambiguous`, and `unknown`.

## <a name="requirements"></a>Requirements & Download

### Requirements

For running this application the following software must be installed:
- `JRE (Java Runtime Environmen) 1.6` or higher;
- Or `JDK (Java Development Kit)` 1.6 or higher.

### Download

The last version of this implementation can be download by clicking on the link `release`
at the top of this page.

## <a name="command-line"></a>Running in Command Line

The application can be run from the command line, by running:

```
java -jar <path-to-the-application-jar> {mandatory-options} [optional-options]
```

**Options:**

| Option         | Type 	  | Description
| -------------- | ---------- | -----------
| -h             | optional   | help
| -s &lt;file_name&gt; | mandatory  | Seed words file.
| -g &lt;file_name&gt; | mandatory  | Graph file. Load it as an undirected graph.
| -u &lt;file_name&gt; | mandatory | Same as -g
| -d &lt;file_name&gt; | mandatory | Graph file. Load it as a directed graph.
| -o &lt;file_name&gt; | optional   | Output file.
| -e encoding    | optional   | Caracter encoding of all the files.

NOTE: options -g, -u and -s are are mutually exclusive. Specify just one. 

### <a name="command-line-example"></a>Example

This example, shows how to reproduce the previous example in the command line, to
classify a set of words as `positive`, `negative` and `neutral`. 

- First, we create a file with the word **good** classified as `positive`, **bad** 
classified as `negative`, and the word **common** classified as `neutral`. 
This file is available at [examples/01-tiny-graph-english/seed-words-utf8.csv](examples/01-tiny-graph-english/seed-words-utf8.csv) 
and its content is shown below:

```
good,1
bad,-1
common,0
```

- Then, we create a file of graph edges. A sample of this file is shown below.
The full file is available at [examples/01-tiny-graph-english/graph-edges-utf8.txt](examples/01-tiny-graph-english/graph-edges-utf8.txt). 

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

The result of the above command is a list of words classified with their polarity.
This list of words is saved as [examples/01-tiny-graph-english/dic-output.csv](examples/01-tiny-graph-english/dic-output.csv). 
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

# <a name="java-api"></a>Running through the Java API

The application can be called through the Java API. The previous example and previous
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

# <a name="other-examples"></a>Other Examples

### <a name="other-examples-example1"></a>ExampleTinyDirectedGraph.java 

This example shows how to propagate the `positive` and `negative` over a tiny 
**directed graph** of synonyms and antonymys. This graph is shown below. In this
graph **A** is classified as `positive` and **B** as `negative` (graph shown on the *initial state*).

![tiny-directed-graph-of-letters-epia](/docs/figures/tiny-directed-graph-of-letters-epia.png)

After applying a propagation algorithm, we get the graph shown on its *final state*.
In this graph:
- `A, C, F` are classified as `positive`;
- `B, E, H, I` are classified as `negative`;
- `D, G` are classified as `ambiguous` (they are simultaneously `positive` and `negative`).

The full Java code to reproduce this is example is available on
[ExampleTinyDirectedGraph.java](src/pt/psantos/phd/polarity/propagation/examples/ExampleTinyDirectedGraph.java#L149).

This example is described in [[1]](#epia2011), on section **2.3 Intuitive and Simple 
Polarity Propagation - Algorithm**.

### <a name="other-examples-example2"></a>ExampleTinyUndirectedGraph.java

This example shows how to propagate the `positive`, `negative` and `neutral` 
polarity over a tiny **undirected graph** of synonyms. This graph is shown below. 
In this graph `0, 1` and `2` are classified as `positive`, `negative` and `neutral` 
respectively (graph shown on the *initial state*).

![tiny-undirected-graph-of-numbers-propor](/docs/figures/tiny-undirected-graph-of-numbers-propor.png)

After applying a propagation algorithm, we get the graph shown on its *final state*.
In this graph:
- `0, 6, 8, 9, 10` are classified as `positive`;
- `1, 4` are classified as `negative`;
- `2, 3` are classified as `neutral`;
- `5, 7` are classified as `ambiguous` (they are simultaneously positive and negative);
- `11, 12` end up with `no polarity`.

The full Java code to reproduce this is example is available on
[ExampleTinyUndirectedGraph](src/pt/psantos/phd/polarity/propagation/examples/ExampleTinyUndirectedGraph.java).

This example is described in [[2]](#propor2012), on section **3 The Polarity Propagation Algorithm**.

## <a name="other-examples-example3"></a>ExamplePapel.java

The full Java code to reproduce this is example is available on
[ExamplePapel](src/pt/psantos/phd/polarity/propagation/examples/ExamplePapel.java).

This example shows how to create an **undirect graph of synonyms** for Portuguese 
adjectives from [PAPEL](http://www.linguateca.pt/PAPEL/).

# <a name="references"></a>References

1. <a name="propor2012"></a>Santos, A. P., Gonçalo Oliveira, H., Ramos, C., & Marques, N. C. (2012). 
[A Bootstrapping Algorithm for Learning the Polarity of Words](http://www.psantos.com.pt/files/trabalhos-academicos/publicacoes/2012PROPOR_bootstrappingAlgorithm.pdf). 
In Proceedings of 10th International Conference - Computational Processing of the Portuguese 
Language (PROPOR 2012), volume 7243 of LNCS, pp. 229-234. Coimbra, Portugal. 
Springer.

2. <a name="epia2011"></a>Santos, A. P., Ramos, C., & Marques, N. C. (2011). 
[Determining the Polarity of Words through a Common Online Dictionary](http://www.psantos.com.pt/files/trabalhos-academicos/publicacoes/2011EPIA_polarityOfWordsThroughCommonDictionary.pdf). 
In Proceedings of 15th Portuguese Conference on Artificial on Artificial intelligence 
(EPIA 2011), volume 7026 of LNCS, pp. 649-663. Lisbon, Portugal. Springer.


# <a name="license"></a>License
- [Licence](LICENSE)
