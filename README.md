# Spring AI hackathon

This is an adaptation of the Spring AI demo for Retrieval-Augmented Generation with Large Language Models, by Josh Long:
https://github.com/spring-tips/llm-rag-with-spring-ai/

The accompanying video is hosted here: https://www.youtube.com/watch?v=p3aLjH2VPzU

This has been further expanded upon with features such as:

+ Function calling
+ Structured outputs
+ More practical examples using relational database

### General set-up

#### Quick-start

+ Modify 42 settings.xml to be able to pull Spring snapshot repositories -> `<mirrorOf>*,!spring-milestones,!spring-snapshots</mirrorOf>`
    + `settings.xml` is usually defined in the current logged in users `.m2` folder
+ Input OpenAI api key in `application-local.yml` property: `spring.ai.openai.api-key`
    + Important: This file is excluded in git. Only put the secret here! NOT in `application.yml`
    + You need to create this `application-local.yml` yourself!
+ In root folder run: `docker-compose up -d`
    + Kill it using `docker-compose down` in root folder.
+ Run application using `mvn clean spring-boot:run -Dspring-boot.run.profiles=local`
+ Set the `app.active-chatbot` property to the implementation that you wish to use! See the `chatbot` package for all chatbot implementations
    + You set this property in the `application.yml`
+ After running, start prompting directly in the console!

#### Structure

This application consists of:

+ SpringBoot application with Spring AI (OpenAI) & Elasticsearch dependencies
  + Different chatbot functions which each displays a feature that is offered by SpringAI (and OpenAI)
  + Spring AI is the 'glue', whilst OpenAI offers most of the core features
+ Different data sources:
  + PostgresQL with mock data for more traditional structured data
  + Pre-built vector-store databases hosted in Elasticsearch, read-only!
    + This is done since embedding data is expensive, doing so 20x for the same mock-data would be a waste.
  + Resource text files for creating your own embeddings in a local elasticsearch cluster
+ Docker-Compose YAML to run a local Elasticsearch vector store instance and postgres database

### Elasticsearch

Elasticsearch is the place where our vector stores are. For this hackathon, these vector stores were built ahead of time.

This is done since creating embeddings can be time-consuming and expensive depending on the amount of data.

The provided API keys are read-only, to prevent data modification.

If you wish to edit data, or to use this example more after the hackathon, you can use the local elasticsearch docker container.

#### Own-cluster

If you wish, you can use the local docker container to set up your own local Elasticsearch cluster.

You will have to:

- Change connecting uris in the `application.yml` (local connection is commented out)
- Create the embeddings yourself! See an examples in `SpringAIConfiguration.java` and `FileVectorStoreDataLoader.java` how to do so.

### Database (PostgresQL)

Everyone is hosting their own PostgresQL service locally. The database automatically loads in a database dump containing the relevant data.
This data (if applicable) is already embedded(or indexed) in the shared Elasticsearch cluster.

This allows us to modify the data locally without changing the data for others. This is especially useful when using things like function calling

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

### Connecting to PostgresQL through separate client

We recommend using the database functionality inside Intellij IDEA. If this is not an option, pgAdmin is a good alternative.

https://www.pgadmin.org/download/

You can find the connection details in the `docker-compose.yml`
