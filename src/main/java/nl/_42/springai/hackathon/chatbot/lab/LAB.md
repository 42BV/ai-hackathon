# LAB

This is an assignment for the hackathon. You can use this to get familiar with vector stores and function calling.

## Case

We want a service desk system that is able to automatically answer questions
based on previous answers using an LLM.
These questions should be answered based on previously completed tickets.
If a question cannot be answered, a ticket should be created.
New comments can also be added to the ticket.
Once a ticket is completed, it should be stored in the vector store so that we can use it to answer new questions.

## Breakdown

Let's break this case into a few requirements:

- When asking a question, the LLM will get prompt stuffed with answers from previously completed tickets
  - This makes use of a vector store with semantic searching in order to get relevant tickets
- When a question is not answered, a new ticket is created
  - This makes use of function calling in order to make a callback to application code
  - The callback will create a new database entity
- New comments can be added to tickets based on responses sent to the LLM
  - This again makes use of function calling, but instead of creating a new entity, it will update an existing one
- A ticket can be marked as 'completed'. As soon as this happens, the ticket should be sent to the vector store so
  the knowledge can be used to answer other tickets.

## Assignment

### What has been done

The following has already been implemented:

- Database domain, entities, repository and test data, see [ticket](..%2F..%2Fdomain%2Fticket) folder.
- Basic configuration of the chatbot, with a function callback to make new tickets:
  - [SpringAIServiceDeskChatbot.java](SpringAIServiceDeskChatbot.java)
  - [SpringAITicketConfiguration.java](SpringAITicketConfiguration.java)
  - See [lab]() folder
- All initial testdata tickets have already been loaded into the vector store.
- Code to store completed tickets into the vector store
  using [TicketService.java](..%2F..%2Fdomain%2Fticket%2FTicketService.java)

### What needs to be done

The following has NOT been implemented yet:

- Retrieving tickets from the vector store and prompt stuffing these so AI can use it as context (using vector store)
- Adding new comments onto existing tickets based on comments made to the LLM (using function calling)
- Adding the ticket to the vector store as soon as it is marked as 'completed' by the user (using function calling)
  - Note that others will also be able to use your tickets at this point, since we all share the same vector store
- Additional fine-tuning of prompt and existing functions descriptions in order to make the AI understand our use-case better
- Fine-tuning to ensure privacy of other users is met, and not 'too much' data is being exposed

## Getting started

- Ensure you set the `app.active-chatbot` property to `service-desk` in the [application.yml](..%2F..%2F..%2F..%2F..%2F..%2F..%2Fresources%2Fapplication.yml)
- Ensure you set the `spring.ai.elasticsearch.vector-store.index-name` to `ai-hackathon-ticket` in
  the [application.yml](..%2F..%2F..%2F..%2F..%2F..%2F..%2Fresources%2Fapplication.yml)
- See the [README.md](..%2F..%2F..%2F..%2F..%2F..%2F..%2F..%2F..%2FREADME.md) for further instructions how to run the database and start the application.
- If you are stuck, look at the [examples](..%2Fexamples). It showcases all things asked!
- Some code has been made for you already! See the [TicketService.java](..%2F..%2Fdomain%2Fticket%2FTicketService.java)
