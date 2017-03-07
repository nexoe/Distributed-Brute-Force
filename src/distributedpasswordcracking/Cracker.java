package distributedpasswordcracking;

import util.StringUtilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rasmu on 02-03-2017.
 */
public class Cracker implements Runnable{

    private MessageDigest messageDigest;
    private List<String> dictionary;
    private List<UserInfo> userInfos;
    private List<UserInfoClearText> results;

    public Cracker(List<String> dictionary, List<UserInfoClearText> results, List<UserInfo> userInfos){
        try{
            messageDigest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        this.dictionary = dictionary;
        this.userInfos = userInfos;
        this.results = results;
    }


    /**
     * Checks a single word from a dictionary, against a list of encrypted passwords.
     * Tries different variations on the dictionary entry, like all uppercase, adding digits to the end of the entry, etc.
     *
     * @param dictionaryEntry a single word from a dictionary, i.e. a possible password
     * @param userInfos a list of user information records: username + encrypted password
     */
    private List<UserInfoClearText> checkWordWithVariations(final String dictionaryEntry, final List<UserInfo> userInfos) {
        final List<UserInfoClearText> result = new ArrayList<UserInfoClearText>();

        final String possiblePassword = dictionaryEntry;
        final List<UserInfoClearText> partialResult = checkSingleWord(userInfos, possiblePassword);
        result.addAll(partialResult);

        final String possiblePasswordUpperCase = dictionaryEntry.toUpperCase();
        final List<UserInfoClearText> partialResultUpperCase = checkSingleWord(userInfos, possiblePasswordUpperCase);
        result.addAll(partialResultUpperCase);

        final String possiblePasswordCapitalized = StringUtilities.capitalize(dictionaryEntry);
        final List<UserInfoClearText> partialResultCapitalized  = checkSingleWord(userInfos, possiblePasswordCapitalized);
        result.addAll(partialResultCapitalized);

        final String possiblePasswordReverse = new StringBuilder(dictionaryEntry).reverse().toString();
        final List<UserInfoClearText> partialResultReverse = checkSingleWord(userInfos, possiblePasswordReverse);
        result.addAll(partialResultReverse);

        for (int i = 0; i < 100; i++) {
            final String possiblePasswordEndDigit = dictionaryEntry + i;
            final List<UserInfoClearText> partialResultEndDigit= checkSingleWord(userInfos, possiblePasswordEndDigit);
            result.addAll(partialResultEndDigit);
        }

        for (int i = 0; i < 100; i++) {
            final String possiblePasswordStartDigit = i + dictionaryEntry;
            final List<UserInfoClearText> partialResultStartDigit = checkSingleWord(userInfos, possiblePasswordStartDigit);
            result.addAll(partialResultStartDigit);
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 100; j++) {
                final String possiblePasswordStartEndDigit = i + dictionaryEntry + j;
                final List<UserInfoClearText> partialResultStartEndDigit = checkSingleWord(userInfos, possiblePasswordStartEndDigit);
                result.addAll(partialResultStartEndDigit);
            }
        }

        return result;
    }

    /**
     * Check a single  word (may include a single variation)from the dictionary against a list of encrypted passwords
     * @param userInfos a list of user information records: username + encrypted password
     * @param possiblePassword a single dictionary entry (may include a single variation)
     * @return the user information record, if the dictionary entry matches the users password, or {@code  null} if not.
     */
    private List<UserInfoClearText> checkSingleWord(final List<UserInfo> userInfos, final String possiblePassword) {
        final byte[] digest = messageDigest.digest(possiblePassword.getBytes());
        final List<UserInfoClearText> result = new ArrayList<UserInfoClearText>();
        for (UserInfo userInfo : userInfos) {
            if (Arrays.equals(userInfo.getEntryptedPassword(), digest)) {
                result.add(new UserInfoClearText(userInfo.getUsername(), possiblePassword));
                System.out.println(Thread.currentThread().getName() + result);
            }
        }
        return result;
    }

    @Override
    public void run() {
        for (int i = 0; i < dictionary.size()-1; i++) {
            if(dictionary.get(i).equals("")){
                break;
            }
            List<UserInfoClearText> partialresult = checkWordWithVariations(dictionary.get(i),userInfos);
            results.addAll(partialresult);
        }
    }
}
