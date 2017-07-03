package br.com.wemind.marketplacetribanco.utils;

/**
 * For miscellaneous form/code validation methods
 */
public class Validation {

    private static final int CNPJ_LENGTH = 14;

    /**
     * Returns whether <tt>cnpj</tt> is a valid Brazilian CNPJ
     * code. Note: this <em>does not verify if the code is
     * indeed registered, this method merely verifies if the
     * check digits are valid</em>.
     *
     * @param cnpj the code to be verified
     * @return <tt>true</tt> if this is a valid CNPJ.
     * <tt>false</tt> otherwise
     */
    public static boolean hasValidCnpjCheckDigits(CharSequence cnpj) {
        // Strip punctuation
        String stripped = cnpj.toString().replaceAll("[^0-9]", "");

        // Check length
        if (stripped.length() != CNPJ_LENGTH) {
            return false;
        }

        // Validate check digits
        int[] digits = splitToInts(stripped);

        // CD: check digit
        int[] correctCD = calcCnpjCheckDigits(digits);

        return correctCD[0] == digits[12] && correctCD[1] == digits[13];
    }

    public static int[] calcCnpjCheckDigits(CharSequence cnpj) {
        // Strip punctuation
        String stripped = cnpj.toString().replaceAll("[^0-9]", "");

        int[] digits = splitToInts(stripped);

        return calcCnpjCheckDigits(digits);
    }

    private static int[] splitToInts(String stripped) {
        char[] charDigits = stripped.toCharArray();
        int[] digits = new int[charDigits.length];
        for (int i = 0; i < charDigits.length; i++) {
            digits[i] = Character.getNumericValue(charDigits[i]);
        }
        return digits;
    }

    public static int[] calcCnpjCheckDigits(int[] digits) {
        if (digits.length < 12) {
            throw new IllegalArgumentException("CNPJ digit array must be at least 12 digits long");
        }
        /* Pseudocode
            // First check digit
            v[1] := 5×cnpj[1] + 4×cnpj[2]  + 3×cnpj[3]  + 2×cnpj[4]
            v[1] += 9×cnpj[5] + 8×cnpj[6]  + 7×cnpj[7]  + 6×cnpj[8]
            v[1] += 5×cnpj[9] + 4×cnpj[10] + 3×cnpj[11] + 2×cnpj[12]
            v[1] := 11 - v[1] mod 11
            v[1] := 0 if v[1] ≥ 10

            // Second check digit
            v[2] := 6×cnpj[1] + 5×cnpj[2]  + 4×cnpj[3]  + 3×cnpj[4]
            v[2] += 2×cnpj[5] + 9×cnpj[6]  + 8×cnpj[7]  + 7×cnpj[8]
            v[2] += 6×cnpj[9] + 5×cnpj[10] + 4×cnpj[11] + 3×cnpj[12]
            v[2] += 2×v[1]
            v[2] := 11 - v[2] mod 11
            v[2] := 0 if v[2] ≥ 10
        * */

        int firstCD = 5 * digits[0] + 4 * digits[1] + 3 * digits[2] + 2 * digits[3]
                + 9 * digits[4] + 8 * digits[5] + 7 * digits[6] + 6 * digits[7]
                + 5 * digits[8] + 4 * digits[9] + 3 * digits[10] + 2 * digits[11];

        firstCD = 11 - firstCD % 11;
        if (firstCD > 9) firstCD = 0;

        int secondCD = 6 * digits[0] + 5 * digits[1] + 4 * digits[2] + 3 * digits[3]
                + 2 * digits[4] + 9 * digits[5] + 8 * digits[6] + 7 * digits[7]
                + 6 * digits[8] + 5 * digits[9] + 4 * digits[10] + 3 * digits[11]
                + 2 * firstCD;
        secondCD = 11 - secondCD % 11;
        if (secondCD > 9) secondCD = 0;

        return new int[]{firstCD, secondCD};
    }
}
