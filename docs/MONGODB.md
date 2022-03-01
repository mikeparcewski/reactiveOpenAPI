## Running MongoDB

There are a couple ways to run with Mongo...

* **Local** - You've installed MongoDB locally
* **Atlas** - Which works across all cloud providers and has a free tier - https://docs.atlas.mongodb.com/getting-started/#deploy-a-free-tier-cluster
* **Self Managed Cloud** - Deploy as a self managed option
  * https://docs.microsoft.com/en-us/azure/cosmos-db/mongodb-introduction or https://docs.microsoft.com/en-us/hybrid/app-solutions/solution-deployment-guide-mongodb-ha
  * https://console.cloud.google.com/marketplace/product/click-to-deploy-images
  * https://docs.aws.amazon.com/quickstart/latest/mongodb/welcome.html
* [Docker](#Docker)

#### Local

Really nothing to do, except maybe change the port/db name/password in the [application.yml](../src/main/resources/application.yml) 
just checkout the spring.data.mongodb section

```yaml
  data:
    mongodb:
      host: "localhost"
      port: 27017
      database: "reactiveOpenAPIMongo"
      ### if using password, uncomment and change below
      # password: mikep
```

Otherwise, this is pretty straightforward.

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
      database: "reactiveOpenAPIMongo"
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
use reactiveOpenAPIMongo
```

**reactiveOpenAPIMongo** is the name and "use" actually creates it.  If you don't believe me, just run...

```shell
show dbs
```

Now you're ready to run this.



