package com.tree.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public DataSeeder testDataSeeder() {
        // Return null or a no-op seeder to disable seeding in tests
        return null;
    }
}
