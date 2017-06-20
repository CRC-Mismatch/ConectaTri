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
     * @param cnpj    the code to be verified
     * @return      <tt>true</tt> if this is a valid CNPJ.
     *              <tt>false</tt> otherwise
     */
    public static boolean isValidCnpj(CharSequence cnpj) {
        return false;
        /*// Strip punctuation
        String stripped = cnpj.toString().replaceAll("[^0-9]", "");

        // Check length
        if (stripped.length() != CNPJ_LENGTH) {
            return false;
        }

        // Validate check digits

        // CD: check digit
        char[] digits = stripped.toCharArray();
        double firstCD = (double) digits[digits.length - 2];
        double secondCD = (double) digits[digits.length - 1];*/
    }
}
