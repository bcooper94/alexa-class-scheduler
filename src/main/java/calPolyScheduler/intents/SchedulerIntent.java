package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
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
    protected Map<String, String> slots;

    private static final int JANUARY = 1;
    private static final int MARCH = 3;
    private static final int JUNE = 6;
    private static final int SEPTEMBER = 9;
    private static final int DECEMBER = 12;

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
                this.slots.put(key, curSlot.getValue());
                slotStr.append(String.format("%s: %s ", key, curSlot.getValue()));
            }
        }
        String slotsLog = slotStr.toString().trim() + "]";
        log.debug("{} slots={}", this.intent.getName(), slotsLog);
    }

    public abstract SpeechletResponse createResponse();

    protected String getCurrentQuarter() {
        String month;
        int monthCode = Calendar.getInstance().get(Calendar.MONTH);

        if (monthCode >= SEPTEMBER && monthCode <= DECEMBER) {
            month = "Fall";
        }
        else if (monthCode >= JANUARY && monthCode <= MARCH) {
            month = "Winter";
        }
        else if (monthCode > MARCH && monthCode <= JUNE) {
            month = "Spring";
        }
        else {
            month = "Summer";
        }

        return month;
    }

    protected String getCurrentYear() {
        return "" + Calendar.getInstance().get(Calendar.YEAR);
    }

//    public abstract void sendQuery();
}
