package com.gbujak.nolege.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableJdbcRepositories
public class DatabaseConfig {

    private static final String APP_NAME = "nolege";

    @Bean
    public DataSource dataSource() {
        Path appDataDir = getAppDataDirectory();
        try {
            Files.createDirectories(appDataDir);
        } catch (Exception e) {
            throw new RuntimeException("Could not create application data directory", e);
        }

        File dbFile = appDataDir.resolve("nolege.db").toFile();
        String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath() + "?foreign_keys=true";

        return DataSourceBuilder.create()
            .driverClassName("org.sqlite.JDBC")
            .url(jdbcUrl)
            .build();
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .load();
    }

    private Path getAppDataDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home");

        if (os.contains("win")) {
            String appData = System.getenv("APPDATA");
            return (appData != null && !appData.isEmpty())
                ? Paths.get(appData, APP_NAME)
                : Paths.get(home, "AppData", "Roaming", APP_NAME);
        } else if (os.contains("mac")) {
            return Paths.get(home, "Library", "Application Support", APP_NAME);
        } else {
            String xdgDataHome = System.getenv("XDG_DATA_HOME");
            return (xdgDataHome != null && !xdgDataHome.isEmpty())
                ? Paths.get(xdgDataHome, APP_NAME)
                : Paths.get(home, ".local", "share", APP_NAME);
        }
    }
}