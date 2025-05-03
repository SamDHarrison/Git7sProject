package git7s.flashcardai;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            String[] card = responseParam.split("\\(");
            for (String str : card){
                String[] fb = str.split(":");
                Flashcards.put(fb[0], fb[1]);
            }
        }
    }

    public void showFlashCards() {
        for (String str : Flashcards.keySet()){
            System.out.println("FRONT: " + str + " BACK: " + Flashcards.get(str));
        }
    }



}
