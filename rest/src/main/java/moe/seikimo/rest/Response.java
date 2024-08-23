package moe.seikimo.rest;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.Setter;

import java.io.InputStream;

@Setter
public class Response<T> {
    private T body;
    private HttpStatus statusCode;

    public Response() {
        this.statusCode = HttpStatus.OK;
        this.body = null;
    }

    public Response(HttpStatus statusCode) {
        this();

        this.statusCode = statusCode;
    }

    public Response(T body) {
        this();

        this.body = body;
    }

    public Response(T body, HttpStatus statusCode) {
        this();

        this.statusCode = statusCode;
        this.body = body;
    }

    /**
     * This method should be called when the response is ready to be sent.
     *
     * @param ctx The context of the request.
     */
    public void apply(Context ctx) {
        ctx.status(this.statusCode);

        // Set the body based on provided type.
        if (this.body != null) {
            if (this.body instanceof String strBody) {
                ctx.result(strBody);
            } else if (this.body instanceof byte[] bytesBody) {
                ctx.result(bytesBody);
            } else if (this.body instanceof InputStream streamBody) {
                ctx.result(streamBody);
            } else {
                ctx.json(this.body);
            }
        }
    }
}
