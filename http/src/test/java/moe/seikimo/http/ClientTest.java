package moe.seikimo.http;

import moe.seikimo.general.MapBuilder;
import moe.seikimo.http.client.HttpClient2;
import moe.seikimo.http.client.Request;

import java.util.Arrays;

public final class ClientTest {
    public static final class Interceptor implements HttpClient2.Interceptor {
        @Override
        public void intercept(Request request) {
            System.out.println("Original request: " + request);
            request.addHeader("Client-Test", "true");
        }
    }

    public static void main(String[] args) {
        var client = new HttpClient2()
                .intercept(new Interceptor());

        {
            var request = new Request.Builder()
                    .url("https://app.seikimo.moe/social/available")
                    .addHeader("User-Agent", "Seikimo/1.0")
                    .build();

            var response = client.execute(request);
            System.out.println(response.bodyAsString());
        }

        {
            var request = new Request.Builder()
                    .url("https://app.seikimo.moe/playlist/create")
                    .addHeader("Authorization", "1234")
                    .addBodyJson(MapBuilder.strings()
                            .put("name", "value")
                            .build())
                    .responseType(Request.ResponseType.BYTES)
                    .method("POST")
                    .build();

            var response = client.execute(request);
            System.out.println(Arrays.toString(response.bodyAsBytes()));
        }
    }
}
