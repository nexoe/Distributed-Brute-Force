package distributedpasswordcracking;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rasmu on 02-03-2017.
 */
public class DictionarySplitter {

    private FileReader fileReader = new FileReader("webster-dictionary-reduced.txt");
    private BufferedReader dictionary = new BufferedReader(fileReader);
    private ArrayList<String> dictionaryArray = new ArrayList<String>();

    public DictionarySplitter() throws FileNotFoundException {
        String entry = "";
        do {
            try {
                entry = dictionary.readLine();
                dictionaryArray.add(entry);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (entry != null);
    }


    /**
     * Splits the dictionary into specified amount of seperats lists
     * @param numberOfSplits number of lists wanted
     **/
    public ArrayList<List<String>> split(int numberOfSplits){
        ArrayList<List<String>> splits = new ArrayList<>();
        int linesPerSplit = dictionaryArray.size()/numberOfSplits;
        for (int i = 0; i < numberOfSplits; i++) {
            splits.add(dictionaryArray.subList((linesPerSplit*i),(linesPerSplit*(i+1))));
        }
        return splits;
    };




}