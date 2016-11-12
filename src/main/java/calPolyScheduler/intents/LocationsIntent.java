package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import dal.Course;
import dal.Query;
import dal.QueryKey;
import dal.QueryOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Brandon Cooper
 */
public class LocationsIntent extends SchedulerIntent {
    public LocationsIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        String department = slots.get("Department"),
                courseNum = slots.get("CourseNum"),
                quarter = slots.get("Quarter"),
                year = slots.get("Year"),
                courseType = slots.get("Type");
        List<Query> constraints = new ArrayList<>();

        if (department == null || courseNum == null) {
            return askForClarification();
        }
        if (quarter == null) {
            quarter = getCurrentQuarter();
        }
        if (year == null) {
            year = getCurrentYear();
        }

        constraints.add(new Query(QueryKey.DEPARTMENT, department, QueryOperation.EQUAL));
        constraints.add(new Query(QueryKey.COURSE_NUM, courseNum, QueryOperation.EQUAL));
        constraints.add(new Query(QueryKey.QUARTER, quarter, QueryOperation.EQUAL));
        constraints.add(new Query(QueryKey.YEAR, year, QueryOperation.EQUAL));

//        if (courseType != null && quarter != null && year != null) {
//            constraints.add(new Query(QueryKey.TYPE, courseType, QueryOperation.EQUAL));
//            constraints.add(new Query(QueryKey.QUARTER, quarter, QueryOperation.EQUAL));
//            constraints.add(new Query(QueryKey.YEAR, year, QueryOperation.EQUAL));
//        } else if (courseType != null && quarter != null) {
//            constraints.add(new Query(QueryKey.TYPE, courseType, QueryOperation.EQUAL));
//            constraints.add(new Query(QueryKey.QUARTER, quarter, QueryOperation.EQUAL));
//        }
        if (courseType != null) {
            constraints.add(new Query(QueryKey.TYPE, courseType, QueryOperation.EQUAL));
        }
//        else if (quarter != null && year != null) {
//            constraints.add(new Query(QueryKey.QUARTER, quarter, QueryOperation.EQUAL));
//            constraints.add(new Query(QueryKey.YEAR, year, QueryOperation.EQUAL));
//        } else if (quarter != null) {
//            constraints.add(new Query(QueryKey.QUARTER, quarter, QueryOperation.EQUAL));
//        }

        return courseLocationsResponse(constraints, quarter, year);
    }

    private SpeechletResponse courseLocationsResponse(List<Query> constraints,
                                                      String quarter, String year) {
        SpeechletResponse response;
        CourseResponseBuilder responseBuilder = new CourseResponseBuilder();
        List<Course> courses = db.read(constraints);
        String cardTitle = "Course Locations",
                defaultSpeech = String.format("Sorry, I wasn't able to find any courses for %s %s",
                        quarter, year),
                defaultCardContent = String.format("No matching courses were found for %s %s.", quarter, year);

        if (courses.size() > 0) {
            String courseOutput = String.format("I found the following course locations for %s %s.",
                    quarter, year) +
                    responseBuilder.convertCourse(courses, Arrays.asList(
                            QueryKey.DEPARTMENT, QueryKey.COURSE_NUM, QueryKey.LOCATION));
            response = setAnswer(courseOutput, cardTitle, courseOutput);
        } else {
            response = setAnswer(defaultSpeech, cardTitle, defaultCardContent);
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
