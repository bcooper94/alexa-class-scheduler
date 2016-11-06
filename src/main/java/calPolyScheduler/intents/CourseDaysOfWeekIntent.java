package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CourseDaysOfWeekIntent extends SchedulerIntent {
    public CourseDaysOfWeekIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        log.info("Course Days of Week Intent");
        String responseText = determineUtterance();

        SimpleCard card = new SimpleCard();
        card.setTitle("Course Days of Week");
        card.setContent(responseText);

        PlainTextOutputSpeech response = new PlainTextOutputSpeech();
        response.setText(responseText);

        return SpeechletResponse.newTellResponse(response, card);
    }

    private String determineUtterance() {
        String val = "";

        if (slots.get("DayOne") != null && slots.get("DayTwo") != null &&
            slots.get("DayThree") != null && slots.get("DayFour") != null &&
            slots.get("Department") != null) {
            val = "You asked for what courses were offered 4 days a week";
        }
        else if (slots.get("DayOne") != null && slots.get("DayTwo") != null &&
                slots.get("DayThree") != null && slots.get("Department") != null) {
            val = "You asked for what courses were offered 3 days a week";
        }
        else if (slots.get("DayOne") != null && slots.get("DayTwo") != null &&
                slots.get("Department") != null) {
            val = "You asked for what courses were offered 2 days a week";
        }
        else if (slots.get("DayOne") != null && slots.get("Department") != null) {
            val = "You asked for what courses were offered 1 day a week";
        }
        else if (slots.get("Department") != null && slots.get("CourseNum") != null &&
                slots.get("Section") != null) {
            val = "You asked for what day a specific section was offered";
        }
        else if (slots.get("Department") != null && slots.get("CourseNum") != null) {
            val = "You asked for what days a course was offered";
        }
        return val;
    }
}
