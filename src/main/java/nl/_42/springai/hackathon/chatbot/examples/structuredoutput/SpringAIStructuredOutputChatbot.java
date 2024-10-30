package nl._42.springai.hackathon.chatbot.examples.structuredoutput;

import java.util.List;

import nl._42.springai.hackathon.chatbot.DefaultClientBuilder;
import nl._42.springai.hackathon.chatbot.SpringAIChatBot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.ResponseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * This chatbot responds according to a JSON schema. It will ALWAYS adhere to the schema.
 * {@link '<a href="https://spring.io/blog/2024/08/09/spring-ai-embraces-openais-structured-outputs-enhancing-json-response">...</a>'}
 */
@Component
@ConditionalOnProperty(value = "app.active-chatbot", havingValue = "structured-output")
public class SpringAIStructuredOutputChatbot implements SpringAIChatBot<StructuredOutputExample> {

    public static final String SYSTEM_PROMPT = """
                You are recipe generator and provide step-by-step guides how to create any recipe.
                Your recipes are always heavily based on chocolate, no matter if it is appropriate or not.
            """;

    /**
     * Use ChatGPT to help generate this schema based on your Java classes
     * NOTE: You must adhere to the OpenAI subset of the JSON Schema language format.
     * <a href="https://platform.openai.com/docs/guides/structured-outputs/supported-schemas">docs</a>
     */
    public static final String JSON_SCHEMA = """
              {
                "$schema" : "https://json-schema.org/draft/2020-12/schema",
                "type" : "object",
                "required": ["steps"],
                "properties" : {
                  "steps" : {
                    "type" : "array",
                    "items" : {
                      "type" : "object",
                      "properties" : {
                        "durationInSeconds" : {
                          "type" : "integer"
                        },
                        "reasonOfImportance" : {
                          "type" : "string"
                        },
                        "step" : {
                          "type" : "string"
                        }
                      },
                      "required": ["durationInSeconds", "reasonOfImportance", "step"],
                      "additionalProperties" : false
                    }
                  }
                },
                "additionalProperties" : false
              }
            """;

    private final ChatClient aiClient;
    private final BeanOutputConverter<StructuredOutputExample> outputConverter;
    private final OpenAiChatOptions promptOptions = OpenAiChatOptions
            .builder()
            .withResponseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, JSON_SCHEMA))
            .build();

    public SpringAIStructuredOutputChatbot(ChatClient.Builder clientBuilder) {
        this.aiClient = DefaultClientBuilder.addDefaults(clientBuilder).build();
        this.outputConverter = new BeanOutputConverter<>(StructuredOutputExample.class);

    }

    public StructuredOutputExample chat(String message) {

        Message systemMessage = new SystemPromptTemplate(SYSTEM_PROMPT).createMessage();

        UserMessage userMessage = new UserMessage(message);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), promptOptions);

        // There is also the variant using:
        // `aiClient.prompt(prompt).call().entity(StructuredOutputExample.class)`
        // But that solution just dumps everything into the prompt and 'asks the model nicely' to adhere to the schema, unsafe!
        String content = aiClient.prompt(prompt).call().content();

        //We then convert back to our class using the output converter
        return outputConverter.convert(content);
    }
}
