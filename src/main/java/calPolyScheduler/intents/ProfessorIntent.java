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

public class ProfessorIntent extends SchedulerIntent {
    public ProfessorIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        log.info("Professor Intent");
        String responseText = determineUtterance();

        SimpleCard card = new SimpleCard();
        card.setTitle("Professor");
        card.setContent(responseText);

        PlainTextOutputSpeech response = new PlainTextOutputSpeech();
        response.setText(responseText);

        return SpeechletResponse.newTellResponse(response, card);
    }

    private String determineUtterance() {
        String val = "";

        if (slots.get("Professor") != null) {
            val = "You asked to list all the courses taught be a professor";
        }
        else if (slots.get("Department") != null && slots.get("CourseNum") != null) {
            val = "You asked for a list of professors who teach a course";
        }

        return val;
    }
}
