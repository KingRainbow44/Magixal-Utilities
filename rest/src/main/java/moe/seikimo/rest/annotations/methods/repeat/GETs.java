package moe.seikimo.rest.annotations.methods.repeat;

import moe.seikimo.rest.annotations.methods.GET;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GETs {
    GET[] value() default {};
}
