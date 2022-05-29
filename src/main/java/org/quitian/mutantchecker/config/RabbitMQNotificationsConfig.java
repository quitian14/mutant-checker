package org.quitian.mutantchecker.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQNotificationsConfig {

    @Bean
    public Queue dnaValidationQueue(){
        return new Queue("dna_validations", true);
    }
}