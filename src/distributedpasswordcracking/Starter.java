package distributedpasswordcracking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rasmu on 02-03-2017.
 */
public class Starter {

    private static ArrayList<Thread> threads = new ArrayList<>();
    private static List<UserInfoClearText> result = Collections.synchronizedList(new ArrayList<>());
    private static List<UserInfo> userlist;

    public static void main(String[] args) throws IOException {
        int numberOfThreads = 4;
        ArrayList<List<String>> dictionaryLists =  new DictionarySplitter("webster-dictionary.txt").split(numberOfThreads);
        userlist = PasswordFileHandler.readPasswordFile("passwords.txt");

        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(new Cracker(dictionaryLists.get(i),result,userlist)));
        }

        System.out.println("Starting cracking");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).start();
        }


        while(isThreadsAlive() == true){
            //Wait until all threads are done
        }


        long endtime = System.currentTimeMillis();
        System.out.println(result);
        long  usedTime = endtime - startTime;
        System.out.println("Used time: " + usedTime / 1000 + " seconds = " + usedTime / 60000.0 + " minutes");
        System.out.println("Users found: " + result.size());

    }

    private static boolean isThreadsAlive(){
        boolean isAlive = false;
        for (int i = 0; i < threads.size(); i++) {
            if(threads.get(i).isAlive()){
                isAlive = true;
            }
        }
        return isAlive;
    }


}
