package moe.seikimo.http.client;

import lombok.*;
import lombok.experimental.Accessors;
import moe.seikimo.general.EncodingUtils;
import moe.seikimo.general.FileUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Request {
    private static final List<String> METHODS = List.of(
            "GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS"
    );
    private static final List<String> NEED_BODY = List.of(
            "POST", "PUT", "DELETE", "PATCH"
    );

    private URI url;
    private String method;
    private byte[] body;
    private Map<String, String> headers;
    private final ResponseType response;

    /**
     * Adds a header to the request.
     * This will overwrite any existing headers with the same name.
     *
     * @param name The name of the header.
     * @param value The value of the header.
     * @return The request.
     */
    public Request addHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    /**
     * Creates an HTTP request for use by Java.
     *
     * @return The request.
     */
    public HttpRequest convert() {
        // Create a new request.
        var builder = HttpRequest.newBuilder().uri(this.url);

        // Apply the body and method.
        if (this.body != null) {
            builder = builder
                    .method(this.method, BodyPublishers.ofByteArray(this.body));
        } else {
            builder = builder.method(this.method, BodyPublishers.noBody());
        }

        // Apply the headers.
        for (var entry : this.headers.entrySet()) {
            builder = builder.header(entry.getKey(), entry.getValue());
        }

        return builder.build();
    }

    @Getter
    @RequiredArgsConstructor
    public enum ResponseType {
        STRING(BodyHandlers.ofString()),
        BYTES(BodyHandlers.ofByteArray()),
        FILE(BodyHandlers.ofInputStream());

        final HttpResponse.BodyHandler<?> handler;
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static final class Builder {
        private String url, method = "GET";
        private byte[] body = null;
        private ResponseType responseType
                = ResponseType.STRING;

        private final Map<String, String> headers
                = new HashMap<>();

        /**
         * Adds a header to the request.
         *
         * @param key The header key.
         * @param value The header value.
         */
        public Request.Builder addHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        /**
         * Adds multiple headers to the request.
         *
         * @param headers The headers.
         */
        public Request.Builder setHeaders(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        /**
         * Adds a body to the request.
         * The body will be encoded as JSON.
         *
         * @param body The body.
         * @return The builder.
         */
        public Request.Builder addBodyJson(Object body) {
            this.body = EncodingUtils.jsonEncode(body).getBytes();
            this.headers.put("Content-Type", "application/json");
            return this;
        }

        /**
         * Adds a form body to the request.
         * The body will be encoded as a URL-encoded string.
         *
         * @param body The body.
         * @return The builder.
         */
        public Request.Builder addBody(Map<String, String> body) {
            // Piece together the body.
            var builder = new StringBuilder();
            for (var entry : body.entrySet()) {
                builder.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
            // Remove the last ampersand.
            builder.deleteCharAt(builder.length() - 1);

            this.body = builder.toString().getBytes();
            this.headers.put("Content-Type", "application/x-www-form-urlencoded");
            return this;
        }

        /**
         * Adds a text body to the request.
         * The body will be encoded as plain text.
         *
         * @param body The body.
         * @return The builder.
         */
        public Request.Builder addBody(String body) {
            this.body = body.getBytes();
            this.headers.put("Content-Type", "text/plain");
            return this;
        }

        /**
         * Adds a binary body to the request.
         * The body will be encoded as a byte array.
         *
         * @param body The body.
         * @return The builder.
         */
        public Request.Builder addBody(byte[] body) {
            this.body = body;
            this.headers.put("Content-Type", "application/octet-stream");
            return this;
        }

        /**
         * Adds a binary body to the request.
         * The body will be encoded as a byte array.
         *
         * @param body The body.
         * @return The builder.
         */
        public Request.Builder addBody(File body) {
            this.body = FileUtils.readFile(body);
            this.headers.put("Content-Type", "application/octet-stream");
            return this;
        }

        /**
         * Validates and creates a request.
         *
         * @return The request.
         */
        @SneakyThrows
        public Request build() {
            // Validate the URL.
            if (this.url == null || this.url.isEmpty()) {
                throw new IllegalStateException("URL cannot be null.");
            }

            // Validate the method.
            if (this.method == null || this.method.isEmpty()) {
                throw new IllegalStateException("Method cannot be null.");
            }
            if (!Request.METHODS.contains(this.method)) {
                throw new IllegalStateException("Method must be one of: "
                        + String.join(", ", Request.METHODS));
            }
            if (Request.NEED_BODY.contains(this.method) && this.body == null) {
                throw new IllegalStateException("Method requires a body.");
            }

            return new Request(new URI(this.url), this.method,
                    this.body, this.headers, this.responseType);
        }
    }
}
