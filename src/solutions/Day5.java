package solutions;

import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day5 {
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day5.txt";
        System.out.println(new Day5().part1(testFileName));
        System.out.println(new Day5().part1(fileName));
    }

    class SrcDestMaps {
        long src, dst, range;
        public SrcDestMaps(String[] nums) {
            this.dst = Long.parseLong(nums[0]);
            this.src = Long.parseLong(nums[1]);
            this.range = Long.parseLong(nums[2]);
        }
    }

    public Long part1(String fileName) {
        List<String> lines = FileUtils.readFile(fileName);
        List<Long> seedNums = Arrays.stream(lines.get(0).substring(lines.get(0).indexOf(' ')).strip().split(" ")).map(x -> Long.parseLong(x)).collect(Collectors.toList());
        List<List<SrcDestMaps>> mutationMaps = parseIntoMaps(lines);
        return getFinalRes(seedNums.stream(), mutationMaps);
    }

    public long part2(String fileName) {
        Long res = Long.MAX_VALUE;
        List<String> lines = FileUtils.readFile(fileName);
        List<List<SrcDestMaps>> mutationMaps = parseIntoMaps(lines);

        String[] seedNumsStr = lines.get(0).substring(lines.get(0).indexOf(' ')).strip().split(" ");
        List<long []> intervals = new ArrayList<>();
        for (int i = 0; i < seedNumsStr.length - 1; i+= 2) {
            long [] pair = new long[2];
            pair[0] = Long.parseLong(seedNumsStr[i]);
            pair[1] = Long.parseLong(seedNumsStr[i+1])+pair[0] - 1;
            intervals.add(pair);
        }

        List<long []> merged = mergeIntervals(intervals);
        for (long [] interv : merged) {
            res = Math.min(res, getFinalRes(Stream.iterate(interv[0], (x -> x + 1)).limit(interv[1] - interv[0] + 1), mutationMaps));
        }
        return res;
    }

    List<long []> mergeIntervals(List<long []> intervals) {
        List<long []> res = new ArrayList<>();
        //sort by start and then end
        intervals.sort( (a , b) -> a[0] == b[0] ? Long.compare(a[1], b[1]) : Long.compare(a[0], b[0]));

        long curSt = intervals.get(0)[0];
        long curEnd = intervals.get(0)[1];
        for (int i = 1; i < intervals.size(); i++) {
             long nextSt = intervals.get(i)[0];
             long nextEnd = intervals.get(i)[1];
             if (nextSt > curEnd) {
                 //finish and write cur int
                 res.add(new long[]{curSt, curEnd});
                 curSt = nextSt;
                 curEnd = nextEnd;
             } else {
                 curEnd = Math.max(curEnd, nextEnd);
             }
        }
        res.add(new long[]{curSt, curEnd});
        return res;
    }
    public long getFinalRes(Stream<Long> seedNums, List<List<SrcDestMaps>> mutationMaps) {
        return seedNums.map(x -> calcSeedLocation(x, mutationMaps)).reduce(Long::min).get();
    }

    public long calcSeedLocation(long curSeedNum, List<List<SrcDestMaps>> srcDstMapList) {
        long res = curSeedNum;
          for (List<SrcDestMaps> curMapping : srcDstMapList) {
            res = calcNextNum(res, curMapping);
        }
        return res;
    }

    public long calcNextNum(long curNum, List<SrcDestMaps> curMapping) {
        long res = curNum;
        for (SrcDestMaps sdm : curMapping) {
            if (res >= sdm.src && res < (sdm.src + sdm.range)) {
                return sdm.dst + res - sdm.src;
            }
        }
        return res;
    }
    public List<List<SrcDestMaps>> parseIntoMaps(List<String> lines) {
        List<List<SrcDestMaps>> res = new ArrayList<>(7);
        String curLine;
        for (int i = 2; i < lines.size(); i++) {
            curLine = lines.get(i).strip();
            if (Character.isLetter(curLine.charAt(0))) {
                i++;
                curLine = lines.get(i).strip();
                List<SrcDestMaps> curMap = new ArrayList<>();
                while(!curLine.isBlank()) {
                    String[] nums = curLine.split(" ");
                    curMap.add(new SrcDestMaps(nums));
                    i++;
                    if (i < lines.size()) {
                        curLine = lines.get(i).strip();
                    } else {
                        break;
                    }
                }
                res.add(curMap);
            }
        }
        return res;
    }
}
