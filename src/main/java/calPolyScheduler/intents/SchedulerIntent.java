package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by brandon on 11/1/16.
 */
public abstract class SchedulerIntent {
    protected static Logger log = LoggerFactory.getLogger(SchedulerIntent.class);
    protected Intent intent;
    protected Session session;
    protected Map<String, Slot> slots;

    public SchedulerIntent(Intent intent, Session session) {
        this.intent = intent;
        this.session = session;
        this.slots = new HashMap<>();
        Map<String, Slot> slots = intent.getSlots();
        StringBuilder slotStr = new StringBuilder("[");
        Slot curSlot;
        for (String key : slots.keySet()) {
            curSlot = slots.get(key);
            if (curSlot.getValue() != null) {
                this.slots.put(key, curSlot);
                slotStr.append(String.format("%s: %s", key, curSlot.getValue()));
            }
        }
        slotStr.append("]");
        log.debug("{} slots={}", this.intent.getName(), slotStr.toString());
    }

    public abstract SpeechletResponse createResponse();

//    public abstract void sendQuery();
}
