package com.example.book.context;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing is enabled using @EnableJpaAuditing.
 */
@Configuration
@EnableJpaAuditing()
public class JpaContext {

}