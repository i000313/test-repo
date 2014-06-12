
package pt.psantos.phd.polarity.propagation.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import pt.psantos.phd.polarity.propagation.algorithm.Word;

/**
 * Class for loading a file of seed words.
 * 
 * Example of a valid file:
 * <pre>{@code
 * bad, -1
 * god, 1
 * common, 0
 * # Lines starting by # are comments and are ignored. 
 * 
 * # The previous empty line is also ignored.
 * just,5.0
 * bastard,-5.0
 * normal,0
 * }
 * 
 * </pre>
 * 
 * @since 0.6.0
 * @version 0.6.0
 * @author PSantos
 */
public class SeedWordsLoader {

    /**
     * Return a list of seed words read from the file {@code file} passed as 
     * parameter.
     * 
     * @param file file with the seed words to load.
     * @param charsetName the charset name of the file or {@code null} for using
     * the default charset name.
     * @return the list of seed words red from file. This list can be empty but
     * is never null.
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    public static List<Word> load(File file, String charsetName) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        List<Word> seedWords = new ArrayList<Word>();

        charsetName = (charsetName == null 
                ? Charset.defaultCharset().name() : charsetName);
        
        Reader reader = new InputStreamReader(new FileInputStream(file), charsetName);
        BufferedReader br = new BufferedReader(reader);
        String line;
        String[] fields = null;

        // For each line
        while ((line = br.readLine()) != null) {
            // If the line is a comment or is empty 
            if (line.matches("^\\s{0,}(#|/\\*|//).{0,}") || line.trim().isEmpty()) {
                continue; // ignore this line
            }
            
            fields = line.split("(;|:|,|\\s)\\s{0,}");
            
            if(fields.length < 2) {
                continue; // ignore this line
            }
            
            String seedWord = fields[0].trim();
            float polarity = Float.valueOf(fields[1].trim());
            
            Word w = new Word(seedWord);
            
            if(polarity>0) {
                w.setAsPositiveSeed();
            } else if(polarity<0) {
                w.setAsNegativeSeed();
            } else if(polarity == 0) {
                w.setAsNeutralSeed();
            }
            
            seedWords.add(w);
        }
        
        return seedWords;
    }
}
