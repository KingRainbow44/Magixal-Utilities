package moe.seikimo.rest.example;

import com.google.gson.JsonObject;
import io.javalin.http.HttpStatus;
import moe.seikimo.general.JObject;
import moe.seikimo.rest.Request;
import moe.seikimo.rest.Response;
import moe.seikimo.rest.RestApplication;
import moe.seikimo.rest.annotations.NotFound;
import moe.seikimo.rest.annotations.methods.GET;
import moe.seikimo.rest.annotations.methods.POST;
import moe.seikimo.rest.annotations.methods.PUT;

public final class BasicApplication {
    /**
     * The main method of the application.
     *
     * @param args The arguments passed to the application.
     */
    public static void main(String[] args) {
        RestApplication.run(BasicApplication.class);
    }

    @NotFound
    public String notFound() {
        return "Unable to route this request!";
    }

    @GET
    @GET("/hello")
    public String index() {
        return "Hello World!";
    }

    @PUT("/update")
    public Response<JsonObject> update() {
        var object = JObject.c()
                .add("status", "ok")
                .add("timestamp", System.currentTimeMillis())
                .gson();
        return new Response<>(object, HttpStatus.FOUND);
    }

    @GET("/route")
    @GET("/route/{param}")
    public String route(Request request) {
        var param = request.param("param", "none");
        return "You requested to check out %s, with a parameter of %s!"
                .formatted(request.route(), param);
    }

    @GET("/response")
    @POST("/response")
    public String response() {
        return "ok";
    }
}
