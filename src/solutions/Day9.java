package solutions;

import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 {
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day9.txt";
        List<String> testLines = FileUtils.readFile(testFileName);
        List<String> lines = FileUtils.readFile(fileName);
        System.out.println(new Day9().solve(testLines, 1));
        System.out.println(new Day9().solve(lines, 1));
    }

    public long solve(List<String> lines, int part) {
        long res = 0L;
        for (String l: lines) {
            List<Integer> nums = Arrays.stream(l.split("\s+")).map(x -> Integer.parseInt(x)).collect(Collectors.toList());
            if (part == 2)
                res +=  nums.get(0) - findNextNum(nums, 2);
            else
                res += findNextNum(nums, 1) + nums.get(nums.size() - 1);
        }
        return res;
    }

    public int findNextNum(List<Integer> prevNums, int part) {
        boolean isNotAllZeros = false;
        List<Integer> curNums = new ArrayList<>();
        for (int i = 1; i < prevNums.size(); i++) {
            int nextNum = prevNums.get(i) - prevNums.get(i-1);
            curNums.add(nextNum);
            if (nextNum != 0) {
                isNotAllZeros = true;
            }
        }

        //base case
        if (!isNotAllZeros) {
            return 0;
        }

        int toAdd = findNextNum(curNums, part);
        if (part == 1)
            return toAdd + curNums.get(curNums.size() - 1);
        else
            return curNums.get(0) - toAdd;
    }
}
