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

/**
 * Created by brandon on 11/1/16.
 */
public class CourseListQueryIntent extends SchedulerIntent {
    private static Logger log = LoggerFactory.getLogger(CourseListQueryIntent.class);

    private Map<String, Slot> slots;

    public CourseListQueryIntent(Intent intent, Session session) {
        super(intent, session);
        this.slots = new HashMap<>();

        Map<String, Slot> slots = intent.getSlots();
        StringBuilder slotStr = new StringBuilder("[");
        Slot curSlot;
        for (String key : this.slots.keySet()) {
            curSlot = this.slots.get(key);
            if (curSlot.getValue() != null) {
                this.slots.put(key, curSlot);
                slotStr.append(String.format("%s: %s", key, curSlot.getValue()));
            }
        }
        slotStr.append("]");
        log.debug("{} slots={}", intent.getName(), slotStr);
    }

    @Override
    public SpeechletResponse createResponse() {
        log.info("getCourseListResponse");
        String responseText = "Testing get course query intent";

        SimpleCard card = new SimpleCard();
        card.setTitle("Course List");
        card.setContent(responseText);

        PlainTextOutputSpeech response = new PlainTextOutputSpeech();
        response.setText(responseText);

        return SpeechletResponse.newTellResponse(response, card);
    }
}
