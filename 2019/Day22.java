package adventofcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sun on 12/21/2019.
 * Slam Shuffle
 */
public class Day22 {

    public static void main(String[] args) {
//        part1(2019);
//        part12(2020);
//        computeCycles();
        for (int[] pair : pairs) {
            int diff = pair[0] - pair[1];
/*
            if (diff == pair[1] + 1) {
                System.out.printf("%d : half\n", pair[0]);
            } else if (diff == 1) {
                System.out.printf("%d : one less\n", pair[0]);
            } else {
//                System.out.printf("%d : %d\t, %d\n", pair[0], pair[1], diff);
            }
*/

//            System.out.printf("%d : %f\t %d\n", pair[0], diff * 1.0 / pair[1], pair[0] % 2020);
            System.out.printf("%d : %f\t %d\n", pair[0], pair[0] * 1.0 / pair[1], pair[0] % 2020);
        }
    }

    static void computeCycles() {
        for (long total : totals) {
            System.out.print(total + " , ");
            whatsAt(2020, total);
        }
    }
    // 10007 -> 5003
    // 15551 -> 7775
    // 20047 -> 10023
    // 23173 -> 7724
    // 89477 -> 89476
    // 89491 -> 2355

    static void whatsAt(int target, long total) {
//        long total = 119315717514047L;
        long times = 101741582076661L;
//        total = 89491;
        long position = target;
        for (long i = 0; i < 99910000; i++) {
            if (i > 0 && position == 2020) {
                System.out.println(i);
                return;
            }
            for (int rr = INPUT.length - 1; rr >= 0; rr--) {
                String step = INPUT[rr];
                String[] split = step.split(" ");
                String lastPart = split[split.length - 1];
                if (step.startsWith("deal with increment")) {
                    int inc = Integer.parseInt(lastPart);
                    position = dealWithInc(total, position, inc);
                } else if (step.startsWith("deal into new stack")) {
                    position = total - 1 - position;
                } else if (step.startsWith("cut")) {
                    int n = Integer.parseInt(lastPart);
                    position = (position + n + total) % total;
                }
            }
//            System.out.println(position);
        }
    }

    static void whereIs(int target) {
        long total = 119315717514047L;
        long times = 101741582076661L;
        total = 10007;
        long position = target;
        for (long i = 0; i < 1; i++) {
            for (String step : INPUT) {
                String[] split = step.split(" ");
                String lastPart = split[split.length - 1];
                if (step.startsWith("deal with increment")) {
                    int inc = Integer.parseInt(lastPart);
                    position = (position * inc) % total;
                } else if (step.startsWith("deal into new stack")) {
                    position = total - 1 - position;
                } else if (step.startsWith("cut")) {
                    int n = Integer.parseInt(lastPart);
                    position = (total + position - n) % total;
                }
            }
        }
        System.out.println(position);
    }

    static void part12(int target) {
        int total = 10007;
        List<Integer> cards = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            cards.add(i);
        }
        for (int i = 0; i < 1; i++) {
            for (String step : INPUT) {
                String[] split = step.split(" ");
                String lastPart = split[split.length - 1];
                if (step.startsWith("deal with increment")) {
                    int inc = Integer.parseInt(lastPart);
                    dealWithInc(cards, inc);
                } else if (step.startsWith("deal into new stack")) {
                    dealIntoNewStack(cards);
                } else if (step.startsWith("cut")) {
                    int n = Integer.parseInt(lastPart);
                    cut(cards, n);
                }
            }
            System.out.println(cards.get(target));
        }
    }

    static void part1(int target) {
        List<Integer> cards = new ArrayList<>(10007);
        for (int i = 0; i < 10007; i++) {
            cards.add(i);
        }
        for (String step : INPUT) {
            String[] split = step.split(" ");
            String lastPart = split[split.length - 1];
            if (step.startsWith("deal with increment")) {
                int inc = Integer.parseInt(lastPart);
                dealWithInc(cards, inc);
            } else if (step.startsWith("deal into new stack")) {
                dealIntoNewStack(cards);
            } else if (step.startsWith("cut")) {
                int n = Integer.parseInt(lastPart);
                cut(cards, n);
            }
        }
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) == target) {
                System.out.println(i);
                break;
            }
        }
    }

    static void dealIntoNewStack(List<Integer> cards) {
        Collections.reverse(cards);
    }

    static void cut(List<Integer> cards, int n) {
        if (n > 0) {
            for (int i = 0; i < n; i++) {
                cards.add(cards.remove(0));
            }
        } else if (n < 0) {
            n = -n;
            for (int i = 0; i < n; i++) {
                cards.add(0, cards.remove(cards.size() - 1));
            }
        }
    }

    static void dealWithInc(List<Integer> cards, int inc) {
        int[] output = new int[cards.size()];
        int position = 0;
        while (!cards.isEmpty()) {
            output[position] = cards.remove(0);
            position = (position + inc) % output.length;
        }
        for (int i : output) {
            cards.add(i);
        }
    }

    static long dealWithInc(long total, long position, int inc) {
        long[] initial = new long[inc];
        initial[0] = 0;
        int start = inc;
        long value = 1;
        for (int i = 1; i < inc; i++) {
            value += (total - start) / inc + 1;
            start = (int) ((start + inc - total % inc) % inc);
            initial[start] = value;
        }
        return initial[(int) (position % inc)] + (position / inc);
    }

    static final String[] INPUT = new String[]{
            "deal with increment 16",
            "cut -7810",
            "deal with increment 70",
            "cut 8978",
            "deal into new stack",
            "deal with increment 14",
            "cut 9822",
            "deal with increment 31",
            "cut -3630",
            "deal with increment 37",
            "cut -929",
            "deal with increment 74",
            "cut -9268",
            "deal with increment 47",
            "cut -7540",
            "deal with increment 13",
            "cut -5066",
            "deal with increment 73",
            "cut 1605",
            "deal into new stack",
            "cut 1615",
            "deal with increment 72",
            "cut 1025",
            "deal with increment 28",
            "cut 6427",
            "deal with increment 10",
            "deal into new stack",
            "cut -8336",
            "deal with increment 33",
            "cut -9834",
            "deal with increment 64",
            "deal into new stack",
            "deal with increment 42",
            "cut 7013",
            "deal into new stack",
            "deal with increment 55",
            "deal into new stack",
            "cut 8349",
            "deal into new stack",
            "deal with increment 41",
            "cut -9073",
            "deal with increment 11",
            "deal into new stack",
            "deal with increment 46",
            "cut 613",
            "deal with increment 66",
            "cut 4794",
            "deal with increment 3",
            "cut -6200",
            "deal with increment 52",
            "deal into new stack",
            "cut 1328",
            "deal with increment 29",
            "cut -1670",
            "deal into new stack",
            "cut -706",
            "deal with increment 66",
            "cut -2827",
            "deal with increment 6",
            "cut 8493",
            "deal with increment 10",
            "deal into new stack",
            "deal with increment 75",
            "cut 3163",
            "deal with increment 14",
            "cut 4848",
            "deal with increment 66",
            "deal into new stack",
            "deal with increment 52",
            "deal into new stack",
            "deal with increment 71",
            "deal into new stack",
            "deal with increment 50",
            "cut -9466",
            "deal with increment 46",
            "cut -9621",
            "deal into new stack",
            "deal with increment 14",
            "deal into new stack",
            "cut 7236",
            "deal with increment 71",
            "cut -9836",
            "deal with increment 16",
            "deal into new stack",
            "cut -1519",
            "deal with increment 53",
            "cut -5190",
            "deal with increment 68",
            "cut 4313",
            "deal into new stack",
            "deal with increment 39",
            "deal into new stack",
            "deal with increment 5",
            "cut 3476",
            "deal with increment 61",
            "cut -2533",
            "deal with increment 10",
            "cut 4921",
            "deal with increment 67",
            "cut 3563"
    };

    static final int[][] pairs = new int[][]{
            {101107, 50553},
            {101111, 101110},
            {101113, 16852},
            {101117, 50558},
            {101119, 50559},
            {101141, 50570},
            {101149, 101148},
            {101159, 101158},
            {101161, 20232},
            {101173, 50586},
            {101183, 101182},
            {101197, 101196},
            {101203, 50601},
            {101207, 101206},
            {101209, 101208},
            {101221, 33740},
            {101267, 50633},
            {101273, 101272},
            {101279, 101278},
            {101281, 33760},
            {101287, 50643},
            {101293, 25323},
            {101323, 50661},
            {101333, 50666},
            {101341, 33780},
            {101347, 33782},
            {101359, 3754},
            {101363, 101362},
            {101377, 16896},
            {101383, 101382},
            {101399, 101398},
            {101411, 50705},
            {101419, 33806},
            {101429, 50714},
            {101449, 33816},
            {101467, 11274},
            {101477, 25369},
            {101483, 50741},
            {101489, 101488},
            {101501, 50750},
            {101503, 50751},
            {101513, 101512},
            {101527, 50763},
            {101531, 50765},
            {10103, 5051},
            {10111, 5055},
            {10133, 10132},
            {10139, 5069},
            {10141, 10140},
            {10151, 1450},
            {10159, 10158},
            {10163, 5081},
            {10169, 5084},
            {10177, 1272},
            {10181, 10180},
            {10193, 784},
            {10211, 2042},
            {10223, 5111},
            {10243, 5121},
            {10247, 10246},
            {10253, 5126},
            {10259, 10258},
            {10267, 10266},
            {10271, 5135},
            {10273, 3424},
            {10289, 10288},
            {10301, 10300},
            {10303, 10302},
            {10313, 5156},
            {10321, 516},
            {10331, 5165},
            {10333, 5166},
            {10337, 10336},
            {10343, 5171},
            {10357, 3452},
            {10369, 2592},
            {10391, 10390},
            {10399, 5199},
            {10427, 10426},
            {10429, 5214},
            {10433, 652},
            {10453, 10452},
            {10457, 10456},
            {10459, 5229},
            {10463, 5231},
            {10477, 5238},
            {10487, 10486},
            {10499, 58},
            {10501, 875},
            {10513, 5256},
            {10529, 752},
            {10531, 78},
            {10559, 5279},
            {10567, 5283},
            {10589, 2647},
            {10597, 3532},
            {10601, 5300},
            {10607, 10606},
            {10613, 2653},
            {10627, 1771},
            {10631, 10630},
            {10639, 10638},
            {10651, 10650},
            {10657, 10656},
            {10663, 3554},
            {10667, 5333},
            {10687, 5343},
            {10691, 1069},
            {10709, 2677},
            {10711, 10710},
            {10723, 5361},
            {10729, 10728},
            {10733, 5366},
            {10739, 5369},
            {10753, 448},
            {10771, 10770},
            {10781, 770},
            {10789, 2697},
            {10799, 10798},
            {10831, 1805},
            {10837, 172},
            {10847, 10846},
            {10853, 2713},
            {10859, 10858},
            {10861, 2715},
            {10867, 5433},
            {10883, 10882},
            {10889, 5444},
            {10891, 10890},
            {10903, 5451},
            {10909, 10908},
            {10937, 5468},
            {10939, 10938},
            {10949, 644},
            {10957, 5478},
            {10973, 10972},
            {10979, 5489},
            {10987, 1831},
            {10993, 10992},
            {11003, 5501},
            {11027, 11026},
            {11047, 5523},
            {11057, 2764},
            {11059, 11058},
            {11069, 2767},
            {11071, 1230},
            {11083, 1847},
            {11087, 11086},
            {11093, 188},
            {11113, 3704},
            {11117, 11116},
            {11119, 5559},
            {11131, 1855},
            {11149, 11148},
            {11159, 11158},
            {11161, 2232},
            {11171, 11170},
            {11173, 1596},
            {11177, 5588},
            {11197, 933},
            {11213, 5606},
            {11239, 5619},
            {11243, 5621},
            {11251, 11250},
            {11257, 2814},
            {11261, 563},
            {11273, 1409},
            {11279, 5639},
            {11287, 11286},
            {11299, 1883},
            {11311, 1131},
            {11317, 1886},
            {11321, 11320},
            {11329, 11328},
            {11351, 5675},
            {11353, 11352},
            {11369, 1421},
            {11383, 813},
            {11393, 1424},
            {11399, 41},
            {11411, 5705},
            {11423, 5711},
            {11437, 2859},
            {11443, 5721},
            {11447, 5723},
            {11467, 1911},
            {11471, 1147},
            {11483, 5741},
            {11489, 5744},
            {11491, 1149},
            {11497, 3832},
            {11503, 5751},
            {11519, 5759},
            {11527, 1921},
            {11549, 11548},
            {11551, 3850},
            {11579, 11578},
            {11587, 11586},
            {11593, 11592},
            {11597, 11596},
            {11617, 3872},
            {11621, 11620},
            {11633, 11632},
            {11657, 1457},
            {11677, 1946},
            {11681, 2336},
            {11689, 11688},
            {11699, 5849},
            {11701, 5850},
            {11717, 11716},
            {11719, 11718},
            {11731, 1955},
            {11743, 5871},
            {11777, 11776},
            {11779, 11778},
            {11783, 11782},
            {11789, 5894},
            {11801, 11800},
            {11807, 11806},
            {11813, 11812},
            {11821, 2955},
            {11827, 11826},
            {11831, 1183},
            {11833, 87},
            {11839, 1973},
            {11863, 3954},
            {11867, 11866},
            {11887, 283},
            {11897, 1487},
            {11903, 5951},
            {11909, 5954},
            {11923, 11922},
            {11927, 5963},
            {11933, 2983},
            {11939, 5969},
            {11941, 995},
            {11953, 1328},
            {11959, 5979},
            {11969, 5984},
            {11971, 2394},
            {11981, 11980},
            {11987, 11986},
            {12007, 12006},
            {12011, 6005},
            {12037, 12036},
            {12041, 1505},
            {12043, 6021},
            {12049, 6024},
            {12071, 6035},
            {12073, 4024},
            {12097, 6048},
            {12101, 6050},
            {12107, 6053},
            {12109, 12108},
            {12113, 12112},
            {12119, 12118},
            {12143, 6071},
            {12149, 12148},
            {12157, 6078},
            {12161, 6080},
            {12163, 6081},
            {12197, 3049},
            {12203, 6101},
            {12211, 12210}
    };

    static final long[] totals = new long[]{
            101107, 101111, 101113, 101117, 101119, 101141, 101149, 101159, 101161, 101173,
            101183, 101197, 101203, 101207, 101209, 101221, 101267, 101273, 101279, 101281,
            101287, 101293, 101323, 101333, 101341, 101347, 101359, 101363, 101377, 101383,
            101399, 101411, 101419, 101429, 101449, 101467, 101477, 101483, 101489, 101501,
            101503, 101513, 101527, 101531, 101533, 101537, 101561, 101573, 101581, 101599,
            101603, 101611, 101627, 101641, 101653, 101663, 101681, 101693, 101701, 101719,
            101723, 101737, 101741, 101747, 101749, 101771, 101789, 101797, 101807, 101833,
            101837, 101839, 101863, 101869, 101873, 101879, 101891, 101917, 101921, 101929,
            101939, 101957, 101963, 101977, 101987, 101999, 102001, 102013, 102019, 102023,
            102031, 102043, 102059, 102061, 102071, 102077, 102079, 102101, 102103, 102107,
            102121, 102139, 102149, 102161, 102181, 102191, 102197, 102199, 102203, 102217,
            102229, 102233, 102241, 102251, 102253, 102259, 102293, 102299, 102301, 102317,
            102329, 102337, 102359, 102367, 102397, 102407, 102409, 102433, 102437, 102451,
            102461, 102481, 102497, 102499, 102503, 102523, 102533, 102539, 102547, 102551,
            102559, 102563, 102587, 102593, 102607, 102611, 102643, 102647, 102653, 102667,
            102673, 102677, 102679, 102701, 102761, 102763, 102769, 102793, 102797, 102811,
            102829, 102841, 102859, 102871, 102877, 102881, 102911, 102913, 102929, 102931,
            102953, 102967, 102983, 103001, 103007, 103043, 103049, 103067, 103069, 103079,
            103087, 103091, 103093, 103099, 103123, 103141, 103171, 103177, 103183, 103217,
            103231, 103237, 103289, 103291, 103307, 103319, 103333, 103349, 103357, 103387,
            103391, 103393, 103399, 103409, 103421, 103423, 103451, 103457, 103471, 103483,
            103511, 103529, 103549, 103553, 103561, 103567, 103573, 103577, 103583, 103591,
            103613, 103619, 103643, 103651, 103657, 103669, 103681, 103687, 103699, 103703,
            103723, 103769, 103787, 103801, 103811, 103813, 103837, 103841, 103843, 103867,
            103889, 103903, 103913, 103919, 103951, 103963, 103967, 103969, 103979, 103981,
            103991, 103993, 103997, 104003, 104009, 104021, 104033, 104047, 104053, 104059,
            104087, 104089, 104107, 104113, 104119, 104123, 104147, 104149, 104161, 104173,
            104179, 104183, 104207, 104231, 104233, 104239, 104243, 104281, 104287, 104297,
            104309, 104311, 104323, 104327, 104347, 104369, 104381, 104383, 104393, 104399,
            104417, 104459, 104471, 104473, 104479, 104491, 104513, 104527, 104537, 104543,
            104549, 104551, 104561, 104579, 104593, 104597, 104623, 104639, 104651, 104659,
            104677, 104681, 104683, 104693, 104701, 104707, 104711, 104717, 104723, 104729
    };

}
