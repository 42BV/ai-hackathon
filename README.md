# Spring AI hackathon

This is an adaptation of the Spring AI demo for Retrieval-Augmented Generation with Large Language Models, by Josh Long:
https://github.com/spring-tips/llm-rag-with-spring-ai/

The accompanying video is hosted here: https://www.youtube.com/watch?v=p3aLjH2VPzU

This has been further expanded upon with features such as:

+ Function calling
+ Structured outputs
+ More practical examples using relational databases

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
  + Resource text files for displaying semantic searching using vector stores
  + PostgresQL with mock data for more traditional structured data
  + Pre-built vector-store databases hosted in Elasticsearch, read-only!
    + This is done since embedding data is expensive, doing so 20x for the same mock-data would be a waste.
+ Docker-Compose YAML to run a local Elasticsearch vector store instance and postgres database

### Connecting to database

### Connecting to Elasticsearch
- 

- `http://localhost:9200` //todo replace with real url!!!