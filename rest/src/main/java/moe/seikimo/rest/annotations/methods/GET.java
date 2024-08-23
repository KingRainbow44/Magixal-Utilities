package moe.seikimo.rest.annotations.methods;

import moe.seikimo.rest.annotations.methods.repeat.GETs;

import java.lang.annotation.*;

@Repeatable(GETs.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GET {
    String value() default "/";
}
