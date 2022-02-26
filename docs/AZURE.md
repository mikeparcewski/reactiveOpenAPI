## Azure EventHubs

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
      <groupId>com.azure.spring</groupId>
      <artifactId>azure-spring-cloud-stream-binder-eventhubs</artifactId>
      <version>${eventhubs-binder.version}</version>
    </dependency>
  ```
* Now in our [application.yml](../src/main/resources/application.yml) you'll need to set the profile to "azure" which will force the application to find the application-azure.yml file in the same location.
    * Find the section below...
      ```yaml
        spring:
          profiles:
          ## Potential options include kafka, aws, azure
          active:     
      ```
    * The section will be blank (active that is), just change to include azure...
        ```yaml
          spring:
            profiles:
            ## Potential options include kafka, aws, azure
            active: azure
        ```
* Finally, need to add your Azure configuration [application-azure.yml](../src/main/resources/application-azure.yml)...
    ```yaml
     spring:
       cloud:
         azure:
           eventhub:
             connection-string: [eventhub-namespace-connection-string]
             checkpoint-storage-account: [name-of-storage-account]
             checkpoint-access-key: [checkpoint-access-key]
             checkpoint-container: [container-in-storage-account]
    ```

> Alternative (better) to adding the "azure" profile to application.yml is to pass the profile when running command line (e.g. **mvn spring-boot:run -Dspring.profiles.active=azure**)

If you followed most of the tutorial from Azure (link above) or just know Azure, you should be able to fill these 
out fairly easily, if not RTFM.

The one hard thing, if you haven't done it before, is getting your connection-string, but again Microsoft has a page 
for that https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-get-connection-string, everything else 
is pretty obvious.

NOTE: When updating the yaml file, please make sure to update "vendorChange" to whatever you named the Kinesis stream.

#### Helpful Links

* https://docs.microsoft.com/en-us/java/api/overview/azure/spring-cloud-stream-binder-eventhubs-readme?view=azure-java-stable