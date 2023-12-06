import utils.FileProcessing;
import utils.StringIntUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day4 {
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day4.txt";
        System.out.println(new Day4().part2(testFileName));
        System.out.println(new Day4().part2(fileName));
    }

    public int part2(String fileName) {
        Map<Integer, Integer> cardToCopyNum = new HashMap<>();
        List<String> lines = FileProcessing.readFile(fileName);
        // initialize card to numbers map
        for (int i = 1; i <= lines.size(); i++) {
            cardToCopyNum.put(i, 1);
        }

        for (int i = 0; i < lines.size(); i++) {
            int curLineNum = i + 1;
            int curCardCount = cardToCopyNum.get(curLineNum);
            String l = lines.get(i);
            l = l.split(":")[1].strip();
            l = l.replace("|", "&");
            String[] splitStr = l.split("&");
            Set<Integer> myNums = getNums(splitStr[1]);
            Set<Integer> winNums = getNums(splitStr[0]);
            int matches = getMatches(winNums, myNums);
            for (int j = 1; j <= matches; j++) {
                cardToCopyNum.put(curLineNum + j, cardToCopyNum.get(curLineNum + j) + curCardCount);
            }
        }
        return cardToCopyNum.values().stream().reduce(Integer::sum).get();
    }
    public int part1(String fileName) {
        int res = 0;
        List<String> lines = FileProcessing.readFile(fileName);

        for (String l : lines) {
            l = l.split(":")[1].strip();
            l = l.replace("|", "&");
            String[] splitStr = l.split("&");
            Set<Integer> myNums = getNums(splitStr[1]);
            Set<Integer> winNums = getNums(splitStr[0]);
            res += getPoints(winNums, myNums);
        }


        return res;
    }

    public int getMatches(Set<Integer> winNums, Set<Integer> myNums) {
        int pts = 0;
        for (Integer i : winNums) {
            if (myNums.contains(i)) {
                pts++;
            }
        }
        return pts;
    }

    public int getPoints(Set<Integer> winNums, Set<Integer> myNums) {
        int pts = 0;
        for (Integer i : winNums) {
            if (myNums.contains(i)) {
                if (pts == 0) {
                    pts = 1;
                } else {
                    pts *= 2;
                }
            }
        }
        return pts;
    }

    public Set<Integer> getNums(String a) {
        a = a.strip();
        String [] numsStr = a.split("\s+");
        Set<Integer> res = new HashSet<>();
        for (String s : numsStr) {
            res.add(Integer.parseInt(s));
        }
        return res;
    }
}
