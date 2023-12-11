package solutions;

import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Day10 {
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day10.txt";
        List<String> testLines = FileUtils.readFile(testFileName);
        List<String> lines = FileUtils.readFile(fileName);
        System.out.println(new Day10().solvePart2(testLines));
       System.out.println(new Day10().solvePart2(lines));
    }

    public int solvePart2(List<String> lines) {
        int res = 0;
        List<int []> loopPath = getLoopPath(lines);
        // set all points not in loop as X
        int[][] fieldWithOnlyLoop = new int[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            int [] tmp = new int[lines.get(0).length()];
            Arrays.fill(tmp, 0);
            fieldWithOnlyLoop[i] = tmp;
        }
        int[] topr = new int[]{Integer.MAX_VALUE, 0};
        int[] botl = new int[]{0, Integer.MAX_VALUE};
        for (int [] i : loopPath) {
            fieldWithOnlyLoop[i[0]][i[1]] = 1;
            if (i[0] < topr[0]) {
                topr = i;
            } else if (i[0] == topr[0] && i[1] > topr[1]) {
                topr = i;
            }
            if (i[0] > botl[0]) {
                botl = i;
            } else if (i[0] == botl[0] && i[1] < botl[1]) {
                botl = i;
            }
        }
        for (int i = 0; i < fieldWithOnlyLoop.length; i++) {
            for (int j = 0; j < fieldWithOnlyLoop[i].length; j++) {
                if (fieldWithOnlyLoop[i][j] == 0) {
                    int sum = 0;
                    // sum diagonally going top left
                    for (int row = i, col = j; row >= 0 && col >= 0; row--, col--) {
                           // manually checked my S is a 7 so including that here as well
                        if (lines.get(row).charAt(col) == 'L' || lines.get(row).charAt(col) == '7' || lines.get(row).charAt(col) == 'S') {
                                continue;
                            } else {
                                sum += fieldWithOnlyLoop[row][col];
                            }
                    }
                    if (sum%2 != 0) {
                        res++;
                    }
                }
            }
        }
        return res;
    }

    public int solvePart1(List<String> lines) {
        return getLoopPath(lines).size()/2;
    }

    public List<int[]> getLoopPath(List<String> lines) {
        List<int[]> path = new ArrayList<>();
        //long res = 0L;
        int startrow = 0, startcol = 0;
        for (int i = 0; i < lines.size(); i++)
            for (int j = 0; j < lines.get(i).length(); j++)
                if (lines.get(i).charAt(j) == 'S') {
                    startrow = i;
                    startcol = j;
                }
        //from the 4 adjacent cells, try to reach S back again while counting steps
        //if cycle then break
        List<int[]> dirs = Arrays.asList(new int[]{1, 0}, new int[]{-1,0}, new int[]{0,1}, new int[]{0,-1});
        for (int i = 0; i < 4; i++) {
            int[] nextpos = new int[2];
            nextpos[0] = startrow + dirs.get(i)[0];
            nextpos[1] = startcol + dirs.get(i)[1];
            //path = new ArrayList<>();
            path = getStepsToStart(lines, nextpos, new int[]{startrow, startcol});
            if (!path.isEmpty()) {
                System.out.println("Path size is " + path.size());
                return path;
            }
        }
        return path;
    }

    public List<int []> getStepsToStart(List<String> lines, int [] startpos, int[] target) {
        List<int[]> path = new ArrayList<>();
        //boolean[][] isVisited = new boolean[lines.size()][lines.size()];

        if (outOfRange(startpos, lines.size(), lines.get(0).length())) return path;

       // int steps = 0;
        Queue<List<int[]>> bfsQ = new LinkedList<>();
        path.add(startpos);
        bfsQ.add(path);
        while (!bfsQ.isEmpty()) {
            path = bfsQ.remove();
            //int [] u = bfsQ.remove();
            //isVisited[u[0]][u[1]] = true;
            //path.add(u);
            int[] last = path.get(path.size() - 1);
            if (last[0] == target[0] && last[1] == target[1]) {
                System.out.println("Steps = " + path.size());
                if (path.size() > 2) {
                    return path;
                }
                continue;
                //isVisited[u[0]][u[1]] = false;
            }
            char curPipe = lines.get(last[0]).charAt(last[1]);
            int[][] dirs = dirMap.get(curPipe);

            // Recur for all the vertices
            // adjacent to current vertex
            if (dirs != null) {
                //steps++;
                for (int i = 0; i < dirs.length; i++) {
                    int[] nextNode = new int[]{last[0] + dirs[i][0], last[1] + dirs[i][1]};
                    if (!outOfRange(nextNode, lines.size(), lines.get(0).length()) && isNotVisited(nextNode, path)) {
                        List<int []> newPath = new ArrayList<>(path);
                        newPath.add(nextNode);
                        bfsQ.add(newPath);
                    }
                }
            }

        }
        return new ArrayList<>();
    }

    private static boolean isNotVisited(int[] x,
                                        List<int[]> path)
    {
        for(int i = 0; i < path.size(); i++)
            if (path.get(i)[0] == x[0] && path.get(i)[1] == x[1])
                return false;
        return true;
    }


    public static Map<Character, int[][]> dirMap = Map.of(
            '|', (new int[][]{new int[]{-1, 0}, new int[]{1,0}}),
            '-', new int[][]{new int[]{0,-1}, new int[]{0,1}},
            'F', new int[][]{new int[]{1, 0}, new int[]{0,1}},
            'J', new int[][]{new int[]{-1,0}, new int[]{0,-1}},
            '7', new int[][]{new int[]{0,-1}, new int[]{1,0}},
            'L', new int[][]{new int[]{0,1}, new int[]{-1,0}});

    public boolean outOfRange(int [] cur, int rows, int cols) {
        if (cur[0] < 0 || cur[0] >= rows || cur[1] < 0 || cur[1] >= cols )
            return true;
        return false;
    }
}
