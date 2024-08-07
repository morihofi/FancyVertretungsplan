package de.industrieschule.vp.handler.hello;

import de.industrieschule.vp.core.autodiscovery.annotations.WebSocketEndpoint;
import de.industrieschule.vp.core.autodiscovery.templates.WebSocketEndpointTemplate;
import io.javalin.websocket.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A WebSocket endpoint class that provides a simple "Hello" interaction through WebSocket.
 *
 * @version 1.0
 * @author Moritz Hofmann
 */
@WebSocketEndpoint(path = "/wsdemo", apiVersion = "v1", debugOnly = true)
public class HelloWebSocketEndpoint extends WebSocketEndpointTemplate {

    private final Logger log = LogManager.getLogger(getClass());

    /**
     * Handles the WebSocket connection event when a client connects.
     *
     * @param wsConnectContext The WebSocket connection context.
     */
    @Override
    public void onConnect(WsConnectContext wsConnectContext) {
        log.info("Someone connected from {}", wsConnectContext.host());

        wsConnectContext.send("Hey from Hello plugin!");
    }

    /**
     * Handles incoming WebSocket messages.
     *
     * @param wsMessageContext The WebSocket message context.
     */
    @Override
    public void onMessage(WsMessageContext wsMessageContext) {
        log.info("Received message, reply with echo message: {}", wsMessageContext.message());
        wsMessageContext.send(wsMessageContext.message());
    }

    /**
     * Handles the WebSocket connection closure event.
     *
     * @param wsCloseContext The WebSocket close context.
     */
    @Override
    public void onClose(WsCloseContext wsCloseContext) {
        log.info("Connection from {} closed: Reason {}" , wsCloseContext.host(), wsCloseContext.reason());
    }

    /**
     * Handles WebSocket errors.
     *
     * @param wsErrorContext The WebSocket error context.
     */
    @Override
    public void onError(WsErrorContext wsErrorContext) {
        log.info("Connection from {} encountered an error.", wsErrorContext.host());
    }

    /**
     * Handles binary WebSocket messages.
     *
     * @param wsBinaryMessageContext The WebSocket binary message context.
     */
    @Override
    public void onBinaryMessage(WsBinaryMessageContext wsBinaryMessageContext) {
        log.info("Received binary message");
    }
}
