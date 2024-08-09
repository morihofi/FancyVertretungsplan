package de.industrieschule.vp.core.autodiscovery;

/**
 * An exception class representing an error that occurs during the loading and registration of plugins.
 * This exception is thrown when there are issues with loading or registering plugins in the application.
 *
 * @version 1.0
 * @since [Date]
 */
public class ClassDiscoveryException extends Exception {

    /**
     * Constructs a new PluginLoaderException with the specified error message.
     *
     * @param message The error message describing the nature of the exception.
     */
    public ClassDiscoveryException(String message) {
        super(message);
    }
}
