package de.industrieschule.vp.core;

import de.industrieschule.vp.core.config.Config;
import jakarta.persistence.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.*;
import org.reflections.Reflections;

/**
 * Database access
 *
 * @author Moritz Hofmann
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    /**
     * Logger
     */
    private static final Logger log = LogManager.getLogger(HibernateUtil.class);

    /**
     * Initializes the database connection and Hibernate configuration.
     * This method should be called once during application startup.
     */
    public static void initDatabase() {
        if (sessionFactory == null) {


            try {
                Configuration configuration = getConfiguration();

                // Scan Entity classes
                Reflections reflections = new Reflections("de.industrieschule.vp.database");
                for (Class<?> clazz : reflections.getTypesAnnotatedWith(Entity.class)) {
                    configuration.addAnnotatedClass(clazz);
                }

                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(registryBuilder.build());

            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }



            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    this.setName("Database Shutdown Thread");
                    super.run();

                    shutdown();
                }
            });

        }
    }

    /**
     * Retrieves a {@link Configuration} object configured. This
     * method configures the database connection properties based on the type of database specified
     * by {@code dbType}. It sets properties such as the JDBC driver, connection URL, dialect, as well
     * as user credentials and additional Hibernate settings.
     * <p>
     * The method supports configuration for H2 and MariaDB databases. Depending on the {@code dbType},
     * it sets the appropriate JDBC driver, URL format, and Hibernate dialect. Common settings like
     * username, password, SQL logging, schema auto-update, and lazy loading are configured for all
     * database types.
     * <p>
     * Note: The database name, host, user, and password are obtained from the application's main
     * configuration, accessed via {@code Main.appConfig.getDatabase()}.
     *
     * @return A {@link Configuration} object with properties set according to the specified
     * {@code dbType} and the application's main configuration.
     * @throws NullPointerException if {@code dbType} is null.
     */
    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration();

        if (!Config.DB_JDBC.startsWith("jdbc:")) {
            throw new IllegalArgumentException("JDBC configuration string MUST start with \"jdbc:\"");
        }

        // Use connection pool, but use no connection pool when in Debug mode
        if (!Config.DEBUG) {
            // Agroal Connection Pool settings
            configureAgroalConnectionPool(configuration);
        } else {
            // No connection pool
            configuration.setProperty("hibernate.connection.provider_class",
                    "org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl");
        }

        log.info("Configuring JDBC URL and login credentials");
        configuration.setProperty(JdbcSettings.JAKARTA_JDBC_URL, Config.DB_JDBC);
        configuration.setProperty(JdbcSettings.JAKARTA_JDBC_USER, Config.DB_USERNAME);
        configuration.setProperty(JdbcSettings.JAKARTA_JDBC_PASSWORD, Config.DB_PASSWORD);

        if (Config.DEBUG) {
            //Show verbose SQL only on debug
            configuration.setProperty(JdbcSettings.SHOW_SQL, "true");
        }
        configuration.setProperty(SchemaToolingSettings.HBM2DDL_AUTO, "update");

        configuration.setProperty(TransactionSettings.ENABLE_LAZY_LOAD_NO_TRANS, "true");
        return configuration;
    }

    /**
     * Get the Hibernate SessionFactory for database operations.
     *
     * @return The SessionFactory instance.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown(){
        if(sessionFactory == null){
            log.warn("Unable to shutdown Hibernate Database, cause it wasn't initialized");
            return;
        }

        log.info("Initiating shutdown of Hibernate Database");
        try {
            sessionFactory.close();
            log.info("Shutdown of Hibernate Database completed successfully.");
            log.info("Free up resources");
            sessionFactory = null;
        } catch (Exception e) {
            log.error("An error occurred during the shutdown of the Hibernate Database: {}", e.getMessage(), e);
        }
    }

    private static void configureAgroalConnectionPool(Configuration configuration) {
        log.info("Configuring Agroal connection pool");
        configuration.setProperty("hibernate.connection.provider_class", "org.hibernate.agroal.internal.AgroalConnectionProvider");
        configuration.setProperty("hibernate.agroal.minSize", "10");
        configuration.setProperty("hibernate.agroal.maxSize", "50");
        configuration.setProperty("hibernate.agroal.initialSize", "25");
        configuration.setProperty("hibernate.agroal.maxLifetime", "PT1000S"); // Lifetime of 1000 seconds
        configuration.setProperty("hibernate.agroal.validationTimeout",
                "PT5S"); // Set timeout for checking database connectivity to 5 seconds
        configuration.setProperty("hibernate.agroal.validationQuery", "SELECT 1"); // SQL query to check if database is available
    }
}
