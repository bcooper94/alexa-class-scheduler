package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Created by brandon on 11/1/16.
 */
public abstract class SchedulerIntent {
    protected Intent intent;
    protected Session session;

    public SchedulerIntent(Intent intent, Session session) {
        this.intent = intent;
        this.session = session;
    }

    public abstract SpeechletResponse createResponse();
}
