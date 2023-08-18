package moe.seikimo.http.client;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

public final class HttpClient2 {
    private final HttpClient client = HttpClient.newHttpClient();
    private final List<Interceptor> middleware = new ArrayList<>();

    /**
     * Adds an interception function.
     *
     * @param interceptor The interceptor.
     * @return The client.
     */
    public HttpClient2 intercept(Interceptor interceptor) {
        this.middleware.add(interceptor);
        return this;
    }

    /**
     * Executes a request.
     *
     * @param request The request.
     */
    @SneakyThrows
    public Response execute(Request request) {
        // Apply middleware.
        for (var interceptor : this.middleware) {
            interceptor.intercept(request);
        }

        // Send the request.
        var internal = this.client.send(request.convert(),
                request.getResponse().getHandler());

        // Parse the response.
        return new Response(
                internal.statusCode(),
                internal.body(),
                internal.headers()
        );
    }

    public interface Interceptor {
        /**
         * Called when a request is intercepted.
         * This is called before the request is sent.
         *
         * @param request The request.
         */
        void intercept(Request request);
    }
}
