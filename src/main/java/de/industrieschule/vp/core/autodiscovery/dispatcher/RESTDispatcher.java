package de.industrieschule.vp.core.autodiscovery.dispatcher;


import com.google.gson.Gson;
import de.industrieschule.vp.core.autodiscovery.templates.RESTEndpointTemplate;
import de.industrieschule.vp.core.autodiscovery.utility.Parameters;
import de.industrieschule.vp.core.config.Config;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

public class RESTDispatcher implements Handler {


    /**
     * The instance of {@link RESTEndpointTemplate} used to process REST requests.
     */
    private RESTEndpointTemplate restEndpointInstance;

    private final Logger log = LogManager.getLogger(getClass());

    /**
     * Constructs a new RESTDispatcher with the provided instance of {@link RESTEndpointTemplate}.
     *
     * @param restEndpointInstance The instance of {@link RESTEndpointTemplate} to be used for processing REST requests.
     */
    public RESTDispatcher(RESTEndpointTemplate restEndpointInstance) {
        this.restEndpointInstance = restEndpointInstance;
    }

    /**
     * Handles the incoming REST request, processes it using the multiEndpointInstance, and returns the response in JSON format.
     *
     * @param ctx The Javalin HTTP context for handling the request.
     * @throws Exception If an error occurs during request processing.
     */
    @Override
    public void handle(@NotNull Context ctx) {

        Gson gson = new Gson();

        // Create Parameters for processing the REST request
        Parameters params = new Parameters(ctx, null, Parameters.REQUEST_SOURCE.REST);

        // Run the EndpointInstance to process the request and return the result as JSON

        Object instanceResponse = null;
        try {
            log.debug("Handler class {} called", restEndpointInstance.getClass().getName());
            instanceResponse = restEndpointInstance.handleRequest(params);
        } catch (Exception e) {
            log.error("Exception was thrown during handling in RESTEndpointInstance", e);
            APIErrorExceptionResponse response = new APIErrorExceptionResponse();

            if(Config.DEBUG){
                response.setMessage(e.getMessage());
                response.setSimpleClassName(e.getClass().getSimpleName());

                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                pw.flush();
                response.setStackTrace(sw.toString());
            }else {
                //Production mode
                response.setMessage("Internal Server Error. Please try again later.");
                response.setSimpleClassName(null);
                response.setStackTrace(null);
            }


            ctx.header("Content-Type","application/json");
            ctx.status(500);
            instanceResponse = response;
        }

        if(instanceResponse != null){
            log.debug("Serializing response of type {}", instanceResponse.getClass().getName());
            ctx.json(instanceResponse);
        }else {
            //If response of our handler is null, return an HTTP 204 (No Content)
            log.debug("Response of handler {} is null, so set 204 (No Content) HTTP header", restEndpointInstance.getClass().getName());
            ctx.status(204);
        }
    }
}
