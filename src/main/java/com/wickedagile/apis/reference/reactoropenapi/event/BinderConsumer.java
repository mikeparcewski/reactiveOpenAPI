package com.wickedagile.apis.reference.reactoropenapi.event;

import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

/**
 * sample class to prove events are being produced
 * right
 */
@Slf4j
@Configuration
public class BinderConsumer {

  @Bean
  public Consumer<Message<DomainEvent>> consumer() {
    return domainEventMessage -> {
      log.info("RECEIVED BINDER MESSSAGE: {}", domainEventMessage);
    };
  }

}
