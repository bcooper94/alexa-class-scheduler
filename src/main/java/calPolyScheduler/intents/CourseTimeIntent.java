package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.*;
import dal.*;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by brandon on 11/5/16.
 */
public class CourseTimeIntent extends SchedulerIntent {
    SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat militaryFormatter = new SimpleDateFormat("HH:mm");

    public CourseTimeIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        log.info("Course Time Intent");
        String responseText = determineUtterance();

        SimpleCard card = new SimpleCard();
        card.setTitle("Course Time");
        card.setContent(responseText);

        PlainTextOutputSpeech response = new PlainTextOutputSpeech();
        response.setText(responseText);

        if (responseText.isEmpty()) {
            return askForClarification();
        }

        return SpeechletResponse.newTellResponse(response, card);

        // Listing section times before/after a time
//        if (isValidQuery(department, courseNum) && singleTime != null && timeComparison != null) {
//            SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
//            String output = "<speak>You asked for all of the sections of <say-as interpret-as=\"spell-out\">%s</say-as>" +
//                    " <say-as interpret-as=\"spell-out\">%s</say-as> %s <say-as interpret-as=\"time\">%s</say-as></speak>";
//            String cardOutput = String.format("You asked for all of the sections of %s %s %s %s", department, courseNum, timeComparison, singleTime);
//            outputSpeech.setSsml(String.format(output, department, courseNum, timeComparison, singleTime));
//            card.setContent(cardOutput);
//            response = SpeechletResponse.newTellResponse(outputSpeech, card);


//        }
        // Listing section times between two times
//        else if (isValidQuery(department, courseNum) && startTime != null && endTime != null) {
//            SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
//            String speechOutput = "<speak>You asked for all of the sections of <say-as interpret-as=\"spell-out\">%s</say-as>" +
//                    " <say-as interpret-as=\"spell-out\">%s</say-as> between <say-as interpret-as=\"time\">%s</say-as>" +
//                    " and <say-as interpret-as=\"time\">%s</say-as></speak>";
//            String cardOutput = String.format("You asked for all of the sections of %s %s between %s and %s",
//                    department, courseNum, startTime, endTime);
//
//            outputSpeech.setSsml(String.format(speechOutput, department, courseNum, startTime, endTime));
//            card.setContent(cardOutput);
//            response = SpeechletResponse.newTellResponse(outputSpeech, card);
//        }
        // Not enough info. Ask for clarification
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

    //TODO: add 'type' rather than default to lecture
    private String determineUtterance() {
        String department = slots.get("Department"),
                courseNum = slots.get("CourseNum"),
                timeComparison = slots.get("BeforeAfter"),
                singleTime = slots.get("SingleTime"),
                startTime = slots.get("StartTime"),
                endTime = slots.get("EndTime"),
                quarter = slots.get("Quarter"),
                year = slots.get("Year");


        StringBuilder response = new StringBuilder();
        List<Query> queryList = new ArrayList<Query>();
        CourseResponseBuilder crb = new CourseResponseBuilder();

        if (quarter == null)
            quarter = getCurrentQuarter();
        if (year == null)
            year = getCurrentYear();

        queryList.add(new Query(QueryKey.QUARTER, quarter, QueryOperation.EQUAL));
        queryList.add(new Query(QueryKey.YEAR, year, QueryOperation.EQUAL));


        if (department != null && courseNum != null && singleTime != null && timeComparison != null) {
            Date singleTimeDate = militaryFormatter.parse(singleTime, new ParsePosition(0));
            response.append(addQuarterYear(queryList));
            queryList.add(new Query(QueryKey.TYPE, "LEC", QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.DEPARTMENT, department, QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.COURSE_NUM, courseNum, QueryOperation.EQUAL));//, QueryLogic.AND));

            //Query the database!!!
            List<Course> resultList = db.read(queryList);
            List<QueryKey> types = Arrays.asList(QueryKey.SECTION, QueryKey.FIRST_NAME,
                    QueryKey.LAST_NAME, QueryKey.DAYS, QueryKey.START, QueryKey.END,
                    QueryKey.LOCATION);

            response.append(crb.addSpellOut(department) + crb.addSpellOut(courseNum) + " has ");

            for (Course course : resultList) {
                Date startTimeDate =  formatter.parse(course.start, new ParsePosition(0));
                Date endTimeDate = formatter.parse(course.end, new ParsePosition(0));

                if (courseTimeFallsInRange(course, timeComparison, singleTimeDate)) {
                    List<String> courseVars = crb.convertCourseToList(Arrays.asList(course), types);
                    response.append("Lecture" + " section " + courseVars.get(0)
                            + " taught by " + courseVars.get(1) + " " + courseVars.get(2)
                            + " on " + courseVars.get(3) + " from " + courseVars.get(4) + " to "
                            + courseVars.get(5) + " in " + courseVars.get(6) + ". ");
                }
                else {
                    response.append(" COULDN'T ADD ");
                    List<String> courseVars = crb.convertCourseToList(Arrays.asList(course), types);
                    response.append("Lecture" + " section " + courseVars.get(0)
                            + " taught by " + courseVars.get(1) + " " + courseVars.get(2)
                            + " on " + courseVars.get(3) + " from " + courseVars.get(4) + " to "
                            + courseVars.get(5) + " in " + courseVars.get(6) + ". ");
                }
            }
        }
        else if (department != null && courseNum != null && startTime != null && endTime != null) {
            response.append(addQuarterYear(queryList));
            queryList.add(new Query(QueryKey.TYPE, "LEC", QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.DEPARTMENT, department, QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.COURSE_NUM, courseNum, QueryOperation.EQUAL));//, QueryLogic.AND));

            //Query the database!!!
            List<Course> resultList = db.read(queryList);
            List<QueryKey> types = Arrays.asList(QueryKey.SECTION, QueryKey.FIRST_NAME,
                    QueryKey.LAST_NAME, QueryKey.DAYS, QueryKey.START, QueryKey.END,
                    QueryKey.LOCATION);

            response.append(crb.addSpellOut(department) + crb.addSpellOut(courseNum) + " has ");

            for (Course course : resultList) {
                if (courseTimeFallsInRange(course, startTime, endTime)) {
                    List<String> courseVars = crb.convertCourseToList(Arrays.asList(course), types);
                    response.append("Lecture" + " section " + courseVars.get(0)
                            + " taught by " + courseVars.get(1) + " " + courseVars.get(2)
                            + " on " + courseVars.get(3) + " from " + courseVars.get(4) + " to "
                            + courseVars.get(5) + " in " + courseVars.get(6) + ". ");
                }
                else {
                    response.append(" COULDN'T ADD ");
                    List<String> courseVars = crb.convertCourseToList(Arrays.asList(course), types);
                    response.append("Lecture" + " section " + courseVars.get(0)
                            + " taught by " + courseVars.get(1) + " " + courseVars.get(2)
                            + " on " + courseVars.get(3) + " from " + courseVars.get(4) + " to "
                            + courseVars.get(5) + " in " + courseVars.get(6) + ". ");
                }
            }

        }

        return response.toString();
    }

    boolean courseTimeFallsInRange(Course course, String timeComparison, Date singleTimeDate) {
        Date courseStartTimeDate =  formatter.parse(course.start, new ParsePosition(0));
        Date courseEndTimeDate = formatter.parse(course.end, new ParsePosition(0));

        if (timeComparison.equals("before") && courseEndTimeDate.before(singleTimeDate))
            return true;
        else if (timeComparison.equals("after") && courseStartTimeDate.after(singleTimeDate))
            return true;

        return false;
    }

    boolean courseTimeFallsInRange(Course course, String startTime, String endTime) {
        Date courseStartTimeDate =  formatter.parse(course.start, new ParsePosition(0));
        Date courseEndTimeDate = formatter.parse(course.end, new ParsePosition(0));
        Date startTimeDate = militaryFormatter.parse(startTime, new ParsePosition(0));
        Date endTimeDate = militaryFormatter.parse(endTime, new ParsePosition(0));

        if ((courseStartTimeDate.equals(startTimeDate) || courseStartTimeDate.after(startTimeDate))
            && (courseEndTimeDate.equals(endTimeDate) || courseEndTimeDate.before(endTimeDate)))
            return true;

        return false;
    }
}