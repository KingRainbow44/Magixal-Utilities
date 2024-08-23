package moe.seikimo.rest.annotations.methods;

import moe.seikimo.rest.annotations.methods.repeat.PATCHs;

import java.lang.annotation.*;

@Repeatable(PATCHs.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PATCH {
    String value() default "/";
}
