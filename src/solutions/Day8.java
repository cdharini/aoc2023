package solutions;

import utils.FileProcessing;
import utils.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day8 {
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day8.txt";
        List<String> testLines = FileProcessing.readFile(testFileName);
        List<String> lines = FileProcessing.readFile(fileName);
        System.out.println(new Day8().part2(testLines));
        System.out.println(new Day8().part2(lines));
    }

    class Node {
        String name;
        Node left;
        Node right;
        public Node(String name) {
            this.name = name;
        }
    }

    public long part1(List<String> lines) {
        String dir = lines.get(0);

        HashMap<String, Node> nodeMap = parseNodeMap(lines);


        return getSteps(nodeMap.get("AAA"), dir, "ZZZ");
    }

    public long getSteps(Node curNode, String dir, String endStr) {
        long steps = 0L;
        int i = 0;
        while (i < dir.length()) {
            steps++;
            char nextDir = dir.charAt(i);
            if (nextDir == 'L') {
                curNode = curNode.left;
            } else {
                curNode = curNode.right;
            }
            if (curNode.name.endsWith(endStr)) {
                return steps;
            }
            i++;
            if (i == dir.length()) {
                i = 0;
            }
        }
        return steps;
    }

    public long part2(List<String> lines) {
        String dir = lines.get(0);

        HashMap<String, Node> nodeMap = parseNodeMap(lines);
        List<Node> startingNodes = new ArrayList<>();
        nodeMap.entrySet().stream().forEach(x -> {
            boolean add = x.getKey().endsWith("A") ? startingNodes.add(x.getValue()) : false;
        });
        List<Long> stepsPerNode = new ArrayList<>();
        for (int i = 0; i < startingNodes.size(); i++)
            stepsPerNode.add(getSteps(startingNodes.get(i), dir, "Z"));

        return MathUtils.getLCM(stepsPerNode);
    }

    public HashMap<String, Node> parseNodeMap(List<String> lines) {

        HashMap<String, Node> nodeMap = new HashMap<String, Node>();
        for (int i = 2; i < lines.size(); i++) {
            String cur = lines.get(i).substring(0,3);
            String left = lines.get(i).substring(7,10);
            String right = lines.get(i).substring(12,15);
            nodeMap.putIfAbsent(left, new Node(left));
            nodeMap.putIfAbsent(right, new Node(right));
            Node curNode = nodeMap.getOrDefault(cur, new Node(cur));
            curNode.left = nodeMap.get(left);
            curNode.right = nodeMap.get(right);
            nodeMap.put(cur, curNode);
        }
        return nodeMap;
    }


}
