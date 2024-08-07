package de.industrieschule.vp.core.autodiscovery.templates;

import io.javalin.websocket.*;

/**
 This class is used to implement your own WebSocket API Endpoint.
 */
public abstract class WebSocketEndpointTemplate {
    public abstract void onConnect(WsConnectContext wsConnectContext);

    public abstract void onMessage(WsMessageContext wsMessageContext);

    public abstract void onClose(WsCloseContext wsCloseContext);

    public abstract void onError(WsErrorContext wsErrorContext);

    public abstract void onBinaryMessage(WsBinaryMessageContext wsBinaryMessageContext);
}
