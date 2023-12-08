package solutions;

import utils.FileUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day6 {
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day6.txt";
        List<String> testLines = FileUtils.readFile(testFileName);
        List<String> lines = FileUtils.readFile(fileName);
        System.out.println(new Day6().part1QuadraticFormula(testLines));
        System.out.println(new Day6().part1QuadraticFormula(lines));
        System.out.println(new Day6().part2QuadraticFormula(testLines));
        System.out.println(new Day6().part2QuadraticFormula(lines));
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


    public double part2QuadraticFormula(List<String> lines) {
        Long time = Long.parseLong(lines.get(0).substring(lines.get(0).indexOf(' ')).replaceAll("\s+", ""));
        Long dist = Long.parseLong(lines.get(1).substring(lines.get(1).indexOf(' ')).replaceAll("\s+", ""));

        return solveQuadraticForm(time, dist);
    }

    /*
        We're trying to solve for all x where
        distance < x * (time - x) and add them up. This is a quadratic equation of the form -
        x^2 - time*x + distance < 0. So solve for x using the formula
        x = (-b +/- sqrt(b^2 - 4*a*c)) / 2. The two values are the start and end of the range that can win the race
    */
    public double solveQuadraticForm(Long time, Long dist) {
        double sqrtTerm = Math.sqrt(time*time - 4*dist);
        double end = Math.floor((time + sqrtTerm)/2);
        double st = Math.ceil((time - sqrtTerm)/2);

        return end - st + 1;
    }
    public double part1QuadraticFormula(List<String> lines) {
        double res = 1L;
        List<Integer> times = Arrays.stream(lines.get(0).substring(lines.get(0).indexOf(' ')).strip().split("\s+")).map(x -> Integer.parseInt(x)).collect(Collectors.toList());
        List<Integer> distances = Arrays.stream(lines.get(1).substring(lines.get(1).indexOf(' ')).strip().split("\s+")).map(x -> Integer.parseInt(x)).collect(Collectors.toList());

        for (int i = 0; i < times.size(); i++) {
            double curRes = solveQuadraticForm(times.get(i).longValue(), distances.get(i).longValue() + 1);
            res = curRes == 0 ? res : res*curRes;
        }
        return res;
    }

}
