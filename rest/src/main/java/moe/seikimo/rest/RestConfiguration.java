package moe.seikimo.rest;

import io.javalin.config.JavalinConfig;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

import java.util.function.Consumer;

@Data
@Builder
public final class RestConfiguration {
    @Default private int port = 8080;
    @Default private Consumer<JavalinConfig> configurer = config -> {};
}
