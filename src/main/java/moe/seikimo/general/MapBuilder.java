package moe.seikimo.general;

import java.util.HashMap;
import java.util.Map;

public final class MapBuilder<K, V> {
    /**
     * @return A new map builder.
     */
    public static <K, V> MapBuilder<K, V> create() {
        return new MapBuilder<>();
    }

    /**
     * @return A new map builder with string keys and values.
     */
    public static MapBuilder<String, String> strings() {
        return new MapBuilder<>();
    }

    /**
     * @return A new map builder with string keys and object values.
     */
    public static MapBuilder<String, Object> objects() {
        return new MapBuilder<>();
    }

    private final Map<K, V> map = new HashMap<>();

    /**
     * Creates a new map builder.
     *
     * @param key The key.
     * @param value The value.
     * @return The map builder.
     */
    public MapBuilder<K, V> put(K key, V value) {
        this.map.put(key, value);
        return this;
    }

    /**
     * @return The constructed map. This is read and write.
     */
    public Map<K, V> build() {
        return this.map;
    }
}
