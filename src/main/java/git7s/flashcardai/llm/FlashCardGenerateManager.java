package git7s.flashcardai.llm;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import git7s.flashcardai.Main;
import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.model.Card;
import git7s.flashcardai.model.CardManager;

/**
 * This class converts the API response, formats into flashcard
 */
public class FlashCardGenerateManager {
    /**
     * The Flashcards Hashmap references the processed front and back Strings
     */
    private HashMap<String, String> NewFlashcards = new HashMap<String, String>();
    /**
     * String that holds the response from the LLM
     */
    private String response;
    /**
     * Card Manager to inject into the DB
     */
    private CardManager cardManager;
    /**
     * The FlashCardDraft constructor takes the API response and generates formatted cards.
     * @param response The response from the API REST POST call
     */
    public FlashCardGenerateManager(String response) {
        cardManager = new CardManager(new CardDAO());
        this.response = response;
        generateFlashcards();
    }
    /**
     * This method generates the flashcards from the response.
     */
    public void generateFlashcards() {
        if (response != null) {
            Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
            Matcher matcher = pattern.matcher(response);
            while (matcher.find()) {
                String flashcardString = matcher.group(1).trim();  // e.g., "front:back"
                String[] fb = flashcardString.split(":", 2); // Limit split to 2 parts
                if (fb.length == 2) {
                    String front = fb[0].trim();
                    String back = fb[1].trim();
                    NewFlashcards.put(front, back);
                }
            }
        }else {
            System.out.println("Sent response before ready");
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
        for (String str : NewFlashcards.keySet()){
            Card card = new Card(Main.loggedInUserID, subject, topic, str, NewFlashcards.get(str));
            cardManager.addCard(card);
            created++;
            if (created >= quantity) {
                return;
            }
        }
    }


}
