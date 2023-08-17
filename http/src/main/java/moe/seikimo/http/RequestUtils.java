package moe.seikimo.http;

import io.javalin.http.Context;

public interface RequestUtils {
    /**
     * Fetches the IP of a request.
     *
     * @param ctx The context.
     * @return The IP.
     */
    static String ip(Context ctx) {
        // Check headers.
        var address = ctx.header("CF-Connecting-IP");
        if (address != null) return address;

        address = ctx.header("X-Forwarded-For");
        if (address != null) return address;

        address = ctx.header("X-Real-IP");
        if (address != null) return address;

        // Return the request IP.
        return ctx.ip();
    }
}
