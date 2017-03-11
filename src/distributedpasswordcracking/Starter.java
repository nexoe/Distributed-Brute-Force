package distributedpasswordcracking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by rasmu on 02-03-2017.
 */
public class Starter {

    private static ArrayList<Thread> threads = new ArrayList<>();
    private static List<UserInfoClearText> result = Collections.synchronizedList(new ArrayList<>());
    private static List<UserInfo> userlist;

    public static void main(String[] args) throws IOException {
        System.out.println("How many threads do you wish to use");
        Scanner textscan = new Scanner(System.in);
        //Man skriver antallet af Threads man vil bruge.
        int numberOfThreads = textscan.nextInt();

        //Splitter dictionary op i lige store dele, ud fra hvor mange threads der er.
        ArrayList<List<String>> dictionaryLists =  new DictionarySplitter("webster-dictionary.txt").split(numberOfThreads);
        userlist = PasswordFileHandler.readPasswordFile("passwords.txt");

        //En for løkke der laver antal af tråde, som er blevet givet i consollen. Hver tråd får Cracker() med som parameter.
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(new Cracker(dictionaryLists.get(i),result,userlist)));
        }

        System.out.println("Cracking with " + numberOfThreads + " threads");

        //Tager en måling på tiden inden start.
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).start();
        }

        while(isThreadsAlive() == true){
            //Wait until all threads are done
        }


        //Tager en måling på tiden efter cracking er gennemført.
        long endtime = System.currentTimeMillis();
        System.out.println(result);
        //Udregner tiden det har taget at cracke passwords.
        long  usedTime = endtime - startTime;
        System.out.println("Used time: " + usedTime / 1000 + " seconds = " + usedTime / 60000.0 + " minutes");
        System.out.println("Users found: " + result.size());

    }

    //Retunerer enten true eller false alt efter Threadens status.
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
