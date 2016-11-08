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

public class GEIntent extends SchedulerIntent {
    public GEIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        log.info("GE Intent");
        String responseText = determineUtterance();

        SimpleCard card = new SimpleCard();
        card.setTitle("GE");
        card.setContent(responseText);

        PlainTextOutputSpeech response = new PlainTextOutputSpeech();
        response.setText(responseText);

        return SpeechletResponse.newTellResponse(response, card);
    }

    private String determineUtterance() {
        String val = "";

        if (slots.get("CourseRequirement") != null) {
            val = "You asked for all the courses in a specific GE";
        }

        return val;
    }
}
