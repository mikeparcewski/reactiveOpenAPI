# Reactive Application Reference Implementation 
## OpenAPI/BDD/Spring Reactor/Mongo/Kafka

[![]([![](https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggTFJcbiAgICBBW09wZW5BUEldIC0tPnxDb250cmFjdHwgQyhSZWFjdGl2ZSBBUEkpXG4gICAgQltCRERdIC0tPiB8U21va2UgVGVzdHN8IEMoUmVhY3RpdmUgQVBJKVxuICAgIEMgLS0-IHwgQmluZGVycyAmIEludGVjZXB0b3JzIHwgRHtTcHJpbmcgQ2xvdWQgU3RyZWFtcyB9XG4gICAgRCA8LS0-fFBlcnNpc3R8IEVbTW9uZ29EQl1cbiAgICBEIDwtLT58TWVzc2FnZXwgRltLYWZrYV1cbiAgICBEIDwtLT58QXVkaXR8IEdbTG9nZ2luZ10iLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOmZhbHNlfQ)](https://mermaid-js.github.io/mermaid-live-editor/#/edit/eyJjb2RlIjoiZ3JhcGggTFJcbiAgICBBW09wZW5BUEldIC0tPnxDb250cmFjdHwgQyhSZWFjdGl2ZSBBUEkpXG4gICAgQltCRERdIC0tPiB8U21va2UgVGVzdHN8IEMoUmVhY3RpdmUgQVBJKVxuICAgIEMgLS0-IHwgQmluZGVycyAmIEludGVjZXB0b3JzIHwgRHtTcHJpbmcgQ2xvdWQgU3RyZWFtcyB9XG4gICAgRCA8LS0-fFBlcnNpc3R8IEVbTW9uZ29EQl1cbiAgICBEIDwtLT58TWVzc2FnZXwgRltLYWZrYV1cbiAgICBEIDwtLT58QXVkaXR8IEdbTG9nZ2luZ10iLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOmZhbHNlfQ))

A reference application that shows some real world hacks and tricks needed to make some of the cooler tech work in  
real world situations. As far as the app goes, it's a simple Reactive CRUD application developed using OpenAPI that 
demonstrates how you might use kafka, spring domain messaging, BDD and a bunch of other useful stuff.

### The Technology

To name a few...

* [Spring Reactor](https://projectreactor.io/) - The core for our reactive microservice
* [OpenAPI Tools](https://github.com/openapitools/openapi-generator) - Bringing our API-First approach to life
* [BDD For All](https://github.com/Accenture/bdd-for-all/) - Enabling our Test-First (or TDD) approach
* [MongoDB](https://www.mongodb.com/) - How we're persisting data
* [Kafka](https://kafka.apache.org/) - For durable messaging outside our application
* [Spring Domain Events](https://www.baeldung.com/spring-data-ddd) - Fun way of handling domain events internally or externally
* [Lombok](https://projectlombok.org/) - Write less code
* [Mapstruct](https://mapstruct.org/) - Fast way to map our objects
* [JUnit](https://junit.org/junit5/) - For all those other things we need to test

### Prerequisites

To compile & run, you'll need...

* JDK 11
* Maven

#### Database

By default, the application uses an in-memory version of MongoBD.  For instructions on how to change 
to use Docker or one of the cloud providers see [docs/MONGODB.md](docs/MONGODB.md)

Because we wanted to hook into change events from the repository to create and broadcast "domain" events, 
we had to hardcode in MongoDB to the app.  This is because we ran into issues with DomainEvents and some of the 
other generic ways of capturing changes.  Mostly due to bugs between Lombok, Spring Data, etc...

This means that if you want to change DB's, you'll need to change...

* [src/main/java/com/accenture/cloudnative/reference/reactoropenapi/repository/PatientRepository.java](src/main/java/com/accenture/cloudnative/reference/reactoropenapi/repository/PatientRepository.java)
* [src/main/java/com/accenture/cloudnative/reference/reactoropenapi/repository/RepositoryListener.java](src/main/java/com/accenture/cloudnative/reference/reactoropenapi/repository/RepositoryListener.java)
* and of course the configurations

There's not a lot of code and what is there is pretty generic (and many other Spring Data implementations have). 
In a perfect world, we would have 0 code changes, but the things about using the latest and greatest of each 
means interopability you might have found in older version gets tricky.

#### Messaging

For external publishing, we use Kafka.  Like our DB implementation, by default we use an in memory, but using 
the Spring Cloud Streams Binder implementation we can switch this out to a different product (e.g. Google Pub/Sub,
Azure Event Hubs, RabbitMQ, etc...).  If you want to change things up, check out [docs/MESSAGING.md](docs/MESSAGING.md)

### Running the Application

For running, just execute...

```shell
mvn clean spring-boot:run
```

If it works, you should be able to go to http://localhost:8080/docs and see the docs for the API's.  The 
"Try It Out" feature will let you experiment with the API's and you can check the log (in [logs/](logs/)) or 
STDOUT to check out the messages firing.

### How it Works

This reference uses a few different techniques to minimize code (and improve quality).


#### Shifting Left

Before a line of code is written, we've included a bunch of quality and security into the project to ensure that 
from the first lines of code written, we are keeping things clean and secure.

* [Checkstyle](https://checkstyle.sourceforge.io/google_style.html) (Google Java Style) - combines aesthetic and coding
  conventions that make code easier to read and merge across the team
* [Maven Enforcer](https://maven.apache.org/enforcer/maven-enforcer-plugin/) - keeps your dependencies clean (e.g.
  duplicates, competing versions, etc...)
* [OWASP Dependency Check](https://jeremylong.github.io/DependencyCheck/dependency-check-maven/index.html) - see what 
  vulnerabilities the libraries you're using have
* [Spotbugs](https://spotbugs.github.io/) - Open source static analysis for Java code combined
  with [Find Security Bugs](https://find-sec-bugs.github.io/) for security audits
  and [fb-contrib](http://fb-contrib.sourceforge.net/) for some auxiliary audits.
* [PMD/CPD](https://pmd.github.io/) - Another static analyzer focused on things like unused variables/imports, 
  empty catch blocks and other bad (or hacky) practices.
* [JaCoCo](https://www.eclemma.org/jacoco/) - Code coverage that looks at our BDD & JUnit reports to make sure we're
  testing our code. Alternative here is [OpenClover](http://openclover.org/), but JaCoCo seems to be the goto in a lot
  of orgs these days (been around longer)

These are all configured in [pom.xml](pom.xml), and although some of these are run during the test phase, you can get 
detailed reporting by...

```shell
mvn clean verify
```

will tell you if there are any errors and...

```shell
mvn clean site
```

Will provide a full report, you can see it by going to `target/site/index.html` in your browser and 
checkout the what's under the "Project Reports" section.

##### Checkstyle

It's recommended you configure you editor to use checkstyle, for rules you don't agree with you can just update 
your [checkstyle-suppressions.xml](checkstyle-suppressions.xml).  Recommendation is to keep this to a minimum, 
though, since the editor plugin will automatically correct most issues.

https://checkstyle.sourceforge.io/google_style.html

##### Maven Enforcer 

This is one of the reasons our *dependencyManagement* in the pom is so big and also why we have exclusions with 
the Spring Webflux depdency.  This doesn't just check our primary dependencies for [collision](https://www.baeldung.com/maven-version-collision), 
it checks their dependencies as well. The potential issues can be negligible from this, but do you really 
want to find out the hard way?

This can lead to a lot of cruft in the pom file, though, and can make upgrading versions a bit messy, so you need to 
be really thoughtful here and decide what works best for your team.

https://maven.apache.org/enforcer/maven-enforcer-plugin/

##### OWASP Dependency Check

Why wait till your Veracode or Blackduck scan is complete to find out you need to change or upgrade a dependency?

To fix the issues, you have two options (well three)...

* Upgrade the lib to a secure version, by adding it to the *dependencyManagement* in the [pom.xml](pom.xml)
* Add it to the excludes (just ignores the problem) [spotbugs-exclude.xml](spotbugs-exclude.xml)
* Find a different (secure) library - not always easy/reccomended

https://jeremylong.github.io/DependencyCheck/dependency-check-maven/index.html

##### Spotbugs & PMD/CPD

Why waste time on catching things in your manual code reviews, these, combined with some other plugins will pretty 
much guarantee a pass from analyzers like Sonarqube and in combination with the other plugins will have you 
some pretty clean code in your pull requests.

> For those that you just don't want to deal with, [spotbugs-exclude.xml](spotbugs-exclude.xml) is there to override.

https://spotbugs.github.io/ & https://pmd.github.io/

##### JaCoCo

Wonder how much of your code is being tested?  If you're doing BDD/TDD right this should be a no brainer.  
I'm a strong believer in functional testing over unit and if done right, your code coverage should be right, 
since functional tests should flex the muscle of most of your code.  If it's not, two things...

* You've written code you didn't need to
* Your missing some test cases

To see the report, you have two options...

1. `mvn site` will give you the full set of reports, you just need to open up [target/site/project-reports.html](target/site/project-reports.html)
2. `mvn test jacoco:report` will generate the stand alone report in [target/site/jacoco/index.html](target/site/jacoco/index.html)

#### API/Event-First

This project utilizes the OpenAPITools generator to create the base of the application.  Based on the OpenAPI 3.0 spec 
located in [src/main/resources/openapi.yaml](src/main/resources/openapi.yaml) and a simple configuration in 
the [pom.xml](pom.xml) we create...

* All of the DTO models
* The controller, API and delegate classes
* Documentation endpoint

By running

```shell
mvn compile
```

You can see the code generated in **target/generated-sources/**.  This code will handle most of the plumbing, so you can focus on 
the implementation.

In this case, all you need to do is create a delegate implementation (see *com.accenture.cloudnative.reference.reactoropenapi.api.PatientApiDelegate* 
in the generated sources directory).  The reference implementation can be found at 
[src/main/java/com/accenture/cloudnative/reference/reactoropenapi/api/PatientAPIDelegateImpl.java](src/main/java/com/accenture/cloudnative/reference/reactoropenapi/api/PatientDelegateImpl.java).

For this project we chose to generate a Spring Boot Reactive project, but there a ton of options.  Check out more at...

* Main plugin - https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-maven-plugin
* Spring specific configuration options - https://openapi-generator.tech/docs/generators/spring/

> You can switch back and forth from a reactive project to standard blocking API's by changing **&lt;reactive&gt;true&lt;/reactive&gt;** to false.
> You will need to change the Delegate implementation when swapping back/forth, though.

#### Test-first

For our tests we use a BDD style (Gherkin) approach with [BDD For All](https://github.com/Accenture/bdd-for-all).  
Focused on some positive and negative smoke tests, we make sure that our application is functioning as expected 
before we write a line of code.  This is done quite simply by...

1. Creating a test runner [src/test/java/RunCucumberTests.java](src/test/java/RunCucumberTests.java) responsible for starting up the spring app and executing the feature files
2. Then you'll need add some configuration [src/test/resources/application.yml](src/test/resources/application.yml), this will be pretty much be the same for most implementations.  It sets the server/port, but you can do a lot in this.
3. Finally, you'll need one or more feature files.  These are currently found in [src/test/resources/features](src/test/resources/features)

After running tests, you can see the full report in [target/cucumber/cucumber-html-reports/overview-features.html](target/cucumber/cucumber-html-reports/overview-features.html)

##### Creative Component Testing

In addition to executing the functional tests, we also "hook" into the eventing framework to see if we're generating 
the number of events we'd expect from all these CRUD requests.  This is done via the [src/test/java/com/accenture/cloudnative/reference/reactoropenapi/event/EventsConfig.java](src/test/java/com/accenture/cloudnative/reference/reactoropenapi/event/EventsConfig.java) 
class.  We simply collect the events (and their types) and match them against an expected output.

This allows us to write a lot less test code, but test important functionality as part of our functional tests since 
we're still trying to figure out how BDD and Event Driven Architectures can place nicely.

Recommend reviewing the user guide - https://github.com/Accenture/bdd-for-all/blob/develop/docs/USERGUIDE.md - as BDD For All can do a lot 
and to put in perspective, the better your BDD tests are, the better your code coverage will be, which means less Unit tests (Yay!).

#### Reactive Data Repositories

You can't be a true reactive (or streaming) application if you block.  One of the hardest part of designing transactional 
systems in a "reactive" environment is that you have to deal with data sources.

Datastores traditionally were designed to block (e.g. request -> wait <- respond), but between a multitude of vendors 
(including our friends @ [Mongo](https://www.mongodb.com/)) and Spring, they've made this pretty easy to do.

> Final word - Don't ever block a Mono or Flux, a lot of first timers try to work with these objects like they're 
> normal Java POJO's.  They're not.  Do some homework before attempting at home!

#### Spring Reactive Data Repositories

There's a lot of documentation on reactive repostitories, one of the simplest is from [Baeldung](https://www.baeldung.com/spring-data-mongodb-reactive) 
so I don't plan on writing about it, other than it is wicked simple (think [JPA](https://spring.io/projects/spring-data-jpa) just reactive).

I will mention here, though, that we made our code specific to Mongo with two classes

* [src/main/java/com/accenture/cloudnative/reference/reactoropenapi/repository/VendorRepository.java](src/main/java/com/accenture/cloudnative/reference/reactoropenapi/repository/VendorRepository.java) - A simple spring repo
* [src/main/java/com/accenture/cloudnative/reference/reactoropenapi/repository/RepositoryListener.java](src/main/java/com/accenture/cloudnative/reference/reactoropenapi/repository/RepositoryListener.java) - Our change event listener

Now, in a perfect world we would have used a [generic type](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/package-summary.html), which would 
allow us to switch between DB's easily with just configuration changes.  But the world isn't perfect and we needed 
to make this work.  To this end, we used the Vendor specific interfaces so we could capture the change (insert/update/delete/view) 
events.  Check out the comments in RepositoryListener for more.

#### Event Driven (Architecture)

Ok, we're getting to the end of the README and I'm getting tired of typing.  So I'm going to bullet points for this one.

* What is it? https://en.wikipedia.org/wiki/Event-driven_architecture
* Why do it? https://siliconangle.com/2018/12/27/want-software-first-agility-get-event-driven-architecture-says-accenture-reinvent/

##### Internal Events

For internal events we use Springs DomainEvents mechanism, this works almost like a local event bus and allows us 
to publish events and receive them without explicitly linking our code (e.g. method chaining).  Now these events ARE NOT 
durable and if the publisher throws an exception the subscribers will never know :( which means...

> Be smart about using this option.  If you need truly durable change events, would recommend looking at 
> the Change Feed options from your cloud providers - almost every provider has these now for every DB (Sql or not)

An example of an internal processor is the simple [src/main/java/com/accenture/cloudnative/reference/reactoropenapi/audit/Auditor.java](src/main/java/com/accenture/cloudnative/reference/reactoropenapi/audit/Auditor.java) 
which literally just logs the messages it receives, and although there are some better [auditing mechanisms](https://medium.com/swlh/data-auditing-with-spring-data-r2dbc-5d428fc94688) 
available, the idea was to just showcase one potential use case for why you might want an internal event processor.

##### External Events

Yes, we're using Kafka in this example (so much easier for build/test, not necessarily for production), but the point 
is more the implementation we're interested in here.

* The kafka producer uses [spring cloud stream binders](https://spring.io/blog/2020/07/13/introducing-java-functions-for-spring-cloud-stream-applications-part-0) implementation
* This option allows us to switch providers with just configuration changes (see [docs/MESSAGING.md] for more) 
* This flexibility does force us to write a custom implementation [src/main/java/com/accenture/cloudnative/reference/reactoropenapi/event/exception/BinderExceptionHandler.java](src/main/java/com/accenture/cloudnative/reference/reactoropenapi/event/exception/BinderExceptionHandler.java) since Spring has deprecated many of the convenience methods helpers like [EmitterProcessor](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/EmitterProcessor.html)

> NOTE: There are a bunch of binder implementations, from RabbitMQ to Azure Event Hubs.  
> Check out the [configuration page on messaging](docs/MESSAGING.md) for switching things ups.

#### Centralized Exception Handling

Now the folks at [Baeldung might disagree](https://www.baeldung.com/exception-handling-for-rest-with-spring) with our approach, 
but when building enterprise software, centralizing anything is usually super helpful.   It's for this reason 
we used the @ControllerAdvice option for error handling.

You can check out [src/main/java/com/accenture/cloudnative/reference/reactoropenapi/domain/exception/InvalidEntityHandler.java](src/main/java/com/accenture/cloudnative/reference/reactoropenapi/domain/exception/InvalidEntityHandler.java) 
for more detail, but it's really simple...

```java

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity myHandlerMethod(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Arrays.asList(errorMapper.argErrorToError(ex).code(INVALID_PAYLOAD)));
  }
  
}

```

The important parts of the code are the annotations.  First, we have @ControllerAdvice, and this just tells 
Spring that we may have some code to execute should certain situations arise (e.g. exception).

The next annotation is the @ExceptionHandler one, and for this we provide an exception (e.g. IllegalArgumentException).  
All this does is tell Spring that any time an IllegalArgumentException is thrown by any controller, catch it 
and do this.

Now the argument to use something like ResponseStatusException is that you can standardize the exception response 
and provide engineers more control over the handling itself.  I find in most cases this is theory, not the reality. 
The security of ensuring your capturing exceptions from everywhere vs leaving it up to someone who is in a rush 
to deliver a feature, seems like a bigger problem.

##  The End Goal

I don't necessarily believe that reference implementations like this work well if you're just trying to pull 
code to get something to work or fork and try to modify for your own API spec.  My overall goal is to turn this 
into a Maven Archetype before Spring deprecates the code :)

If you want to help, ping me.