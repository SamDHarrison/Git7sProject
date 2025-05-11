package git7s.flashcardai;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.*;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class handles the calling to the Ollama API and response.
 */
public class LLMGenerator {
    /**
     * This object is responsible for interacting with the API
     */
    private HttpClient client = HttpClient.newHttpClient();
    /**
     * This object is responsible for storing the API response - Completeable Future type enables an asynchronous response
     */
    private CompletableFuture<HttpResponse<String>> futureResponse;
    /**
     * This Atomic Boolean is used to track if the response from the API is complete.
     */
    private AtomicBoolean responded = new AtomicBoolean(false);

    /**
     * The fetchFlashCards method is responsible for sending the POST request to the Ollama API
     * @param data The user's input
     * @param count The amount of cards requested
     */
    public void fetchFlashCards(String data, Integer count) {
        String url = "http://localhost:11434/api/generate";
        String payload = generatePayload(data, count);

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

    /**
     * The fetchResponse method is responsible for sending the POST request to the Ollama API
     * @param query The user's input
     */
    public void fetchResponse(String query) {
        String url = "http://localhost:11434/api/generate";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json") // Specify content type
                .POST(HttpRequest.BodyPublishers.ofString(generateQueryPayload(query))) // Set method
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


    /**
     * Generate payload is responsible for taking the user's input and formatting into a LLM query.
     * @param data User's input
     * @param quantity Specified flashcard quantity to be generated
     * @return String the payload to be sent
     */
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

    /**
     * Checks if the API has responded.
     * @return True if has responded, false if needs to keep waiting
     */
    public boolean checkResponse(){
        return responded.get() && futureResponse != null;
    }

    /**
     * This variable gets the completed response from the API.
     * @return String, full raw response from the API
     */
    public HttpResponse<String> getResponse(){
        return futureResponse.join();
    }

    /**
     * The payload generation for a regular query
     * @param prompt The prompt for the AI LLM
     * @return String response
     */
    private String generateQueryPayload(String prompt) {

        // Use Gson to format the JSON properly
        Gson gson = new Gson();
        Map<String, Object> jsonMap = Map.of(
                "model", "gemma3",
                "prompt", prompt,
                "stream", false
        );
        return gson.toJson(jsonMap);
    }
}
