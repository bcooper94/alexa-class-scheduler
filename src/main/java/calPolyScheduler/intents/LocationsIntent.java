package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;

/**
 * Created by brandon on 11/5/16.
 */
public class LocationsIntent extends SchedulerIntent {
    public LocationsIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        SpeechletResponse response;
        SimpleCard card = new SimpleCard();
        card.setTitle("Section Timings");
        String department = slots.get("Department"),
                courseNum = slots.get("CourseNum"),
                quarter = slots.get("Quarter"),
                year = slots.get("Year"),
                courseType = slots.get("Type");

        if (department == null || courseNum == null) {
            return askForClarification();
        }

        if (courseType != null && quarter != null && year != null) {
            SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
            String ssmlOutput = "<speak>You asked for the locations of the %s sections of <say-as interpret-as=\"spell-out\">%s</say-as>" +
                    " <say-as interpret-as=\"spell-out\">%s</say-as> for %s quarter in <say-as interpret-as=\"date\">%s</say-as></speak>";
            ssmlOutput = String.format(ssmlOutput, courseType, department, courseNum, quarter, year);
            String output = String.format("Listing the locations of the %s sections of %s %s for %s quarter in %s.",
                    courseType, department, courseNum, quarter, year);
            outputSpeech.setSsml(ssmlOutput);
            card.setContent(output);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }
        else if (courseType != null && quarter != null) {
            SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
            String ssmlOutput = "<speak>You asked for the locations of the %s sections of <say-as interpret-as=\"spell-out\">%s</say-as>" +
                    " <say-as interpret-as=\"spell-out\">%s</say-as> for %s quarter</speak>";
            ssmlOutput = String.format(ssmlOutput, courseType, department, courseNum, quarter);
            String output = String.format("Listing the locations of the %s sections of %s %s for %s quarter.",
                    courseType, department, courseNum, quarter);
            outputSpeech.setSsml(ssmlOutput);
            card.setContent(output);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }
        else if (courseType != null) {
            SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
            String ssmlOutput = "<speak>You asked for the locations of the %s sections of <say-as interpret-as=\"spell-out\">%s</say-as>" +
                    " <say-as interpret-as=\"spell-out\">%s</say-as></speak>";
            ssmlOutput = String.format(ssmlOutput, courseType, department, courseNum);
            String output = String.format("Listing the locations of the %s sections of %s %s.",
                    courseType, department, courseNum);
            outputSpeech.setSsml(ssmlOutput);
            card.setContent(output);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }
        else if (quarter != null && year != null) {
            SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
            String ssmlOutput = "<speak>You asked for the locations of <say-as interpret-as=\"spell-out\">%s</say-as>" +
                    " <say-as interpret-as=\"spell-out\">%s</say-as> for %s quarter in <say-as interpret-as=\"date\">%s</say-as></speak>";
            ssmlOutput = String.format(ssmlOutput, department, courseNum, quarter, year);
            String output = String.format("Listing the locations of %s %s for %s quarter in %s.",
                    department, courseNum, quarter, year);
            outputSpeech.setSsml(ssmlOutput);
            card.setContent(output);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }
        else if (quarter != null) {
            SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
            String ssmlOutput = "<speak>You asked for the locations of <say-as interpret-as=\"spell-out\">%s</say-as>" +
                    " <say-as interpret-as=\"spell-out\">%s</say-as> for %s quarter</speak>";
            ssmlOutput = String.format(ssmlOutput, department, courseNum, quarter);
            String output = String.format("Listing the locations of %s %s for %s quarter.",
                    department, courseNum, quarter);
            outputSpeech.setSsml(ssmlOutput);
            card.setContent(output);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }
        else {
            SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
            String ssmlOutput = "<speak>You asked for the locations of <say-as interpret-as=\"spell-out\">%s</say-as>" +
                    " <say-as interpret-as=\"spell-out\">%s</say-as></speak>";
            ssmlOutput = String.format(ssmlOutput, department, courseNum);
            String output = String.format("Listing the locations of %s %s.",
                    department, courseNum);
            outputSpeech.setSsml(ssmlOutput);
            card.setContent(output);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }

        return response;
    }

    private SpeechletResponse askForClarification() {
        Reprompt reprompt = new Reprompt();
        PlainTextOutputSpeech repromptText = new PlainTextOutputSpeech();
        PlainTextOutputSpeech askOutput = new PlainTextOutputSpeech();

        askOutput.setText("I don't have enough information. Try asking me about the location of a course.");
        repromptText.setText("Can you repeat that?");
        reprompt.setOutputSpeech(repromptText);

        return SpeechletResponse.newAskResponse(askOutput, reprompt);
    }
}
