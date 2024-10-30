package nl._42.springai.hackathon.chatbot;

public interface SpringAIChatBot<T> {

    T chat(String message);

}
