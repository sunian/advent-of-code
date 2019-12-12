package adventofcode;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sun on 12/11/2019.
 * The N-Body Problem
 */
public class Day12 {
    public static void main(String[] args) {
//        System.out.println(part1());
        printPrimeFactors(167624);
        printPrimeFactors(231614);
        printPrimeFactors(96236);
        // answer to part2 is 8*23*911*115807*49*491
    }

    // x takes 167624
    // y takes 231614
    // z takes 96236

    static void printPrimeFactors(int n) {
        System.out.print(n + " = ");
        int left = n;
        for (int factor = 2; factor <= left; factor++) {
            int count = 0;
            while (left % factor == 0) {
                count++;
                left /= factor;
            }
            if (count > 0) {
                System.out.printf("%d^%d ", factor, count);
            }
        }
        System.out.println();
    }

    static int part2() {
        int steps = 0;
        Set<String> states = new HashSet<>();
        while (true) {
            states.add(getState(2));
            for (Moon moon : MOONS) {
                for (Moon other : MOONS) {
                    if (other == moon) {
                        continue;
                    }
                    moon.updateVelocity(other);
                }
            }
            for (Moon moon : MOONS) {
                moon.applyVelocity();
            }
            steps++;
            if (states.contains(getState(2))) {
                break;
            }
        }
        return steps;
    }

    static String getState(int dimen) {
        StringBuilder sb = new StringBuilder();
        for (Moon moon : MOONS) {
            sb.append(moon.getState(dimen));
            sb.append('|');
        }
        return sb.toString();
    }

    static int part1() {
        for (int step = 0; step < 1000; step++) {
            for (Moon moon : MOONS) {
                for (Moon other : MOONS) {
                    if (other == moon) {
                        continue;
                    }
                    moon.updateVelocity(other);
                }
            }
            for (Moon moon : MOONS) {
                moon.applyVelocity();
            }
        }
        int total = 0;
        for (Moon moon : MOONS) {
            total += moon.getTotalEnergy();
        }
        return total;
    }

    static final class Moon {
        final int[] position;
        final int[] velocity = new int[3];

        Moon(int[] position) {
            this.position = position;
        }

        void updateVelocity(Moon moon) {
            for (int i = 0; i < velocity.length; i++) {
                int diff = moon.position[i] - position[i];
                if (diff > 0) {
                    velocity[i]++;
                } else if (diff < 0) {
                    velocity[i]--;
                }
            }
        }

        void applyVelocity() {
            for (int i = 0; i < position.length; i++) {
                position[i] += velocity[i];
            }
        }

        int getPotentialEnergy() {
            int total = 0;
            for (int i : position) {
                total += Math.abs(i);
            }
            return total;
        }

        int getKineticEnergy() {
            int total = 0;
            for (int i : velocity) {
                total += Math.abs(i);
            }
            return total;
        }

        int getTotalEnergy() {
            return getPotentialEnergy() * getKineticEnergy();
        }

        String getState(int dimen) {
            return String.format("[%d,%d]", position[dimen], velocity[dimen]);
        }

    }

    static final Moon[] MOONS = new Moon[]{
            new Moon(new int[]{4, 12, 13}),
            new Moon(new int[]{-9, 14, -3}),
            new Moon(new int[]{-7, -1, 2}),
            new Moon(new int[]{-11, 17, -1}),
    };
}
