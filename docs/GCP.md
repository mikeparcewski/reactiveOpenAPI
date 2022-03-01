## Google Cloud Pub/Sub

Running this app using Pub/Sub is pretty straight forward when running in the cloud, but getting it running 
locally can be a bit tricky since they don't provide an in-memory implementation.  

NOTE: Before you do anything [remove Kafka implementation](KAFKA.md)

### Configuration

Once we've removed the Kafka implementation, we move to adding the GCP configuration.  This requires two things...

* First, we add the GCP configuration into our [src/test/resources/application.yml](src/test/resources/application.yml)
   ```yaml
   spring:
      cloud:
       ...
       gcp:
         project-id: testing
         pubsub:
           credentials:
             location: classpath:/gcp-creds.json
   ```
* Next, you'll need to create your GCP service account credentials - https://cloud.google.com/pubsub/docs/authentication
* Once you have the credentials, you'll want to add them to your [src/main/resources](src/main/resources) folder. You can either change the name of the file to `gcp-creds.json` or update the `location` value you just added to application.yml.

### Running Locally
For this you need to set up the emulator and make a small change to your [src/main/resources/application.yml](src/main/resources/application.yml), 
let's start with the configuration change...

```yaml
spring:
   cloud:
    ...
    gcp:
      emulatorHost: localhost:8085
```
For this, you'll just need to add the `emulatorHost` line on the same level as the `project-id`.  Next you'll need 
to run the emulator.  Here's the quick and dirty...

1. Install `gcloud` - https://cloud.google.com/sdk/docs/quickstart
2. Install the emulator - `gcloud components install pubsub-emulator`
3. Get latest updates - `gcloud components update`
4. Start the server - `gcloud beta emulators pubsub start --project=testing`
   **NOTE:** the project name can be anything, it's fake
5. Next up, pick your poison and set up your environment variables - https://cloud.google.com/pubsub/docs/emulator#env

You're ready to party, just run.

> NOTE: You still need the GCP credentials even with the emulator.  But for the emulator, it can all be fake.

### Running In the Cloud

Really easy...

1. Create a project (if you don't already have) and a topic - https://cloud.google.com/pubsub/docs/quickstart-console - but don't do anything else on the page.
2. Update the application.yml file...
    * project-id - the project you created (or a re-reusing)
    * destination - under both "consumer" and "producer" with the name of the topic you created

See, easy.

> NOTE: If you're new to this stuff, it's worth watching the opening tutorial