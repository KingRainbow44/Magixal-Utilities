package moe.seikimo.http.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import moe.seikimo.general.EncodingUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpHeaders;
import java.nio.file.Files;

@Accessors(fluent = true)
@RequiredArgsConstructor
public final class Response {
    @Getter private final int statusCode;
    private final Object body;
    private final HttpHeaders headers;

    /**
     * Fetches a header.
     *
     * @param name The name of the header.
     * @return The header value, or a blank string if it isn't set.
     */
    public String header(String name) {
        return this.headers
                .firstValue(name)
                .orElse("");
    }

    /**
     * Fetches a header.
     * This can return null.
     *
     * @param name The name of the header.
     * @return The header value, or null if it isn't set.
     */
    public String nullHeader(String name) {
        return this.headers
                .firstValue(name)
                .orElse(null);
    }

    /**
     * JSON-decodes the response body.
     * This can only be used if the response is JSON.
     *
     * @param clazz The class to decode to.
     * @return The decoded object.
     */
    public <T> T bodyAsClass(Class<T> clazz) {
        // Check if the response is JSON.
        if (!this.header("Content-Type")
                .equals("application/json")) {
            throw new IllegalStateException("Response is not JSON");
        }

        return EncodingUtils.jsonDecode(
                this.bodyAsString(), clazz);
    }

    /**
     * Saves the response body to a file.
     *
     * @param saveTo The file to save to.
     */
    public void bodyAsFile(File saveTo) {
        try {
            Files.write(saveTo.toPath(), this.bodyAsBytes());
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to write file", exception);
        }
    }

    /**
     * @return The response body as a byte array.
     */
    public byte[] bodyAsBytes() {
        try {
            var body = new byte[0];

            // Attempt to read the body.
            if (this.body instanceof byte[] bytesBody)
                body = bytesBody;
            else if (this.body instanceof InputStream streamBody)
                body = streamBody.readAllBytes();

            return body;
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to read response body", exception);
        }
    }

    /**
     * @return The response body as a string.
     */
    public String bodyAsString() {
        if (!(this.body instanceof String strBody))
            throw new IllegalStateException("Response body is not a string");

        return strBody;
    }
}
