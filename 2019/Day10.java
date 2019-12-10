package adventofcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sun on 12/10/2019.
 * Monitoring Station
 */
public class Day10 {

    public static void main(String[] args) {
//        printRatios();
        System.out.println(part1());
        part2();
    }

    static void printRatios() {
        List<Ratio> ratios = new ArrayList<>();
        for (int a = 1; a < 23; a++) {
            for (int b = 1; b < 23; b++) {
                if (areCoPrime(a, b)) {
                    ratios.add(new Ratio(a, b));
                }
            }
        }
        Collections.sort(ratios);
        for (Ratio ratio : ratios) {
            System.out.println(ratio);
        }
    }

    static class Ratio implements Comparable<Ratio> {
        final int row;
        final int col;

        public Ratio(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int compareTo(Ratio o) {
            Double myRatio = row / (double) col;
            Double theirRatio = o.row / (double) o.col;
            return myRatio.compareTo(theirRatio);
        }

        @Override
        public String toString() {
            return String.format("{%d,%d},", row, col);
        }
    }

    static int vaporized = 0;

    static void part2() {
        boolean[][] grid = getGrid();
        int sourceRow = 11;
        int sourceCol = 19;
        while (true) {
            if (vaporizeSingle(grid, sourceRow, sourceCol, -1, 0)) {
                return;
            }
            if (vaporize(grid, sourceRow, sourceCol, -1, 1)) {
                return;
            }
            if (vaporizeSingle(grid, sourceRow, sourceCol, 0, 1)) {
                return;
            }
            if (vaporize(grid, sourceRow, sourceCol, 1, 1)) {
                return;
            }
            if (vaporizeSingle(grid, sourceRow, sourceCol, 1, 0)) {
                return;
            }
            if (vaporize(grid, sourceRow, sourceCol, 1, -1)) {
                return;
            }
            if (vaporizeSingle(grid, sourceRow, sourceCol, 0, -1)) {
                return;
            }
            if (vaporize(grid, sourceRow, sourceCol, -1, -1)) {
                return;
            }
        }
    }

    static int part1() {
        boolean[][] grid = getGrid();
        int max = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col]) {
                    int count = count(grid, row, col);
                    if (count == 230) { // precomputed 230 is absolute max
                        System.out.println("max is at " + row + " " + col);
                    }
                    max = Math.max(max, count);
                }
            }
        }
        return max;
    }

    static int count(boolean[][] grid, int row, int col) {
        int count = 0;
        count += count(grid, row, col, 1, 1);
        count += count(grid, row, col, 1, -1);
        count += count(grid, row, col, -1, 1);
        count += count(grid, row, col, -1, -1);
        count += countSingle(grid, row, col, 1, 0);
        count += countSingle(grid, row, col, -1, 0);
        count += countSingle(grid, row, col, 0, 1);
        count += countSingle(grid, row, col, 0, -1);
        return count;
    }

    static boolean vaporize(boolean[][] grid, int row, int col, int rowMul, int colMul) {
        boolean swap = rowMul != colMul;
        for (int[] ratio : RATIOS) {
            int newRow = row;
            int newCol = col;
            while (true) {
                if (swap) {
                    newRow += ratio[1] * rowMul;
                    newCol += ratio[0] * colMul;
                } else {
                    newRow += ratio[0] * rowMul;
                    newCol += ratio[1] * colMul;
                }
                if (!inBounds(grid, newRow, newCol)) {
                    break;
                }
                if (grid[newRow][newCol]) {
                    vaporized++;
                    grid[newRow][newCol] = false;
                    if (vaporized == 200) {
                        System.out.println("200th at " + newRow + " " + newCol);
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }

    static boolean vaporizeSingle(boolean[][] grid, int row, int col, int rowDiff, int colDiff) {
        int newRow = row;
        int newCol = col;
        while (true) {
            newRow += rowDiff;
            newCol += colDiff;
            if (!inBounds(grid, newRow, newCol)) {
                break;
            }
            if (grid[newRow][newCol]) {
                vaporized++;
                grid[newRow][newCol] = false;
                if (vaporized == 200) {
                    System.out.println("200th at " + newRow + " " + newCol);
                    return true;
                }
                break;
            }
        }
        return false;
    }

    static int count(boolean[][] grid, int row, int col, int rowMul, int colMul) {
        int count = 0;
        for (int[] ratio : RATIOS) {
            int newRow = row;
            int newCol = col;
            while (true) {
                newRow += ratio[0] * rowMul;
                newCol += ratio[1] * colMul;
                if (!inBounds(grid, newRow, newCol)) {
                    break;
                }
                if (grid[newRow][newCol]) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    static int countSingle(boolean[][] grid, int row, int col, int rowDiff, int colDiff) {
        int count = 0;
        int newRow = row;
        int newCol = col;
        while (true) {
            newRow += rowDiff;
            newCol += colDiff;
            if (!inBounds(grid, newRow, newCol)) {
                break;
            }
            if (grid[newRow][newCol]) {
                count++;
                break;
            }
        }
        return count;
    }

    static boolean inBounds(boolean[][] grid, int row, int col) {
        if (row < 0 || col < 0) {
            return false;
        }
        if (row >= grid.length) {
            return false;
        }
        if (col >= grid[row].length) {
            return false;
        }
        return true;
    }

    static boolean[][] getGrid() {
        String[] rows = INPUT.split("\n");
        boolean[][] grid = new boolean[rows.length][rows[0].length()];
        for (int row = 0; row < grid.length; row++) {
            String s = rows[row];
            for (int col = 0; col < s.length(); col++) {
                grid[row][col] = s.charAt(col) == '#';
            }
        }
        return grid;
    }

    static boolean areCoPrime(int a, int b) {
        for (int i = 2; i <= a && i <= b; i++) {
            if (a % i == 0 && b % i == 0) {
                return false;
            }
        }
        return true;
    }

    // 23 x 23
    static final String INPUT =
            ".###..#......###..#...#\n" +
                    "#.#..#.##..###..#...#.#\n" +
                    "#.#.#.##.#..##.#.###.##\n" +
                    ".#..#...####.#.##..##..\n" +
                    "#.###.#.####.##.#######\n" +
                    "..#######..##..##.#.###\n" +
                    ".##.#...##.##.####..###\n" +
                    "....####.####.#########\n" +
                    "#.########.#...##.####.\n" +
                    ".#.#..#.#.#.#.##.###.##\n" +
                    "#..#.#..##...#..#.####.\n" +
                    ".###.#.#...###....###..\n" +
                    "###..#.###..###.#.###.#\n" +
                    "...###.##.#.##.#...#..#\n" +
                    "#......#.#.##..#...#.#.\n" +
                    "###.##.#..##...#..#.#.#\n" +
                    "###..###..##.##..##.###\n" +
                    "###.###.####....######.\n" +
                    ".###.#####.#.#.#.#####.\n" +
                    "##.#.###.###.##.##..##.\n" +
                    "##.#..#..#..#.####.#.#.\n" +
                    ".#.#.#.##.##########..#\n" +
                    "#####.##......#.#.####.";

    static final int[][] RATIOS = new int[][]{
            {1, 22},
            {1, 21},
            {1, 20},
            {1, 19},
            {1, 18},
            {1, 17},
            {1, 16},
            {1, 15},
            {1, 14},
            {1, 13},
            {1, 12},
            {1, 11},
            {2, 21},
            {1, 10},
            {2, 19},
            {1, 9},
            {2, 17},
            {1, 8},
            {2, 15},
            {3, 22},
            {1, 7},
            {3, 20},
            {2, 13},
            {3, 19},
            {1, 6},
            {3, 17},
            {2, 11},
            {3, 16},
            {4, 21},
            {1, 5},
            {4, 19},
            {3, 14},
            {2, 9},
            {5, 22},
            {3, 13},
            {4, 17},
            {5, 21},
            {1, 4},
            {5, 19},
            {4, 15},
            {3, 11},
            {5, 18},
            {2, 7},
            {5, 17},
            {3, 10},
            {4, 13},
            {5, 16},
            {6, 19},
            {7, 22},
            {1, 3},
            {7, 20},
            {6, 17},
            {5, 14},
            {4, 11},
            {7, 19},
            {3, 8},
            {8, 21},
            {5, 13},
            {7, 18},
            {2, 5},
            {9, 22},
            {7, 17},
            {5, 12},
            {8, 19},
            {3, 7},
            {7, 16},
            {4, 9},
            {9, 20},
            {5, 11},
            {6, 13},
            {7, 15},
            {8, 17},
            {9, 19},
            {10, 21},
            {1, 2},
            {11, 21},
            {10, 19},
            {9, 17},
            {8, 15},
            {7, 13},
            {6, 11},
            {11, 20},
            {5, 9},
            {9, 16},
            {4, 7},
            {11, 19},
            {7, 12},
            {10, 17},
            {13, 22},
            {3, 5},
            {11, 18},
            {8, 13},
            {13, 21},
            {5, 8},
            {12, 19},
            {7, 11},
            {9, 14},
            {11, 17},
            {13, 20},
            {2, 3},
            {15, 22},
            {13, 19},
            {11, 16},
            {9, 13},
            {7, 10},
            {12, 17},
            {5, 7},
            {13, 18},
            {8, 11},
            {11, 15},
            {14, 19},
            {3, 4},
            {16, 21},
            {13, 17},
            {10, 13},
            {17, 22},
            {7, 9},
            {11, 14},
            {15, 19},
            {4, 5},
            {17, 21},
            {13, 16},
            {9, 11},
            {14, 17},
            {5, 6},
            {16, 19},
            {11, 13},
            {17, 20},
            {6, 7},
            {19, 22},
            {13, 15},
            {7, 8},
            {15, 17},
            {8, 9},
            {17, 19},
            {9, 10},
            {19, 21},
            {10, 11},
            {11, 12},
            {12, 13},
            {13, 14},
            {14, 15},
            {15, 16},
            {16, 17},
            {17, 18},
            {18, 19},
            {19, 20},
            {20, 21},
            {21, 22},
            {1, 1},
            {22, 21},
            {21, 20},
            {20, 19},
            {19, 18},
            {18, 17},
            {17, 16},
            {16, 15},
            {15, 14},
            {14, 13},
            {13, 12},
            {12, 11},
            {11, 10},
            {21, 19},
            {10, 9},
            {19, 17},
            {9, 8},
            {17, 15},
            {8, 7},
            {15, 13},
            {22, 19},
            {7, 6},
            {20, 17},
            {13, 11},
            {19, 16},
            {6, 5},
            {17, 14},
            {11, 9},
            {16, 13},
            {21, 17},
            {5, 4},
            {19, 15},
            {14, 11},
            {9, 7},
            {22, 17},
            {13, 10},
            {17, 13},
            {21, 16},
            {4, 3},
            {19, 14},
            {15, 11},
            {11, 8},
            {18, 13},
            {7, 5},
            {17, 12},
            {10, 7},
            {13, 9},
            {16, 11},
            {19, 13},
            {22, 15},
            {3, 2},
            {20, 13},
            {17, 11},
            {14, 9},
            {11, 7},
            {19, 12},
            {8, 5},
            {21, 13},
            {13, 8},
            {18, 11},
            {5, 3},
            {22, 13},
            {17, 10},
            {12, 7},
            {19, 11},
            {7, 4},
            {16, 9},
            {9, 5},
            {20, 11},
            {11, 6},
            {13, 7},
            {15, 8},
            {17, 9},
            {19, 10},
            {21, 11},
            {2, 1},
            {21, 10},
            {19, 9},
            {17, 8},
            {15, 7},
            {13, 6},
            {11, 5},
            {20, 9},
            {9, 4},
            {16, 7},
            {7, 3},
            {19, 8},
            {12, 5},
            {17, 7},
            {22, 9},
            {5, 2},
            {18, 7},
            {13, 5},
            {21, 8},
            {8, 3},
            {19, 7},
            {11, 4},
            {14, 5},
            {17, 6},
            {20, 7},
            {3, 1},
            {22, 7},
            {19, 6},
            {16, 5},
            {13, 4},
            {10, 3},
            {17, 5},
            {7, 2},
            {18, 5},
            {11, 3},
            {15, 4},
            {19, 5},
            {4, 1},
            {21, 5},
            {17, 4},
            {13, 3},
            {22, 5},
            {9, 2},
            {14, 3},
            {19, 4},
            {5, 1},
            {21, 4},
            {16, 3},
            {11, 2},
            {17, 3},
            {6, 1},
            {19, 3},
            {13, 2},
            {20, 3},
            {7, 1},
            {22, 3},
            {15, 2},
            {8, 1},
            {17, 2},
            {9, 1},
            {19, 2},
            {10, 1},
            {21, 2},
            {11, 1},
            {12, 1},
            {13, 1},
            {14, 1},
            {15, 1},
            {16, 1},
            {17, 1},
            {18, 1},
            {19, 1},
            {20, 1},
            {21, 1},
            {22, 1}
    };
}
