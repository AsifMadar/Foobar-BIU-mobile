package il.ac.biu.project.foobar.utils;

import android.graphics.Bitmap;

public class SignUpValidations {


    /**
     * Check if the input contains only English characters and numbers.
     *
     * @param input The input string to check.
     * @return True if the input contains only English characters and numbers, false otherwise.
     */
    public static boolean containsOnlyEnglishCharsAndNumbers(String input) {
        return input.matches("[a-zA-Z0-9]+");
    }

    /**
     * Check if the input contains only English characters, numbers and space between two words.
     *
     * @param input The input string to check.
     * @return True if the input contains only English characters,numbers, and space between two words, false otherwise.
     */
    public static boolean containsOnlyEnglishCharsAndNumbersAndSpace(String input) {
        return input.matches("[a-zA-Z0-9]+( [a-zA-Z0-9]+)*");

    }

    /**
     * Check if the string length is within a specified range.
     *
     * @param input     The input string to check.
     * @param minLength The minimum allowed length.
     * @param maxLength The maximum allowed length.
     * @return True if the string length is within the specified range, false otherwise.
     */
    public static boolean isStringLengthInRange(String input, int minLength, int maxLength) {
        int length = input.length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Check if all input fields are valid.
     *
     * @return True if all input fields are valid, false otherwise.
     */
    public static boolean isAllValid(String username, String password, String displayName, String rePassword, Bitmap img) {
        return (containsOnlyEnglishCharsAndNumbers(username)
                && isStringLengthInRange(username, 5, 16))
                && (containsOnlyEnglishCharsAndNumbers(password)
                && isStringLengthInRange(password, 8, 20))
                && (containsOnlyEnglishCharsAndNumbersAndSpace(displayName)
                && isStringLengthInRange(displayName, 2, 20))
                && rePassword.equals(password)
                && img != null;
    }
}
