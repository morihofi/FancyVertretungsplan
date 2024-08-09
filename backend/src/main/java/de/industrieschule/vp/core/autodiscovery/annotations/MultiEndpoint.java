package de.industrieschule.vp.core.autodiscovery.annotations;

import de.industrieschule.vp.core.autodiscovery.GraphQLEndpoint;
import io.javalin.http.HandlerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MultiEndpoint {

    /**
     * Field name in GraphQL Schema
     */
    String graphQLFieldName();

    GraphQLEndpoint.GRAPHQL_FIELD_TYPE graphQLFieldType();

    /**
     * HTTP Types when using REST Interface
     */
    HandlerType[] restType();
    /**
     * API Path when using REST Interface
     */
    String restPath();

    /**
     * API Version Prefix when using REST Interface (e.g. "v1" for example)
     */
    String[] restVersionPrefix() default "";

    boolean debugOnly() default false;

}
