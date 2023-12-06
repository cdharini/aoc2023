package utils;

import java.util.HashMap;
import java.util.Map;

public class StringIntUtils {
    /*
    Takes a string with letters and numbers and adds the correct number representation in front of them
    eg. aeightwo9four7aaa -> a8eigh2two94four7aaa
     */
    public static String replaceWrittenDigitsWithNumbers(String i) {
         final Map<String, Integer> strNumToNum = Map.ofEntries(
                Map.entry("one", 1),
                Map.entry("two", 2),
                Map.entry("three", 3),
                Map.entry("four", 4),
                Map.entry("five", 5),
                Map.entry("six", 6),
                Map.entry("seven", 7),
                Map.entry("eight", 8),
                Map.entry("nine", 9)
        );
         Map<Integer, String> indexWithInteger = new HashMap<>();
        for (Map.Entry<String, Integer> e : strNumToNum.entrySet()) {
            int ind = i.indexOf( e.getKey());
            if (ind != -1 ) {
                indexWithInteger.put(ind, e.getKey());
            }
        }
        for (Map.Entry<Integer, String> e : indexWithInteger.entrySet()) {
            String toReplaceWith = strNumToNum.get(e.getValue()) + e.getValue();
            i = i.replace(e.getValue(), toReplaceWith);
        }
        return i;
    }
}
