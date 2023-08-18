package moe.seikimo.data;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import moe.seikimo.general.NumberUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public interface DatabaseUtils {
    /**
     * This reference must be set before using any of the methods.
     */
    AtomicReference<Datastore> DATASTORE = new AtomicReference<>();

    /**
     * Creates a unique numerical ID.
     *
     * @param type The type of object to create the ID for.
     * @param length The length of the ID.
     * @return The unique ID.
     */
    static long uniqueId(Class<? extends DatabaseObject<?>> type, int length) {
        return DatabaseUtils.distinct(
                type, "_id",
                () -> NumberUtils.randomId(length));
    }

    /**
     * Fetches a database object by a parameter.
     *
     * @param type The type of object to fetch.
     * @param param The parameter to search for.
     * @param value The value of the parameter.
     * @return The object, or null if not found.
     */
    static <T extends DatabaseObject<?>> T fetch(
            Class<T> type,
            String param, Object value
    ) {
        return DATASTORE.get().find(type)
                .filter(Filters.eq(param, value))
                .first();
    }

    /**
     * Fetches all database objects by a parameter.
     *
     * @param type The type of object to fetch.
     * @param params The parameters to search for.
     * @return The objects, or null if not found.
     */
    static <T extends DatabaseObject<?>> List<T> fetchAll(
            Class<T> type, Map<String, Object> params
    ) {
        var query = DATASTORE.get().find(type);
        for (var entry : params.entrySet()) {
            query = query.filter(Filters.eq(entry.getKey(), entry.getValue()));
        }

        try (var cursor = query.iterator()) {
            return cursor.toList();
        }
    }

    /**
     * Fetches all database objects by a parameter.
     *
     * @param type The type of object to fetch.
     * @param params The parameters to search for.
     * @return The objects, or null if not found.
     */
    static <T extends DatabaseObject<?>> List<T> fetchAll(
            Class<T> type, Filter... params
    ) {
        var query = DATASTORE.get()
                .find(type).filter(params);
        try (var cursor = query.iterator()) {
            return cursor.toList();
        }
    }

    /**
     * Counts the number of database objects.
     *
     * @param type The type of object to count.
     * @return The number of objects.
     */
    static long count(Class<? extends DatabaseObject<?>> type) {
        return DATASTORE.get().find(type).count();
    }

    /**
     * Requires a distinct value across all documents.
     *
     * @param type The type of object to search for.
     * @param searchFor The parameter to search for.
     * @param supplier The supplier to get the value from.
     * @return The value.
     */
    static <T> T distinct(Class<? extends DatabaseObject<?>> type, String searchFor, Supplier<T> supplier) {
        // Get the value.
        var value = supplier.get();
        var query = DATASTORE.get()
                .find(type).filter(Filters.eq(searchFor, value));

        // If the query is not empty, return the value.
        if (query.first() != null) {
            return DatabaseUtils.distinct(type, searchFor, supplier);
        }

        // Otherwise, return the value.
        return value;
    }
}
