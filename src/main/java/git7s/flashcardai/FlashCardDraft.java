package git7s.flashcardai;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This class converts the API response, formats into flashcard
 */
public class FlashCardDraft {
    /**
     * The response from the API REST POST call
     */
    private HttpResponse<String> response;
    /**
     * The Flashcards Hashmap references the processed front and back Strings
     */
    private HashMap<String, String> Flashcards = new HashMap<String, String>();

    /**
     * The FlashCardDraft constructor takes the API response and generates formatted cards.
     * @param response The response from the API REST POST call
     */
    public FlashCardDraft(HttpResponse<String> response) {
        this.response = response;
        generateFlashcards();

    }

    /**
     * This method generates the flashcards from the response.
     */
    public void generateFlashcards() {
        String jsonBody = response.body();
        JsonObject jsonObject = JsonParser.parseString(jsonBody).getAsJsonObject();
        String responseParam = jsonObject.get("response").getAsString();

        if (responseParam != null) {
            System.out.println(responseParam);
            Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
            Matcher matcher = pattern.matcher(responseParam);
            while (matcher.find()) {
                String flashcardString = matcher.group(1).trim();  // e.g., "front:back"
                String[] fb = flashcardString.split(":", 2); // Limit split to 2 parts
                if (fb.length == 2) {
                    String front = fb[0].trim();
                    String back = fb[1].trim();
                    Flashcards.put(front, back);
                }
            }
        }

    }

    /**
     * This method takes the generated flashcards and adds them to the db
     * @param subject The specified subject
     * @param topic The specified topic
     * @param quantity The specified quantity
     */
    public void addFlashCards(String subject, String topic, int quantity) {

        int created = 0;
        for (String str : Flashcards.keySet()){
            Main.cardDAO.insert(new Card(Main.loggedInUser.getId(), topic, subject, str, Flashcards.get(str)));
            created++;
            if (created >= quantity) {
                return;
            }
        }
    }


}
