
package pt.psantos.phd.polarity.propagation.algorithm;

import java.io.File;
import java.util.List;
import org.jgrapht.graph.SimpleGraph;
import pt.psantos.phd.polarity.propagation.loaders.PapelLoader;
import pt.psantos.phd.polarity.propagation.loaders.SeedWordsLoader;
import pt.psantos.phd.polarity.propagation.outputers.CsvOutput;

/**
 * Class for running the application from the command line.
 * 
 * Example:
 * {@code java -jar polarity-propagation-x.x.x-jar -seed file_name -graph file_name}
 * 
 * @since 0.6.0
 * @version 0.6.0
 * @author PSantos
 */
public class Run {

    /** Mandatory command line arguments */
    private static File seedWordsFile = null;
    private static File graphFile = null;
    /** Optional command line arguments */
    private static File outpFile = null;
    private static String encoding = null; // Encoding for all the files
    
    public static void main(String[] args) throws Exception {
        parseCommandLine(args);
        
        // If the command line is not ok
        if(!isCommandLineOk()) {
            // Return 1. 
            // By convention, a nonzero status code indicates abnormal termination.
            System.exit(1); 
        }
        System.out.println(seedWordsFile);
        System.out.println(graphFile);
        System.out.println(outpFile);
        System.out.println(encoding);
        
        // Reads the graph from file
        PapelLoader loader = new PapelLoader();
        SimpleGraph<Word, LexicalRelation> graph = loader.load(graphFile, encoding);
        List<Word> seedWords = SeedWordsLoader.load(seedWordsFile, encoding);
        
        //PolarityUtils.printGraph(graph);
        
        SimpleGraph<Word, LexicalRelation> finalGraph 
                = PolarityPropagation.propagate(graph, seedWords);
        
        if(outpFile == null) {
            outpFile = new File(graphFile.getParentFile(), "dic-output.csv");
        }
        
        CsvOutput csv = new CsvOutput(outpFile, encoding);
        csv.write(finalGraph);
        
        System.out.println("Output file: " + outpFile.getAbsolutePath());
    }
    
    
    
  /**
   * Parse command line options.
   */
  private static void parseCommandLine(String[] args) throws Exception {

    // iterate over all options (arguments starting with '-')
    for(int i = 0; i < args.length && args[i].charAt(0) == '-'; i++) {
      switch(args[i].charAt(1)) {

        // -e encoding = character encoding for all files
        case 'e':
          Run. encoding = args[++i];
          break;

        // -g file_name = name of the file containing the graph
        case 'g':
          Run.graphFile = new File(args[++i]);
          break;

        // -h = help
        case 'h':
          comandLineUsage();
          break;

        // -o file_name = name of the file to output the final list of words
        case 'o':
          Run.seedWordsFile = new File(args[++i]);
          break;

        // -s file_name = name of the file containing the list of seed words.
        case 's':
          Run.seedWordsFile = new File(args[++i]);
          break;

            
        default:
          System.err.println("Unrecognised option " + args[i]);
          comandLineUsage();
      }
    }
  }
  private static boolean isCommandLineOk() {
      
      boolean ok = true;
      
      // Check the seed words file name
      if(Run.seedWordsFile == null) {
          System.err.println("Enter the seed words file name!");
          ok = false;
      } else if (!Run.seedWordsFile.exists()) {
          System.err.println("File not found: " + Run.seedWordsFile.getAbsolutePath());
          ok = false;
      } 
      
      // Check the graph file name
      if(Run.graphFile == null) {
          System.err.println("Enter the graph file name!");
          ok = false;
      } else if (!Run.graphFile.exists()) {
          System.err.println("File not found: " + Run.graphFile.getAbsolutePath());
          ok = false;
      }
      
      if(!ok) {
          comandLineUsage();
      }
      
      return ok;
  }
  
  private static void comandLineUsage() {
      System.out.println("USAGE: java -jar polarity-propagation-x.x.x-jar -seeds file_name "
              + "-graph file_name [-output file_name ] [-encoding encoding_name]");
  }
    
}
