package moe.seikimo.console;

import java.util.HashMap;
import java.util.Map;

public final class Arguments {
    private final Map<String, String> arguments
            = new HashMap<>();

    /**
     * Parses arguments.
     *
     * @param consoleArgs Console application arguments.
     */
    public Arguments(String[] consoleArgs) {
        for (var arg : consoleArgs) {
            if (!arg.startsWith("--")) continue;
            arg = arg.substring(2);

            var split = arg.split("=");
            this.arguments.put(split[0], split[1]);
        }
    }

    /**
     * Gets an argument.
     *
     * @param key The key.
     * @return The value.
     */
    public String get(String key) {
        return this.arguments.get(key);
    }

    /**
     * Gets an argument.
     * Falls back to a default value.
     *
     * @param key The key.
     * @param fallback The fallback value.
     * @return The value.
     */
    public String get(String key, String fallback) {
        return this.arguments.getOrDefault(key, fallback);
    }

    /**
     * Gets an argument as an integer.
     *
     * @param key The key.
     * @return The value.
     */
    public int getInt(String key) {
        return Integer.parseInt(this.arguments.get(key));
    }

    /**
     * Gets an argument as an integer.
     * Falls back to a default value.
     *
     * @param key The key.
     * @param fallback The fallback value.
     * @return The value.
     */
    public int getInt(String key, int fallback) {
        return Integer.parseInt(this.arguments.getOrDefault(key, String.valueOf(fallback)));
    }

    /**
     * Gets an argument as a boolean.
     *
     * @param key The key.
     * @return The value.
     */
    public boolean get(String key, boolean fallback) {
        return Boolean.parseBoolean(this.arguments.getOrDefault(key, String.valueOf(fallback)));
    }
}
