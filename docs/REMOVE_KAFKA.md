## Removing Kafka

Before adding another broker, you'll probably want to remove Kafka.  To do this we first remove it from the 
POM file [../pom.xml](../pom.xml)

```xml
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-stream-binder-kafka</artifactId>
      <version>${kafka-binder-version}</version>
    </dependency>

    <dependency>
      <groupId>org.springframework.kafka</groupId>
      <artifactId>spring-kafka-test</artifactId>
    </dependency>
```

and then the configuration from [src/main/resources/application.yml](src/main/resources/application.yml)...

```yaml
      ## lots to configure to make production
      ## ready - https://github.com/spring-cloud/spring-cloud-stream-binder-kafka
      kafka:
        binder:
          brokers: localhost
          defaultBrokerPort: 9092
```

and then the Java instantiation [src/main/java/com/wickedagile/apis/reference/reactoropenapi/AppConfiguration.java](src/main/java/com/wickedagile/apis/reference/reactoropenapi/AppConfiguration.java)...

```java
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
```