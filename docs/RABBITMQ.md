## Using RabbitMQ

Like Kafka, the RabbitMQ binder implementation can be run locally or in the cloud pretty easily. 

As always, [remove Kafka implementation](KAFKA.md) before you do anything.

### Running RabbitMQ

Ok, this isn't a tutorial on running RabbitMQ.  If you want to learn more around running it...

* Locally - https://codeburst.io/get-started-with-rabbitmq-on-docker-4428d7f6e46b
* Cloud (AMQP) - https://www.cloudamqp.com/blog/part1-rabbitmq-for-beginners-what-is-rabbitmq.html#:~:text=Set%20up%20a%20RabbitMQ%20instance&text=CloudAMQP%20can%20be%20used%20for,your%20cloud%2Dhosted%20RabbitMQ%20instance.
* AWS - https://docs.aws.amazon.com/amazon-mq/latest/developer-guide/amazon-mq-setting-up.html (although)
* Azure - No managed option available, but you can use cloudamqp for one in Azure if you want
* Google Cloud Platform (GCP) - Probably best to go with cloudamqp too

### Configuration

Once we've removed the Kafka implementation, we move to adding the RabbitMQ configuration.  This requires a few things, starting 
with the RabbitMQ libs in the POM...

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-stream-binder-rabbit</artifactId>
</dependency>
```

Then it's as simple as updating the [/src/main/resources/application.yml](/src/main/resources/application.yml) for cloud...

```yaml
spring:
  rabbitmq:
    addresses: <AMQP URL FROM CLOUDAMQP>
```

or 

```yaml
spring:
  rabbitmq:
    host: localhost
    port: <PORT RABBIT IS RUNNING ON>
    username: <USERNAME>
    password: <PASSWORD>    
```

A lot of times if you're running locally, you probably won't need the username/password.

Really is that easy.

## Unit/Component Testing

Thought I had something good for you?  No, unlike Kafka, and like GCP, Azure the rest there is no in-memory 
version here, so a container is required, and you can read more on it at https://gaddings.io/testing-spring-boot-apps-with-rabbitmq-using-testcontainers/



