package br.com.wemind.marketplacetribanco.utils;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.util.Arrays;
import java.util.List;

/**
 * Collection of text formatting utilities
 */
public class Formatting {
    public static final int CPF_NUMBER_MAX_DIGITS = 11;
    public static final int CNPJ_NUMBER_MAX_DIGITS = 14;
    public static final int CEP_NUMBER_MAX_DIGITS = 8;
    public static final int EXPIRATION_MAX_DIGITS = 4;

    private static final List<Pair<String, Integer>> cpfPairs = Arrays.asList(
            new Pair<>("-", 9),
            new Pair<>(".", 6),
            new Pair<>(".", 3)
    );

    private static final List<Pair<String, Integer>> cnpjPairs = Arrays.asList(
            new Pair<>("-", 12),
            new Pair<>("/", 8),
            new Pair<>(".", 5),
            new Pair<>(".", 2)
    );
    private static final List<Pair<String, Integer>> expirationPairs = Arrays.asList(
            new Pair<>("/", 2)
    );


    /**
     * Receives a {@link CharSequence} and returns a {@link String}
     * formatted as a brazilian CPF number
     *
     * @param seq CharSequence to be formatted
     * @return String formatted as CPF number
     */
    public static String formatCpf(CharSequence seq) {
        String cleanStr = onlyNumbers(seq);

        // Too many digits, return digits only without formatting
        if (cleanStr.length() > CPF_NUMBER_MAX_DIGITS)
            return cleanStr.toString();

        return formatWithPairs(cleanStr, cpfPairs);
    }

    public static String formatCnpj(CharSequence seq) {
        String cleanStr = onlyNumbers(seq);

        // Too many digits, return digits only without formatting
        if (cleanStr.length() > CNPJ_NUMBER_MAX_DIGITS)
            return cleanStr;

        return formatWithPairs(cleanStr, cnpjPairs);
    }

    private static String formatCep(CharSequence seq) {
        String cleanStr = onlyNumbers(seq);
        if (cleanStr.length() > CEP_NUMBER_MAX_DIGITS) {
            return cleanStr;
        }

        final int separatorPosition = 5;
        if (cleanStr.length() > separatorPosition) {
            return new StringBuilder(cleanStr).insert(separatorPosition, "-").toString();

        } else {
            return cleanStr;
        }
    }

    public static String formatExpiration(CharSequence seq) {
        String cleanStr = onlyNumbers(seq);

        if (cleanStr.length() > EXPIRATION_MAX_DIGITS)
            return cleanStr;

        return formatWithPairs(cleanStr, expirationPairs);
    }

    public static String onlyNumbers(CharSequence seq) {
        return seq.toString().replaceAll("[^0-9]", "");
    }

    /**
     * @param str        String to be formatted by inserting certain strings in
     *                   their corresponding positions as defined by <code>sepPosList</code>.
     *                   Insertions only occur if <code>position < str.length()</code>
     * @param sepPosList List of pairs with each pair
     *                   containing a separator character and its
     *                   corresponding insertion position into <code>str</code>.
     *                   Positions <b>must be in ascending order</b>.
     * @return Formatted String
     */
    @NonNull
    public static String formatWithPairs(String str, List<Pair<String, Integer>> sepPosList) {
        StringBuilder formatted = new StringBuilder(str);

        for (Pair<String, Integer> pair : sepPosList) {
            if (pair.second < str.length())
                formatted.insert(pair.second, pair.first);

        }

        return formatted.toString();
    }

    public interface StringFormatter {
        String format(CharSequence seq);

    }

    public static class CpfFormatter implements StringFormatter {

        @Override
        public String format(CharSequence seq) {
            return formatCpf(seq);
        }

    }

    public static class CnpjFormatter implements StringFormatter {
        @Override
        public String format(CharSequence seq) {
            return formatCnpj(seq);
        }

    }

    public static class CepFormatter implements StringFormatter {
        @Override
        public String format(CharSequence seq) {
            return formatCep(seq);
        }

    }

    public static class ExpirationFormatter implements StringFormatter {

        @Override
        public String format(CharSequence seq) {
            return formatExpiration(seq);
        }
    }
}