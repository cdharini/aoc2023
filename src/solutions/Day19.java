package solutions;

import utils.FileUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day19 {
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day19.txt";
        List<String> testLines = FileUtils.readFile(testFileName);
        List<String> lines = FileUtils.readFile(fileName);
        System.out.println(new Day19().solvePart2(testLines));
        System.out.println(new Day19().solvePart2(lines));
    }

    record Part(int x, int m, int a, int s) {}

    record Rule(char partCategory, char comparisonSign, int min, int max, String dest) {}

    public long solvePart1(List<String> lines) {
        long res = 0;
        Map<String, List<Rule>> workflow = new HashMap<>();
        int i = 0;
        while (i < lines.size() && !lines.get(i).isBlank()) {
            String l = lines.get(i).replace("{", " ");
            String[] splits = l.split("\s+");
            workflow.put(splits[0], makeRules(splits[1].split(",")));
            i++;
        }
        i++;
        while (i < lines.size()) {
            String[] l = lines.get(i).replaceAll("[{},=]" , " ").split(" ");
            Part p = new Part(Integer.parseInt(l[2]), Integer.parseInt(l[4]),Integer.parseInt(l[6]), Integer.parseInt(l[8]) );
            res += getRating(p, workflow);
            i++;
        }

        return res;
    }

    public long solvePart2(List<String> lines) {
        Map<String, List<Rule>> workflow = new HashMap<>();
        int i = 0;
        while (i < lines.size() && !lines.get(i).isBlank()) {
            String l = lines.get(i).replace("{", " ");
            String[] splits = l.split("\s+");
            workflow.put(splits[0], makeRules(splits[1].split(",")));
            i++;
        }
        return getResultRec("in", new CurInterval(1,4000,1,4000,1,4000,1,4000), workflow);
    }

    record CurInterval (long xmin, long xmax, long mmin, long mmax, long amin, long amax, long smin, long smax) {}

    public long getResultRec(String dest, CurInterval cur, Map<String, List<Rule>> workflowMap) {
        if (dest.equals("A")) {
            long res = (cur.xmax - cur.xmin +1) * (cur.amax - cur.amin +1) * (cur.mmax - cur.mmin +1) * (cur.smax - cur.smin +1);
            return res;
        }
        if (dest.equals("R"))
            return 0;
        List<Rule> rules = workflowMap.get(dest);
        long res = 0;
        for (Rule r: rules) {
            CurInterval nextInt = null;
            switch (r.partCategory) {
                case '-':
                    nextInt = cur;
                    break;
                case 'x':
                    if (r.comparisonSign == '<') {
                        nextInt = new CurInterval(Math.max(cur.xmin, r.min), Math.min(cur.xmax, r.max-1), cur.mmin, cur.mmax, cur.amin, cur.amax, cur.smin, cur.smax);
                        cur = new CurInterval(Math.max(cur.xmin, r.max), Math.max(cur.xmax, r.max), cur.mmin, cur.mmax, cur.amin, cur.amax, cur.smin, cur.smax);
                    }else {
                        nextInt = new CurInterval(Math.max(cur.xmin, r.min+1), Math.min(cur.xmax, r.max), cur.mmin, cur.mmax, cur.amin, cur.amax, cur.smin, cur.smax);
                        cur = new CurInterval(Math.min(cur.xmin, r.min), Math.min(cur.xmax, r.min), cur.mmin, cur.mmax, cur.amin, cur.amax, cur.smin, cur.smax);
                    }break;
                case 'm':
                    if (r.comparisonSign == '<') {
                        nextInt = new CurInterval(cur.xmin, cur.xmax, Math.max(cur.mmin, r.min), Math.min(cur.mmax, r.max-1), cur.amin, cur.amax, cur.smin, cur.smax);

                        cur = new CurInterval(cur.xmin, cur.xmax, Math.max(cur.mmin, r.max), Math.max(cur.mmax, r.max), cur.amin, cur.amax, cur.smin, cur.smax);
                    }else {
                        nextInt = new CurInterval(cur.xmin, cur.xmax, Math.max(cur.mmin, r.min+1), Math.min(cur.mmax, r.max), cur.amin, cur.amax, cur.smin, cur.smax);

                        cur = new CurInterval(cur.xmin, cur.xmax, Math.min(cur.mmin, r.min), Math.min(cur.mmax, r.min), cur.amin, cur.amax, cur.smin, cur.smax);
                    }break;
                case 'a':
                    if (r.comparisonSign == '<') {
                        nextInt = new CurInterval(cur.xmin, cur.xmax, cur.mmin, cur.mmax, Math.max(cur.amin, r.min), Math.min(cur.amax, r.max-1), cur.smin, cur.smax);

                        cur = new CurInterval(cur.xmin, cur.xmax, cur.mmin, cur.mmax, Math.max(cur.amin, r.max), Math.max(cur.amax, r.max), cur.smin, cur.smax);
                    }else {
                        nextInt = new CurInterval(cur.xmin, cur.xmax, cur.mmin, cur.mmax, Math.max(cur.amin, r.min+1), Math.min(cur.amax, r.max+1), cur.smin, cur.smax);

                        cur = new CurInterval(cur.xmin, cur.xmax, cur.mmin, cur.mmax, Math.min(cur.amin, r.min), Math.min(cur.amax, r.min), cur.smin, cur.smax);
                    }break;
                case 's':
                     if (r.comparisonSign == '<') {
                         nextInt = new CurInterval(cur.xmin, cur.xmax, cur.mmin, cur.mmax, cur.amin, cur.amax, Math.max(cur.smin, r.min), Math.min(cur.smax, r.max-1));
                         cur = new CurInterval(cur.xmin, cur.xmax, cur.mmin, cur.mmax, cur.amin, cur.amax, Math.max(cur.smin, r.max), Math.max(cur.smax, r.max));
                    }else {
                         nextInt = new CurInterval(cur.xmin, cur.xmax, cur.mmin, cur.mmax, cur.amin, cur.amax, Math.max(cur.smin, r.min+1), Math.min(cur.smax, r.max));

                         cur = new CurInterval(cur.xmin, cur.xmax, cur.mmin, cur.mmax, cur.amin, cur.amax, Math.min(cur.smin, r.min), Math.min(cur.smax, r.min));
                    }break;
            }
            res += getResultRec(r.dest, nextInt, workflowMap);

        }
        return res;
    }

    public long getRating(Part p, Map<String, List<Rule>> workflowMap) {
        String dest = "in";
        while (workflowMap.containsKey(dest)) {
                List<Rule> curRules = workflowMap.get(dest);
                for (Rule r : curRules) {
                    String nextDest = "";
                    switch (r.partCategory) {
                        case '-':
                            nextDest = r.dest;
                            break;
                        case 'x':
                            if (p.x > r.min && p.x < r.max)
                                nextDest = r.dest;
                            break;
                        case 'm':
                            if (p.m > r.min && p.m < r.max)
                                nextDest = r.dest;
                            break;
                        case 'a':
                            if (p.a > r.min && p.a < r.max)
                                nextDest = r.dest;
                            break;
                        case 's':
                            if (p.s > r.min && p.s < r.max)
                                nextDest = r.dest;
                            break;

                    }
                    if (!nextDest.isEmpty()) { dest = nextDest; break;}
                }

        }
        if (dest.equals("A")) {
            return p.x + p.m + p.a + p.s;
        } else if (dest.equals("R")) {
            return 0;
        }
        return 0;
    }

    public List<Rule> makeRules(String[] ruleStrs) {
        return Arrays.stream(ruleStrs).map(x ->
        {
            Rule rule;
            if (x.contains(":")) {
                String [] splitx = x.split(":");
                int min = 0;
                int max = 4001;
                char sign = splitx[0].charAt(1);
                if (sign == '>')
                    min = Integer.parseInt(splitx[0].substring(2));
                else
                    max = Integer.parseInt(splitx[0].substring(2));
                rule = new Rule(splitx[0].charAt(0),sign, min,max,splitx[1]);
            } else {
                rule = new Rule('-', '-', 0, 4001, x.substring(0, x.length()-1));
            }
            return rule;
        }).collect(Collectors.toList());
    }
}
