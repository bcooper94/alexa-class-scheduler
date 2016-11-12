package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
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
                geType = slots.get("GEType"),
                quarter = slots.get("Quarter"),
                year = slots.get("Year");
        List<Query> constraints = new ArrayList<>();

        if (quarter == null) {
            quarter = getCurrentQuarter();
        }
        if (year == null) {
            year = getCurrentYear();
        }

        constraints.add(new Query(QueryKey.QUARTER, quarter, QueryOperation.EQUAL));
        constraints.add(new Query(QueryKey.YEAR, year, QueryOperation.EQUAL));

        // Listing all sections of a course
        if (department != null && courseNum != null) {
            constraints.add(new Query(QueryKey.DEPARTMENT, department, QueryOperation.EQUAL));
            constraints.add(new Query(QueryKey.COURSE_NUM, courseNum, QueryOperation.EQUAL));
            response = courseListResponse(constraints, Arrays.asList(QueryKey.DAYS, QueryKey.TIME_RANGE),
                    quarter, year);
        }
        // Listing all courses within a department
        else if (department != null) {
            constraints.add(new Query(QueryKey.DEPARTMENT, department, QueryOperation.EQUAL));
            response = courseListResponse(constraints, Arrays.asList(QueryKey.DEPARTMENT, QueryKey.COURSE_NUM),
                    quarter, year);
        } else if (college != null) {
            constraints.add(new Query(QueryKey.COLLEGE, college, QueryOperation.EQUAL));
            response = courseListResponse(constraints, Arrays.asList(QueryKey.DEPARTMENT, QueryKey.COURSE_NUM),
                    quarter, year);
        } else if (geType != null) {
            constraints.add(new Query(QueryKey.TYPE, geType, QueryOperation.EQUAL));
            response = courseListResponse(constraints, Arrays.asList(QueryKey.DEPARTMENT, QueryKey.COURSE_NUM),
                    quarter, year);
        } else {
            String collegeResponse = "The colleges are: the College of Agriculture Food" +
                    " and Environmental Science, the College of Architecture and Environmental" +
                    " Design, Orfalea College of Business, the College of Liberal Arts," +
                    "the College of Engineering, and the College of Science and Math.",
                    cardContent = "College of Agriculture, Food, and Environmental Science" +
                            "\nCollege of Architecture and Environmental Design\n" +
                            "Orfalea College of Business\nCollege of Liberal Arts\n" +
                            "College of Engineering\nCollege of Science and Math";
            response = setAnswer(collegeResponse, "Cal Poly Colleges", cardContent);
        }

        return response;
    }

    private SpeechletResponse courseListResponse(List<Query> queries, List<QueryKey> responseKeys,
                                                 String quarter, String year) {
        SpeechletResponse response;
        CourseResponseBuilder responseBuilder = new CourseResponseBuilder();
        List<Course> courses = db.read(queries);
        String cardTitle = "Section Timings",
                defaultSpeech = String.format("Sorry, I wasn't able to find any courses for %s %s",
                        quarter, year),
                defaultCardContent = String.format("No matching courses were found for %s %s.",
                        quarter, year);

        if (courses.size() > 0) {
            String courseOutput = String.format("I found the following courses for %s %s. ",
                    quarter, year) + responseBuilder.convertCourse(courses, responseKeys);
            log.info("HierarchyList response text: {}", courseOutput);
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

        askOutput.setText("I don't have enough information. Try asking about sections or courses within a department or college.");
        repromptText.setText("Can you repeat that?");
        reprompt.setOutputSpeech(repromptText);

        return SpeechletResponse.newAskResponse(askOutput, reprompt);
    }
}
