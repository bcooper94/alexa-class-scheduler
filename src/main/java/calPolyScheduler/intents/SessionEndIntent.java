package calPolyScheduler.intents;

import calPolyScheduler.Schedule;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Created by brandon on 12/3/16.
 */
public class SessionEndIntent extends SchedulerIntent{
    private Schedule schedule;

    public SessionEndIntent(Intent intent, Session session, Schedule schedule) {
        super(intent, session);
        this.schedule = schedule;
    }

    @Override
    public SpeechletResponse createResponse() {
        SpeechletResponse response;
        String cardTitle = "End Session";
        setIsDone(true);

        if (schedule == null || schedule.isEmpty()) {
            response = setAnswer("All right.", cardTitle, "Ending session.");
        }
        else {
            response = setAnswer("All right. Here's your schedule. " + schedule.getList(),
                    cardTitle, schedule.getLisForList());
        }

        return response;
    }
}
