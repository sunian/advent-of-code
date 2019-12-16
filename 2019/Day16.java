package adventofcode;

/**
 * Created by Sun on 12/16/2019.
 * Flawed Frequency Transmission
 */
public class Day16 {
    static int[] NUMS;
    static int[] pattern = new int[]{0, 1, 0, -1};
    static int end = 0;
    static int start = 0;

    public static void main(String[] args) {
        NUMS = new int[INPUT.length()];
        for (int i = 0; i < NUMS.length; i++) {
            NUMS[i] = Integer.parseInt("" + INPUT.charAt(i));
        }
        int[] part2 = new int[NUMS.length * (10000 - 9186)];
        for (int i = 0; i < part2.length; i++) {
            part2[i] = NUMS[i % NUMS.length];
        }
        end = part2.length;
//        start = 23;
//        end = 24 + 8;
        System.out.println(INPUT.length() * 10000);
        System.out.println(5970951);
        System.out.println(INPUT.length() * 10000 - 5970951);
        System.out.println(5970951 % INPUT.length());
        String ref = part2(part2);
        System.out.println(ref);
        // skip first 9186 copies
/*
        while (true) {
            String output = part2(part2);
            if (output.equals(ref)) {
                System.out.println("yes " + end);
            } else {
                System.out.println("no  " + end);
            }
            end++;
        }
*/
//        System.out.println(part2(part2));
//        System.out.println(part2(NUMS));
//        System.out.println(Arrays.toString(part1(new int[]{0, 3, 0, 3, 6, 7, 3, 2, 5, 7, 7, 2, 1, 2, 9, 4, 4, 0, 6, 3, 4, 9, 1, 5, 6, 5, 4, 7, 4, 6, 6, 4})));
//        System.out.println(doFFT(new int[20]));
    }

    static int[] part1(int[] input) {
        for (int i = 0; i < 100; i++) {
            input = doFFT(input);
        }
        return input;
    }

    static String part2(int[] input) {
        for (int i = 0; i < 100; i++) {
            input = doFFT(input);
            System.out.println("done with " + i);
        }
        String s = "";
        for (int i = 0; i < 8; i++) {
            s += input[i + 51]; // 5970951
        }
        return s;
    }

    /*static int part2(int[] input) {
        int count = 0;
        int[] temp = Arrays.copyOf(input, input.length);
        while (true) {
            temp = doFFT(temp);
            count++;
            if (Arrays.equals(input, temp)) {
                break;
            }
        }
        return count;
    }*/

    static int[] doFFT(int[] input) {
        int[] output = new int[input.length];
        for (int dup = 1; dup <= output.length; dup++) {
            long total = 0;
            for (int i = start; i < end; i++) {
                int multiplier = pattern[((i + 1) / dup) % pattern.length];
//                System.out.printf("\t%d", multiplier);
                total = (total + input[i] * multiplier);
            }
//            System.out.println();
            int digit = (int) (total % 10);
            output[dup - 1] = Math.abs(digit);
        }
/*
        for (int i = 0; i < 1; i++) {
            output[i] = 0;
        }
*/
        return output;
    }
// 46886713

    static final String INPUT = "59709511599794439805414014219880358445064269099345553494818286560304063399998657801629526113732466767578373307474609375929817361595469200826872565688108197109235040815426214109531925822745223338550232315662686923864318114370485155264844201080947518854684797571383091421294624331652208294087891792537136754322020911070917298783639755047408644387571604201164859259810557018398847239752708232169701196560341721916475238073458804201344527868552819678854931434638430059601039507016639454054034562680193879342212848230089775870308946301489595646123293699890239353150457214490749319019572887046296522891429720825181513685763060659768372996371503017206185697";
}
