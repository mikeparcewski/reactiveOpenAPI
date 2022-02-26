## Using Kafka

There are a couple ways to run with Kafka...

* **Docker** - https://developer.confluent.io/quickstart/kafka-docker/
* **Confluent Kafka** - Which works across all cloud providers and has a free tier - https://www.confluent.io/get-started
* **Self Managed Cloud** - Deploy as a self managed option
    * https://aws.amazon.com/about-aws/whats-new/2020/04/create-amazon-msk-clusters-with-t3-brokers/
    * https://console.cloud.google.com/marketplace/details/click-to-deploy-images/kafka?
    * https://docs.microsoft.com/en-us/azure/hdinsight/kafka/apache-kafka-get-started

### First Comes First

Configuration is king!  Let's start by getting you ready...

* First find below in the pom.xml...
  ```xml
  <dependencyConvergence/>
  ```
* Next, delete that line.  There's a ton of issues with with dependency convergernce in the Kafka libs.  If you were going to production, I'd say don't delete and solve (will take about 10 minutes).
* Then add the following to your [pom.xml](../pom.xml) file...
  ```xml
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-stream-binder-kafka</artifactId>
      <version>${kafka-binder.version}</version>
    </dependency>
  ```
* Now in our [application.yml](../src/main/resources/application.yml) you'll need to set the profile to "kafka" which will force the application to find the application-kafka.yml file in the same location.
  * Find the section below...
    ```yaml
      spring:
        profiles:
        ## Potential options include kafka, aws, azure
        active:     
    ```
  * The section will be blank (active that is), just change to include kafka...
      ```yaml
        spring:
          profiles:
          ## Potential options include kafka, aws, azure
          active: kafka
      ```
* Finally, go checkout [application-kafka.yml](../src/main/resources/application-kafka.yml) and update any configuration settings

> Alternative (better) to adding the "kafka" profile to application.yml is to pass the profile when running command line (e.g. **mvn spring-boot:run -Dspring.profiles.active=kafka**)

### Setting Up Kafka Locally

I'm not going to waste your time with my own install.  Go to the source (Confluent) and follow 
their docker - https://developer.confluent.io/quickstart/kafka-docker/ - setup, it is by far the easiest 
way to set up and I've even named the topics used after their default topic example **quickstart**.

The existing configuration is for localhost, running on default port (9092) without a password.  By default, if you use the docker instructions, this should just work.  If you need to change, left a link for those instructions in the application-kafka.yml file and below...

https://github.com/spring-cloud/spring-cloud-stream-binder-kafka

### Running Against Cloud

Skip the install and you just need to play with the config, use the link for details.