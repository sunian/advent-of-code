package adventofcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sun on 12/23/2019.
 * Planet of Discord
 */
public class Day24 {
    static char[][] grid;
    static int[][] offsets = new int[][]{
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };

    public static void main(String[] args) {
        part1();
        part2();
    }

    static void parseInput() {
        String[] lines = INPUT.split("\n");
        grid = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            grid[i] = lines[i].toCharArray();
        }
    }

    static void part1() {
        parseInput();
        Set<String> prev = new HashSet<>();
        while (true) {
            String s = valueOf(grid);
            if (prev.contains(s)) {
                System.out.println(biodiversityOf(grid));
                return;
            }
            prev.add(s);
            generation(grid);
        }
    }

    static void part2() {
        parseInput();
        Set<Long> positions = new HashSet<>();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (hasBug(grid, row, col)) {
                    long pos = 400 * 1000000 + row * 100 + col;
                    positions.add(pos);
                }
            }
        }
        for (int i = 0; i < 200; i++) {
            generation(positions);
        }
        System.out.println(positions.size());
    }

    static void generation(Set<Long> positions) {
        Map<Long, Integer> neighborCounts = new HashMap<>();
        Callback callback = (l, r, c) -> {
            long pos = l * 1000000 + r * 100 + c;
            if (!neighborCounts.containsKey(pos)) {
                neighborCounts.put(pos, countNeighbors(positions, l, r, c));
            }
        };
        for (Long position : positions) {
            int level = (int) (position / 1000000);
            int row = (int) ((position / 100) % 100);
            int col = (int) (position % 100);
            callback.handle(level, row, col);
            iterateNeighbors(level, row, col, callback);
        }
        for (Map.Entry<Long, Integer> entry : neighborCounts.entrySet()) {
            if (positions.contains(entry.getKey())) {
                if (entry.getValue() != 1) {
                    positions.remove(entry.getKey());
                }
            } else {
                if (entry.getValue() == 1 || entry.getValue() == 2) {
                    positions.add(entry.getKey());
                }
            }
        }
    }

    static int countNeighbors(Set<Long> positions, int level, int row, int col) {
        final int[] count = {0};
        Callback callback = (l, r, c) -> {
            long pos = l * 1000000 + r * 100 + c;
            if (positions.contains(pos)) {
                count[0]++;
            }
        };
        iterateNeighbors(level, row, col, callback);
        return count[0];
    }

    static void iterateNeighbors(int level, int row, int col, Callback callback) {
        for (int[] offset : offsets) {
            int r = row + offset[0];
            int c = col + offset[1];
            if (r == 2 && c == 2) {
                if (row == 1) {
                    for (int n = 0; n < 5; n++) {
                        callback.handle(level - 1, 0, n);
                    }
                } else if (row == 3) {
                    for (int n = 0; n < 5; n++) {
                        callback.handle(level - 1, 4, n);
                    }
                } else if (col == 1) {
                    for (int n = 0; n < 5; n++) {
                        callback.handle(level - 1, n, 0);
                    }
                } else {
                    for (int n = 0; n < 5; n++) {
                        callback.handle(level - 1, n, 4);
                    }
                }
            } else if (r < 0) {
                callback.handle(level + 1, 1, 2);
            } else if (r >= 5) {
                callback.handle(level + 1, 3, 2);
            } else if (c < 0) {
                callback.handle(level + 1, 2, 1);
            } else if (c >= 5) {
                callback.handle(level + 1, 2, 3);
            } else {
                callback.handle(level, r, c);
            }
        }
    }

    static void generation(char[][] grid) {
        int[][] neighborCounts = new int[grid.length][grid[0].length];
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                int count = 0;
                for (int[] offset : offsets) {
                    if (hasBug(grid, row + offset[0], col + offset[1])) {
                        count++;
                    }
                }
                neighborCounts[row][col] = count;
            }
        }
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                int count = neighborCounts[row][col];
                if (hasBug(grid, row, col)) {
                    if (count != 1) {
                        grid[row][col] = '.';
                    }
                } else {
                    if (count == 1 || count == 2) {
                        grid[row][col] = '#';
                    }
                }
            }
        }
    }

    static boolean hasBug(char[][] grid, int row, int col) {
        if (row < 0 || col < 0 || row >= grid.length || col >= grid[row].length) {
            return false;
        }
        return grid[row][col] == '#';
    }

    static String valueOf(char[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : grid) {
            sb.append(row);
            sb.append('\n');
        }
        return sb.toString();
    }

    static long biodiversityOf(char[][] grid) {
        long biodiversity = 0;
        long inc = 1;
        for (char[] row : grid) {
            for (char space : row) {
                if (space == '#') {
                    biodiversity += inc;
                }
                inc *= 2;
            }
        }
        return biodiversity;
    }

    interface Callback {
        void handle(int l, int r, int c);
    }

    static final String INPUT = "" +
            "##.#.\n" +
            "#.###\n" +
            "##...\n" +
            "...#.\n" +
            "#.##.";
}
