package moe.seikimo.rest.interfaces;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Functional interface for route handlers.
 */
public interface RouteHandler<T extends Annotation> {
    void handler(T annotation, Object instance, Method method);
}
