## AWS Kinesis

Probably seeing a common theme here, outside of the setup, the changes needed for each of the implmentations 
are pretty much the same...

### Configuration

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
      <artifactId>spring-cloud-stream-binder-kinesis</artifactId>
      <version>${kinesis-binder.version}</version>
    </dependency>
  ```
* Now in our [application.yml](../src/main/resources/application.yml) you'll need to set the profile to "aws" which will force the application to find the application-aws.yml file in the same location.
    * Find the section below...
      ```yaml
        spring:
          profiles:
          ## Potential options include kafka, aws, azure
          active:     
      ```
    * The section will be blank (active that is), just change to include aws...
        ```yaml
          spring:
            profiles:
            ## Potential options include kafka, aws, azure
            active: aws
        ```
* Finally, need to add your AWS configuration [application-aws.yml](../src/main/resources/application-aws.yml)...
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

> Alternative (better) to adding the "aws" profile to application.yml is to pass the profile when running command line (e.g. **mvn spring-boot:run -Dspring.profiles.active=aws**)

NOTE: When updating the yaml file, please make sure to update "vendorChange" to whatever you named the Kinesis stream.

> To know this is working, look for the log line **RECEIVED BINDER MESSSAGE**

The "autoconfigure" part of the config was added for folks who don't use AWS regularly or have a SDK profile on your machine.
If you know what I'm talking about (or have a profile, but don't) you can remove this line and the credentials/region detail.

If you don't have a profile from the SDK, removing this will break stuff.

You can learn more at https://github.com/spring-cloud/spring-cloud-stream-binder-aws-kinesis/blob/main/spring-cloud-stream-binder-kinesis-docs/src/main/asciidoc/overview.adoc