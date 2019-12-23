package adventofcode;

/**
 * Created by Sun on 12/21/2019.
 * Springdroid Adventure
 */
public class Day21 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    static String code2 = "NOT A T\n" +
            "OR T J\n" +
            "NOT B T\n" +
            "OR T J\n" +
            "NOT C T\n" +
            "OR T J\n" +
            "OR E T\n" +
            "AND E T\n" +
            "OR H T\n" +
            "AND D T\n" +
            "AND T J\n";

    static void part2() {
        Day9.Computer computer = new Day9.Computer(INPUT);
        String code = code2 + "RUN\n";
        Day9.Computer.Output output = computer.compute(code);
        System.out.println(output.toAscii());
    }

    static String code1 = "NOT A T\n" +
            "OR T J\n" +
            "NOT B T\n" +
            "OR T J\n" +
            "NOT C T\n" +
            "OR T J\n" +
            "AND D J\n";

    static void part1() {
        Day9.Computer computer = new Day9.Computer(INPUT);
        String code = code1 + "WALK\n";
        Day9.Computer.Output output = computer.compute(code);
        System.out.println(output.toAscii());
    }

    static final long[] INPUT = new long[]{
            109, 2050, 21102, 966, 1, 1, 21101, 0, 13, 0, 1106, 0, 1378, 21101, 0, 20, 0, 1105, 1, 1337, 21102, 1, 27, 0, 1105, 1, 1279, 1208, 1, 65, 748, 1005, 748, 73, 1208, 1, 79, 748, 1005, 748, 110, 1208, 1, 78, 748, 1005, 748, 132, 1208, 1, 87, 748, 1005, 748, 169, 1208, 1, 82, 748, 1005, 748, 239, 21101, 0, 1041, 1, 21101, 73, 0, 0, 1106, 0, 1421, 21102, 1, 78, 1, 21102, 1, 1041, 2, 21102, 1, 88, 0, 1106, 0, 1301, 21102, 68, 1, 1, 21101, 1041, 0, 2, 21101, 103, 0, 0, 1105, 1, 1301, 1102, 1, 1, 750, 1106, 0, 298, 21102, 1, 82, 1, 21102, 1, 1041, 2, 21101, 125, 0, 0, 1105, 1, 1301, 1101, 2, 0, 750, 1106, 0, 298, 21101, 0, 79, 1, 21102, 1, 1041, 2, 21102, 147, 1, 0, 1105, 1, 1301, 21102, 1, 84, 1, 21101, 0, 1041, 2, 21102, 162, 1, 0, 1106, 0, 1301, 1102, 3, 1, 750, 1106, 0, 298, 21102, 1, 65, 1, 21101, 0, 1041, 2, 21101, 184, 0, 0, 1105, 1, 1301, 21101, 0, 76, 1, 21101, 1041, 0, 2, 21102, 1, 199, 0, 1106, 0, 1301, 21101, 0, 75, 1, 21102, 1, 1041, 2, 21101, 0, 214, 0, 1106, 0, 1301, 21102, 221, 1, 0, 1106, 0, 1337, 21102, 10, 1, 1, 21102, 1041, 1, 2, 21102, 1, 236, 0, 1106, 0, 1301, 1105, 1, 553, 21101, 0, 85, 1, 21101, 0, 1041, 2, 21101, 254, 0, 0, 1105, 1, 1301, 21101, 0, 78, 1, 21102, 1, 1041, 2, 21101, 269, 0, 0, 1106, 0, 1301, 21102, 276, 1, 0, 1106, 0, 1337, 21101, 0, 10, 1, 21101, 0, 1041, 2, 21101, 291, 0, 0, 1106, 0, 1301, 1101, 0, 1, 755, 1105, 1, 553, 21101, 0, 32, 1, 21101, 1041, 0, 2, 21102, 313, 1, 0, 1105, 1, 1301, 21101, 0, 320, 0, 1106, 0, 1337, 21102, 327, 1, 0, 1105, 1, 1279, 1201, 1, 0, 749, 21102, 1, 65, 2, 21102, 73, 1, 3, 21102, 1, 346, 0, 1105, 1, 1889, 1206, 1, 367, 1007, 749, 69, 748, 1005, 748, 360, 1101, 0, 1, 756, 1001, 749, -64, 751, 1106, 0, 406, 1008, 749, 74, 748, 1006, 748, 381, 1101, -1, 0, 751, 1105, 1, 406, 1008, 749, 84, 748, 1006, 748, 395, 1102, -2, 1, 751, 1105, 1, 406, 21101, 1100, 0, 1, 21101, 0, 406, 0, 1105, 1, 1421, 21101, 32, 0, 1, 21101, 0, 1100, 2, 21102, 1, 421, 0, 1106, 0, 1301, 21101, 428, 0, 0, 1106, 0, 1337, 21102, 435, 1, 0, 1105, 1, 1279, 2101, 0, 1, 749, 1008, 749, 74, 748, 1006, 748, 453, 1102, 1, -1, 752, 1105, 1, 478, 1008, 749, 84, 748, 1006, 748, 467, 1102, -2, 1, 752, 1105, 1, 478, 21101, 0, 1168, 1, 21101, 0, 478, 0, 1106, 0, 1421, 21102, 1, 485, 0, 1106, 0, 1337, 21101, 10, 0, 1, 21102, 1168, 1, 2, 21102, 500, 1, 0, 1105, 1, 1301, 1007, 920, 15, 748, 1005, 748, 518, 21102, 1, 1209, 1, 21102, 1, 518, 0, 1106, 0, 1421, 1002, 920, 3, 529, 1001, 529, 921, 529, 101, 0, 750, 0, 1001, 529, 1, 537, 101, 0, 751, 0, 1001, 537, 1, 545, 101, 0, 752, 0, 1001, 920, 1, 920, 1105, 1, 13, 1005, 755, 577, 1006, 756, 570, 21102, 1, 1100, 1, 21102, 570, 1, 0, 1105, 1, 1421, 21102, 987, 1, 1, 1106, 0, 581, 21101, 0, 1001, 1, 21101, 588, 0, 0, 1106, 0, 1378, 1101, 758, 0, 593, 1002, 0, 1, 753, 1006, 753, 654, 21002, 753, 1, 1, 21101, 0, 610, 0, 1106, 0, 667, 21102, 1, 0, 1, 21101, 0, 621, 0, 1106, 0, 1463, 1205, 1, 647, 21102, 1015, 1, 1, 21101, 635, 0, 0, 1106, 0, 1378, 21101, 0, 1, 1, 21101, 646, 0, 0, 1105, 1, 1463, 99, 1001, 593, 1, 593, 1105, 1, 592, 1006, 755, 664, 1102, 0, 1, 755, 1106, 0, 647, 4, 754, 99, 109, 2, 1101, 0, 726, 757, 21201, -1, 0, 1, 21102, 1, 9, 2, 21102, 1, 697, 3, 21101, 0, 692, 0, 1106, 0, 1913, 109, -2, 2105, 1, 0, 109, 2, 1001, 757, 0, 706, 1201, -1, 0, 0, 1001, 757, 1, 757, 109, -2, 2106, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 63, 223, 95, 191, 127, 159, 0, 71, 251, 103, 175, 157, 35, 62, 213, 50, 229, 170, 198, 197, 246, 217, 232, 187, 117, 113, 222, 58, 118, 46, 230, 233, 119, 204, 101, 84, 172, 111, 47, 86, 254, 124, 205, 136, 56, 245, 107, 201, 60, 53, 171, 55, 102, 120, 235, 173, 115, 236, 179, 226, 253, 158, 244, 152, 109, 185, 182, 77, 69, 206, 218, 249, 153, 196, 188, 59, 216, 203, 169, 100, 116, 177, 231, 220, 49, 143, 212, 167, 243, 141, 114, 122, 238, 200, 43, 54, 214, 250, 252, 70, 61, 126, 106, 207, 199, 98, 142, 163, 99, 183, 228, 42, 137, 78, 87, 38, 108, 219, 221, 237, 51, 215, 166, 138, 190, 139, 39, 184, 92, 239, 34, 79, 202, 174, 186, 227, 121, 125, 156, 248, 93, 168, 76, 140, 110, 123, 57, 85, 162, 94, 241, 178, 181, 155, 68, 247, 154, 234, 189, 242, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 73, 110, 112, 117, 116, 32, 105, 110, 115, 116, 114, 117, 99, 116, 105, 111, 110, 115, 58, 10, 13, 10, 87, 97, 108, 107, 105, 110, 103, 46, 46, 46, 10, 10, 13, 10, 82, 117, 110, 110, 105, 110, 103, 46, 46, 46, 10, 10, 25, 10, 68, 105, 100, 110, 39, 116, 32, 109, 97, 107, 101, 32, 105, 116, 32, 97, 99, 114, 111, 115, 115, 58, 10, 10, 58, 73, 110, 118, 97, 108, 105, 100, 32, 111, 112, 101, 114, 97, 116, 105, 111, 110, 59, 32, 101, 120, 112, 101, 99, 116, 101, 100, 32, 115, 111, 109, 101, 116, 104, 105, 110, 103, 32, 108, 105, 107, 101, 32, 65, 78, 68, 44, 32, 79, 82, 44, 32, 111, 114, 32, 78, 79, 84, 67, 73, 110, 118, 97, 108, 105, 100, 32, 102, 105, 114, 115, 116, 32, 97, 114, 103, 117, 109, 101, 110, 116, 59, 32, 101, 120, 112, 101, 99, 116, 101, 100, 32, 115, 111, 109, 101, 116, 104, 105, 110, 103, 32, 108, 105, 107, 101, 32, 65, 44, 32, 66, 44, 32, 67, 44, 32, 68, 44, 32, 74, 44, 32, 111, 114, 32, 84, 40, 73, 110, 118, 97, 108, 105, 100, 32, 115, 101, 99, 111, 110, 100, 32, 97, 114, 103, 117, 109, 101, 110, 116, 59, 32, 101, 120, 112, 101, 99, 116, 101, 100, 32, 74, 32, 111, 114, 32, 84, 52, 79, 117, 116, 32, 111, 102, 32, 109, 101, 109, 111, 114, 121, 59, 32, 97, 116, 32, 109, 111, 115, 116, 32, 49, 53, 32, 105, 110, 115, 116, 114, 117, 99, 116, 105, 111, 110, 115, 32, 99, 97, 110, 32, 98, 101, 32, 115, 116, 111, 114, 101, 100, 0, 109, 1, 1005, 1262, 1270, 3, 1262, 21002, 1262, 1, 0, 109, -1, 2105, 1, 0, 109, 1, 21101, 1288, 0, 0, 1105, 1, 1263, 20102, 1, 1262, 0, 1102, 0, 1, 1262, 109, -1, 2105, 1, 0, 109, 5, 21102, 1, 1310, 0, 1105, 1, 1279, 21202, 1, 1, -2, 22208, -2, -4, -1, 1205, -1, 1332, 22102, 1, -3, 1, 21101, 0, 1332, 0, 1105, 1, 1421, 109, -5, 2106, 0, 0, 109, 2, 21102, 1346, 1, 0, 1105, 1, 1263, 21208, 1, 32, -1, 1205, -1, 1363, 21208, 1, 9, -1, 1205, -1, 1363, 1106, 0, 1373, 21102, 1, 1370, 0, 1105, 1, 1279, 1106, 0, 1339, 109, -2, 2105, 1, 0, 109, 5, 2101, 0, -4, 1385, 21002, 0, 1, -2, 22101, 1, -4, -4, 21102, 0, 1, -3, 22208, -3, -2, -1, 1205, -1, 1416, 2201, -4, -3, 1408, 4, 0, 21201, -3, 1, -3, 1106, 0, 1396, 109, -5, 2106, 0, 0, 109, 2, 104, 10, 22102, 1, -1, 1, 21102, 1436, 1, 0, 1106, 0, 1378, 104, 10, 99, 109, -2, 2105, 1, 0, 109, 3, 20002, 593, 753, -1, 22202, -1, -2, -1, 201, -1, 754, 754, 109, -3, 2106, 0, 0, 109, 10, 21101, 0, 5, -5, 21101, 0, 1, -4, 21102, 0, 1, -3, 1206, -9, 1555, 21102, 1, 3, -6, 21101, 5, 0, -7, 22208, -7, -5, -8, 1206, -8, 1507, 22208, -6, -4, -8, 1206, -8, 1507, 104, 64, 1106, 0, 1529, 1205, -6, 1527, 1201, -7, 716, 1515, 21002, 0, -11, -8, 21201, -8, 46, -8, 204, -8, 1106, 0, 1529, 104, 46, 21201, -7, 1, -7, 21207, -7, 22, -8, 1205, -8, 1488, 104, 10, 21201, -6, -1, -6, 21207, -6, 0, -8, 1206, -8, 1484, 104, 10, 21207, -4, 1, -8, 1206, -8, 1569, 21102, 1, 0, -9, 1106, 0, 1689, 21208, -5, 21, -8, 1206, -8, 1583, 21102, 1, 1, -9, 1105, 1, 1689, 1201, -5, 716, 1588, 21001, 0, 0, -2, 21208, -4, 1, -1, 22202, -2, -1, -1, 1205, -2, 1613, 22101, 0, -5, 1, 21101, 0, 1613, 0, 1106, 0, 1444, 1206, -1, 1634, 22102, 1, -5, 1, 21102, 1, 1627, 0, 1105, 1, 1694, 1206, 1, 1634, 21102, 1, 2, -3, 22107, 1, -4, -8, 22201, -1, -8, -8, 1206, -8, 1649, 21201, -5, 1, -5, 1206, -3, 1663, 21201, -3, -1, -3, 21201, -4, 1, -4, 1106, 0, 1667, 21201, -4, -1, -4, 21208, -4, 0, -1, 1201, -5, 716, 1676, 22002, 0, -1, -1, 1206, -1, 1686, 21102, 1, 1, -4, 1105, 1, 1477, 109, -10, 2105, 1, 0, 109, 11, 21101, 0, 0, -6, 21101, 0, 0, -8, 21102, 1, 0, -7, 20208, -6, 920, -9, 1205, -9, 1880, 21202, -6, 3, -9, 1201, -9, 921, 1725, 20101, 0, 0, -5, 1001, 1725, 1, 1733, 20101, 0, 0, -4, 22101, 0, -4, 1, 21101, 0, 1, 2, 21101, 9, 0, 3, 21101, 1754, 0, 0, 1106, 0, 1889, 1206, 1, 1772, 2201, -10, -4, 1766, 1001, 1766, 716, 1766, 21002, 0, 1, -3, 1105, 1, 1790, 21208, -4, -1, -9, 1206, -9, 1786, 21202, -8, 1, -3, 1106, 0, 1790, 21202, -7, 1, -3, 1001, 1733, 1, 1796, 20101, 0, 0, -2, 21208, -2, -1, -9, 1206, -9, 1812, 21201, -8, 0, -1, 1105, 1, 1816, 21201, -7, 0, -1, 21208, -5, 1, -9, 1205, -9, 1837, 21208, -5, 2, -9, 1205, -9, 1844, 21208, -3, 0, -1, 1106, 0, 1855, 22202, -3, -1, -1, 1105, 1, 1855, 22201, -3, -1, -1, 22107, 0, -1, -1, 1105, 1, 1855, 21208, -2, -1, -9, 1206, -9, 1869, 22102, 1, -1, -8, 1105, 1, 1873, 22101, 0, -1, -7, 21201, -6, 1, -6, 1106, 0, 1708, 22102, 1, -8, -10, 109, -11, 2106, 0, 0, 109, 7, 22207, -6, -5, -3, 22207, -4, -6, -2, 22201, -3, -2, -1, 21208, -1, 0, -6, 109, -7, 2106, 0, 0, 0, 109, 5, 2101, 0, -2, 1912, 21207, -4, 0, -1, 1206, -1, 1930, 21102, 0, 1, -4, 22102, 1, -4, 1, 22101, 0, -3, 2, 21101, 1, 0, 3, 21101, 1949, 0, 0, 1105, 1, 1954, 109, -5, 2105, 1, 0, 109, 6, 21207, -4, 1, -1, 1206, -1, 1977, 22207, -5, -3, -1, 1206, -1, 1977, 21202, -5, 1, -5, 1106, 0, 2045, 22102, 1, -5, 1, 21201, -4, -1, 2, 21202, -3, 2, 3, 21102, 1, 1996, 0, 1106, 0, 1954, 21202, 1, 1, -5, 21101, 0, 1, -2, 22207, -5, -3, -1, 1206, -1, 2015, 21101, 0, 0, -2, 22202, -3, -2, -3, 22107, 0, -4, -1, 1206, -1, 2037, 22102, 1, -2, 1, 21101, 2037, 0, 0, 106, 0, 1912, 21202, -3, -1, -3, 22201, -5, -3, -5, 109, -6, 2105, 1, 0
    };
}
