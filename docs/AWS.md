## AWS Kinesis

Probably seeing a common theme here, outside of the setup, the changes needed for each of the implmentations 
are pretty much the same, starting with...

NOTE: Before you do anything [remove Kafka implementation](KAFKA.md)

### Configuration

Once you've removed kafka, we'll do what we normally do, update the pom dependencies by adding the following...

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-stream-binder-kinesis</artifactId>
  <version>2.1.0</version>
</dependency>
```

> IMPORTANT NOTE: You will need to either remove the "maven enforcer" plugin or fix the depedency collisions when adding these libs 

Next up, need to add your AWS configuration...

```yaml
spring:
  ...
  autoconfigure:
    exclude: org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration
cloud:
  aws:
    credentials:
      accessKey: <ACCESS_KEY>
      secretKey: <SECRET_KEY>
    region:
      static: us-east-1
    stack:
      auto: false
```

You'll notice in addition, I added the autoconfiguration exclude line, if you use AWS regularly and have a profile 
on your machine, you won't need this, but if you don't (or have no clue what I'm talking about) this will save you an error 
in the logs.

### Testing locally

Good luck!  If you know of something, let me know.