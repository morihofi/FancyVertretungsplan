package de.industrieschule.vp.core.config;

import de.industrieschule.vp.core.Main;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;

public class Config {
    private static final Dotenv dotenv;

    static {
        try {
            dotenv = Dotenv
                    .configure()
                    .systemProperties()
                    .directory(Main.resolveDataDir().resolve("../").toAbsolutePath().toString())
                    .load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String API_PREFIX_DIR = dotenv.get("API_PREFIX_DIR");
    public static final String DB_JDBC = dotenv.get("DB_JDBC");
    public static final String DB_USERNAME = dotenv.get("DB_USERNAME");
    public static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");
    public static final Integer API_PORT = Integer.valueOf(dotenv.get("API_PORT"));
    public static final Boolean DEBUG = Boolean.valueOf(dotenv.get("DEBUG"));
    public static final String JWT_ISSUER = dotenv.get("JWT_ISSUER");
}
