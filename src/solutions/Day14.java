package solutions;

import utils.FileUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Day14 {
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day14.txt";
        List<String> testLines = FileUtils.readFile(testFileName);
        List<String> lines = FileUtils.readFile(fileName);
        //System.out.println(new Day14().solvePart2(testLines));
        System.out.println(new Day14().solvePart2(lines));
    }

    public long solvePart1(List<String> lines) {
        char[][] matrix = getMatrix(lines);
        //shift rocks up
        slideRocks(matrix, true, 0, matrix.length, true);
        return calcLoadOnNorthBeam(matrix);
    }
    public char[][] getMatrix(List<String> lines) {
        char[][] matrix =
                new char[lines.size()][lines.get(0).length()];
        int i = 0;
        for (String l : lines) {
            matrix[i] = l.toCharArray();
            i++;
        }
        return matrix;
    }
    public long calcLoadOnNorthBeam(char[][] matrix) {
        long res = 0;
        for (int row = 0; row < matrix.length; row ++) {
            int numOs = 0;
            for (int col = 0; col < matrix[0].length; col++) {
                if (matrix[row][col] == 'O')
                    numOs++;
            }
            res += (long) numOs *(matrix.length-row);
        }
        return res;
    }

    record MatrixLoad(long load, int cycleNum){}

    public long solvePart2(List<String> lines) {
        long res = 0;
        char[][] matrix = getMatrix(lines);

        int loopLen = Integer.MAX_VALUE;

        int numCycles = 1000000000;
        int matrixHeight = matrix.length;
        int loopst = 0;
        HashMap<String, MatrixLoad> map = new HashMap<>();
        for (int i = 0; i < numCycles; i++) {
            slideRocks(matrix, true, 0, matrixHeight, true);
            slideRocks(matrix, false, 0, matrixHeight, true);
            slideRocks(matrix, true, 0, matrixHeight, false);
            slideRocks(matrix, false, 0, matrixHeight, false);

            long load = calcLoadOnNorthBeam(matrix);
            String matrixStr = getString(matrix);
            if (map.containsKey(matrixStr)) {
                int prevpos = map.get(matrixStr).cycleNum;
                loopLen = i - prevpos;
                loopst = prevpos;
                break;
            } else {
                map.put(matrixStr, new MatrixLoad(load, i));
            }
        }
        System.out.println(loopLen);
        int loadLoopPos = loopst + (numCycles-loopst-1) % loopLen;
        for (MatrixLoad m : map.values()) {
            if (m.cycleNum == loadLoopPos) {
                return m.load;
            }
        }
        return 0;
    }

    public String getString(char[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            sb.append(matrix[i]);
        }
        return sb.toString();
    }

    public void slideRocks(char[][] matrix, boolean slideAlongCol, int low, int high, boolean goUp) {
        if (slideAlongCol) {
            for (int col = 0; col < matrix[0].length; col++) {
                int curEmptySpot = goUp? low : high - 1;
                int row = goUp? low : high - 1;
                while ((goUp && row < matrix.length) || (!goUp && row >= 0)){
                    if (matrix[row][col] == '#') {
                        row = goUp? row+1 : row-1;
                        curEmptySpot = row;
                    } else if (matrix[row][col] == 'O') {
                        char tmp = matrix[curEmptySpot][col];
                        matrix[curEmptySpot][col] = 'O';
                        matrix[row][col] = tmp;
                        curEmptySpot = goUp ? curEmptySpot+1 : curEmptySpot-1;
                        row = goUp ? row+1 : row -1;
                    } else {
                        row = goUp? row + 1 : row -1;
                    }
                }
            }
        } else {
            boolean goLeft = goUp;
            for (int row = 0; row < matrix.length; row++) {
                int curEmptySpot = goLeft? low : high - 1;
                int col = goLeft? low : high - 1;
                while ((goLeft && col < matrix[0].length) || (!goLeft && col >= 0)){
                    if (matrix[row][col] == '#') {
                        col = goLeft? col+1 : col-1;
                        curEmptySpot = col;
                    } else if (matrix[row][col] == 'O') {
                        char tmp = matrix[row][curEmptySpot];
                        matrix[row][curEmptySpot] = 'O';
                        matrix[row][col] = tmp;
                        curEmptySpot = goLeft ? curEmptySpot+1 : curEmptySpot-1;
                        col = goLeft ? col+1 : col -1;
                    } else {
                        col = goLeft? col + 1 : col -1;
                    }
                }
            }
        }
    }

}
