package it.kernelpanic.chronos.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Configuration file for the connection to the database
 * In development we used localhost for the connection. While moving into pre-production and production
 * the path to the database must be update as well.
 * We use a database into a separate server inside the segregated network hosting Postgres.
 * This class must contain information regarding the url (containing both port and database to be used),
 * the username to access the db and the corresponding password.
 */
@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://192.168.1.91:5432/chronos");
        dataSource.setUsername("postgres");
        dataSource.setPassword("PostgreSQL2020!");

        return dataSource;
    }

}
