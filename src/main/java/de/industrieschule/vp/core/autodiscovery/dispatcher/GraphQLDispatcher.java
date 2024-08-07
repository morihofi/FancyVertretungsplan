package de.industrieschule.vp.core.autodiscovery.dispatcher;

import de.industrieschule.vp.core.autodiscovery.GraphQLLocalContext;
import de.industrieschule.vp.core.autodiscovery.templates.MultiEndpointTemplate;
import de.industrieschule.vp.core.autodiscovery.utility.Parameters;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * A data fetcher implementation for GraphQL that serves as a dispatcher to handle GraphQL queries.
 * It uses an instance of {@link MultiEndpointTemplate} to process GraphQL queries and return results.
 *
 * @param <T> The type of data to be fetched and returned by this dispatcher.
 * @author MHO
 */
public class GraphQLDispatcher<T> implements DataFetcher<T> {
    /**
     * The instance of {@link MultiEndpointTemplate} used to process GraphQL queries.
     */
    private final MultiEndpointTemplate multiEndpointInstance;

    /**
     * Constructs a new GraphQLDispatcher with the provided instance of {@link MultiEndpointTemplate}.
     *
     * @param multiEndpointInstance The instance of {@link MultiEndpointTemplate} to be used for processing queries.
     */
    public GraphQLDispatcher(MultiEndpointTemplate multiEndpointInstance) {
        this.multiEndpointInstance = multiEndpointInstance;
    }

    /**
     * Retrieves data based on the provided GraphQL query and environment.
     *
     * @param environment The GraphQL data fetching environment.
     * @return The fetched data of type {@code T}.
     * @throws Exception If an error occurs during data fetching.
     */
    @Override
    public T get(DataFetchingEnvironment environment) throws Exception {
        // Retrieve the GraphQLLocalContext set in GraphQLEndpoint class
        GraphQLLocalContext graphQLLocalContext = environment.getLocalContext();

        // Create Parameters for processing the query
        Parameters params = new Parameters(graphQLLocalContext.javalinContext(), environment, Parameters.REQUEST_SOURCE.GRAPH_QL);

        // Run the multiEndpointInstance to process the GraphQL query and return the result
        return (T) multiEndpointInstance.handleRequest(params);
    }
}
