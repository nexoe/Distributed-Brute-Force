package distributedpasswordcracking;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rasmu on 02-03-2017.
 */
public class UsersSplitter {

    private final List<UserInfo> userInfos = PasswordFileHandler.readPasswordFile("passwords.txt");

    public UsersSplitter() throws IOException {
    }


    /**
     * Splits the dictionary into specified amount of seperats lists
     * @param numberOfSplits number of lists wanted
     **/
    public ArrayList<List<UserInfo>> split(int numberOfSplits){
        ArrayList<List<UserInfo>> splits = new ArrayList<>();
        int linesPerSplit = userInfos.size()/numberOfSplits;
        for (int i = 0; i < numberOfSplits; i++) {
            splits.add(userInfos.subList((linesPerSplit*i),(linesPerSplit*(i+1))));
        }
        return splits;
    };




}
