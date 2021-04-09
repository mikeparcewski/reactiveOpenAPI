## Messaging

Unlike Mongo, you can easily switch out your underlying messaging platform without a code change.  This is done 
just by changing the binders registered in [pom.xml](pom.xml) and settings in 
[src/main/resources/application.yml](src/main/resources/application.yml).

### Changing the Binder

Spring Cloud Streams makes it easy to switch out the binder implementations without changing the code.  Check out
https://spring.io/projects/spring-cloud-stream for the different implementations.

* [GCP Pub/Sub Learnings](GCP.md)
* [Azure Event Hubs](AZURE.md)
* [RabbitMQ](RABBITMQ.md)
* 

> NOTE: I've tried a few different binders out based on this codebase and didn't have any issues.  
> You shouldn't need to change code to get anything working.

### Running Kafka

There are a couple ways to run with Kafka...

* **In Memory** - This is the default, you don't need to do anything to get it working
* **Confluent Kafka** - Which works across all cloud providers and has a free tier - https://www.confluent.io/get-started
* **Self Managed Cloud** - Deploy as a self managed option
    * https://aws.amazon.com/about-aws/whats-new/2020/04/create-amazon-msk-clusters-with-t3-brokers/
    * https://console.cloud.google.com/marketplace/details/click-to-deploy-images/kafka?
    * https://docs.microsoft.com/en-us/azure/hdinsight/kafka/apache-kafka-get-started

### Configuration
Out of the box, this app uses the in memory test database.  To disable that, just find the following section in your pom.xml file...

```xml
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka-test</artifactId>
    </dependency>
```

and change it to only be available for testing by adding `<scope>test</scope>`...

```xml
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka-test</artifactId>
        <scope>test</scope>
    </dependency>
```

In addition, you'll need to comment out the EmbeddedKafka instantiation that happens in
[src/main/java/com/accenture/cloudnative/reference/reactoropenapi/AppConfiguration.java](src/main/java/com/accenture/cloudnative/reference/reactoropenapi/AppConfiguration.java)

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

> If you ever want to move back to in memory, just remove that `<scope>test</scope>` line, uncomment the broker 
> creation in AppConfiguration and make sure to update your config back to localhost (if you changed it)

#### Cloud Variation
If you're changing to use a cloud version of Kafka, in addition to adding `<scope>test</scope>` from your POM file,
we also need to change the location of our database in [src/main/resources/application.yaml](src/main/resources/application.yaml)

Start by looking for the following secion...

```yaml
spring:
  cloud:
      ...
      ## lots to configure to make production
      ## ready - https://github.com/spring-cloud/spring-cloud-stream-binder-kafka
      kafka:
        binder:
          brokers: localhost
          defaultBrokerPort: 9092
```

The link in yaml file shows how to configure and depending on your implementation choice (e.g. cloud, confluent, docker) 
the documentation will explain how to update.  If using simple docker instance, you might not need to change 
anything here.