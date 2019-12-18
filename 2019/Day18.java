package adventofcode;

import java.util.*;

/**
 * Created by Sun on 12/18/2019.
 * Many-Worlds Interpretation
 */
public class Day18 {

    static char[][] grid;
    static Map<String, Integer> memoize = new HashMap<>();
    static Map<Character, Set<Character>> connected = new HashMap<>();
    static Map<String, Integer> path = new HashMap<>();

    public static void main(String[] args) {
        String[] lines = MAP.split("\n");
        grid = new char[lines.length][];
        for (int i = 0; i < grid.length; i++) {
            grid[i] = lines[i].toCharArray();
        }
        int part = 2;
        if (part == 1) {
            populatePath();
            System.out.println(part1());
        } else if (part == 2) {
            updateGridForPart2();
            populatePath();
            System.out.println(part2());
        }
    }

    static void populatePath() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] != '#' && grid[row][col] != '.') {
                    traverse(row, col);
                }
            }
        }
    }

    static void updateGridForPart2() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == '@') {
                    for (int r = -1; r <= 1; r++) {
                        for (int c = -1; c <= 1; c++) {
                            if (Math.abs(r) > 0 && Math.abs(c) > 0) {
                                if (r < 0 && c < 0) {
                                    grid[row + r][col + c] = '0';
                                } else if (r < 0 && c > 0) {
                                    grid[row + r][col + c] = '1';
                                } else if (r > 0 && c < 0) {
                                    grid[row + r][col + c] = '2';
                                } else {
                                    grid[row + r][col + c] = '3';
                                }
                            } else {
                                grid[row + r][col + c] = '#';
                            }
                        }
                    }
                    return;
                }
            }
        }
    }

    static int part1() {
        memoize.clear();
        return consider1('@', "");
    }

    static int part2() {
        memoize.clear();
        return consider2("0123", "");
    }

    static int consider1(char place, String obtained) {
        if (obtained.length() >= 26) {
            return 0;
        }
        char[] chars = obtained.toCharArray();
        Arrays.sort(chars);
        String key = (new String(chars)) + "," + place;
        if (memoize.containsKey(key)) {
            return memoize.get(key);
        }
        Set<Character> visited = new HashSet<>();
        List<Character> prev = new ArrayList<>();
        List<Character> next = new ArrayList<>();
        List<Integer> prevSteps = new ArrayList<>();
        List<Integer> nextSteps = new ArrayList<>();
        visited.add(place);
        prev.add(place);
        prevSteps.add(0);
        List<Candidate1> candidates = new ArrayList<>();
        Helper1 traverser = (symbol, newSteps) -> {
            if (visited.contains(symbol) || symbol == '@') {
                return;
            }
            visited.add(symbol);

            if (obtained.indexOf(Character.toLowerCase(symbol)) >= 0) {
                next.add(symbol);
                nextSteps.add(newSteps);
                return;
            }
            if (Character.isLowerCase(symbol)) {
                candidates.add(new Candidate1(symbol, newSteps, obtained + symbol));
            }
        };
        while (!prev.isEmpty()) {
            next.clear();
            nextSteps.clear();
            for (int i = 0; i < prev.size(); i++) {
                char symbol = prev.get(i);
                int newSteps = prevSteps.get(i);
                for (char target : connected.get(symbol)) {
                    int extraSteps = path.get(symbol + "" + target);
                    traverser.compute(target, newSteps + extraSteps);
                }
            }
            prev.clear();
            prev.addAll(next);
            prevSteps.clear();
            prevSteps.addAll(nextSteps);
        }
        int min = Integer.MAX_VALUE;
        for (Candidate1 candidate : candidates) {
            int consider = consider1(candidate.place, candidate.obtained);
            min = Math.min(min, consider + candidate.steps);
        }
        memoize.put(key, min);
        return min;
    }

    static int consider2(String places, String obtained) {
        if (obtained.length() >= 26) {
            return 0;
        }
        char[] chars = obtained.toCharArray();
        Arrays.sort(chars);
        String key = (new String(chars)) + "," + places;
        if (memoize.containsKey(key)) {
            return memoize.get(key);
        }
        Set<String> visited = new HashSet<>();
        List<String> prev = new ArrayList<>();
        List<String> next = new ArrayList<>();
        List<Integer> prevSteps = new ArrayList<>();
        List<Integer> nextSteps = new ArrayList<>();
        visited.add(places);
        prev.add(places);
        prevSteps.add(0);
        List<Candidate2> candidates = new ArrayList<>();
        Helper2 traverser = (symbols, newSteps) -> {
            if (visited.contains(symbols)) {
                return;
            }
            visited.add(symbols);
            Character unseen = null;
            char[] charArray = symbols.toCharArray();
            for (char symbol : charArray) {
                if (symbol >= '0' && symbol <= '3') {
                    continue;
                }
                if (obtained.indexOf(Character.toLowerCase(symbol)) < 0) {
                    unseen = symbol;
                    break;
                }
            }
            if (unseen == null) {
                next.add(symbols);
                nextSteps.add(newSteps);
                return;
            }
            for (int i = 0; i < charArray.length; i++) {
                char symbol = charArray[i];
                if (Character.isUpperCase(symbol)) {
                    return;
                }
                if (symbol != unseen && symbol != places.charAt(i)) {
                    return;
                }
            }

            if (Character.isLowerCase(unseen)) {
                candidates.add(new Candidate2(symbols, newSteps, obtained + unseen));
            }
        };
        while (!prev.isEmpty()) {
            next.clear();
            nextSteps.clear();
            for (int i = 0; i < prev.size(); i++) {
                String symbols = prev.get(i);
                int newSteps = prevSteps.get(i);
                for (int j = 0; j < symbols.length(); j++) {
                    char symbol = symbols.charAt(j);
                    for (char target : connected.get(symbol)) {
                        int extraSteps = path.get(symbol + "" + target);
                        char[] charArray = symbols.toCharArray();
                        charArray[j] = target;
                        int steps = newSteps + extraSteps;
                        traverser.compute(new String(charArray), steps);
                    }
                }
            }
            prev.clear();
            prev.addAll(next);
            prevSteps.clear();
            prevSteps.addAll(nextSteps);
        }
        int min = Integer.MAX_VALUE;
        for (Candidate2 candidate : candidates) {
            int consider = consider2(candidate.places, candidate.obtained);
            if (consider == Integer.MAX_VALUE) {
                continue;
            }
            min = Math.min(min, consider + candidate.steps);
        }
        memoize.put(key, min);
        return min;
    }

    static void traverse(int row, int col) {
        char me = grid[row][col];
        Set<Integer> visited = new HashSet<>();
        List<Integer> prev = new ArrayList<>();
        List<Integer> next = new ArrayList<>();
        int start = row * 1000 + col;
        visited.add(start);
        prev.add(start);
        Traverser traverser = (r, c, newSteps) -> {
            if (r < 0 || c < 0 || r >= grid.length || c >= grid[r].length) {
                return;
            }
            int place = r * 1000 + c;
            if (visited.contains(place)) {
                return;
            }
            visited.add(place);
            char symbol = grid[r][c];
            if (symbol == '#') {
                return;
            }
            if (symbol == '.') {
                next.add(place);
                return;
            }
            Set<Character> mySet = connected.get(me);
            if (mySet == null) {
                mySet = new HashSet<>();
            }
            mySet.add(symbol);
            connected.put(me, mySet);
            path.put(me + "" + symbol, newSteps);
        };
        int steps = 0;
        while (!prev.isEmpty()) {
            next.clear();
            steps++;
            for (int place : prev) {
                int r = place / 1000;
                int c = place % 1000;
                traverser.compute(r - 1, c, steps);
                traverser.compute(r + 1, c, steps);
                traverser.compute(r, c - 1, steps);
                traverser.compute(r, c + 1, steps);
            }
            prev.clear();
            prev.addAll(next);
        }
    }

    static class Candidate1 {
        char place;
        int steps;
        String obtained;

        public Candidate1(char place, int steps, String obtained) {
            this.place = place;
            this.steps = steps;
            this.obtained = obtained;
        }
    }

    static class Candidate2 {
        String places;
        int steps;
        String obtained;

        public Candidate2(String places, int steps, String obtained) {
            this.places = places;
            this.steps = steps;
            this.obtained = obtained;
        }
    }

    interface Traverser {
        void compute(int row, int col, int steps);
    }

    interface Helper1 {
        void compute(char symbol, int steps);
    }

    interface Helper2 {
        void compute(String symbols, int steps);
    }

    static final String MAP = "#################################################################################\n" +
            "#.........#.......#...#...#..v..#.....#.#.........#...#....g#...L...#......z#...#\n" +
            "#.#.#####.#.#####.###.#.#.#.#.###I#.#.#.#.#.#######E#.#.###.#.#####.#.#####.#.###\n" +
            "#.#.....#...#.........#.#...#.....#.#...#.#.#..n....#.#.#.#...#...#.#...#...#..p#\n" +
            "#.#####.#####.#######.#.###########.###.#.#.#.#.#####.#.#.#####.#.#.###.#O###.#.#\n" +
            "#.#...#.#.....#...#...#...........#.#...#.#.#.#.#...#.#.#.......#.#.....#.#...#.#\n" +
            "#.###.#.#######.#.#.###########.###.#####.#.#.###.#.#.#.#.#####.#########.#####.#\n" +
            "#...#.#..y..#...#.#...#.........#...#...#.#.#.....#.#...#.#...#.........#.#.....#\n" +
            "###.#.#####.#.###.#####.#########.###.#.#.#.#######.#####.#.#.###.#.#####.#.#####\n" +
            "#...#....c#...#.#.......#.......#.#...#.#.#.......#....x#.#.#.#...#.#.....#.....#\n" +
            "#.###.#.#######.#########.#####.#.#.###.#.#####.#.#####.###.#.#.#####.#####.###.#\n" +
            "#.#...#.................#.....#.#.....#.#...#...#...#.#...#.#.#.#.....#...#.#...#\n" +
            "#.#.#############.#.#####.###.#.#.#####.#####.#####.#.###.#.#.#.#.#####.#.###.#.#\n" +
            "#.#...#.....#...#.#.#...#...#.#.#.#.....#.....#...#.....#.#.#...#.....#.#.#...#.#\n" +
            "#.###.#.#.#.#.###.###.#.###.#.#.#.#.###.#.###.#.#.#####.#.#.#########.#.#.#.###.#\n" +
            "#...#.#.#.#k..#...#...#...#.#.#.#.#...#.#.#...#.#.#.....#.#.........#.#.#...#.#.#\n" +
            "###.#.###F#####.###.#####.###.#.#####.#.#.#####.#.#.#####.#.#####.###.#.#####.#.#\n" +
            "#.#.#...#...#...#...#...#.....#.....#.#.#.......#.#.....#.#.....#.....#.#.....#.#\n" +
            "#.#.###.###.#.###.#.#.#.###########.#.###.#######.#####.#.#######.#####.#.#####.#\n" +
            "#.P.#.#.....#.#...#.#.#.....#...#...#...#...#...#.#...#.#.....H.#...#...#.....#.#\n" +
            "#.###.#.#####.#.###.#.#####.#.###.#####.#.###.#.#.#.#.#########.###.###.#.###.#.#\n" +
            "#...#...#.....#.#...#.#.....#.....#.....#.#...#...#.#.......#.#.#.#.#...#.#.#...#\n" +
            "###.#####.#####.#######.#####.#####.#.#.#.#.#######.#####.#.#.#.#.#.#.###.#.#####\n" +
            "#.#.#...#.....#.....#...#...#...#.#.#.#.#.#...#.....#.....#.#...#.#.#...#...#...#\n" +
            "#.#.#.#.#####.#####.#.###.#.###.#.#.#.###.###.#.#####.#.#########.#.###U#####.#.#\n" +
            "#.#...#...#...#.....#.#.#.#...#.#...#...#...#...#.#...#.#.....#.....#...#.....#.#\n" +
            "#.#######.#.#.#.#####.#.#.###.#.#.#####.###.#####.#.#####.###.#.#####.###.#####.#\n" +
            "#.....#...#.#.#...#...#...#.#...#...#...#.#.#...#.#.....#.#.#.#.#.....#...#.....#\n" +
            "#.#####.###.#.###.#.###.###.#####.###W#.#.#.#.#.#.#####.#.#.#.#.#.#.###.###.###.#\n" +
            "#...#...#...#.#.#...#.#.....#.....#...#.#.#.#.#.......#...#.#...#.#...#.#.#...#.#\n" +
            "#.#.#.###.###.#.#####.#####.#####.#.#####.#.#.#######.#####.#######.#.#T#.###.#.#\n" +
            "#.#.#.#.....#.....#.....#...#...#.#.....#...#.#.....#.#...........#.#.....#...#.#\n" +
            "#.#.#.#.###.#####.#.#.###.###.#.#.#####.#.###.#.###.###.#####.###.#########.###.#\n" +
            "#.#...#...#...#.#.#.#.#...#...#.#.#.....#...#.....#.......#...#.#.#.....#...#...#\n" +
            "#.#######.###.#.#.#.###.###.###.###.###.###.#############.#.###.#.#.###.#.###.###\n" +
            "#.#.......#.#.#.....#...#...J.#.....#...#.#...#...#...#...#...#...#...#...#...#.#\n" +
            "#.#######.#.#.#######.#######.#######.###.###.#.#.#.#.#######.#.###.#.#####.###.#\n" +
            "#.......#...#.#.......#.....#.....#.#.#.#...#...#...#...#.....#...#.#...#.#...#.#\n" +
            "#######.#####.#.#######.###.#####.#.#.#.#.#############.#.#######.#####.#.###.#.#\n" +
            "#.............#.........#.........#.......................#.............#.......#\n" +
            "#######################################.@.#######################################\n" +
            "#.........#...#.........#.....#...#...........#...........#..q....#.....#.......#\n" +
            "#####.###.#.###D#####.###.#.#.#.###.#.#.#.###.#######.#.#.#####.#.###.#.#.#####.#\n" +
            "#.....#.....#...#.#...#...#.#...#...#.#.#...#.......#.#.#j#...#.#.#...#d#.....#.#\n" +
            "#.#.#######.#.###.#.#.#.###.###.#.###.#.#.#.#######.###.#.#.#.#.#.#.###.###.#.###\n" +
            "#t#.#.....#.#...#.#.#.#...#...#.#.#.#.#.#.#...#....w....#...#...#...#.#...#.#...#\n" +
            "#.###.###.#.###.#.#.#####.###.###.#.#.#.#####.#######.###############.###.#.###.#\n" +
            "#.....#.#.#.#...#.#...Q.#...#.......#.#.#.....#...#...#.......#.#.....#...#...#.#\n" +
            "#.#####.#.###.###.#####.###.#######.#.###.###.#.#.#####.#####.#.#.###.#.###.###.#\n" +
            "#.#.....#...#.#.......#.....#...#...#...#.#...#.#.#.......#...#...#.#...#...#...#\n" +
            "#.#####.###.#.#R#####.#######.#.#######.#.#####.#.#.#######.###.###.#######.#.###\n" +
            "#.....#...#...#.#...#...#.....#.....#...#...#...#...#.......#.........#...#.#...#\n" +
            "#####.#.#.#######.#.#.#.#.#########.#.#.###.#.#######.###############.#.#.#.###.#\n" +
            "#.#...#.#...#.....#.#.#.#...#...#...#.#.#.#...#...#...#.............#...#.#...#.#\n" +
            "#.#.###.###.#S#####.#.#####.#.###.###.#.#.#####.#.#.###.###########.#####.#####.#\n" +
            "#...#...#.#...#...#...#.....#...#.#...#.#....m#.#.#...#.......#.....#...#.....#.#\n" +
            "#.#####.#.#######.###.#.#####.#.#.###.#.#.###.#.#.###.###.#####.#####.#.#####.#.#\n" +
            "#u......#.........#...#...#.#.#.#...#.#.#.#...#.#...#.#...#.....#.....#.....#.#.#\n" +
            "#########.#########.#####.#.#.#.###.#.#.#.#####.###.#.#.###.#######.#####.###.#.#\n" +
            "#.M.....#.......#...#.....#.#.#...#.#.#.#.........#.#.#.#.#.......#..s#.#.#...#.#\n" +
            "#####.#.#.#####.#.###.#####.#.###.#.#.###.#######.#.#.#.#.#######.###.#.#.#.###.#\n" +
            "#.....#.#.....#.#...#.#.....#.#.#...#...#...#...#.#.#...#...#.....#.#.#.#...#...#\n" +
            "#.#########B###.###.#######.#.#.#######.#####.#.###.#######.#######.#.#.#####.#.#\n" +
            "#.#.A...#...#.....#.....#...#.#.#.....#.#.....#...#...#.......#.....#.#.......#.#\n" +
            "#.#.###.#.###.#########.#.###.#.#.#.#.#.#.#######.#.#.#.#####.#.###.#.#######.#.#\n" +
            "#.....#.#...#.........#.#.#...#...#.#...#.....#.....#...#a....#.#...#.....#...#.#\n" +
            "#####.#.#.###########.#.#.#.###.###.###.#.###.#.###############.#.#.#####.#.###.#\n" +
            "#...#.#...#...........#...#...#...#.#...#.#...#.#.......#.......#.#.#.....#.#...#\n" +
            "#.#.#######.#############.###.#####.#####.#.###.#.#####.#.#######.###.#####.#.###\n" +
            "#.#.........#.............#.#.V...#.....#.#.C.#.#.....#.#.#.....#...#.#r....#...#\n" +
            "#.#########.#.#############.#####.#####.#####.#.#####.#.#.#####.###.#.#.#######.#\n" +
            "#.#.........#...#...#.G...#.....#.#...#.#...#.#.#.#...#...#b..#.#...#.Y.#.....#.#\n" +
            "#.#############.#.#.#####.#.#.###.#.#.#.#.#.#.#.#.#.#######.#.#.#.#######.#.###.#\n" +
            "#.#...........#...#.....#...#.#...#.#.#.#.#...#...#.#...K...#...#.#.......#.#...#\n" +
            "#.#X#######.###########.#####.#.###.#.#.#.#########.#####.#######.#.#.#######.###\n" +
            "#.#...#...#...N.#.#...#.#l..#.#...#.#...#.......#...#..f#.......#...#.#......o#.#\n" +
            "#.###.#.#.###.#.#.#.#.#.#.#.#.###.#.###.#.#####.#.###.#.#.#####.#####.#.#######.#\n" +
            "#.#...#.#.....#...#.#.#...#...#...#..i#.#...#.#...#...#.#.#.....#.#.Z.#...#...#.#\n" +
            "#.#.###.###########.#.#######.#.#####.#.###.#.#######.#.###.###.#.#.#####.#.#.#.#\n" +
            "#h....#..........e..#.........#.......#.#.............#.....#.....#.........#...#\n" +
            "#################################################################################";
}
