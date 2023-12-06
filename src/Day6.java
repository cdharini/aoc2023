import utils.FileProcessing;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day6 {
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day6.txt";
        List<String> testLines = FileProcessing.readFile(testFileName);
        List<String> lines = FileProcessing.readFile(fileName);
        System.out.println(new Day6().part1(testLines));
        System.out.println(new Day6().part1(lines));
    }

    public long part1(List<String> lines) {
        long res = 1L;
        List<Integer> times = Arrays.stream(lines.get(0).substring(lines.get(0).indexOf(' ')).strip().split("\s+")).map(x -> Integer.parseInt(x)).collect(Collectors.toList());
        List<Integer> distances = Arrays.stream(lines.get(1).substring(lines.get(1).indexOf(' ')).strip().split("\s+")).map(x -> Integer.parseInt(x)).collect(Collectors.toList());

        for (int i = 0; i < times.size(); i++) {
            int raceTime = times.get(i);
            int raceDist = distances.get(i);
            int numWins = IntStream.rangeClosed(1, times.get(i) - 1).map(x -> x*(raceTime-x) > raceDist ? 1 : 0).reduce(Integer::sum).getAsInt();
            res = numWins > 0 ? res * numWins : res;
        }

        return res;
    }

    public long part2(List<String> lines) {
        Long time = Long.parseLong(lines.get(0).substring(lines.get(0).indexOf(' ')).replaceAll("\s+", ""));
        Long dist = Long.parseLong(lines.get(1).substring(lines.get(1).indexOf(' ')).replaceAll("\s+", ""));

        return LongStream.rangeClosed(1, time - 1).map(x -> x*(time-x) > dist ? 1 : 0).reduce(Long::sum).getAsLong();
    }

}
