## Running MongoDB

There are a couple ways to run with Mongo...

* **In Memory** - This is the default, you don't need to do anything to get it working
* **Atlas** - Which works across all cloud providers and has a free tier - https://docs.atlas.mongodb.com/getting-started/#deploy-a-free-tier-cluster
* **Self Managed Cloud** - Deploy as a self managed option
  * https://docs.microsoft.com/en-us/azure/cosmos-db/mongodb-introduction or https://docs.microsoft.com/en-us/hybrid/app-solutions/solution-deployment-guide-mongodb-ha
  * https://console.cloud.google.com/marketplace/product/click-to-deploy-images
  * https://docs.aws.amazon.com/quickstart/latest/mongodb/welcome.html
* [Docker](#Docker)

### Configuration
Out of the box, this app uses the in memory test database.  To disable that, just find the following section in your pom.xml file...

```xml
    <dependency>
      <groupId>de.flapdoodle.embed</groupId>
      <artifactId>de.flapdoodle.embed.mongo</artifactId>
    </dependency>
```

and change it to only be available for testing by adding `<scope>test</scope>`...

```xml
    <dependency>
      <groupId>de.flapdoodle.embed</groupId>
      <artifactId>de.flapdoodle.embed.mongo</artifactId>
      <scope>test</scope>
    </dependency>
```

> If you ever want to move back to in memory, just remove that `<scope>test</scope>` line and make sure to update
> you config back to localhost (if you changed it)

If you're using Docker (and the setup guide for it below), you're done, just start up and have some fun!

#### Cloud Variation
If you're changing to use a cloud version of the DB, in addition to adding `<scope>test</scope>` from your POM file, 
we also need to change the location of our database in [src/main/resources/application.yaml](src/main/resources/application.yaml)

Start by looking for the following secion...

```yaml
spring:
  application:
    ...
  data:
    mongodb:
      host: "localhost"
      port: "27017"
      database: "testdb"
```

Next you'll probably end up changing `localhost` to something like...

```properties
mongodb+srv://<USER>>:<PASS>>@project.named.mongodb.net/retryWrites=true&w=majority
```

You'll want to make sure you understand what the retryWrites and w params actually mean when configuring - https://docs.atlas.mongodb.com/resilient-application/

Also note, there seems to be a TLS issue that can be overcome with using `java -Djdk.tls.client.protocols=TLSv1.2`.  For more information...

* https://bugs.openjdk.java.net/browse/JDK-8236039
* https://developer.mongodb.com/community/forums/t/sslhandshakeexception-should-not-be-presented-in-certificate-request/12493

### Docker
If you don't have docker installed, you're a developer and probably should.  Otherwise, running Mongo is pretty easy.

```shell
docker pull mongo
```

Pulls the image and then use the following to start it up and expose on the port(s) this app is currently configured for.

```shell
docker run -d -p 27017-27019:27017-27019 --name mongodb mongo
```

Next we need to create the database, so let's get to a shell first...

```shell
docker exec -it mongodb bash
```

The above throws you into a shell on the running mongo instance, next we'll just fire up the mongo shell client...

```shell
mongo
```

Yes, you read that right and at this point you're in the database.  Now mongo doesn't have a "create", but it's still easy...

```shell
use testdb
```

**testdb** is the name and "use" actually creates it.  If you don't believe me, just run...

```shell
show dbs
```

Now you're ready to run this.



