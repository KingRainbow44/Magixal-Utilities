package moe.seikimo.rest.annotations.methods.repeat;

import moe.seikimo.rest.annotations.methods.DELETE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DELETEs {
    DELETE[] value() default {};
}
