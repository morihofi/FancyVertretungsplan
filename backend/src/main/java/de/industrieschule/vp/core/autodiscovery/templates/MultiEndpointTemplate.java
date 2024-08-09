package de.industrieschule.vp.core.autodiscovery.templates;

/**
 * Template for an Endpoint, that supports REST and GraphQL queries/mutations out of the same code
 * @param <T> Return Type, must be specified in GraphQL Schema
 */
public abstract class MultiEndpointTemplate<T> extends RESTEndpointTemplate {

}
