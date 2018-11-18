package org.web3j.compat;

/**
 * Provides compatibility methods to substitute those methods from Java SDK that
 * are not supported on Android.
 */
public final class Compat {

    private Compat() {
    }

    /**
     * Ports {@link String#join(CharSequence, CharSequence...)}.
     */
    public static String join(CharSequence delimiter, CharSequence... elements) {
        if (elements.length == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder(7 * elements.length);
        sb.append(elements[0]);
        for (int i = 1; i < elements.length; i++) {
            sb.append(delimiter);
            sb.append(elements[i]);

        }
        return sb.toString();
    }
}
