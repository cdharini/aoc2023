package solutions;

import utils.FileUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day2 {

    static Map<String, Integer> allowed = Map.of("red", 12, "green", 13, "blue", 14);
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day2.txt";
        System.out.println(part2(fileName));
    }

    public static long part1(String fileName) {
        long res = 0L;
        List<String> lines = FileUtils.readFile(fileName);
        for (String line : lines) {
            if (isPossible(line)) {
                res += getGameNum(line);
            }
        }
        return res;
    }

    public static long part2(String fileName) {
        long res = 0L;
        List<String> lines = FileUtils.readFile(fileName);
        for (String line : lines) {
                res += powerSet(line);
        }
        return res;
    }

    public static long powerSet(String line) {
        //eg: Game 9: 5 green, 8 blue, 8 red; 2 blue, 6 green, 8 red; 6 red, 9 green
        String[] turns = line.split(":|;");
        long res = 1L;
        Map<String, Integer> minColor = new HashMap<>(Map.of("red", 0, "blue", 0, "green", 0));
        // turn 1 to x is "5 green, 8 blue, 8 red", "2 blue, 6 green, 8 red", "6 red, 9 green"
        for (int i = 1; i < turns.length; i++) {
            String cur = turns[i].strip();
            String [] numsAndColor = cur.split(",");
            for (int j = 0; j < numsAndColor.length; j++) {
                // "5", "green"
                String[] numsCol = numsAndColor[j].strip().split(" ");
                int num = Integer.valueOf(numsCol[0]);
                if (num > minColor.get(numsCol[1])) {
                    minColor.put(numsCol[1], num);
                }
            }
        }
        for (Integer i : minColor.values()) {
            res = res* i;
        }
        return res;
    }

    public static boolean isPossible(String line) {
        String[] turns = line.split(":|;");
        for (int i = 1; i < turns.length; i++) {
            String cur = turns[i].strip();
            String [] numsAndColor = cur.split(",");
            for (int j = 0; j < numsAndColor.length; j++) {
                String[] numsCol = numsAndColor[j].strip().split(" ");
                int num = Integer.valueOf(numsCol[0]);
                if (num > allowed.get(numsCol[1])) {
                    return false;
                }
            }
        }
        return true;
    }
    public static int getGameNum(String line) {
        return Integer.valueOf(line.split(":| ")[1]);
    }
}
