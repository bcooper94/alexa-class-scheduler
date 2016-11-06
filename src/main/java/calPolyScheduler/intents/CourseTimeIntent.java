package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.*;

/**
 * Created by brandon on 11/5/16.
 */
public class CourseTimeIntent extends SchedulerIntent {
    public CourseTimeIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        SpeechletResponse response;
        String department = slots.get("Department"),
                courseNum = slots.get("CourseNum"),
                timeComparison = slots.get("BeforeAfter"),
                singleTime = slots.get("SingleTime"),
                startTime = slots.get("StartTime"),
                endTime = slots.get("EndTime");
        SimpleCard card = new SimpleCard();
        card.setTitle("Section Timings");

        // Listing section times before/after a time
        if (isValidQuery(department, courseNum) && singleTime != null && timeComparison != null) {
            SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
            String output = "<speak>You asked for all of the sections of <say-as interpret-as=\"spell-out\">%s</say-as>" +
                    " <say-as interpret-as=\"spell-out\">%s</say-as> %s <say-as interpret-as=\"time\">%s</say-as></speak>";
            String cardOutput = String.format("You asked for all of the sections of %s %s %s %s", department, courseNum, timeComparison, singleTime);
            outputSpeech.setSsml(String.format(output, department, courseNum, timeComparison, singleTime));
            card.setContent(cardOutput);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }
        // Listing section times between two times
        else if (isValidQuery(department, courseNum) && startTime != null && endTime != null) {
            SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
            String speechOutput = "<speak>You asked for all of the sections of <say-as interpret-as=\"spell-out\">%s</say-as>" +
                    " <say-as interpret-as=\"spell-out\">%s</say-as> between <say-as interpret-as=\"time\">%s</say-as>" +
                    " and <say-as interpret-as=\"time\">%s</say-as></speak>";
            String cardOutput = String.format("You asked for all of the sections of %s %s between %s and %s",
                    department, courseNum, startTime, endTime);

            outputSpeech.setSsml(String.format(speechOutput, department, courseNum, startTime, endTime));
            card.setContent(cardOutput);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }
        // Not enough info. Ask for clarification
        else {
            response = askForClarification();
        }

        return response;
    }

    private boolean isValidQuery(String department, String courseNum) {
        return department != null && courseNum != null;
    }

    private SpeechletResponse askForClarification() {
        Reprompt reprompt = new Reprompt();
        PlainTextOutputSpeech repromptText = new PlainTextOutputSpeech();
        PlainTextOutputSpeech askOutput = new PlainTextOutputSpeech();

        askOutput.setText("I don't have enough information. Try asking me about the timing of a course.");
        repromptText.setText("Can you repeat that?");
        reprompt.setOutputSpeech(repromptText);

        return SpeechletResponse.newAskResponse(askOutput, reprompt);
    }
}
