import git7s.flashcardai.Card;
import git7s.flashcardai.Main;
import git7s.flashcardai.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DashboardTest {

    private double correctDouble1;
    private double incorrectDouble2;
    private double incorrectDouble3;

    private int correctCount1;
    private int correctCount2;
    private int correctCount3;

    private int incorrectCount1;
    private int incorrectCount2;
    private int incorrectCount3;

    @BeforeEach
    public void setUp() {
        correctDouble1 = 0.5;
        incorrectDouble2 = -0.5;
        incorrectDouble3 = 30.5;

        correctCount1 = 5;
        correctCount2 = 99;
        correctCount3 = 150;

        incorrectCount1 = -5;
        incorrectCount2 = -10;
        incorrectCount3 = -11;

    }
    @Test
    public void createAccountTest() {
        assertEquals(true, checkStudyData(correctCount1, correctCount2, correctDouble1));
        assertEquals(false, checkStudyData(correctCount1, incorrectCount1, correctDouble1));
        assertEquals(false, checkStudyData(incorrectCount1, correctCount2, correctDouble1));
        assertEquals(false, checkStudyData(correctCount3, correctCount1, incorrectDouble3));
        assertEquals(false, checkStudyData(correctCount3, correctCount3, incorrectDouble3));
    }
    //Return false if studydata is not correctly formatted
    private boolean checkStudyData(int c, int i, double p){
        int correct = c;
        int incorrect = i;
        double percent = p;

        //Bounds never can be < 0
        if(correct < 0) {
            return false;
        }

        if(incorrect < 0) {
            return false;
        }

        if (percent < 0 || percent > 1) {
            return false;
        }

        return true;
    }
}