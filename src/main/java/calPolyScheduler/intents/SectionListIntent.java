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

public class SectionListIntent extends SchedulerIntent {
    public SectionListIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        log.info("Section List Intent");
        String responseText = determineUtterance();

        SimpleCard card = new SimpleCard();
        card.setTitle("Section List");
        card.setContent(responseText);

        PlainTextOutputSpeech response = new PlainTextOutputSpeech();
        response.setText(responseText);

        return SpeechletResponse.newTellResponse(response, card);
    }

    private String determineUtterance() {
        String val = "";

        if (slots.get("Type") != null && slots.get("Department") != null &&
                slots.get("CourseNum") != null) {
            val = "You asked for the type of class for a course";
        }

        return val;
    }
}
