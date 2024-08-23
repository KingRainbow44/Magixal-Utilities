package moe.seikimo.rest.annotations.methods.repeat;

import moe.seikimo.rest.annotations.methods.PATCH;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PATCHs {
    PATCH[] value() default {};
}
