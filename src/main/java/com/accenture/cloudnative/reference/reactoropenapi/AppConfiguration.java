package com.accenture.cloudnative.reference.reactoropenapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AppConfiguration {

  /**
   * you'll want to comment this out if you don't want to use
   * in memory kafka
   * @return an embedded kafka broker instance
   */
  @Bean
  EmbeddedKafkaBroker broker() {
    return new EmbeddedKafkaBroker(1)
        .kafkaPorts(9092)
        .brokerListProperty("spring.kafka.bootstrap-servers"); // override application property
  }

}
