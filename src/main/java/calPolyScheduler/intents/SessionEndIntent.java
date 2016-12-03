package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Created by brandon on 12/3/16.
 */
public class SessionEndIntent extends SchedulerIntent{
    public SessionEndIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        setIsDone(true);
        return this.setAnswer("All right.", "End Session", "Ending session.");
    }
}
