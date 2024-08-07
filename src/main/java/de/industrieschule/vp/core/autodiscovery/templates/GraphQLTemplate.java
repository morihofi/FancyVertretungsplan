package de.industrieschule.vp.core.autodiscovery.templates;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * An abstract class representing a template for implementing GraphQL data fetchers.
 * Subclasses of this class should extend it and implement the {@link #get(DataFetchingEnvironment)} method
 * to define how data should be fetched and returned for a specific GraphQL field.
 *
 * @param <T> The type of data to be fetched and returned by the GraphQL data fetcher.
 * @author Moritz Hofmann
 */
public abstract class GraphQLTemplate<T> implements DataFetcher<T> {

    /**
     * This method should be implemented by subclasses to define how data should be fetched and returned
     * for a specific GraphQL field.
     *
     * @param dataFetchingEnvironment The GraphQL data fetching environment containing information about the field
     *                                being queried and its arguments.
     * @return The fetched data of type {@code T}.
     * @throws Exception If an error occurs during data fetching.
     */
    @Override
    public abstract T get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception;
}
