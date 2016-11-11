package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import dal.*;

import java.util.*;

public class ProfessorIntent extends SchedulerIntent {
    public ProfessorIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        log.info("Professor Intent");
        String responseText = determineUtterance();
        return setAnswer(responseText, "Professor Intent", responseText);
//        SimpleCard card = new SimpleCard();
//        card.setTitle("Professor");
//        card.setContent(responseText);
//
//        PlainTextOutputSpeech response = new PlainTextOutputSpeech();
//        response.setText(responseText);
//
//        return SpeechletResponse.newTellResponse(response, card);
    }

    private String determineUtterance() {
        CourseResponseBuilder crb = new CourseResponseBuilder();
        List<Query> queryList = new ArrayList<Query>();
        String response = "";

        if (slots.get("ProfessorLastName") != null) {
            response = addQuarterYear(queryList);
            queryList.add(new Query(QueryKey.LAST_NAME, slots.get("ProfessorLastName"), QueryOperation.EQUAL));

            List<Course> resultList = db.read(queryList);
            List<QueryKey> types = Arrays.asList(QueryKey.DEPARTMENT, QueryKey.COURSE_NUM);
            response += " Professor " + slots.get("ProfessorLastName") + " teaches " + crb.convertCourse(resultList, types);
        }
        else if (slots.get("Department") != null && slots.get("CourseNum") != null) {
            response = addQuarterYear(queryList);
            queryList.add(new Query(QueryKey.DEPARTMENT, slots.get("Department"), QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.COURSE_NUM, slots.get("CourseNum"), QueryOperation.EQUAL));

            List<Course> resultList = db.read(queryList);
            List<QueryKey> types = Arrays.asList(QueryKey.LAST_NAME);

            response += " the professors teaching " + crb.addSpellOut(slots.get("Department")) + " " +
                    slots.get("CourseNum") + " is " + crb.convertCourse(resultList, types);
        }

        return response;
    }
}
