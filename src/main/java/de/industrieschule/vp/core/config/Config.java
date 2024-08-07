package de.industrieschule.vp.config;

public class Config {
    public static final String API_PREFIX_DIR = System.getenv("API_PREFIX_DIR");
    public static final String DB_JDBC = System.getenv("DB_JDBC");
    public static final Integer API_PORT = Integer.valueOf(System.getenv("API_PORT"));
    public static final Boolean DEBUG = Boolean.valueOf(System.getenv("DEBUG"));
}
