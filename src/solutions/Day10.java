package solutions;

import utils.FileUtils;

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
        //System.out.println(new Day10().solve(testLines));
       System.out.println(new Day10().solve(lines));
    }

    public double solve(List<String> lines) {
        long res = 0L;
        int startrow = 0, startcol = 0;
        for (int i = 0; i < lines.size(); i++)
            for (int j = 0; j < lines.get(i).length(); j++)
                if (lines.get(i).charAt(j) == 'S') {
                    startrow = i;
                    startcol = j;
                }
        //from the 4 adjacent cells, try to reach S back again while counting steps
        //if cycle then break
        //List<int[]> adjIndexes = new ArrayList<>();
        List<int[]> dirs = Arrays.asList(new int[]{1, 0}, new int[]{-1,0}, new int[]{0,1}, new int[]{0,-1});
        for (int i = 0; i < 4; i++) {
            int[] nextpos = new int[2];
            nextpos[0] = startrow + dirs.get(i)[0];
            nextpos[1] = startcol + dirs.get(i)[1];
            int steps = getStepsToStart(lines, nextpos, new int[]{startrow, startcol});
            if (steps != -1) {
                return (steps+1)/2.0;
            }
        }
        return res;
    }

    public int getStepsToStart(List<String> lines, int [] startpos, int[] target) {
        boolean[][] isVisited = new boolean[lines.size()][lines.size()];

        if (outOfRange(startpos, lines.size())) return -1;

        int steps = 0;
        Queue<int[]> bfsQ = new LinkedList<>();
        bfsQ.add(startpos);
        while (!bfsQ.isEmpty()) {
            int [] u = bfsQ.remove();
            isVisited[u[0]][u[1]] = true;
            if (u[0] == target[0] && u[1] == target[1]) {
                System.out.println("Steps = " + steps);
                if (steps != 1) {
                    return steps;
                }
                isVisited[u[0]][u[1]] = false;
                continue;
            }
            char curPipe = lines.get(u[0]).charAt(u[1]);
            int[][] dirs = dirMap.get(curPipe);

            // Recur for all the vertices
            // adjacent to current vertex
            if (dirs != null) {
                steps++;
                for (int i = 0; i < dirs.length; i++) {
                    int[] nextNode = new int[]{u[0] + dirs[i][0], u[1] + dirs[i][1]};
                    if (!outOfRange(nextNode, lines.size()) && !isVisited[nextNode[0]][nextNode[1]])
                        bfsQ.add(nextNode);
                }
            }

        }
        return -1;

    }

//    public int getStepsToStart(List<String> lines, int[] startpos, int targetRow, int targetCol) {
//        boolean[][] isVisited = new boolean[lines.size()][lines.size()];
//        //ArrayList<Integer> pathList = new ArrayList<>();
//
//        // add source to path[]
//       // pathList.add(s);
//
//        // Call recursive utility
//        Steps steps = new Steps();
//        printAllPathsUtil(startpos, new int[]{targetRow, targetCol}, isVisited, lines, steps);
//        return steps.steps;
//    }

//    class Steps {
//        int steps;
//        public Steps() {
//            steps = 0;
//        }
//    }

    public static Map<Character, int[][]> dirMap = Map.of(
            '|', (new int[][]{new int[]{-1, 0}, new int[]{1,0}}),
            '-', new int[][]{new int[]{0,-1}, new int[]{0,1}},
            'F', new int[][]{new int[]{1, 0}, new int[]{0,1}},
            'J', new int[][]{new int[]{-1,0}, new int[]{0,-1}},
            '7', new int[][]{new int[]{0,-1}, new int[]{1,0}},
            'L', new int[][]{new int[]{0,1}, new int[]{-1,0}});

//    private int printAllPathsUtil(int[] u, int[] d,
//                                   boolean[][] isVisited, List<String> lines,
//                                   Steps steps)
//    {
//        if (outOfRange(u, lines.size())) return -1;
//        if (lines.get(u[0]).charAt(u[1]) == '.') return -1;
//
//        if (u[0] == d[0] && u[1] == d[1]) {
//            System.out.println(steps.steps);
//            // if match found then no need to traverse more till depth
//            return steps.steps;
//        }
//
//        // Mark the current node
//        isVisited[u[0]][u[1]] = true;
//
//        char curPipe = lines.get(u[0]).charAt(u[1]);
//        int[][] dirs = dirMap.get(curPipe);
//
//        // Recur for all the vertices
//        // adjacent to current vertex
//        if (dirs != null)
//        for (int i = 0; i < dirs.length; i++) {
//            int[] nextNode = new int[]{u[0] + dirs[i][0], u[1] + dirs[i][1]};
//            if (!isVisited[nextNode[0]][nextNode[1]]) {
//                // store current node
//                // in path[]
//                steps.steps++;
//                printAllPathsUtil(nextNode, d, isVisited, lines, steps);
//
//                // remove current node
//                // in path[]
//                steps.steps--;
//            }
//        }
//
//        // Mark the current node
//        isVisited[u[0]][u[1]] = false;
//        return -1;
//    }

    public boolean outOfRange(int [] cur, int rows) {
        if (cur[0] < 0 || cur[0] >= rows || cur[1] < 0 || cur[1] >= rows )
            return true;
        return false;
    }
}
