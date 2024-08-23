package moe.seikimo.rest.annotations.methods;

import moe.seikimo.rest.annotations.methods.repeat.DELETEs;

import java.lang.annotation.*;

@Repeatable(DELETEs.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DELETE {
    String value() default "/";
}
