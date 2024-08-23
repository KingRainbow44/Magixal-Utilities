package moe.seikimo.rest.annotations.methods;

import moe.seikimo.rest.annotations.methods.repeat.PUTs;

import java.lang.annotation.*;

@Repeatable(PUTs.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PUT {
    String value() default "/";
}
