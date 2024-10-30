package nl._42.springai.hackathon.chatbot.examples.structuredoutput;

import java.util.List;
import java.util.stream.Collectors;

public record StructuredOutputExample(
        List<StructuredOutputExampleStep> steps
) {

    /**
     * Function so we can print this easily in logs
     */
    public String toString() {
        return steps.stream()
                .map(s -> String.join(" - ", s.step(), s.reasonOfImportance(), String.valueOf(s.durationInSeconds())))
                .collect(Collectors.joining("\n"));
    }
}

record StructuredOutputExampleStep(
        String step,
        String reasonOfImportance,
        int durationInSeconds
) {
}
