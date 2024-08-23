package moe.seikimo.rest.annotations.methods;

import moe.seikimo.rest.annotations.methods.repeat.POSTs;

import java.lang.annotation.*;

@Repeatable(POSTs.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface POST {
    String value() default "/";
}
