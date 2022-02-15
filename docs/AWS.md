## AWS Kinesis

Probably seeing a common theme here, outside of the setup, the changes needed for each of the implmentations 
are pretty much the same...

> IMPORTANT NOTE: You will need to disable **dependencyConvergence** in the maven enforcer plugin
> because there is a lot of conflicting AWS jars.  It's easy, just go to the pom.xml
> and find &lt;dependencyConvergence&gt; and remove that line (and just that line)

### Configuration

So...

* First add the following to your pom.xml file...
  ```xml
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-stream-binder-kinesis</artifactId>
      <version>${kinesis-binder.version}</version>
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
* The section will be blank (active that is), just change to include aws...
    ```yaml
      spring:
        profiles:
        ## Potential options include kafka,
        active: aws
    ```
* Next up, need to add your AWS configuration [application-aws.yml](../src/main/resources/application-aws.yml)...
    ```yaml
    aws:
      credentials:
        accessKey: <ACCESS_KEY>
        secretKey: <SECRET_KEY>
      region:
        static: us-east-1
      stack:
        auto: false
    autoconfigure:
      exclude: org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration
    ```
  Replace your credentials and region (if needed) and have some fun!

You'll notice in addition, I added the autoconfiguration exclude line, if you use AWS regularly and have a profile 
on your machine, you won't need this, but if you don't (or have no clue what I'm talking about) this will save you an error 
in the logs.

> To know it's working, look for the log line **RECEIVED BINDER MESSSAGE**