## Azure EventHubs

The binder implementation with EventHubs is fairly straight forward to use, but like GCP, the Azure folks
don't really have an easy way to run this locally.

NOTE: Before you do anything [remove Kafka implementation](REMOVE_KAFKA.md)

### Configuration
Once we've removed the Kafka implementation, we move to adding the Azure configuration.  This requires three things...

First, we go and [create the necessary resources in Azure](https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-cloud-stream-binder-java-app-azure-event-hub)

Next up, we need to add the following to our [POM](/pom.xml)...

```xml
<dependency>
    <groupId>com.azure.spring</groupId>
    <artifactId>azure-spring-cloud-stream-binder-eventhubs</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
<groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.1</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>2.3.1</version>
    <scope>runtime</scope>
</dependency>
```

> IMPORTANT NOTE: You will need to either remove the "maven enforcer" plugin or fix the depedency collisions when adding these libs 

This is followed by some edits to our application configuration.  First things first, you need to add in the  
Azure configuration information...

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

If you followed most of the tutorial from Azure (link above) or just know Azure, you should be able to fill these 
out fairly easily, if not RTFM.

The one hard thing, if you haven't done it before, is getting your connection-string, but again Micrsoft has a page 
for that https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-get-connection-string, everything else 
is pretty obvious.

> IMPORTANT NOTE: We already have spring.cloud configuration, so don't past the whole section in, just find the 
> cloud configuration section that already exists in the application.yml and put the event hub info in

Now, we finish up with the binder configuration.  As of now, you should of removed the kafka configuration and have a section that looks 
somewhat like...

```yaml
spring:
  cloud:
    stream:
      bindings:
        auditor-in-0:
          group: ${spring.application.name}
          destination: "vendorChange"
        producer-out-0:
          destination: "vendorChange"
    function:
      definition: producer
```

First things first, change your destination (if you didn't name it **vendorChange**) under `producer-out-0`.

Wait, guess that's it.  All of the additional configuration is for consumers (which this app isn't using right now),  
at least from a durable queue.


#### Helpful Links

* https://docs.microsoft.com/en-us/java/api/overview/azure/spring-cloud-stream-binder-eventhubs-readme?view=azure-java-stable