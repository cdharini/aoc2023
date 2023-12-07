package solutions;

import utils.FileProcessing;
import utils.StringIntUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.println(day1(2));
    }

    public static long day1(int part) {
        List<String> input = FileProcessing.readFile("src/inputs/day1.txt");
        long res = 0L;
        int firstDigit, lastDigit;
        for (String i : input) {
            if (part != 1) {
                i = StringIntUtils.replaceWrittenDigitsWithNumbers(i);
            }
            firstDigit = getFirstNumber(i);
            lastDigit = getLastNumber(i);
            int num = firstDigit*10 + lastDigit;
            res += num;
        }
        return res;
    }

    public static int getFirstNumber(String a) {
        Pattern intOnly = Pattern.compile("\\d");
        Matcher makeMatch = intOnly.matcher(a);
        makeMatch.find();
        String inputInt = makeMatch.group();
        return Integer.valueOf(inputInt);
    }

    public static int getLastNumber(String a) {
        return getFirstNumber(new StringBuilder(a).reverse().toString());
    }

}
