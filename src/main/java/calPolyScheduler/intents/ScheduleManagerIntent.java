package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

public class ScheduleManagerIntent extends SchedulerIntent {
    public ScheduleManagerIntent(Intent intent, Session session) {
        super(intent, session);
    }

    public SpeechletResponse createResponse() {
        return null;
    }
}
