## Using Kafka

There are a couple ways to run with Kafka...

* **Docker** - https://developer.confluent.io/quickstart/kafka-docker/
* **Confluent Kafka** - Which works across all cloud providers and has a free tier - https://www.confluent.io/get-started
* **Self Managed Cloud** - Deploy as a self managed option
    * https://aws.amazon.com/about-aws/whats-new/2020/04/create-amazon-msk-clusters-with-t3-brokers/
    * https://console.cloud.google.com/marketplace/details/click-to-deploy-images/kafka?
    * https://docs.microsoft.com/en-us/azure/hdinsight/kafka/apache-kafka-get-started

### First Comes First

In our [application.yml](../src/main/resources/application.yml) you'll need to set the profile to "kafka" 
which will force the application to find the application-kafka.yml file in the same location.

So...

* Begin by adding the following dependency to your pom.xml file
  ```xml
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-stream-binder-kafka</artifactId>
      <version>${kafka-binder.version}</version>
    </dependency>
  ```
* Go into [application.yml](../src/main/resources/application.yml)
  * Find the section below...
    ```yaml
      spring:
        profiles:
        ## Potential options include kafka,
        active:     
    ```
* The section will be blank (active that is), just change to include kafka...
    ```yaml
      spring:
        profiles:
        ## Potential options include kafka,
        active: kafka
    ```

> Alternative (better) is to pass the profile when running command line (e.g. **mvn spring-boot:run -Dspring.profiles.active=kafka**)

### Local

I'm not going to waste your time with easy installs.  Go to the source (Confluent) and follow 
their docker - https://developer.confluent.io/quickstart/kafka-docker/ - setup, it is by far the easiest 
way to set up and I've even named the topics used after their default topic example **quickstart**.

The existing configuration is for localhost, running on default port (9092) without a password.  By default, if you use the docker instructions, this should just work.  If you need to change, left a link for those instructions in the application-kafka.yml file and below...

https://github.com/spring-cloud/spring-cloud-stream-binder-kafka

#### Cloud Variation

Running against cloud is a lot like running against local, same steps, just need 
to configure appropriately.  You'll edit the same config [application-kafka.yml](../src/main/resources/application-kafka.yml) 
just with some specific parameters.  A