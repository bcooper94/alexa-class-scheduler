package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Created by brandon on 11/5/16.
 */
public class CourseTimeIntent extends SchedulerIntent {
    public CourseTimeIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        String department = 
    }
}
