package adventofcode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sun on 12/10/2019.
 * Space Police
 */
public class Day11 {
    public static void main(String[] args) {
        System.out.println(part1());
        part2();
    }

    // up is 0, right is 1, down is 2, left is 3
    static void part2() {
        Day9.Computer computer = new Day9.Computer(PROGRAM);
        int x = 0;
        int y = 0;
        int dir = 0;
        int minX = x;
        int maxX = x;
        int minY = y;
        int maxY = y;
        Map<String, Integer> colors = new HashMap<>();
        colors.put(x + "," + y, 1);
        Day9.Computer.Output output = computer.compute(Collections.emptyList());
        while (true) {
            boolean shouldPaint = true;
            for (Long num : output.output) {
                if (shouldPaint) {
                    colors.put(x + "," + y, num.intValue());
                    minX = Math.min(minX, x);
                    maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y);
                    maxY = Math.max(maxY, y);
                } else {
                    if (num == 0) {
                        dir = (dir + 3) % 4;
                    } else {
                        dir = (dir + 1) % 4;
                    }
                    switch (dir) {
                        case 0:
                            y++;
                            break;
                        case 1:
                            x++;
                            break;
                        case 2:
                            y--;
                            break;
                        case 3:
                            x--;
                            break;
                    }
                }
                shouldPaint = !shouldPaint;
            }
            if (output.halted) {
                break;
            } else {
                Integer color = colors.get(x + "," + y);
                if (color == null) {
                    color = 0;
                }
                output = computer.compute(Collections.singletonList(color.longValue()));
            }
        }
        for (int yy = maxY; yy >= minY; yy--) {
            for (int xx = minX; xx <= maxX; xx++) {
                Integer color = colors.get(xx + "," + yy);
                if (color == null || color == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print("##");
                }
            }
            System.out.println();
        }
    }

    static int part1() {
        Day9.Computer computer = new Day9.Computer(PROGRAM);
        int x = 0;
        int y = 0;
        int dir = 0;
        Map<String, Integer> colors = new HashMap<>();
        Day9.Computer.Output output = computer.compute(Collections.emptyList());
        while (true) {
            boolean shouldPaint = true;
            for (Long num : output.output) {
                if (shouldPaint) {
                    colors.put(x + "," + y, num.intValue());
                } else {
                    if (num == 0) {
                        dir = (dir + 3) % 4;
                    } else {
                        dir = (dir + 1) % 4;
                    }
                    switch (dir) {
                        case 0:
                            y++;
                            break;
                        case 1:
                            x++;
                            break;
                        case 2:
                            y--;
                            break;
                        case 3:
                            x--;
                            break;
                    }
                }
                shouldPaint = !shouldPaint;
            }
            if (output.halted) {
                break;
            } else {
                Integer color = colors.get(x + "," + y);
                if (color == null) {
                    color = 0;
                }
                output = computer.compute(Collections.singletonList(color.longValue()));
            }
        }
        return colors.size();
    }

    static final long[] PROGRAM = new long[]{3, 8, 1005, 8, 344, 1106, 0, 11, 0, 0, 0, 104, 1, 104, 0, 3, 8, 1002, 8, -1, 10, 1001, 10, 1, 10, 4, 10, 108, 1, 8, 10, 4, 10, 101, 0, 8, 28, 1006, 0, 40, 2,
            1009, 2, 10, 1, 1108, 13, 10, 1, 1007, 6, 10, 3, 8, 1002, 8, -1, 10, 101, 1, 10, 10, 4, 10, 1008, 8, 1, 10, 4, 10, 1002, 8, 1, 66, 1006, 0, 91, 2, 103, 5, 10, 1006, 0, 12, 3, 8, 102, -1, 8, 10,
            1001, 10, 1, 10, 4, 10, 1008, 8, 0, 10, 4, 10, 102, 1, 8, 98, 3, 8, 102, -1, 8, 10, 1001, 10, 1, 10, 4, 10, 1008, 8, 0, 10, 4, 10, 102, 1, 8, 120, 1, 1001, 15, 10, 2, 102, 4, 10, 3, 8, 1002, 8, -1,
            10, 101, 1, 10, 10, 4, 10, 108, 0, 8, 10, 4, 10, 1002, 8, 1, 149, 1, 106, 9, 10, 1, 5, 5, 10, 1, 1106, 6, 10, 2, 5, 15, 10, 3, 8, 102, -1, 8, 10, 101, 1, 10, 10, 4, 10, 108, 1, 8, 10, 4, 10, 1001, 8,
            0, 187, 2, 1106, 9, 10, 2, 9, 13, 10, 3, 8, 1002, 8, -1, 10, 101, 1, 10, 10, 4, 10, 1008, 8, 1, 10, 4, 10, 1002, 8, 1, 218, 1, 1106, 3, 10, 1006, 0, 13, 2, 1005, 15, 10, 2, 1006, 19, 10, 3, 8, 102,
            -1, 8, 10, 101, 1, 10, 10, 4, 10, 108, 0, 8, 10, 4, 10, 1002, 8, 1, 254, 2, 1108, 14, 10, 1006, 0, 33, 1, 7, 20, 10, 2, 105, 6, 10, 3, 8, 1002, 8, -1, 10, 1001, 10, 1, 10, 4, 10, 1008, 8, 1, 10, 4,
            10, 101, 0, 8, 292, 1006, 0, 82, 2, 6, 7, 10, 3, 8, 102, -1, 8, 10, 101, 1, 10, 10, 4, 10, 108, 0, 8, 10, 4, 10, 1001, 8, 0, 320, 1006, 0, 11, 101, 1, 9, 9, 1007, 9, 979, 10, 1005, 10, 15, 99, 109,
            666, 104, 0, 104, 1, 21102, 932700857100L, 1, 1, 21101, 0, 361, 0, 1106, 0, 465, 21102, 825599210392L, 1, 1, 21102, 1, 372, 0, 1106, 0, 465, 3, 10, 104, 0, 104, 1, 3, 10, 104, 0, 104, 0, 3, 10, 104,
            0, 104, 1, 3, 10, 104, 0, 104, 1, 3, 10, 104, 0, 104, 0, 3, 10, 104, 0, 104, 1, 21101, 29195529219L, 0, 1, 21101, 419, 0, 0, 1106, 0, 465, 21101, 0, 235324673063L, 1, 21102, 1, 430, 0, 1105, 1, 465,
            3, 10, 104, 0, 104, 0, 3, 10, 104, 0, 104, 0, 21102, 988225098508L, 1, 1, 21102, 453, 1, 0, 1106, 0, 465, 21102, 988753318756L, 1, 1, 21101, 464, 0, 0, 1106, 0, 465, 99, 109, 2, 22101, 0, -1, 1, 21102,
            40, 1, 2, 21101, 0, 496, 3, 21101, 0, 486, 0, 1105, 1, 529, 109, -2, 2106, 0, 0, 0, 1, 0, 0, 1, 109, 2, 3, 10, 204, -1, 1001, 491, 492, 507, 4, 0, 1001, 491, 1, 491, 108, 4, 491, 10, 1006, 10, 523,
            1101, 0, 0, 491, 109, -2, 2106, 0, 0, 0, 109, 4, 2102, 1, -1, 528, 1207, -3, 0, 10, 1006, 10, 546, 21102, 0, 1, -3, 21201, -3, 0, 1, 22102, 1, -2, 2, 21101, 0, 1, 3, 21102, 565, 1, 0, 1105, 1, 570,
            109, -4, 2105, 1, 0, 109, 5, 1207, -3, 1, 10, 1006, 10, 593, 2207, -4, -2, 10, 1006, 10, 593, 22102, 1, -4, -4, 1105, 1, 661, 22101, 0, -4, 1, 21201, -3, -1, 2, 21202, -2, 2, 3, 21101, 612, 0, 0,
            1106, 0, 570, 22101, 0, 1, -4, 21102, 1, 1, -1, 2207, -4, -2, 10, 1006, 10, 631, 21101, 0, 0, -1, 22202, -2, -1, -2, 2107, 0, -3, 10, 1006, 10, 653, 21202, -1, 1, 1, 21101, 0, 653, 0, 106, 0, 528,
            21202, -2, -1, -2, 22201, -4, -2, -4, 109, -5, 2105, 1, 0};
}
