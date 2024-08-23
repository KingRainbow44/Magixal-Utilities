package moe.seikimo.rest;

import io.javalin.http.Context;
import moe.seikimo.general.EncodingUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record Request(Context handle) {
    /**
     * @return The requested route.
     */
    public String route() {
        return this.handle().path();
    }

    /// <editor-fold desc="Query Parameters">

    /**
     * Fetches the raw value of a query parameter.
     *
     * @param name The name of the parameter.
     * @return The raw value of the parameter.
     * @throws IllegalArgumentException If the parameter does not exist.
     */
    public String query(String name) {
        return this.handle().queryParam(name);
    }

    /**
     * Fetches the raw value of a query parameter, or a fallback value if the parameter does not exist.
     *
     * @param name The name of the parameter.
     * @param fallback The fallback value.
     * @return The raw value of the parameter, or the fallback value if the parameter does not exist.
     */
    @Nullable
    public String query(String name, String fallback) {
        var map = this.handle().queryParamMap();
        var params = map.getOrDefault(name, List.of(fallback));
        return params.stream().findFirst().orElse(null);
    }

    /// </editor-fold>

    /// <editor-fold desc="Path Parameters">

    /**
     * Fetches the raw value of a path parameter.
     *
     * @param name The name of the parameter.
     * @return The raw value of the parameter.
     * @throws IllegalArgumentException If the parameter does not exist.
     */
    public String param(String name) {
        return this.handle().pathParam(name);
    }

    /**
     * Fetches the raw value of a path parameter, or a fallback value if the parameter does not exist.
     *
     * @param name The name of the parameter.
     * @param fallback The fallback value.
     * @return The raw value of the parameter, or the fallback value if the parameter does not exist.
     */
    public String param(String name, String fallback) {
        var map = this.handle().pathParamMap();
        return map.getOrDefault(name, fallback);
    }

    /**
     * Attempts to deserialize the value of a path parameter.
     *
     * @param name The name of the parameter.
     * @param as The class to deserialize the parameter as.
     * @return The deserialized value of the parameter.
     * @throws IllegalArgumentException If the parameter does not exist.
     */
    public <T> T param(String name, Class<? extends T> as) {
        var value = this.param(name, null, as);
        if (value == null) {
            throw new IllegalArgumentException("Parameter " + name + " does not exist.");
        }
        return value;
    }

    /**
     * Attempts to deserialize the value of a path parameter.
     *
     * @param name The name of the parameter.
     * @param fallback The fallback value.
     * @param as The class to deserialize the parameter as.
     * @return The deserialized value of the parameter.
     */
    public <T> T param(String name, T fallback, Class<? extends T> as) {
        var map = this.handle().pathParamMap();
        if (!map.containsKey(name)) {
            return fallback;
        }
        var paramValue = map.get(name);

        if (as == String.class) {
            return as.cast(paramValue);
        } else if (as == int.class) {
            return as.cast(Integer.parseInt(paramValue));
        } else if (as == long.class) {
            return as.cast(Long.parseLong(paramValue));
        } else if (as == double.class) {
            return as.cast(Double.parseDouble(paramValue));
        } else if (as == float.class) {
            return as.cast(Float.parseFloat(paramValue));
        } else if (as == boolean.class) {
            return as.cast(Boolean.parseBoolean(paramValue));
        } else {
            return EncodingUtils.jsonDecode(paramValue, as);
        }
    }

    /// </editor-fold>
}
