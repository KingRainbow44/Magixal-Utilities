package moe.seikimo.rest;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinGson;
import lombok.extern.slf4j.Slf4j;
import moe.seikimo.rest.annotations.NotFound;
import moe.seikimo.rest.annotations.Route;
import moe.seikimo.rest.annotations.methods.*;
import moe.seikimo.rest.annotations.methods.repeat.*;
import moe.seikimo.rest.interfaces.RouteHandler;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Initializer for the REST application.
 */
@Slf4j
public final class RestApplication {
    /**
     * This method should be called during the {@code main} method of the application.
     *
     * @param clazz The main class of the application.
     */
    public static void run(Class<?> clazz) {
        RestApplication.run(clazz, RestConfiguration.builder().build());
    }

    /**
     * This method should be called during the {@code main} method of the application.
     *
     * @param clazz The main class of the application.
     * @param config The configuration for the application.
     */
    public static void run(Class<?> clazz, RestConfiguration config) {
        var app = new RestApplication(
                config,
                clazz.getClassLoader(),
                clazz.getPackage().getName());
        app.register();
        app.bind(config.getPort());
    }

    private final RestConfiguration config;
    private final Reflections reflector;
    private final Javalin javalin;

    private final Map<String, Object> instances = new HashMap<>();

    private RestApplication(RestConfiguration config, ClassLoader classLoader, String packageName) {
        this.config = config;
        this.reflector = new Reflections(new ConfigurationBuilder()
                .forPackage(packageName, classLoader)
                .addScanners(
                        Scanners.TypesAnnotated,
                        Scanners.MethodsAnnotated,
                        Scanners.SubTypes
                ));
        this.javalin = Javalin.create(javalinConfig -> {
            // Apply defaults.
            javalinConfig.jsonMapper(new JavalinGson());

            // Invoke the configured consumer.
            config.getConfigurer().accept(javalinConfig);
        });
    }

    /**
     * This registers the path and its handler for most request types.
     * The exceptions are: HEAD, OPTIONS.
     *
     * @param path The path to register.
     * @param handler The handler to register.
     */
    private void all(String path, Handler handler) {
        this.javalin.get(path, handler);
        this.javalin.post(path, handler);
        this.javalin.delete(path, handler);
        this.javalin.patch(path, handler);
        this.javalin.put(path, handler);
    }

    /**
     * Creates or returns a new instance of the method's class.
     *
     * @param method The method to create an instance for.
     * @return The instance of the method's class.
     */
    private Object instance(Method method) {
        var clazz = method.getDeclaringClass();
        return this.instances.computeIfAbsent(clazz.getName(), key -> {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                log.warn("Failed to create instance for class: {}", clazz.getName(), ex);
                return null;
            }
        });
    }

    /**
     * Registers a route handler.
     *
     * @param handler The handler to register.
     * @param <T> The type of annotation the handler is for.
     */
    private <T extends Annotation> void register(Class<T> annotation, RouteHandler<T> handler) {
        var all = this.reflector.getMethodsAnnotatedWith(annotation);
        for (var method : all) try {
            var instance = this.instance(method);
            var value = method.getAnnotation(annotation);
            handler.handler(value, instance, method);
        } catch (Exception ex) {
            log.warn("Failed to register handler for method: {}", method.getName(), ex);
        }
    }

    /**
     * Registers a route.
     *
     * @param annotation The annotation to search for.
     * @param underlying This is the annotation which gets read.
     * @param handler The handler to register.
     */
    private <T extends Annotation, S extends Annotation> void register(
            Class<T> annotation,
            Class<S> underlying,
            RouteHandler<S> handler) {
        // Register duplicate annotations.
        var all = this.reflector.getMethodsAnnotatedWith(annotation);
        for (var method : all) try {
            var instance = this.instance(method);
            var values = method.getAnnotationsByType(underlying);
            for (var val : values) {
                handler.handler(val, instance, method);
            }
        } catch (Exception ex) {
            log.warn("Failed to register handler for method: {}", method.getName(), ex);
        }

        // Register single annotations.
        this.register(underlying, handler);
    }

    /**
     * Registers a route.
     *
     * @param annotation The annotation to search for.
     * @param underlying This is the annotation which gets read.
     * @param route The route to register.
     * @param type The type of handler to register.
     */
    private <T extends Annotation, S extends Annotation> void register(
            Class<T> annotation,
            Class<S> underlying,
            Function<S, String> route,
            HandlerType type) {
        this.register(annotation, underlying, (handler, handle, method) -> {
            this.javalin.addHttpHandler(
                    type, route.apply(handler),
                    new RequestHandler(handle, method)
            );
        });
    }

    /**
     * Registers all the routes in the application.
     */
    public void register() {
        this.register(Route.class, (annotation, instance, method) -> {
            this.all(annotation.value(), new RequestHandler(instance, method));
            log.debug("Registering route: {}", method.getName());
        });

        this.register(NotFound.class, (annotation, instance, method) -> {
            this.javalin.error(HttpStatus.NOT_FOUND, new RequestHandler(instance, method));
            log.debug("Registering status handler '404': {}", method.getName());
        });

        /// <editor-fold desc="Registering Routes" defaultstate="collapsed">
        this.register(GETs.class, GET.class, (annotation, instance, method) -> {
            this.javalin.get(annotation.value(), new RequestHandler(instance, method));
            log.debug("Registering GET route: {}", method.getName());
        });

        this.register(POSTs.class, POST.class, (annotation, instance, method) -> {
            this.javalin.post(annotation.value(), new RequestHandler(instance, method));
            log.debug("Registering POST route: {}", method.getName());
        });

        this.register(PUTs.class, PUT.class, (annotation, instance, method) -> {
            this.javalin.put(annotation.value(), new RequestHandler(instance, method));
            log.debug("Registering PUT route: {}", method.getName());
        });

        this.register(PATCHs.class, PATCH.class, (annotation, instance, method) -> {
            this.javalin.patch(annotation.value(), new RequestHandler(instance, method));
            log.debug("Registering PATCH route: {}", method.getName());
        });

        this.register(DELETEs.class, DELETE.class, (annotation, instance, method) -> {
            this.javalin.put(annotation.value(), new RequestHandler(instance, method));
            log.debug("Registering DELETE route: {}", method.getName());
        });
        /// </editor-fold>
    }

    /**
     * Binds on the specified port.
     */
    public void bind(int port) {
        this.javalin.start(port);
    }
}
