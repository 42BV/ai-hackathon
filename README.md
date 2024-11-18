# Spring AI hackathon

## Quick-start

+ ONLY If using a global maven repository definition! Modify the settings.xml to be able to pull Spring snapshot
  repositories
  + This applies if you are using a globally inclusive `<mirrorOf>*</mirrorOf>`
  + You must do ONE of the following:
    + OPTION 1: Rename your `settings.xml` to `settings-backup.xml`
    + OPTION 2: Add the following to the `<mirrorOf>*</mirrorOf>` property in your settings.xml:
      + `<mirrorOf>*,!spring-milestones,!spring-snapshots</mirrorOf>`
  + `settings.xml` is usually defined in the current logged-in users `.m2` folder
    + MacOs: `~/.m2`
    + Windows: `C:\Users\[username]\.m2\settings.xml`
+ Fill in the required credentials to external systems:
  + Most of you will have received a complete `application-local.yml`. Just put this in the [resources](src%2Fmain%2Fresources) folder.
  + If you did not receive an application-local.yml, you will have to create it yourself. Do the following:
    + Create a `application-local.yml` file in the [resources](src%2Fmain%2Fresources) folder.
    + Input OpenAI api key in `application-local.yml` property: `spring.ai.openai.api-key`
    + Input Elasticsearch api-key in `application-local.yml` property: `app.elasticsearch.api-key`
    + Important: This file is excluded in git. Only put the secret here! NOT in `application.yml`
+ In root folder run: `docker-compose up -d`
  + You can kill it when done using `docker-compose down` in root folder.
  + Some IDE's also support going over to the [docker-compose.yml](docker-compose.yml) file and running it from there if this is easier for you!
+ Run application using `mvn clean spring-boot:run -Dspring-boot.run.profiles=local`
+ Set the `app.active-chatbot` property to the implementation that you wish to use! See the `chatbot` package for all chatbot implementations
  + You set this property in the [application.yml](src%2Fmain%2Fresources%2Fapplication.yml)
+ After running, start prompting directly in the console!

## Good to know

+ All test-data is loaded in the docker database automatically on startup using database script
+ Our OpenAPI api keys are capped to a limit, if you use too much, you (and others using the same key) will be cut off
+ All vector store data is pre-built based on this test data. You will connect to a shared (online hosted) vector store. Keep in mind that changes to data in
  your database will not reflect in the vector store since it will not automatically embed this data again
  + This makes it so all of us do not have to make the same embeddings, which can be time-consuming and costly
  + If you want to try and build your own vectors from data, refer to one of the following classes:
    + [FileVectorStoreDataLoader.java](src%2Fmain%2Fjava%2Fnl%2F_42%2Fspringai%2Fhackathon%2Fdomain%2Ffile%2FFileVectorStoreDataLoader.java)
    + [PublicationVectorStoreDataLoader.java](src%2Fmain%2Fjava%2Fnl%2F_42%2Fspringai%2Fhackathon%2Fdomain%2Fpublication%2FPublicationVectorStoreDataLoader.java)
    + [TicketVectorStoreRepository.java](src%2Fmain%2Fjava%2Fnl%2F_42%2Fspringai%2Fhackathon%2Fdomain%2Fticket%2FTicketVectorStoreRepository.java)
    + PLEASE keep in mind that large datasets will take time to embed and cost more credit.
+ All chatbots are setup in a way that the AI chatbot will keep your conversation context in mind. So you can ask follow up questions. This is setup in
  the [DefaultClientBuilder.java](src%2Fmain%2Fjava%2Fnl%2F_42%2Fspringai%2Fhackathon%2Fchatbot%2FDefaultClientBuilder.java).
  + Every restart the context resets, so if you want to start clean, just restart the application!

## Assignment

First of all, in this hackathon you are free to create and do as you please!
We have added different domain entities and test-data sets to play around with.

In case you want a more specific assignment to do during this hackathon. Please see
the [LAB.md](src%2Fmain%2Fjava%2Fnl%2F_42%2Fspringai%2Fhackathon%2Fchatbot%2Flab%2FLAB.md) and corresponding java classes.

Otherwise you can also just explore and try out the [examples](src%2Fmain%2Fjava%2Fnl%2F_42%2Fspringai%2Fhackathon%2Fchatbot%2Fexamples)!

You can also refer below to use-cases to try out:

### Use-cases to try out

Below are examples of use-cases. Some of these examples have been implemented in
the [examples](src%2Fmain%2Fjava%2Fnl%2F_42%2Fspringai%2Fhackathon%2Fchatbot%2Fexamples) already!

- Function-calls:
  - Data correction (ex. grammar and spelling) using function calling
  - Classification or summarization of data (ex. for reviews, publications) using function calling
  - Natural language querying (text-to-sql) to get aggregations etc. using function calling
- Vector stores:
  - Semantic searching and answering questions about publications using a vector store

### Data sets

The following datasets already exist:

- Publications: AI generated publication dataset. Used for vector store example
  - This dataset is loaded into your local database, and is already fully embedded in the Elasticsearch vector store
  - Database table: `publication`
  - Index: `ai-hackathon-publication`
- Users and reviews: AI generated reviews combined with user data. Used for function calling example
  - This dataset is loaded into your local database automatically. It is not embedded in Elasticsearch
  - Database tables: `app_user` and `user_review`
- Users and tickets: AI generated service desk tickets. Part of the [LAB.md](src%2Fmain%2Fjava%2Fnl%2F_42%2Fspringai%2Fhackathon%2Fchatbot%2Flab%2FLAB.md)`lab`
  assignment
  - This dataset is loaded into your local database, and is already fully embedded in the Elasticsearch vector store
  - Database tables: `app_user` and `ticket`
  - Index: `ai-hackathon-ticket`
- Bee movie: bzzzt...
  - This dataset is only loaded directly into the vector store
  - Index: `ai-hackathon-beemovie`
- Great gatsby
  - This dataset is only loaded directly into the vector store
  - Index: `ai-hackathon-great-gatsby`
- Frankenstein
  - This dataset is only loaded directly into the vector store
  - Index: `ai-hackathon-frankenstein`

You can view this test data using the 'Connecting to Elasticsearch through separate client' and 'Connecting to PostgresQL through separate client' below.

## Elasticsearch

Elasticsearch is our vector store for this hackathon. These vector stores were built ahead of time.

This is done since creating embeddings can be time-consuming and expensive depending on the amount of data.

The provided API keys are read-only, to prevent data modification.

If you wish to edit data, or to use this example more after the hackathon, you can use the local elasticsearch docker container.

### Own-cluster

If you wish, you can use the local docker container as your own Elasticsearch cluster.

You will have to:

- Modify the docker-compose.yml and put back the commented out `elasticsearch` section.
- Change connection details in the [application.yml](src%2Fmain%2Fresources%2Fapplication.yml)
- Create the embeddings yourself! See examples in `SpringAIConfiguration.java`, `PublicationVectorStoreDataLoader.java` and `FileVectorStoreDataLoader.java` how
  to do so.
- Again, keep in mind, do not start embedding **large** datasets, this takes time and costs $$$

### Connecting to Elasticsearch through separate client

- Install https://elasticvue.com/
    - This is a small but powerful Elasticsearch client
    - Can be installed as desktop client or as browser plugin
- Connect to the cluster
    - In the top bar you can `Add cluster`
    - Use `API key` authentication, see `application.yml` for URI and api-key.
- The most interesting tabs are the `indices` and `search` tabs.
    - `indices` allows you to see all the available vector stores which are already indexed on the cluster
    - `search` allows you to see the data within a specific vector store
- NOTE: Everyone is sharing this cluster during the hackathon, that is why all API keys are ready only!

## Database (PostgresQL)

Everyone is hosting their own PostgresQL service locally. The database automatically loads in a database dump containing the relevant data.
This data (if applicable) is already embedded(or indexed) in the shared Elasticsearch cluster.

This allows us to modify the data locally without changing the data for others. This is especially useful when using things like function calling

### Connecting to PostgresQL through separate client

We recommend using the database functionality inside Intellij IDEA. If this is not an option, pgAdmin is a good alternative.

https://www.pgadmin.org/download/

You can find the connection details in the `docker-compose.yml`

## Application structure

This application consists of:

+ SpringBoot application with Spring AI (OpenAI) & Elasticsearch dependencies
    + Different chatbot functions which each displays a feature that is offered by SpringAI (and OpenAI)
    + Spring AI is the 'glue', whilst OpenAI offers most of the core features
+ Different data sources and test data generation tools:
    + PostgresQL with mock data for more traditional structured data
        + User
        + Publications
        + Reviews
    + Pre-built vector-store databases hosted in Elasticsearch, read-only!
        + This is done since embedding data is expensive, doing so 20x for the same mock-data would be a waste.
    + Resource text files for creating your own embeddings in a local elasticsearch cluster
+ Docker-Compose YAML to run a local Elasticsearch vector store instance and postgres database

## Inspiration

This is an adaptation of the Spring AI demo for Retrieval-Augmented Generation with Large Language Models, by Josh Long:
https://github.com/spring-tips/llm-rag-with-spring-ai/

The accompanying video is hosted here: https://www.youtube.com/watch?v=p3aLjH2VPzU

This has been further expanded upon with features such as:

+ Function calling
+ Structured outputs
+ More practical examples using relational database