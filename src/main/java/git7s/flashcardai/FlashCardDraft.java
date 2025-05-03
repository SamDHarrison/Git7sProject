package git7s.flashcardai;
import com.google.gson.Gson;

import java.net.http.HttpResponse;
import java.util.ArrayList;

public class FlashCardDraft {

    private String response;
    private ArrayList<ArrayList<String>> flashcards = new ArrayList<ArrayList<String>>();

    public FlashCardDraft(HttpResponse<String> input) {

        System.out.println(input.body());
        response = input.body();
    }


    public void setResponse(String response) {
        this.response = response;
    }


        public static FlashCardDraft fromJson(String json) {
            Gson gson = new Gson();
            return gson.fromJson(json, FlashCardDraft.class);
        }

}
