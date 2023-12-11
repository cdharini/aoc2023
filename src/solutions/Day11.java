package solutions;

import utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day11 {
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day11.txt";
        List<String> testLines = FileUtils.readFile(testFileName);
        List<String> lines = FileUtils.readFile(fileName);
        int expansionVal_part1 = 1;
        int expansionval = 999999;
        System.out.println(new Day11().solve(testLines, expansionval));
        System.out.println(new Day11().solve(lines, expansionval));
    }

    public long solve(List<String> lines, int expansionVal) {
        long res = 0L;
        List<int[]> galaxies = new ArrayList<>();
        List<Integer> emptyRows = new ArrayList<>();
        List<Integer> emptyCols = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            boolean emptyRow = true;
            for (int j = 0; j < lines.get(i).length(); j++) {
                if (lines.get(i).charAt(j) == '#') {
                    emptyRow = false;
                    galaxies.add(new int[] {i,j});
                }
            }
            if (emptyRow) {
                emptyRows.add(i);
            }
        }
        for (int j = 0; j < lines.size(); j++) {
            boolean emptyCol = true;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).charAt(j) == '#') {
                    emptyCol = false;
                }
            }
            if (emptyCol) {
                emptyCols.add(j);
            }
        }

        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i+1; j < galaxies.size(); j++) {
                int[] stGal = galaxies.get(i);
                int[] endGal = galaxies.get(j);
                int normalDist = Math.abs(stGal[0] - endGal[0]) + Math.abs(stGal[1] - endGal[1]);
                res = res + normalDist + numEmptyRowsBet(stGal, endGal, emptyRows)*expansionVal+ expansionVal*numEmptyColsBet(stGal, endGal, emptyCols);
            }
        }
        return res;
    }

    public int numEmptyRowsBet(int[] stGal, int[] endGal, List<Integer> emptyRows) {
        if (stGal[0] < endGal[0])
            return emptyRows.stream().filter(s -> s > stGal[0] && s < endGal[0]).collect(Collectors.toList()).size();
        else if (stGal[0] > endGal[0])
            return emptyRows.stream().filter(s -> s > endGal[0] && s < stGal[0]).collect(Collectors.toList()).size();
        else
            return 0;
    }
    public int numEmptyColsBet(int[] stGal, int[] endGal, List<Integer> emptyCols) {
        if (stGal[1] < endGal[1])
            return emptyCols.stream().filter(s -> s > stGal[1] && s < endGal[1]).collect(Collectors.toList()).size();
        else if (stGal[1] > endGal[1])
            return emptyCols.stream().filter(s -> s > endGal[1] && s < stGal[1]).collect(Collectors.toList()).size();
        else
            return 0;
    }
}
