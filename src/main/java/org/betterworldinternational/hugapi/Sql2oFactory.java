package org.betterworldinternational.hugapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sql2o.Sql2o;

@Configuration
class Sql2oFactory {
    @Bean
    public Sql2o create() {
        return new Sql2o("jdbc:mysql://localhost:3306/hug?useUnicode=yes&characterEncoding=UTF-8", "root", "root");
    }
}