package solutions;

import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day15 {
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day15.txt";
        List<String> testLines = FileUtils.readFile(testFileName);
        List<String> lines = FileUtils.readFile(fileName);
        System.out.println(new Day15().solvePart2(testLines));
        System.out.println(new Day15().solvePart2(lines));
    }

    public int solvePart1(List<String> lines) {
        String[] steps = lines.get(0).strip().split(",");
        return Arrays.stream(steps).map(x -> getHashCode(x)).reduce(Integer::sum).get();
    }
    class Lens {
        String name;
        int focalLen;

        public Lens(String n, int fl) {
            name = n;
            focalLen = fl;
        }

        @Override
        public boolean equals(Object obj) {
            Lens b = (Lens) obj;
            return this.name.equals(b.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
    public long solvePart2(List<String> lines) {
        long res = 0;
        String[] steps = lines.get(0).strip().split(",");
        List<List<Lens>> boxes = new ArrayList<>(256);
        for (int i = 0; i < 256; i++) {
            boxes.add(new ArrayList<>());
        }
        for (String step : steps) {
            //parse the step into op, focal length, lens name
            String [] parts = step.split("=");
            String lensName;
            char operation;
            int focalLen = 0;
            if (parts.length == 2) {
                lensName = parts[0];
                operation = '=';
                focalLen = Integer.parseInt(parts[1]);
            } else {
                lensName = parts[0].substring(0, parts[0].length() - 1);
                operation = '-';
            }
            //calc hash of lens name
            int boxNum = getHashCode(lensName);
            Lens curLens = new Lens(lensName, focalLen);
            //execute op i.e put or remove from slot
            switch(operation) {
                case '=':
                    int ind = boxes.get(boxNum).indexOf(curLens);
                    if (ind >= 0) {
                        boxes.get(boxNum).set(ind, curLens);
                    } else {
                        boxes.get(boxNum).add(curLens);
                    }
                    break;
                case '-':
                    boxes.get(boxNum).remove(curLens);
                    break;
            }
        }
        //calculate the result
        for (int i = 0; i < boxes.size(); i++) {
            List<Lens> box = boxes.get(i);
            for (int j = 0; j < box.size(); j++) {
                Lens l = box.get(j);
                res += (i+1)*(j+1)*l.focalLen;
            }
        }
        return res;
    }

    public int getHashCode(String str) {
        int cur = 0;
        for (int i = 0; i < str.length(); i++) {
            cur += (int) str.charAt(i);
            cur *= 17;
            cur %= 256;
        }
        return cur;
    }

}
