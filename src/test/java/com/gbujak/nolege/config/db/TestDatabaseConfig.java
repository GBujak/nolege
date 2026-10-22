package com.gbujak.nolege.config.db;

import org.flywaydb.core.Flyway;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@TestConfiguration
public class TestDatabaseConfig {

    @Bean
    @Primary
    public DataSource testDataSource() {
        String jdbcUrl = "jdbc:sqlite:file::memory:?cache=shared&foreign_keys=true";

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl(jdbcUrl);
        return dataSource;
    }

    @Bean(initMethod = "migrate")
    @Primary
    public Flyway testFlyway(DataSource testDataSource) {
        return Flyway.configure()
            .dataSource(testDataSource)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .load();
    }
}