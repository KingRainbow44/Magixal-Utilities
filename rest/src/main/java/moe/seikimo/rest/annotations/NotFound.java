package moe.seikimo.rest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This handles the 404 Not Found status code.
 * If a request fails with 404, the annotated method will always be called.
 * If this annotation does not exist, they will be directed to a static 404 page.
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotFound {
}
