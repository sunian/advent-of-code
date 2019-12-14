package adventofcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sun on 12/14/2019.
 * Space Stoichiometry
 */
public class Day14 {

    static Map<String, Formula> formulas;
    static Map<String, Long> extra;

    public static void main(String[] args) {
        formulas = parseFormulas();
        extra = new HashMap<>();
        System.out.println(part1());
        System.out.println(part2());
    }

    static long part2() {
        long doable = 1;
        long tooMuch = 5000000;
        while (tooMuch - doable > 1) {
            extra.clear();
            long diff = tooMuch - doable;
            diff /= 2;
            long requestAmount = doable + diff;
            long oreNeeded = getOreNeededFor("FUEL", requestAmount);
            if (oreNeeded > 1000000000000L) {
                tooMuch = requestAmount;
            } else {
                doable = requestAmount;
            }
        }
        return doable;
    }

    static long part1() {
        return getOreNeededFor("FUEL", 1);
    }

    static long getOreNeededFor(String product, long requestAmount) {
        Formula formula = formulas.get(product);
        long oreNeeded = 0;
        long multiplier = (long) Math.ceil(requestAmount / (double) formula.productAmount);
        for (int i = 0; i < formula.ingredients.size(); i++) {
            String name = formula.ingredients.get(i);
            long amount = formula.amounts.get(i) * multiplier;
            if (name.equals("ORE")) {
                oreNeeded += amount;
            } else {
                Long extraAmount = extra.get(name);
                if (extraAmount == null) {
                    extraAmount = 0L;
                }
                if (extraAmount >= amount) {
                    extraAmount -= amount;
                    extra.put(name, extraAmount);
                } else {
                    extra.put(name, 0L);
                    oreNeeded += getOreNeededFor(name, amount - extraAmount);
                }
            }
        }

        Long extraAmount = extra.get(product);
        if (extraAmount == null) {
            extraAmount = 0L;
        }

        extraAmount += formula.productAmount * multiplier - requestAmount; // how much extra product was produced
        extra.put(product, extraAmount);
        return oreNeeded;
    }

    static Map<String, Formula> parseFormulas() {
        Map<String, Formula> formulas = new HashMap<>();
        for (String formula : INPUT) {
            String[] expressions = formula.split("=>");
            String[] product = expressions[1].trim().split(" ");
            int productAmount = Integer.parseInt(product[0].trim());
            String productName = product[1].trim();
            Formula f = new Formula(productAmount);
            String[] ingredients = expressions[0].trim().split(", ");
            for (String ingredient : ingredients) {
                String[] split = ingredient.trim().split(" ");
                int ingredientAmount = Integer.parseInt(split[0].trim());
                String ingredientName = split[1].trim();
                f.add(ingredientName, ingredientAmount);
            }
            formulas.put(productName, f);
        }
        return formulas;
    }

    static class Formula {
        final int productAmount;
        final List<String> ingredients = new ArrayList<>();
        final List<Integer> amounts = new ArrayList<>();

        public Formula(int productAmount) {
            this.productAmount = productAmount;
        }

        void add(String ingredient, int amount) {
            ingredients.add(ingredient);
            amounts.add(amount);
        }

    }

    static final String[] INPUT = new String[]{
            "4 QBQB, 2 NTLZ => 2 DPJP",
            "5 SCSDX, 3 WBLBS => 5 GVPG",
            "128 ORE => 1 WCQS",
            "14 LHMZ => 2 SWBFV",
            "5 NZJV, 1 MCLXC => 2 BSRT",
            "1 WJHZ => 6 HRZV",
            "5 SPNML, 1 QTVZL => 6 HBGD",
            "1 BSRT, 1 JRBM, 1 GVPG => 2 XVDQT",
            "10 CBQSB => 6 NRXGX",
            "6 TBFQ => 7 QPXS",
            "1 LKSVN => 1 FBFC",
            "39 CBQSB => 7 PSLXZ",
            "3 HBGD, 4 RCZF => 4 ZCTS",
            "2 BMDV, 6 DPJP => 1 RCZF",
            "1 GPBXP, 11 SWBFV, 12 XSBGR, 7 ZCLVG, 9 VQLN, 12 HRZV, 3 VLDVB, 3 QTVZL, 12 DVSD, 62 PSLXZ => 1 FUEL",
            "10 CFPG, 1 TBFQ => 3 NHKZB",
            "24 QLMJ => 1 SCSDX",
            "2 VKHZC => 1 SMLPV",
            "3 SMLPV, 11 NZJV, 1 HTSXK => 2 GPBXP",
            "1 SCKB => 3 TBFQ",
            "3 VKHZC, 2 XVDQT => 6 PHJH",
            "3 QBQB => 3 XHWH",
            "19 NHKZB, 3 MBQVK, 10 HTSXK, 2 GXVQG, 8 VKHZC, 1 XHWH, 1 RCZF => 5 ZCLVG",
            "1 GVPG => 4 QTVZL",
            "4 TMHMV => 7 LHMZ",
            "5 NRXGX, 9 NTLZ, 3 PSLXZ => 1 BMDV",
            "10 MCLXC => 3 VKHZC",
            "1 KTLR => 1 VLDVB",
            "5 HTSXK => 6 TMHMV",
            "5 LKSVN, 1 CGQHF, 11 WJHZ => 1 HGZC",
            "15 XHWH, 1 WBLBS => 4 NZJV",
            "3 MCLXC => 9 KTLR",
            "1 CBQSB => 1 SCKB",
            "140 ORE => 4 LKSVN",
            "2 NZJV, 8 XVDQT, 1 PHJH => 8 GXVQG",
            "21 NJXV, 1 XHWH, 12 TMHMV, 1 QPXS, 10 ZCTS, 3 TBFQ, 1 VLDVB => 7 DVSD",
            "4 QLMJ, 2 LKSVN => 1 NTLZ",
            "1 LKSVN => 4 QBQB",
            "1 SPNML, 3 CPBQ => 4 BKLPC",
            "2 CFPG => 5 MCLXC",
            "147 ORE => 7 CGQHF",
            "7 HGZC, 5 QLMJ => 3 CFPG",
            "3 LCLQV, 3 MLXGB, 1 NTLZ => 8 JRBM",
            "4 NHWG => 5 GPQN",
            "2 XHWH => 7 WBLBS",
            "7 CGFN, 2 RCZF, 13 NHWG, 1 VLDVB, 3 PHJH, 9 CBQSB => 9 XSBGR",
            "181 ORE => 7 WJHZ",
            "8 WJHZ => 9 CBQSB",
            "3 BTQWK, 8 BKLPC => 2 CGFN",
            "3 SCSDX => 3 NJXV",
            "6 JTBM, 23 GPQN => 1 VQLN",
            "23 MCLXC, 1 NTLZ => 7 SPNML",
            "1 SPNML => 2 JTBM",
            "1 BMDV => 7 HTSXK",
            "1 WBLBS => 9 NHWG",
            "4 FBFC, 1 LKSVN, 4 VKHZC => 7 CPBQ",
            "1 WCQS => 7 QLMJ",
            "1 BMDV, 2 DPJP => 6 MBQVK",
            "3 XHWH, 5 QLMJ => 4 LCLQV",
            "1 CBQSB, 2 PSLXZ => 2 MLXGB",
            "3 NHWG => 9 BTQWK"
    };
}
