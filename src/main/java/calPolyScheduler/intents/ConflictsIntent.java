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

public class ConflictsIntent extends SchedulerIntent {
    public ConflictsIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        log.info("Conflict Intent");
        String responseText = determineUtterance();

        SimpleCard card = new SimpleCard();
        card.setTitle("Conflicts");
        card.setContent(responseText);

        PlainTextOutputSpeech response = new PlainTextOutputSpeech();
        response.setText(responseText);

        return SpeechletResponse.newTellResponse(response, card);
    }

    private String determineUtterance() {
        String val = "";

        if (slots.get("DepartmentOne") != null && slots.get("CourseNumOne") != null &&
            slots.get("SectionOne") != null && slots.get("DepartmentTwo") != null &&
            slots.get("CourseNumTwo") != null && slots.get("SectionTwo") != null) {
            val = "You asked if two specific courses sections conflicted";
        }
        else if (slots.get("DepartmentOne") != null && slots.get("CourseNumOne") != null &&
                 slots.get("DepartmentTwo") != null && slots.get("CourseNumTwo") != null &&
                 slots.get("SectionTwo") != null) {
            val = "You asked if a course conflicted with a specific section";
        }
        else if (slots.get("DepartmentOne") != null && slots.get("CourseNumOne") != null &&
                slots.get("DepartmentTwo") != null && slots.get("CourseNumTwo") != null) {
            val = "You asked if two courses conflicted";
        }
        else if (slots.get("DepartmentOne") != null && slots.get("CourseNumOne") != null &&
                slots.get("SectionOne") != null) {
            val = "You asked for a list of conflicts with a specific section";
        }

        return val;
    }
}
