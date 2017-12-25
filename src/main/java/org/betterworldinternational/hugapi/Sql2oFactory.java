package org.betterworldinternational.hugapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sql2o.Sql2o;

@Configuration
class Sql2oFactory {
    @Bean
    public Sql2o create(
            @Value("${db.connectionUrl}") String connectionUrl,
            @Value("${db.username}") String username,
            @Value("${db.password}") String password) {
        return new Sql2o(connectionUrl, username, password);
    }
}