package git7s.flashcardai;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.*;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class LLMGenerator {

    private HttpClient client = HttpClient.newHttpClient();
    private CompletableFuture<HttpResponse<String>> futureResponse;
    private AtomicBoolean responded = new AtomicBoolean(false);

    public void fetchFlashCards(String data) {
        String url = "http://localhost:11434/api/generate";
        String payload = generatePayload(data, 10);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json") // Specify content type
                .POST(HttpRequest.BodyPublishers.ofString(payload)) // Set method
                .build();

        try {
            futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        responded.set(true); // Mark as completed
                        return response;
                    }).exceptionally(e -> {
                        e.printStackTrace();
                        responded.set(false); // Handle failure
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private String generatePayload(String data, int quantity) {

        String prompt = "Do not send any text other than the data requested, using the requested format Create a set of "+quantity+" questions and answers for flashcards" +
                "They must be questions and answers or name and definition. feel free to use your own knowledge to expand the set. Illegal characters in questions and answers are (, ), : as " +
                "these are used by the template below and the pattern matching algorithm. ONLY 1 ANSWER/DEFINITION PER QUESTION/NAME "+
                "using the following data format to be automatically read: " +
                "Format: (front:back)(front:back) Example: (Java:A programming language)(Object Oriented Programming:Programming based " +
                "on object design principles) " +
                "Notes: " + data + " - FINISH";

        // Use Gson to format the JSON properly
        Gson gson = new Gson();
        Map<String, Object> jsonMap = Map.of(
                "model", "gemma3",
                "prompt", prompt,
                "stream", false
        );
        return gson.toJson(jsonMap);
    }

    public boolean checkResponse(){
        return responded.get() && futureResponse != null;
    }

    public HttpResponse<String> getResponse(){
        return futureResponse.join();
    }
}
