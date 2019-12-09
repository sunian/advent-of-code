package adventofcode;

/**
 * Created by Sun on 12/4/2019.
 * Secure Container
 */
public class Day4 {

    public static void main(String[] args) {
        System.out.println(part2());
    }

    static int part2() {
        int count = 0;
        for (int num = MIN; num <= MAX; num++) {
            int repeatCount = 0;
            boolean hasDouble = false;
            boolean monotonic = true;
            char[] chars = String.valueOf(num).toCharArray();
            for (int i = 1; i < chars.length; i++) {
                char c = chars[i];
                char prev = chars[i - 1];
                if (c < prev) {
                    monotonic = false;
                    break;
                }
                if (c == prev) {
                    repeatCount++;
                } else {
                    if (repeatCount == 1) {
                        hasDouble = true;
                    }
                    repeatCount = 0;
                }
            }
            if (repeatCount == 1) {
                hasDouble = true;
            }
            if (hasDouble && monotonic) {
                count++;
            }
        }
        return count;
    }

    static int part1() {
        int count = 0;
        for (int num = MIN; num <= MAX; num++) {
            boolean hasDouble = false;
            boolean monotonic = true;
            char[] chars = String.valueOf(num).toCharArray();
            for (int i = 1; i < chars.length; i++) {
                char c = chars[i];
                char prev = chars[i - 1];
                if (c == prev) {
                    hasDouble = true;
                } else if (c < prev) {
                    monotonic = false;
                    break;
                }
            }
            if (hasDouble && monotonic) {
                count++;
            }
        }
        return count;
    }

    static final int MIN = 307237;
    static final int MAX = 769058;
}
