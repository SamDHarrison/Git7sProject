package git7s.flashcardai.llm;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
     * Define Possible Query types
     */
    public enum QueryType {
        EXPLAIN_QUERY,
        GENERATE_QUERY;
    }
    /**
     * The fetchFlashCards method is responsible for sending the POST request to the Ollama API
     * @param payload Specified JSON Payload
     */
    private void fetchQuery(String payload) {
        String url = "http://localhost:11434/api/generate";

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
     * This method gets the completed response from the API.
     * @return String, full raw response from the API
     */
    public String getResponse(){
        if (responded.get() && futureResponse != null) {
            return JsonParser.parseString(futureResponse.join().body()).getAsJsonObject().get("response").getAsString();
        }
        else {
            return null;
        }
    }
    /**
     * Begin the query process - called from controller classes
     * @param queryType The type of query (Enum)
     * @param input The input string
     * @param quantity Additional specifier for number of flashcards or paragraphs etc.
     */
    public void sendQuery(QueryType queryType, String input, int quantity) {
        String query;
        switch (queryType) {
            case EXPLAIN_QUERY: {
                query =  "Please explain this concept in 1 paragraph" + input;
                break;
            }
            case GENERATE_QUERY: {
                query = "Do not send any text other than the data requested, using the requested format Create a set of "+quantity+
                        " questions and answers for flashcards" + "They must be questions and answers or name and definition. " +
                        "Feel free to use your own knowledge to expand the set. Illegal characters in questions and answers are (, ), " +
                        ": as " + "these are used by the template below and the pattern matching algorithm. ONLY 1 ANSWER/DEFINITION PER QUESTION/NAME "+
                        "using the following data format to be automatically read: " + "Format: (front:back)(front:back) " +
                        "Example: (Java:A programming language)(Object Oriented Programming:Programming based " +
                        "on object design principles) " + "Notes: " + input + " - FINISH";
                break;
            }
            default: {
                query = "Invalid Query Type"; //Enum in LLM later add
                break;
            }
        }
        //To JSON
        Gson gson = new Gson();
        Map<String, Object> jsonMap = Map.of(
                "model", "gemma3",
                "prompt", query,
                "stream", false
        );
        String jsonQuery = gson.toJson(jsonMap);
        fetchQuery(jsonQuery);
    }
}
