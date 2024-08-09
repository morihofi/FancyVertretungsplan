package de.industrieschule.vp.handler.hello;

import de.industrieschule.vp.core.autodiscovery.GraphQLEndpoint;
import de.industrieschule.vp.core.autodiscovery.annotations.MultiEndpoint;
import de.industrieschule.vp.core.autodiscovery.templates.MultiEndpointTemplate;
import de.industrieschule.vp.core.autodiscovery.utility.Parameters;
import io.javalin.http.HandlerType;

/**
 * A multi-endpoint class that provides a simple "Hello" message through both GraphQL and REST.
 * <p>
 *     Available using the RESTful API at "/api/hello" or using GraphQL, if "/api" is your API prefix
 * </p>
 *
 * @version 1.0
 * @author Moritz Hofmann
 */
@MultiEndpoint(
        graphQLFieldName = "hello",
        graphQLFieldType = GraphQLEndpoint.GRAPHQL_FIELD_TYPE.QUERY,
        restType = {HandlerType.GET},
        restPath = "/hello",
        debugOnly = true
)
public class HelloMultiEndpoint extends MultiEndpointTemplate<String> {

    /**
     * Retrieves a "Hello" message with a custom name.
     *
     * @param params The parameters containing the name for the greeting.
     * @return A greeting message.
     */
    @Override
    public String handleRequest(Parameters params) {
        return "Hello " + params.getArgument("name", String.class, Parameters.REST_ARGUMENT_TYPE.QUERY) + "!";
    }
}
