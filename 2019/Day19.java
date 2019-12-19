package adventofcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sun on 12/18/2019.
 * Tractor Beam
 */
public class Day19 {
    public static void main(String[] args) {
        System.out.println(part1());
        System.out.println(part2());
    }

    static int part2() {
        long y = 100;
        long x = 0;
        List<Bounds> boundsList = new ArrayList<>();
        for (long i = 0; i < y; i++) {
            boundsList.add(new Bounds(0, 0));
        }
        List<Long> coords = new ArrayList<>();

        while (true) {
            x = boundsList.get(boundsList.size() - 1).start;
            int start = -1;
            while (true) {
                Day9.Computer computer = new Day9.Computer(INPUT);
                coords.clear();
                coords.add(x);
                coords.add(y);
                Day9.Computer.Output output = computer.compute(coords);
                Long answer = output.output.get(0);
                if (answer > 0) {
                    if (start < 0) {
                        start = (int) x;
                    }
                } else if (start > 0) {
                    boundsList.add(new Bounds(start, (int) (x - 1)));
                    break;
                }
                x++;
            }
            Bounds newBounds = boundsList.get(boundsList.size() - 1);
            if (newBounds.size() >= 100) {
                Bounds prevBounds = boundsList.get(boundsList.size() - 100);
                if (prevBounds.isInBounds(newBounds.start) && prevBounds.isInBounds(newBounds.start + 99)) {
                    return newBounds.start * 10000 + (boundsList.size() - 100);
                }
            }
            y++;
        }
    }

    static int part1() {
        int size = 50;
        int count = 0;

        List<Long> coords = new ArrayList<>();
        for (long y = 0; y < size; y++) {
            for (long x = 0; x < size; x++) {
                Day9.Computer computer = new Day9.Computer(INPUT);
                coords.clear();
                coords.add(x);
                coords.add(y);
                Day9.Computer.Output output = computer.compute(coords);
                Long answer = output.output.get(0);
                count += answer;
                System.out.print(answer);
            }
            System.out.println();
        }
        return count;
    }

    static class Bounds {
        int start;
        int end;

        public Bounds(int start, int end) {
            this.start = start;
            this.end = end;
        }

        boolean isInBounds(int x) {
            return x >= start && x <= end;
        }

        int size() {
            return end + 1 - start;
        }
    }

    static final long[] INPUT = new long[]{
            109, 424, 203, 1, 21101, 11, 0, 0, 1105, 1, 282, 21102, 1, 18, 0, 1105, 1, 259, 2102, 1, 1, 221, 203, 1, 21102, 1, 31, 0, 1106, 0, 282, 21101, 38, 0, 0, 1105, 1, 259, 20102, 1, 23, 2, 21202, 1, 1, 3, 21102, 1, 1, 1, 21102, 57, 1, 0, 1105, 1, 303, 2102, 1, 1, 222, 21002, 221, 1, 3, 21002, 221, 1, 2, 21102, 1, 259, 1, 21102, 1, 80, 0, 1105, 1, 225, 21102, 145, 1, 2, 21102, 91, 1, 0, 1106, 0, 303, 1201, 1, 0, 223, 20102, 1, 222, 4, 21102, 259, 1, 3, 21101, 225, 0, 2, 21102, 1, 225, 1, 21101, 0, 118, 0, 1105, 1, 225, 21001, 222, 0, 3, 21101, 91, 0, 2, 21102, 1, 133, 0, 1106, 0, 303, 21202, 1, -1, 1, 22001, 223, 1, 1, 21102, 148, 1, 0, 1105, 1, 259, 1201, 1, 0, 223, 21001, 221, 0, 4, 20101, 0, 222, 3, 21101, 20, 0, 2, 1001, 132, -2, 224, 1002, 224, 2, 224, 1001, 224, 3, 224, 1002, 132, -1, 132, 1, 224, 132, 224, 21001, 224, 1, 1, 21102, 195, 1, 0, 105, 1, 109, 20207, 1, 223, 2, 20101, 0, 23, 1, 21102, -1, 1, 3, 21101, 0, 214, 0, 1105, 1, 303, 22101, 1, 1, 1, 204, 1, 99, 0, 0, 0, 0, 109, 5, 1202, -4, 1, 249, 22101, 0, -3, 1, 22102, 1, -2, 2, 21202, -1, 1, 3, 21102, 1, 250, 0, 1106, 0, 225, 21201, 1, 0, -4, 109, -5, 2105, 1, 0, 109, 3, 22107, 0, -2, -1, 21202, -1, 2, -1, 21201, -1, -1, -1, 22202, -1, -2, -2, 109, -3, 2106, 0, 0, 109, 3, 21207, -2, 0, -1, 1206, -1, 294, 104, 0, 99, 21201, -2, 0, -2, 109, -3, 2105, 1, 0, 109, 5, 22207, -3, -4, -1, 1206, -1, 346, 22201, -4, -3, -4, 21202, -3, -1, -1, 22201, -4, -1, 2, 21202, 2, -1, -1, 22201, -4, -1, 1, 21201, -2, 0, 3, 21102, 343, 1, 0, 1106, 0, 303, 1105, 1, 415, 22207, -2, -3, -1, 1206, -1, 387, 22201, -3, -2, -3, 21202, -2, -1, -1, 22201, -3, -1, 3, 21202, 3, -1, -1, 22201, -3, -1, 2, 22101, 0, -4, 1, 21101, 384, 0, 0, 1105, 1, 303, 1105, 1, 415, 21202, -4, -1, -4, 22201, -4, -3, -4, 22202, -3, -2, -2, 22202, -2, -4, -4, 22202, -3, -2, -3, 21202, -4, -1, -2, 22201, -3, -2, 1, 21202, 1, 1, -4, 109, -5, 2105, 1, 0
    };
}
