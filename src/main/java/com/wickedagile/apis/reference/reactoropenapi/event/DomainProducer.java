package com.wickedagile.apis.reference.reactoropenapi.event;

import com.wickedagile.apis.reference.reactoropenapi.event.exception.BinderExceptionHandler;
import java.util.function.Supplier;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.instrument.messaging.MessagingSleuthOperators;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

/**
 * responsible for publishing messages
 */
@Slf4j
@Component("producer")
@NoArgsConstructor
public class DomainProducer implements Supplier<Flux<Message<?>>>  {

  Sinks.Many<Message<?>> sink = Sinks.many().multicast()
      .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE);

  @Autowired
  BeanFactory beanFactory;

  /**
   * catches an entity change event and processes it
   * @param de the actual domain event
   */
  @Async
  @EventListener
  public void handleEntityChange(DomainEvent de) {
    Message<?> message = createMessage(de);
    sink.emitNext(message, BinderExceptionHandler.emitFailureHandler);
  }

  /**
   * create a message with headers for tracing
   * @param op the entity operation to stuff in the message
   * @return a wrapped message
   */
  <T> Message<?> createMessage(T op) {
    return MessagingSleuthOperators.handleOutputMessage(
        beanFactory,
        MessagingSleuthOperators.forInputMessage(beanFactory, new GenericMessage<>(op))
    );
  }

  @Override
  public Flux<Message<?>> get() {
    return sink.asFlux();
  }

}
