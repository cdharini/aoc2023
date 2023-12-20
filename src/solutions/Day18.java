package solutions;

import utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day18 {

    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day18.txt";
        List<String> testLines = FileUtils.readFile(testFileName);
        List<String> lines = FileUtils.readFile(fileName);
        System.out.println(new Day18().solvePart2(testLines));
        System.out.println(new Day18().solvePart2(lines));
    }

    public long solvePart2(List<String> lines) {
        List<int []> vertices = getVertices(lines);

        return getAreaWithVertices(vertices);
    }

    public long getAreaWithVertices(List<int []> vertices) {
        long area = 0;
        long boundary = 0;
        vertices.add(vertices.get(0));
        for (int i = 1; i < vertices.size() - 1; i++) {
            area += (long) vertices.get(i)[0]*(vertices.get(i-1)[1] - vertices.get(i+1)[1]);
            boundary += (long)Math.abs(vertices.get(i)[0] - vertices.get(i-1)[0]) + Math.abs(vertices.get(i)[1] - vertices.get(i-1)[1]);
        }
        return Math.abs(area)/2 + boundary/2 + 1;
    }

    public List<int []> getVertices(List<String> lines) {
        List<int[]> pts = new ArrayList<>();
        int[] curpos = new int[]{0, 0};
        pts.add(curpos);
        for (String l : lines) {
            String[] splits = l.split("\s+");
            int hexStrSize = splits[2].length();
            String dir = splits[2].substring(hexStrSize - 2, hexStrSize - 1);
            int num = Integer.parseInt(splits[2].substring(2, hexStrSize - 2), 16);
            curpos = generatePoint(curpos, dir, num);
            pts.add(curpos);
        }
        return pts;
    }

    public long solve(List<String> lines, int part) { // too long for part2 so remove this option
        List<int []> pts = getInitialPoints(lines, part);
        int numRows = pts.stream().mapToInt(x -> x[0]).max().getAsInt() + 1;
        int numCols = pts.stream().mapToInt(x -> x[1]).max().getAsInt() + 1;

        //from day 10
        long res = 0;
        Set<Pos> posList = pts.stream().map(x -> new Pos(x[0], x[1])).collect(Collectors.toSet());

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (!posList.contains(new Pos(i, j))) {
                    int sum = 0;
                    // sum diagonally going top left
                    for (int row = i, col = j; row >= 0 && col >= 0; row--, col--) {
                        Pos cur = new Pos(row, col);
                        if (posList.contains(cur) && !isLOr7CornerSet(cur, posList)) {
                            sum += 1;
                        }
                    }
                    if (sum%2 != 0) {
                        res++;
                    }
                }
            }
        }

        return res + pts.size() - 1;
    }

    record Pos(int x, int y){}

    List<int []> getInitialPoints(List<String> lines, int part) {
        List<int []> pts = new ArrayList<>();
        if (part == 1) {
            int[] curpos = new int[]{0, 0};
            pts.add(new int[]{0, 0});
            int minRow = Integer.MAX_VALUE;
            int minCol = Integer.MAX_VALUE;
            for (String l : lines) {
                String[] splits = l.split("\s+");
                String dir = splits[0];
                int num = Integer.valueOf(splits[1]);
                for (int i = 0; i < num; i++) {
                    curpos = generatePoint(curpos, dir, 1);
                    minRow = Integer.min(minRow, curpos[0]);
                    minCol = Integer.min(minCol, curpos[1]);
                    pts.add(curpos);
                }
            }

            pts = adjustPoints(pts, minRow, minCol);
        } else {
            int[] curpos = new int[]{0, 0};
            pts.add(new int[]{0, 0});
            int minRow = Integer.MAX_VALUE;
            int minCol = Integer.MAX_VALUE;
            for (String l : lines) {
                String[] splits = l.split("\s+");
                int hexStrSize = splits[2].length();
                String dir = splits[2].substring(hexStrSize - 2, hexStrSize - 1);
                int num = Integer.valueOf(splits[2].substring(2, hexStrSize - 2), 16);
                for (int i = 0; i < num; i++) {
                    curpos = generatePoint(curpos, dir, 1);
                    minRow = Integer.min(minRow, curpos[0]);
                    minCol = Integer.min(minCol, curpos[1]);
                    pts.add(curpos);
                }
            }
            pts = adjustPoints(pts, minRow, minCol);
        }
        return pts;
    }

    private List<int[]> adjustPoints(List<int[]> pts, int minRow, int minCol) {
        if (minRow < 0)
            pts = pts.stream().map(x -> new int[]{x[0] - minRow, x[1]}).collect(Collectors.toList());
        if (minCol < 0)
            pts = pts.stream().map(x -> new int[]{x[0], x[1] - minCol}).collect(Collectors.toList());
        return pts;
    }

    public boolean isLOr7CornerSet(Pos cur, Set<Pos> posSet) {
        if (posSet.contains(new Pos(cur.x -1, cur.y)) &&  posSet.contains(new Pos(cur.x, cur.y + 1))) return true; // L
        if (posSet.contains(new Pos(cur.x, cur.y - 1)) && posSet.contains(new Pos(cur.x + 1, cur.y))) return true; // 7
        return false;
    }
    int [] generatePoint(int [] curpos, String dir, int dist) {
        switch(dir) {
            case "R", "0":
                return new int[]{curpos[0], curpos[1] + dist};
            case "L", "2":
                return new int[]{curpos[0], curpos[1] - dist};
            case "U", "3":
                return new int[]{curpos[0] - dist, curpos[1]};
            case "D", "1":
                return new int[]{curpos[0] + dist , curpos[1]};
        }
        return  new int[2];
    }
}
