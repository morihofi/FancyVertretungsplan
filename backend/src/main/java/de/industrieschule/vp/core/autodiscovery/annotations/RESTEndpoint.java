package de.industrieschule.vp.core.autodiscovery.annotations;


import io.javalin.http.HandlerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RESTEndpoint {
    HandlerType[] type();
    String path();
    String[] apiVersion() default "";
    boolean debugOnly() default false;
}
