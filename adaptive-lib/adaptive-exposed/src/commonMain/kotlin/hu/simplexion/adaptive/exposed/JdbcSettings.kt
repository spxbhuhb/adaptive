package hu.simplexion.adaptive.exposed

import hu.simplexion.adaptive.settings.dsl.setting

/**
 * Settings for a database connection.
 */
class JdbcSettings {

    /**
     * The JDBC_URL to connect. Examples:
     *
     * ```
     * jdbc:postgresql://127.0.0.1/z2site
     * jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;
     * ```
     */
    val jdbcUrl by setting<String> { "DB_JDBC_URL" }

    /**
     * Class name of the JDBC driver. Examples:
     *
     * ```
     * org.postgresql.Driver
     * org.h2.Driver
     * ```
     */
    val driverClassName by setting<String> { "DB_DRIVER_CLASS_NAME" }

    /**
     * Username to use for the database connection.
     */
    val username by setting<String> { "DB_USER" }

    /**
     * Password to use for the database connection.
     */
    val password by setting<String> { "DB_PASSWORD" } // TODO .. sensitive

}