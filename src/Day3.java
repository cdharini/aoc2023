import utils.FileProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day3 {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day3.txt";
        System.out.println(new Day3().part2(testFileName));
        System.out.println(new Day3().part2(fileName));
    }

    class Pos {
        int x;
        int y;
        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object p) {
            if (p == this) return true;
            if (!(p instanceof  Pos)) return false;
            return ((Pos) p).x == this.x && ((Pos) p).y == this.y;
        }
        @Override
        public int hashCode() {
            String a = x + "-" + y;
            return a.hashCode();
        }
    }

    public int part2(String fileName) {
        int res = 0;
        List<String> lines = FileProcessing.readFile(fileName);
        Map<Pos, List<Integer>> starsByNumsNearBy = new HashMap<>();


        for (int i = 0; i < lines.size(); i++) {
            Map<String, List<Integer>> numToIndex = getNumbersFromString(lines.get(i));
            String prevLine = (i == 0) ? null:lines.get(i-1);
            String nextLine = (i==(lines.size() - 1)) ? null:lines.get(i+1);
            for (Map.Entry<String, List<Integer>> e : numToIndex.entrySet()) {
                for (Integer pos : e.getValue()) {
                    Set<Pos> starPosList = getStarPos(prevLine, lines.get(i), nextLine, pos, pos + e.getKey().length() - 1, i);
                   if (!starPosList.isEmpty()) {
                       for (Pos starPos : starPosList) {
                           List<Integer> numl = starsByNumsNearBy.getOrDefault(starPos, new ArrayList<>());
                           numl.add(Integer.valueOf(e.getKey()));
                           starsByNumsNearBy.put(starPos, numl);
                       }
                   }
                }
            }
        }
        for (Map.Entry<Pos, List<Integer>> e : starsByNumsNearBy.entrySet()) {
            if (e.getValue().size() == 2) {
                res += e.getValue().get(0)*e.getValue().get(1);
            }
        }
        return res;
    }

    public Set<Pos> getStarPos(String prevLine, String curLine, String nextLine, int stIndex, int endIndex, int curRow) {
        Set<Pos> starPos = new HashSet<>();
        if (stIndex != 0) {
            stIndex = stIndex - 1;
        }
        if (endIndex < curLine.length() - 1) {
            endIndex = endIndex + 1;
        }
        //check curLine
        if (curLine.charAt(stIndex)== '*') {
            starPos.add(new Pos( curRow, stIndex));
        }

        if (curLine.charAt(endIndex) == '*') {
            starPos.add(new Pos(curRow, endIndex));
        }

        //check prevLine
        if (prevLine != null) {
            addStars(prevLine, stIndex, endIndex + 1, curRow - 1, starPos);
        }

        //checkNextLine
        if (nextLine != null) {
            addStars(nextLine, stIndex, endIndex + 1, curRow + 1, starPos);
        }
        return starPos;
    }

    public void addStars(String a, int st, int end, int x, Set<Pos> starPos) {
        for (int i = st; i < end; i++) {
            if (a.charAt(i) == '*') {
                starPos.add(new Pos(x,i));
            }
        }
    }

    public static int part1(String fileName) {
        List<String> lines = FileProcessing.readFile(fileName);
        List<Integer> partSet = new ArrayList<Integer>();

        for (int i = 0; i < lines.size(); i++) {
            Map<String, List<Integer>> numToIndex = getNumbersFromString(lines.get(i));
            String prevLine = (i == 0) ? null:lines.get(i-1);
            String nextLine = (i==(lines.size() - 1)) ? null:lines.get(i+1);
            for (Map.Entry<String, List<Integer>> e : numToIndex.entrySet()) {
                for (Integer pos : e.getValue()) {
                    if (isValidPartNum(prevLine, lines.get(i), nextLine, pos, pos + e.getKey().length() - 1)) {
                        partSet.add(Integer.valueOf(e.getKey()));
                    } else {
                        System.out.println(e.getKey() + " is not a valid part number at line " + i);
                    }
                }
            }
        }
        return partSet.stream().reduce(Integer::sum).get();
    }

    public static boolean isValidPartNum(String prevLine, String curLine, String nextLine, int stIndex, int endIndex) {
        if (stIndex != 0) {
            stIndex = stIndex - 1;
        }
        if (endIndex < curLine.length() - 1) {
            endIndex = endIndex + 1;
        }
        //check curLine
            if (isCharSymbol(curLine.charAt(stIndex))) {
                return true;
            }

            if (isCharSymbol(curLine.charAt(endIndex))) {
                return true;
            }

        //check prevLine
        if (prevLine != null) {
            if (containsSymbol(prevLine.substring(stIndex, endIndex + 1))) {
                return true;
            }
        }

        //checkNextLine
        if (nextLine != null) {
            if (containsSymbol(nextLine.substring(stIndex, endIndex + 1))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCharSymbol(Character a) {
        if (!a.equals('.') && !Character.isDigit(a)) {
            return true;
        }
        return false;
    }

    public static boolean containsSymbol(String a) {
        for (Character aChar: a.toCharArray()) {
            if(isCharSymbol(aChar)) {
                return true;
            }
        }
        return false;
    }

    public static Map<String, List<Integer>> getNumbersFromString(String line) {
        // 23....401...425.323..111
        int i = 0;
        Map<String, List<Integer>> resMap = new HashMap<>();
        int index = 0;
        while (i < line.length()) {
            StringBuilder curNum = new StringBuilder();
            index = i;
            if (Character.isDigit(line.charAt(i))) {
              while(i < line.length() && Character.isDigit(line.charAt(i))) {
                  curNum.append(line.charAt(i));
                  i++;
              }
            } else {
                i++;
            }
            if (!curNum.isEmpty()) {
                List<Integer> posList = resMap.getOrDefault(curNum.toString(), new ArrayList<>());
                posList.add(index);
                resMap.put(curNum.toString(), posList);
            }
        }
        return resMap;
    }
}
