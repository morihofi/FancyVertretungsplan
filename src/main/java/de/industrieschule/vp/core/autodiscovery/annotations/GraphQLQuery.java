package de.industrieschule.vp.core.autodiscovery.annotations;

import de.industrieschule.vp.core.autodiscovery.GraphQLEndpoint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GraphQLQuery {
    String fieldName();
    GraphQLEndpoint.GRAPHQL_FIELD_TYPE graphQLFieldType();
}
