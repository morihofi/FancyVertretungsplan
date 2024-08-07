package de.industrieschule.vp.core.autodiscovery.templates;

import de.industrieschule.vp.core.autodiscovery.utility.Parameters;

/**
This class is used to implement your own REST API Endpoint.
 */
public abstract class RESTEndpointTemplate<T> {
    public abstract T handleRequest(Parameters params) throws Exception;
}
