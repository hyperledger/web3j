package org.web3j.compat;

import java.nio.charset.Charset;

/**
 * Provides compatibility methods to substitute those methods from Java SDK that
 * are not supported on Android.
 */
public final class Compat {

    /**
     * Ports {@link java.nio.charset.StandardCharsets#UTF_8}.
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

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
