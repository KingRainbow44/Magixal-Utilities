package moe.seikimo.general;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public interface NumberUtils {
    AtomicReference<Random> RANDOM = new AtomicReference<>(new Random());
    AtomicReference<String> CHARSET = new AtomicReference<>("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

    /**
     * Generates a random string using the charset.
     *
     * @param length The length of the string.
     * @return The random string.
     */
    static String random(int length) {
        var charset = CHARSET.get();
        var builder = new StringBuilder();
        for (var i = 0; i < length; i++) {
            builder.append(charset.charAt(
                    (int) (Math.random() * charset.length())));
        }
        return builder.toString();
    }

    /**
     * Generates a random number.
     *
     * @param min The minimum number.
     * @param max The maximum number.
     * @return The random number.
     */
    static int random(int min, int max) {
        return RANDOM.get().nextInt(max - min) + min;
    }

    /**
     * Generates a random number.
     *
     * @param min The minimum number.
     * @param max The maximum number.
     * @return The random number.
     */
    static long random(long min, long max) {
        return RANDOM.get().nextLong() % (max - min) + min;
    }

    /**
     * Generates a random number.
     *
     * @param length The length of the number.
     * @return The random number.
     */
    static long randomId(int length) {
        var builder = new StringBuilder();
        for (var i = 0; i < length; i++) {
            builder.append(random(0, 9));
        }

        return Long.parseLong(builder.toString());
    }
}
