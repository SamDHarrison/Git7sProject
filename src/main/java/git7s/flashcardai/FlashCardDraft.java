package git7s.flashcardai;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FlashCardDraft {

    private HttpResponse<String> response;
    private HashMap<String, String> Flashcards = new HashMap<String, String>();

    public FlashCardDraft(HttpResponse<String> response) {
        this.response = response;
        generateFlashcards();

    }

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

    public void showFlashCards() {
        for (String str : Flashcards.keySet()){
            System.out.println("FRONT: " + str + " BACK: " + Flashcards.get(str));
        }
        addFlashCards("CAB202", "Week 5");
    }

    public void addFlashCards(String subject, String topic) {
        String firstfront = "";
        String firstback = "";
        for (String str : Flashcards.keySet()){
            Main.cardDAO.insert(new Card(Main.loggedInUser.getId(), topic, subject, str, Flashcards.get(str)));
        }




    }


}
