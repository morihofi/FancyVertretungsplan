package de.industrieschule.vp.autodiscovery.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public  @interface WebSocketEndpoint {
    String path();
    String[] apiVersion() default "";
    boolean debugOnly() default false;
}

