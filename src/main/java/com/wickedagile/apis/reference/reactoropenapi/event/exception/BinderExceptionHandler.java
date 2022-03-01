package com.wickedagile.apis.reference.reactoropenapi.event.exception;

import java.util.concurrent.locks.LockSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Sinks;

/**
 * spring took away most of the convience
 * methods when it comes to binders
 * this is a simple implmentation of one
 */
@Slf4j
public class BinderExceptionHandler {

  /**
   * nanos to wait between retries (is really blocking)
   */
  @Value("${spring.application.processor.emitWaitNanos}")
  static int emitWaitNanos;

  /**
   * provides a mechanism for non-serialized access
   * could extend for other types issues in the future
   * like overflow
   */
  public static final Sinks.EmitFailureHandler emitFailureHandler = (signalType, emitResult) -> {
    switch (emitResult) {
      // going to try again as we're expecting serialization
      // failure
      case FAIL_NON_SERIALIZED:
        LockSupport.parkNanos(emitWaitNanos);
        return true;
      case FAIL_ZERO_SUBSCRIBER:
        log.error("No subscribers provided, please configure with subscribers or remove producer.");
        System.exit(-1);
      default:
        log.info("Producer failing {}", emitResult);
        return false;
    }
  };

}
