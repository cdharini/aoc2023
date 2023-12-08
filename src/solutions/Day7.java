package solutions;

import utils.FileUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day7 {
    static final String ORDER_PART1 = "23456789TJQKA";
    static final String ORDER_PART2 = "J23456789TQKA";

    public static class CardComparator implements Comparator<String> {
            String order;
            public CardComparator(Integer part) {
                if (part == 1)
                    this.order = ORDER_PART1;
                else
                    this.order = ORDER_PART2;
            }
            @Override
            public int compare(String o1, String o2) {
                int pos1 = 0;
                int pos2 = 0;
                for (int i = 0; i < Math.min(o1.length(), o2.length()) && pos1 == pos2; i++) {
                    pos1 = order.indexOf(o1.charAt(i));
                    pos2 = order.indexOf(o2.charAt(i));
                }
                if (pos1 == pos2 && o1.length() != o2.length()) {
                    return o1.length() - o2.length();
                }
                return pos1  - pos2;
            }
    }
    public static void main(String[] args) {
        String testFileName = "src/inputs/test";
        String fileName = "src/inputs/day7.txt";
        List<String> testLines = FileUtils.readFile(testFileName);
        List<String> lines = FileUtils.readFile(fileName);
        System.out.println(new Day7().solve(testLines, 2));
        System.out.println(new Day7().solve(lines, 2));
    }

    public long solve(List<String> lines, Integer part) {
        long res = 0L;
        List<String> hands = new ArrayList<>();
        List<Integer> bids = new ArrayList<>();
        HashMap<String, Integer> handIndex = new HashMap<>();
        int j = 0;
        for (String l : lines) {
            String [] tmp = l.split("\s+");
            hands.add(tmp[0]);
            bids.add(Integer.parseInt(tmp[1]));
            handIndex.put(tmp[0], j);
            j++;
        }

        //parse strings into categories
        List<List<Integer>> cardMappings = new ArrayList<>(7);
        for (int i = 0;i < 7; i++)
            cardMappings.add(new ArrayList<>());
        if (part == 1)
            getCardMappingsPart1(hands, cardMappings);
        else
            getCardMappingsPart2(hands, cardMappings);


        int curRank = 1;
        for (int i = 0; i < cardMappings.size(); i++) {
            List<Integer> curCardMap = cardMappings.get(i);
            if (curCardMap.size() == 1) {
                res += bids.get(curCardMap.get(0))*curRank;
                curRank++;
            } else if (curCardMap.size() > 1) {
                //get all of the card hands, sort them and add them to rank
                List<String> cards = curCardMap.stream().map(x -> hands.get(x)).collect(Collectors.toList());
                Collections.sort(cards, new CardComparator(part));
                for (String c : cards) {
                    res += bids.get(handIndex.get(c))*curRank;
                    curRank++;
                }
            }
        }

        return res;
    }


    public void getCardMappingsPart2(List<String> hands, List<List<Integer>> cardMappings) {
        int joker_index = 3;

        for (int i = 0; i < hands.size(); i++) {
            ArrayList<Integer> parsed = parseHand(hands.get(i));
            int numJokers = parsed.get(joker_index);
            // does not contain joker
            if (numJokers == 0) {
                parsed.sort(Collections.reverseOrder());
                addToCardMappings(cardMappings, parsed, i);
            } else {
                //remove joker and add it to the most freq card
                parsed.remove(joker_index);
                parsed.sort(Comparator.reverseOrder());
                int mostFreq = parsed.get(0) + numJokers;
                parsed.set(0, mostFreq);
                addToCardMappings(cardMappings, parsed, i);
            }
        }

    }

    public void addToCardMappings(List<List<Integer>> cardMappings, List<Integer> parsed, int handInd) {
        int mostFreq = parsed.get(0);
        if (mostFreq == 5) {
            cardMappings.get(6).add(handInd);
        } else if (mostFreq == 4) {
            cardMappings.get(5).add(handInd);
        } else if (mostFreq == 3) {
            if (parsed.get(1) == 2)
                cardMappings.get(4).add(handInd);
            else
                cardMappings.get(3).add(handInd);
        } else if (mostFreq == 2) {
            if (parsed.get(1) == 2)
                cardMappings.get(2).add(handInd);
            else
                cardMappings.get(1).add(handInd);
        } else {
            cardMappings.get(0).add(handInd);
        }
    }

    public void getCardMappingsPart1(List<String> hands, List<List<Integer>> cardMappings) {
        for (int i = 0; i < hands.size(); i++) {
            List<Integer> parsed = parseHand(hands.get(i));
            parsed.sort(Collections.reverseOrder());
            addToCardMappings(cardMappings, parsed, i);
        }
    }

    public ArrayList<Integer> parseHand(String hand) {
        ArrayList<Integer> res = new ArrayList<>(Collections.nCopies(13, 0));

        HashMap<Character, Integer> charMap = new HashMap(Map.of('A',0,'K', 1, 'Q', 2, 'J', 3, 'T', 4, '9',5,'8',6,'7',7,'6',8,'5',9));
        charMap.putAll(Map.of('4',10,'3',11,'2',12 ));

        for (char c : hand.toCharArray()) {
            res.set(charMap.get(c), res.get(charMap.get(c)) + 1);
        }
        return res;
    }
}
