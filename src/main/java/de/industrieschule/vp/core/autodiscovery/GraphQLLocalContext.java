package de.industrieschule.vp.core.autodiscovery;

import io.javalin.http.Context;

/**
 * A class representing a local context for GraphQL execution, containing a Javalin HTTP context.
 * This class is used to provide additional context information, such as the Javalin context, to GraphQL execution.
 *
 * @param javalinContext The Javalin HTTP context associated with the GraphQL execution.
 * @author Moritz Hofmann
 */
public record GraphQLLocalContext(Context javalinContext) {
    /**
     * Constructs a new GraphQLLocalContext with the provided Javalin HTTP context.
     *
     * @param javalinContext The Javalin HTTP context associated with the GraphQL execution.
     */
    public GraphQLLocalContext {
    }

    /**
     * Gets the Javalin HTTP context associated with the GraphQL execution.
     *
     * @return The Javalin HTTP context.
     */
    @Override
    public Context javalinContext() {
        return javalinContext;
    }
}
