package de.industrieschule.vp.handler.animal;

import de.industrieschule.vp.core.autodiscovery.GraphQLEndpoint;
import de.industrieschule.vp.core.autodiscovery.annotations.MultiEndpoint;
import de.industrieschule.vp.core.autodiscovery.templates.MultiEndpointTemplate;
import de.industrieschule.vp.core.autodiscovery.utility.Parameters;
import io.javalin.http.HandlerType;

@MultiEndpoint(
        graphQLFieldName = "petAnimal",
        graphQLFieldType = GraphQLEndpoint.GRAPHQL_FIELD_TYPE.MUTATION,
        restType = {HandlerType.POST, HandlerType.GET},
        restPath = "/petAnimal",
        debugOnly = true
)
public class PetAnimalMultiEndpoint extends MultiEndpointTemplate<String> {
    @Override
    public String handleRequest(Parameters params) throws Exception {
        return "purr purr ~ The animal likes this";
    }
}
