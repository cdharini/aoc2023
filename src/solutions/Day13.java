package solutions;

import utils.FileUtils;

import java.util.List;

public class Day13 {
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day13.txt";
        List<String> testLines = FileUtils.readFile(testFileName);
        List<String> lines = FileUtils.readFile(fileName);
        System.out.println(new Day13().solvePart1(testLines));
        System.out.println(new Day13().solvePart1(lines));
    }

    public long solvePart1(List<String> lines) {
        long res = 0;
        return res;
    }

    public long processPattern(List<String> pattern) {
        long res = -1;
        return res;
    }
}
