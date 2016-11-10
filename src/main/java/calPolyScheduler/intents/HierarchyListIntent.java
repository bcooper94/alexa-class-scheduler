package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import dal.Query;
import dal.QueryKey;
import dal.QueryOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brandon on 11/5/16.
 */
public class HierarchyListIntent extends SchedulerIntent {
    public HierarchyListIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        SpeechletResponse response;
        String college = slots.get("College"),
                department = slots.get("Department"),
                courseNum = slots.get("CourseNum"),
                geType = slots.get("GEType");
        SimpleCard card = new SimpleCard();
        card.setTitle("Section Timings");
        List<Query> constraints = new ArrayList<>();

        // Listing all sections of a course
        if (department != null && courseNum != null) {
            constraints.add(new Query(QueryKey.DEPARTMENT, department, QueryOperation.EQUAL));
            constraints.add(new Query(QueryKey.COURSE_NUM, courseNum, QueryOperation.EQUAL));
            // TODO: Use constraints to query DB
            SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
            String ssmlOutput = "<speak>You asked for the sections of <say-as interpret-as=\"spell-out\">%s</say-as>" +
                    " <say-as interpret-as=\"spell-out\">%s</say-as></speak>";
            ssmlOutput = String.format(ssmlOutput, department, courseNum);
            String output = String.format("Listing the sections of %s %s.", department, courseNum);
            outputSpeech.setSsml(ssmlOutput);
            card.setContent(output);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }
        // Listing all courses within a department
        else if (department != null) {
            constraints.add(new Query(QueryKey.DEPARTMENT, department, QueryOperation.EQUAL));
            // TODO: Use constraints to query DB
            SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
            String ssmlOutput = "<speak>You asked for the courses within the <say-as interpret-as=\"spell-out\">%s</say-as>" +
                    " department</speak>";
            ssmlOutput = String.format(ssmlOutput, department);
            String output = String.format("Listing the courses within the %s department.", department);
            outputSpeech.setSsml(ssmlOutput);
            card.setContent(output);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }
        else if (college != null) {
            constraints.add(new Query(QueryKey.COLLEGE, college, QueryOperation.EQUAL));
            // TODO: Use constraints to query DB
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            String outputText = String.format("You asked for the courses within the %s", college);
            String output = String.format("Listing the courses within the %s.", college);
            outputSpeech.setText(outputText);
            card.setContent(output);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }
        else if (geType != null) {
            constraints.add(new Query(QueryKey.TYPE, geType, QueryOperation.EQUAL));
            // TODO: Use constraints to query DB
            SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
            String ssmlOutput = "<speak>You asked for the courses that fulfill the <say-as interpret-as=\"spell-out\">%s</say-as> requirement</speak>";
            ssmlOutput = String.format(ssmlOutput, geType);
            outputSpeech.setSsml(ssmlOutput);
            card.setContent(String.format("Listing the courses that fulfill the %s requirement.", geType));
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }
        else {
            // TODO: Query DB for all colleges
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Listing all of the colleges.");
            card.setContent("Listing all of the colleges.");
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }

        return response;
    }

    private SpeechletResponse askForClarification() {
        Reprompt reprompt = new Reprompt();
        PlainTextOutputSpeech repromptText = new PlainTextOutputSpeech();
        PlainTextOutputSpeech askOutput = new PlainTextOutputSpeech();

        askOutput.setText("I don't have enough information. Try asking about sections or courses within a department or college.");
        repromptText.setText("Can you repeat that?");
        reprompt.setOutputSpeech(repromptText);

        return SpeechletResponse.newAskResponse(askOutput, reprompt);
    }
}
