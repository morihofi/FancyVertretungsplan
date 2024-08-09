package de.industrieschule.vp.core.autodiscovery.utility;

import graphql.schema.DataFetchingEnvironment;
import io.javalin.http.Context;

import java.sql.SQLException;

public class Parameters {

    private final Context javalinContext;
    private final DataFetchingEnvironment graphQLDatafetchingEnvironment;

  /*  public UserSessionEntity getSession() throws SQLException {
        return ValidateSessionToken.getSessionFromRequestAndValidate(javalinContext);
    }*/

    public enum REQUEST_SOURCE {
        REST, GRAPH_QL
    }

    public enum REST_ARGUMENT_TYPE {
        PATH, QUERY
    }


    public final REQUEST_SOURCE source;

    public Parameters(Context javalinContext, DataFetchingEnvironment graphQLDatafetchingEnvironment, REQUEST_SOURCE source) {
        this.javalinContext = javalinContext;
        this.graphQLDatafetchingEnvironment = graphQLDatafetchingEnvironment;
        this.source = source;
    }

    /**
     * Retrieves the argument value associated with the given parameter name and converts it to the specified target class type.
     * <p>
     * This method fetches the argument value based on the source (either REST or GRAPH_QL). If the value is found and its type matches
     * the target class type, it is returned as is. Otherwise, the value is converted to the desired type using the {@code convertValue} method.
     * </p>
     *
     * @param <T>         the type of the return value
     * @param paramName   the name of the parameter whose value needs to be fetched
     * @param targetClass the class type to which the value should be converted
     * @return the argument value converted to the specified target class type, or {@code null} if the value is not found
     * @throws ClassCastException if the value cannot be cast to the specified target class type and cannot be converted
     */
    public <T> T getArgument(String paramName, Class<T> targetClass, REST_ARGUMENT_TYPE type) {
        Object value = null;
        switch (getSource()) {
            case REST -> {
                if (type == REST_ARGUMENT_TYPE.QUERY) {
                    value = javalinContext.queryParam(paramName);
                } else if (type == REST_ARGUMENT_TYPE.PATH) {
                    value = javalinContext.pathParam(paramName);
                }
            }
            case GRAPH_QL -> value = graphQLDatafetchingEnvironment.getArgument(paramName);
        }

        if (value == null) {
            return null;
        }

        if (targetClass.isAssignableFrom(value.getClass())) {
            return targetClass.cast(value);
        } else {
            return convertValue(value, targetClass);
        }
    }


    private <T> T convertValue(Object value, Class<T> targetClass) {
        if (targetClass == Integer.class) {
            return targetClass.cast(Integer.parseInt((String) value));
        } else if (targetClass == Double.class) {
            return targetClass.cast(Double.parseDouble((String) value));
        } else if (targetClass == Boolean.class) {
            return targetClass.cast(Boolean.parseBoolean((String) value));
        } else if (targetClass == String.class) {
            return targetClass.cast(value.toString());
        } else if (targetClass == Float.class) {
            return targetClass.cast(Float.parseFloat(value.toString()));
        }
        // Weitere Typkonvertierungen können hier hinzugefügt werden

        throw new IllegalArgumentException("Unsupported conversion type: " + targetClass.getName());
    }

    /**
     * Retrieves the value of the specified argument. If the argument is null, returns the provided default value.
     *
     * @param <T>          The type of the argument to be retrieved.
     * @param paramName    The name of the parameter to retrieve.
     * @param defaultValue The default value to return if the argument is null.
     * @param targetClass  The class of the target type.
     * @return The value of the specified argument or the default value if the argument is null.
     */
    public <T> T getArgumentOrDefault(String paramName, T defaultValue, Class<T> targetClass, REST_ARGUMENT_TYPE type) {
        T returnData = getArgument(paramName, targetClass, type);

        if (returnData == null) {
            return defaultValue;
        } else {
            return returnData;
        }
    }

    /**
     * Returns the Javalin Context
     *
     * @return Javalin Context
     */
    public Context getJavalinContext() {
        return javalinContext;
    }

    /**
     * Returns the GraphQL Data-fetching environment
     * <p>
     * <b>Please note, that this method only returns a value, when {@link #getSource()}'s value returns GRAPH_QL</b>
     * </p>
     *
     * @return Javalin Context
     */
    public DataFetchingEnvironment getGraphQLDatafetchingEnvironment() {
        return graphQLDatafetchingEnvironment;
    }

    /**
     * Gets the source of this request, so from which interface the request comes.
     *
     * @return Request source
     */
    public REQUEST_SOURCE getSource() {
        return source;
    }
}
