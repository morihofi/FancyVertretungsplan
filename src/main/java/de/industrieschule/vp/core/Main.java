package de.industrieschule.vp.core;

import de.industrieschule.vp.autodiscovery.EndpointClassDiscovery;
import de.industrieschule.vp.core.utilities.JWTTokenUtil;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.security.Security;

import de.industrieschule.vp.config.Config;

public class Main {

    private static Javalin app;

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) throws Exception {
        LOG.info("Starting Vertretungsplan ...");
        LOG.info("\n ___ ____   ____   __     ______  \n" +
                "|_ _/ ___| / ___|  \\ \\   / /  _ \\ \n" +
                " | |\\___ \\| |   ____\\ \\ / /| |_) |\n" +
                " | | ___) | |__|_____\\ V / |  __/ \n" +
                "|___|____/ \\____|     \\_/  |_|    \n");

        // Register Bouncy Castle Provider
        LOG.info("Register Bouncy Castle Security Provider");
        Security.addProvider(new BouncyCastleProvider());



        app = Javalin.create(javalinConfig -> {
                    javalinConfig.showJavalinBanner = false;
                })
                .before(context -> {
                    //Content-Type
                    context.header("Content-Type", "application/json");
                    //CORS
                    context.header("Access-Control-Allow-Origin", "*");
                    context.header("Access-Control-Allow-Methods", "*");
                    context.header("Access-Control-Allow-Headers", "Accept, Accept-CH, Accept-Charset, Accept-Datetime, Accept-Encoding, Accept-Ext, Accept-Features, Accept-Language, Accept-Params, Accept-Ranges, Access-Control-Allow-Credentials, Access-Control-Allow-Headers, Access-Control-Allow-Methods, Access-Control-Allow-Origin, Access-Control-Expose-Headers, Access-Control-Max-Age, Access-Control-Request-Headers, Access-Control-Request-Method, Age, Allow, Alternates, Authentication-Info, Authorization, C-Ext, C-Man, C-Opt, C-PEP, C-PEP-Info, CONNECT, Cache-Control, Compliance, Connection, Content-Base, Content-Disposition, Content-Encoding, Content-ID, Content-Language, Content-Length, Content-Location, Content-MD5, Content-Range, Content-Script-Type, Content-Security-Policy, Content-Style-Type, Content-Transfer-Encoding, Content-Type, Content-Version, Cookie, Cost, DAV, DELETE, DNT, DPR, Date, Default-Style, Delta-Base, Depth, Derived-From, Destination, Differential-ID, Digest, ETag, Expect, Expires, Ext, From, GET, GetProfile, HEAD, HTTP-date, Host, IM, If, If-Match, If-Modified-Since, If-None-Match, If-Range, If-Unmodified-Since, Keep-Alive, Label, Last-Event-ID, Last-Modified, Link, Location, Lock-Token, MIME-Version, Man, Max-Forwards, Media-Range, Message-ID, Meter, Negotiate, Non-Compliance, OPTION, OPTIONS, OWS, Opt, Optional, Ordering-Type, Origin, Overwrite, P3P, PEP, PICS-Label, POST, PUT, Pep-Info, Permanent, Position, Pragma, ProfileObject, Protocol, Protocol-Query, Protocol-Request, Proxy-Authenticate, Proxy-Authentication-Info, Proxy-Authorization, Proxy-Features, Proxy-Instruction, Public, RWS, Range, Referer, Refresh, Resolution-Hint, Resolver-Location, Retry-After, Safe, Sec-Websocket-Extensions, Sec-Websocket-Key, Sec-Websocket-Origin, Sec-Websocket-Protocol, Sec-Websocket-Version, Security-Scheme, Server, Set-Cookie, Set-Cookie2, SetProfile, SoapAction, Status, Status-URI, Strict-Transport-Security, SubOK, Subst, Surrogate-Capability, Surrogate-Control, TCN, TE, TRACE, Timeout, Title, Trailer, Transfer-Encoding, UA-Color, UA-Media, UA-Pixels, UA-Resolution, UA-Windowpixels, URI, Upgrade, User-Agent, Variant-Vary, Vary, Version, Via, Viewport-Width, WWW-Authenticate, Want-Digest, Warning, Width, X-Content-Duration, X-Content-Security-Policy, X-Content-Type-Options, X-CustomHeader, X-DNSPrefetch-Control, X-Forwarded-For, X-Forwarded-Port, X-Forwarded-Proto, X-Frame-Options, X-Modified, X-OTHER, X-PING, X-PINGOTHER, X-Powered-By, X-Requested-With");
                    context.header("Access-Control-Max-Age", "3600");
                })
                //CORS
                .options(Config.API_PREFIX_DIR + "/*", (context) -> {
                    /*
                    Needed for CORS
                     */
                    context.status(204);
                    context.result();
                })
                .get("/" + Config.API_PREFIX_DIR, ctx -> ctx.result("API is ready"));


        LOG.info("Initializing JWT keys...");
        JWTTokenUtil.initialize();

        LOG.info("Discovering endpoint classes...");
        EndpointClassDiscovery.discoverAndLoadClasses(app, Config.API_PREFIX_DIR);

        LOG.info("\uD83D\uDE80 Initialization complete, starting API");
        app.start(Config.API_PORT);

        LOG.info("\u2705 API is ready");


    }
}