package com.example.cursoreactivo.practicafinal.connection;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.util.LogLevel;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@EnableR2dbcRepositories(basePackages = "com.example.*")
public class PostgresSQLConnection extends AbstractR2dbcConfiguration {

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        var configuration = PostgresqlConnectionConfiguration.builder()
                .connectTimeout(Duration.ofMinutes(5))
                .host("localhost")
                .port(5432)
                .database("postgres")
                .username("postgres")
                .password("postgres")
                .schema("public")
//                .statementTimeout(Duration.ofSeconds(30))
//                .lockWaitTimeout(Duration.ofSeconds(30))
                .errorResponseLogLevel(LogLevel.ERROR)
                .build();
        var connectionFactory = new PostgresqlConnectionFactory(configuration);
        var poolConfiguration = ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMinutes(10))
                .initialSize(1)
                .maxSize(10)
                .maxLifeTime(Duration.ofMinutes(1))
                .acquireRetry(0)
                .maxAcquireTime(Duration.ofMinutes(1))
                .maxCreateConnectionTime(Duration.ofMinutes(3))
                .maxValidationTime(Duration.ofMinutes(60))
                .build();
        return new ConnectionPool(poolConfiguration);
    }
}
