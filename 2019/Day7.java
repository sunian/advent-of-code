package adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sun on 12/7/2019.
 * Amplification Circuit
 */
public class Day7 {
    public static void main(String[] args) {
        System.out.println(part1("", 5, 0)); // part 1
        System.out.println(part1("", 5, 5)); // part 2
    }

    static int part1(String sequence, int amps, int offset) {
        if (sequence.length() == amps) {
            return getOutputOfLoop(sequence, offset);
        }
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < amps; i++) {
            if (sequence.contains(i + "")) {
                continue;
            }
            int result = part1(sequence + i, amps, offset);
            max = Math.max(max, result);
        }
        return max;
    }

    static int getOutputOfLoop(String sequence, int offset) {
        List<Integer> input = Collections.singletonList(0);
        Computer[] computers = new Computer[sequence.length()];
        for (int i = 0; i < computers.length; i++) {
            computers[i] = buildComputer();
        }
        boolean firstLoop = true;
        Computer.Output output = new Computer.Output(Collections.emptyList(), false);
        while (!output.halted) {
            for (int i = 0; i < sequence.length(); i++) {
                int phase = Integer.parseInt(sequence.charAt(i) + "") + offset;
                Computer computer = computers[i];
                if (firstLoop) {
                    List<Integer> newInput = new ArrayList<>();
                    newInput.add(phase);
                    newInput.addAll(input);
                    output = computer.compute(newInput);
                } else {
                    output = computer.compute(input);
                }
                input = output.output;
            }
            firstLoop = false;
        }
        return input.get(0);
    }

    static Computer buildComputer() {
        return new Computer(PROGRAM);
    }

    private static class Computer {
        final int[] tape;
        int position = 0;
        int inputPosition = 0;
        List<Integer> input = new ArrayList<>();

        Computer(int[] program) {
            this.tape = Arrays.copyOf(program, program.length);
        }

        Output compute(List<Integer> newInput) {
            input.addAll(newInput);
            List<Integer> output = new ArrayList<>();
            while (true) {
                int opcode = tape[position];
                int mode1 = (opcode / 100) % 10;
                int mode2 = (opcode / 1000) % 10;
                int mode3 = (opcode / 10000) % 10;
                switch (opcode % 100) {
                    case 1: // add
                        int sum = readValue(tape, position + 1, mode1) + readValue(tape, position + 2, mode2);
                        tape[tape[position + 3]] = sum;
                        position += 4;
                        break;
                    case 2: // multiply
                        int product = readValue(tape, position + 1, mode1) * readValue(tape, position + 2, mode2);
                        tape[tape[position + 3]] = product;
                        position += 4;
                        break;
                    case 3: // read input
                        if (inputPosition >= input.size()) {
                            return new Output(output, false);
                        }
                        tape[tape[position + 1]] = input.get(inputPosition);
                        inputPosition++;
                        position += 2;
                        break;
                    case 4: // write output
                        output.add(readValue(tape, position + 1, mode1));
                        position += 2;
                        break;
                    case 5: // jump-if-true
                        if (readValue(tape, position + 1, mode1) != 0) {
                            position = readValue(tape, position + 2, mode2);
                        } else {
                            position += 3;
                        }
                        break;
                    case 6: // jump-if-false
                        if (readValue(tape, position + 1, mode1) == 0) {
                            position = readValue(tape, position + 2, mode2);
                        } else {
                            position += 3;
                        }
                        break;
                    case 7: // is less than
                        boolean firstIsLess = readValue(tape, position + 1, mode1) < readValue(tape, position + 2, mode2);
                        tape[tape[position + 3]] = firstIsLess ? 1 : 0;
                        position += 4;
                        break;
                    case 8: // is equal
                        boolean areEqual = readValue(tape, position + 1, mode1) == readValue(tape, position + 2, mode2);
                        tape[tape[position + 3]] = areEqual ? 1 : 0;
                        position += 4;
                        break;
                    case 99: // halt
                        return new Output(output, true);
                    default:
                        throw new IllegalStateException("invalid opcode " + opcode + " at position " + position);
                }
            }
        }

        private int readValue(int[] tape, int position, int mode) {
            if (mode > 0) {
                return tape[position];
            } else {
                return tape[tape[position]];
            }
        }

        private static class Output {
            final List<Integer> output;
            final boolean halted;

            public Output(List<Integer> output, boolean halted) {
                this.output = output;
                this.halted = halted;
            }
        }
    }

    static final int[] PROGRAM = new int[]{3, 8, 1001, 8, 10, 8, 105, 1, 0, 0, 21, 30, 55, 80, 101, 118, 199, 280, 361, 442, 99999, 3, 9, 101, 4, 9, 9, 4, 9, 99, 3, 9, 101, 4, 9, 9, 1002, 9, 4, 9, 101, 4, 9, 9, 1002, 9, 5, 9, 1001, 9, 2, 9, 4, 9, 99, 3, 9, 101, 5, 9, 9, 1002, 9, 2, 9, 101, 3, 9, 9, 102, 4, 9, 9, 1001, 9, 2, 9, 4, 9, 99, 3, 9, 102, 2, 9, 9, 101, 5, 9, 9, 102, 3, 9, 9, 101, 3, 9, 9, 4, 9, 99, 3, 9, 1001, 9, 2, 9, 102, 4, 9, 9, 1001, 9, 3, 9, 4, 9, 99, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 99, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 99, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 99, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 99, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 99};

}