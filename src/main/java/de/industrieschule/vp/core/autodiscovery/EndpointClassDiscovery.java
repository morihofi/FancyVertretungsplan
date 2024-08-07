package de.industrieschule.vp.core.autodiscovery;

import de.industrieschule.vp.core.autodiscovery.annotations.AutoloadClass;
import de.industrieschule.vp.core.autodiscovery.annotations.RESTEndpoint;
import de.industrieschule.vp.core.autodiscovery.annotations.WebSocketEndpoint;
import de.industrieschule.vp.core.autodiscovery.dispatcher.RESTDispatcher;
import de.industrieschule.vp.core.autodiscovery.templates.AutoloadClassTemplate;
import de.industrieschule.vp.core.autodiscovery.templates.RESTEndpointTemplate;
import de.industrieschule.vp.core.autodiscovery.templates.WebSocketEndpointTemplate;
import de.industrieschule.vp.core.config.Config;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import io.javalin.router.Endpoint;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * A utility class for loading and registering plugins in the application.
 * This class provides methods to scan the classpath for plugin classes and register them as REST or WebSocket.
 *
 */
public class EndpointClassDiscovery {

    /**
     * The logger instance for logging plugin loading and registration messages.
     */
    private static final Logger log = LogManager.getLogger(EndpointClassDiscovery.class);

    /**
     * The common package prefix used for scanning plugin classes.
     */
    public static final String packagePrefix = "de.industrieschule.vp.handler";

    /**
     * Loads and registers REST, WebSocket, and GraphQL plugins based on annotations and configuration.
     *
     * @param javalin     The Javalin instance to which plugins will be registered.
     * @param pathPrefix  The prefix for API paths where plugins will be registered.
     * @throws NoSuchMethodException     If a plugin class lacks a required constructor.
     * @throws InvocationTargetException If an error occurs while invoking a plugin's constructor.
     * @throws InstantiationException    If there is an issue with instantiating a plugin.
     * @throws IllegalAccessException    If there is illegal access to a plugin class or constructor.
     * @throws IOException               If an IO error occurs during plugin loading.
     * @throws NotFoundException         If a requested resource is not found.
     * @throws ClassDiscoveryException     If there is an issue with loading a plugin.
     */
    public static void discoverAndLoadClasses(Javalin javalin, String pathPrefix) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, NotFoundException, ClassDiscoveryException {

        log.info("\uD83D\uDD0E Scanning in \"" + packagePrefix + ".*\" for component classes ...");

        // Erstelle ein Reflections Objekt, um den root Classpath zu scannen
        Reflections reflections = new Reflections(packagePrefix);

        loadAndRegisterRESTandWebSocketPlugins(pathPrefix, reflections, javalin);
    }

    /**
     * Validates an API path to ensure it meets certain requirements.
     *
     * @param path The API path to be validated.
     * @throws ClassDiscoveryException If the API path is invalid.
     */
    private static void validatePath(String path) throws ClassDiscoveryException {
        if (path.isEmpty()) {
            throw new ClassDiscoveryException("API Path cannot be empty");
        }
        if (!path.startsWith("/")) {
            throw new ClassDiscoveryException("API Path must start with a \"/\"");
        }
    }

    /**
     * Constructs a full API path by combining the path prefix, API version, and the specific path.
     *
     * @param pathPrefix   The prefix for API paths.
     * @param apiVersion   The API version to be included in the path (can be empty).
     * @param path         The specific path for the plugin.
     * @return The constructed full API path.
     */
    private static String constructPath(String pathPrefix, String apiVersion, String path) {
        String versionPrefix = apiVersion.isEmpty() ? "" : "/" + apiVersion;
        return pathPrefix + versionPrefix + path;
    }



    static void loadAndRegisterRESTandWebSocketPlugins(String pathPrefix, Reflections reflections, Javalin javalin) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassDiscoveryException {
        // Finde alle Klassen mit passenden Annotationen
        Set<Class<?>> RESTEndpointClasses = reflections.getTypesAnnotatedWith(RESTEndpoint.class);
        Set<Class<?>> WebSocketEndpointClasses = reflections.getTypesAnnotatedWith(WebSocketEndpoint.class);
        Set<Class<?>> AutoloadClasses = reflections.getTypesAnnotatedWith(AutoloadClass.class);

        for (Class<?> clazz : AutoloadClasses) {

            // Finde den passenden Konstruktor
            java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor();

            boolean debugOnly = clazz.getAnnotation(AutoloadClass.class).debugOnly();

            if (debugOnly && !Config.DEBUG) {
                //Production mode, don't enable plugins that should only run in debug mode
                continue;
            }

            // Erstelle eine neue Instanz der Klasse mit den gegebenen Parametern (Constructor wird auto. aufgerufen)
            AutoloadClassTemplate instance = (AutoloadClassTemplate) constructor.newInstance();

            log.info("\u26A1 Invoking onPluginLoad() method in class {}", clazz.getName());
            instance.onClassLoad();
        }


        // Durchlaufe alle gefundenen Klassen
        for (Class<?> clazz : RESTEndpointClasses) {

            // Finde den passenden Konstruktor
            java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor();

            // Erstelle eine neue Instanz der Klasse mit den gegebenen Parametern (Constructor wird auto. aufgerufen)

            Object objInstance = constructor.newInstance();
            if(!(objInstance instanceof RESTEndpointTemplate)){
                throw new ClassDiscoveryException("Annotation is annotated on class with wrong type. Must be " + RESTEndpointTemplate.class.getName() + ", instead of " + objInstance.getClass().getName());
            }
            RESTEndpointTemplate instance = (RESTEndpointTemplate) objInstance;


            HandlerType[] types = clazz.getAnnotation(RESTEndpoint.class).type();
            String path = clazz.getAnnotation(RESTEndpoint.class).path();
            String[] apiVersions = clazz.getAnnotation(RESTEndpoint.class).apiVersion();
            boolean debugOnly = clazz.getAnnotation(RESTEndpoint.class).debugOnly();

            if (debugOnly && !Config.DEBUG) {
                //Production mode, don't enable plugins that should only run in debug mode
                continue;
            }

            validatePath(path);

            for (String apiVersion : apiVersions) {

                path = constructPath(pathPrefix, apiVersion, path);


                for (HandlerType type : types) {
                    Handler handler = new RESTDispatcher(instance);
                    javalin.addEndpoint(new Endpoint(type, path, handler));
                    log.info("\uD83D\uDD0C REST-Plugin class " + clazz.getName() + " loaded, listening on " + type + " " + path);
                }
            }

        }




        // Durchlaufe alle gefundenen Klassen
        for (Class<?> clazz : WebSocketEndpointClasses) {

            // Finde den passenden Konstruktor
            java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor();

            // Erstelle eine neue Instanz der Klasse mit den gegebenen Parametern (Constructor wird auto. aufgerufen)
            WebSocketEndpointTemplate instance = (WebSocketEndpointTemplate) constructor.newInstance();

            String path = clazz.getAnnotation(WebSocketEndpoint.class).path();
            String[] apiVersions = clazz.getAnnotation(WebSocketEndpoint.class).apiVersion();
            boolean debugOnly = clazz.getAnnotation(WebSocketEndpoint.class).debugOnly();

            if (debugOnly && !Config.DEBUG) {
                //Production mode, don't enable plugins that should only run in debug mode
                continue;
            }

            validatePath(path);

            for (String apiVersion : apiVersions) {
                path = constructPath(pathPrefix,apiVersion, path);

                javalin.ws(path, wsConfig -> {
                    wsConfig.onConnect(wsConnectContext -> {
                        instance.onConnect(wsConnectContext);
                    });
                    wsConfig.onClose(wsCloseContext -> instance.onClose(wsCloseContext));
                    wsConfig.onError(wsErrorContext -> instance.onError(wsErrorContext));
                    wsConfig.onMessage(wsMessageContext -> instance.onMessage(wsMessageContext));
                    wsConfig.onBinaryMessage(wsBinaryMessageContext -> instance.onBinaryMessage(wsBinaryMessageContext));
                });
                log.info("\u2194\uFE0F Websocket-Plugin class " + clazz.getName() + " loaded, listening at " + path);

            }

        }

    }


}
