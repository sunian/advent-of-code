package adventofcode;

/**
 * Created by Sun on 12/16/2019.
 * Flawed Frequency Transmission
 */
public class Day16 {
    static int[] NUMS;
    static int[] pattern = new int[]{0, 1, 0, -1};

    public static void main(String[] args) {
        NUMS = new int[INPUT.length()];
        for (int i = 0; i < NUMS.length; i++) {
            NUMS[i] = Integer.parseInt("" + INPUT.charAt(i));
        }
        System.out.println(part1(NUMS));
        int[] part2 = new int[NUMS.length * (10000 / 2)];
        for (int i = 0; i < part2.length; i++) {
            part2[i] = NUMS[i % NUMS.length];
        }
        String ref = part2(part2, 5970951 - NUMS.length * 10000 / 2);
        System.out.println(ref);
    }

    static String part1(int[] input) {
        for (int i = 0; i < 100; i++) {
            input = doFFT(input);
        }
        String s = "";
        for (int i = 0; i < 8; i++) {
            s += input[i];
        }
        return s;
    }

    static String part2(int[] input, int offset) {
        for (int i = 0; i < 100; i++) {
            input = doHalfFFT(input);
        }
        String s = "";
        for (int i = 0; i < 8; i++) {
            s += input[i + offset];
        }
        return s;
    }

    static int[] doHalfFFT(int[] input) {
        int[] output = new int[input.length];
        long total = 0;
        for (int i = output.length - 1; i >= 0; i--) {
            total += input[i];
            output[i] = (int) Math.abs(total % 10);
        }
        return output;
    }

    static int[] doFFT(int[] input) {
        int[] output = new int[input.length];
        for (int dup = 1; dup <= output.length; dup++) {
            long total = 0;
            for (int i = 0; i < input.length; i++) {
                int multiplier = pattern[((i + 1) / (dup)) % pattern.length];
                total = (total + input[i] * multiplier);
            }
            int digit = (int) (total % 10);
            output[dup - 1] = Math.abs(digit);
        }
        return output;
    }

    static final String INPUT = "59709511599794439805414014219880358445064269099345553494818286560304063399998657801629526113732466767578373307474609375929817361595469200826872565688108197109235040815426214109531925822745223338550232315662686923864318114370485155264844201080947518854684797571383091421294624331652208294087891792537136754322020911070917298783639755047408644387571604201164859259810557018398847239752708232169701196560341721916475238073458804201344527868552819678854931434638430059601039507016639454054034562680193879342212848230089775870308946301489595646123293699890239353150457214490749319019572887046296522891429720825181513685763060659768372996371503017206185697";
}
