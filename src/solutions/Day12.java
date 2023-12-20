
package solutions;

import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 {
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day12.txt";
        List<String> testLines = FileUtils.readFile(testFileName);
        List<String> lines = FileUtils.readFile(fileName);
        System.out.println(new Day12().solvePart2(testLines));
        System.out.println(new Day12().solvePart2(lines));
    }

    public long solvePart2(List<String> lines) {
        long res = 0;
        for (String l : lines)
            res += getCombinationsPart2(l);
        return res;
    }

    public int solvePart1(List<String> lines) {
        int res = 0;
        for (String l : lines)
            res += getCombinations(l);
        return res;
    }

    public int getCombinations(String line) {
        String pattern = line.split(" ")[0];
        List<Integer> nums = Arrays.stream(line.split(" ")[1].split(",")).map(x -> Integer.parseInt(x)).collect(Collectors.toList());
       return getCombinationsRec(pattern, 0,nums);
    }

    public long getCombinationsPart2(String line) {
        String p = line.split(" ")[0];
        List<Integer> n = Arrays.stream(line.split(" ")[1].split(",")).map(x -> Integer.parseInt(x)).collect(Collectors.toList());
        String pattern = p + "?" + p + "?" + p + "?" + p + "?" + p;
        List<Integer> nums = Stream.of(n, n, n, n, n).flatMap(Collection::stream).toList();
        return getCombinationsRecPart2(pattern, nums, 0, 0, new HashMap<>());
    }

    record IndexKey(int x, int y) {}

    public long getCombinationsRecPart2(String pattern, List<Integer> nums, int nextPatInd, int nextNumInd, HashMap<IndexKey, Long> memo) {
        IndexKey indexKey = new IndexKey(nextPatInd, nextNumInd);
        if (memo.containsKey(indexKey)) return memo.get(indexKey);
        long res = 0L;
        if (nextNumInd >= nums.size()) {
            return pattern.substring(nextPatInd).contains("#") ? 0 : 1;
        } else if (nextPatInd >= pattern.length()) {
            return 0;
        }
        if (nextPatInd != 0) {
            if (pattern.charAt(nextPatInd) == '#')
                return 0; // we just finished a pattern so the next one cannot be a spring
            else
                nextPatInd++;
        }
        //skip all the '.'
        while(nextPatInd < pattern.length() - 1 && pattern.charAt(nextPatInd) == '.')
            nextPatInd++;

        int numSprings = nums.get(nextNumInd); // num of springs to check for
        for (int i = nextPatInd; i < pattern.length(); i++) {
            if (i + numSprings > pattern.length())
                break;
            boolean matches = true;
            for (int j = i; j < i + numSprings; j++) {
                if (pattern.charAt(j) == '.') {
                    matches = false;
                }
                if (!matches) {
                    break;
                }
            }
            if (matches) {
                res += getCombinationsRecPart2(pattern, nums, i + numSprings, nextNumInd + 1, memo);
            }
            if (pattern.charAt(i) == '#') {
                break;
            }
        }
        memo.put(indexKey, res);
        return res;
    }

    public int getCombinationsRec(String pattern, int curInd, List<Integer> nums) {
        int res = 0;
        //base case
        if (curInd >= pattern.length()) {
            if (checkPattern(pattern.toCharArray(), nums)) {
                return 1;
            }
            else
                return 0;
        }
        if (pattern.charAt(curInd) != '?') {
            res = getCombinationsRec(pattern, curInd + 1, nums);
            return res;
        }
        //recurse
        StringBuilder pattern1 = new StringBuilder(pattern);
        pattern1.setCharAt(curInd, '#');
        StringBuilder pattern2 = new StringBuilder(pattern);
        pattern2.setCharAt(curInd, '.');
        res = getCombinationsRec(pattern1.toString(), curInd + 1, nums) + getCombinationsRec(pattern2.toString(), curInd + 1, nums);
        return res;
    }

    public boolean checkPattern(char [] pattern, List<Integer> nums) {
        int patternInd = 0, numInd = 0;
        int hashcount = 0;
        while(patternInd < pattern.length) {
            if (pattern[patternInd] == '#') {
                hashcount++;
                patternInd++;
            } else {
                if (numInd >= nums.size()) {
                    return false;
                } else if (hashcount != 0 && nums.get(numInd) != hashcount) {
                    return false;
                } else if (hashcount != 0 && nums.get(numInd) == hashcount){
                    numInd++;
                    hashcount = 0;
                }
                while(patternInd < pattern.length && pattern[patternInd] == '.') {
                    patternInd++;
                }
            }
        }
        if (hashcount!= 0) {
            if(numInd >= nums.size() || nums.get(numInd) != hashcount)
                return false;
            else {
                numInd++;
                hashcount = 0;
            }
        }
        //check for last trailing num and hashcount one last time
        if (numInd < nums.size() || patternInd < pattern.length)
            return false;
        return true;
    }
}
