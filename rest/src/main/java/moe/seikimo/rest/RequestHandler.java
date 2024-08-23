package moe.seikimo.rest;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@AllArgsConstructor
public final class RequestHandler implements Handler {
    private final Object instance;
    private final Method handler;

    /**
     * This is invoked by Javalin.
     *
     * @param context The context of the request.
     */
    @Override
    public void handle(@NotNull Context context) {
        try {
            var result = switch (this.determine()) {
                case NONE -> this.handler.invoke(this.instance);
                case CONTEXT -> {
                    var request = new Request(context);
                    yield this.handler.invoke(this.instance, request);
                }
                default -> throw new IllegalArgumentException("Unsupported handler type.");
            };

            if (result instanceof String strResult) {
                context.result(strResult);
            } else if (result instanceof Response<?> response) {
                response.apply(context);
            }
        } catch (InvocationTargetException | IllegalAccessException ex) {
            log.warn("Exception encountered while trying to invoke handler.", ex);
            context
                    .status(500)
                    .result(String.join(
                            "\n",
                            Arrays.stream(ex.getStackTrace())
                                    .map(StackTraceElement::toString)
                                    .toList()
                    ));
        } catch (IllegalArgumentException ex) {
            log.warn("Exception encountered while trying to invoke handler.", ex);
            context
                    .status(500)
                    .result(ex.getMessage());
        }
    }

    /**
     * @return The type of handler.
     */
    private HandlerType determine() {
        var parameters = this.handler.getParameters();
        if (parameters.length == 0) {
            return HandlerType.NONE;
        }

        if (parameters.length == 1 &&
                parameters[0].getType().equals(Request.class)) {
            return HandlerType.CONTEXT;
        }

        return HandlerType.ARGS;
    }

    enum HandlerType {
        NONE, // No parameters in handler.
        CONTEXT, // Context parameter in handler.
        ARGS, // Arguments in handler.
    }
}
