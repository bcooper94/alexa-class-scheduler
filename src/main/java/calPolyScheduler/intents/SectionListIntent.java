package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import dal.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SectionListIntent extends SchedulerIntent {
    public SectionListIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        log.info("Section List Intent");
        String responseText = determineUtterance();

        SimpleCard card = new SimpleCard();
        card.setTitle("Section List");
        card.setContent(responseText);

        PlainTextOutputSpeech response = new PlainTextOutputSpeech();
        response.setText(responseText);

        return SpeechletResponse.newTellResponse(response, card);
    }

    //Builds a query from the slot variables and queries the database
    //Builds a response from the data returned from db
    private String determineUtterance() {
        StringBuilder response = new StringBuilder();
        List<Query> queryList = new ArrayList<Query>();
        CourseResponseBuilder crb = new CourseResponseBuilder();


        if (slots.get("Type") != null && slots.get("Department") != null &&
                slots.get("CourseNum") != null) {
            response.append(addQuarterYear(queryList));
            queryList.add(new Query(QueryKey.TYPE, slots.get("Type"), QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.DEPARTMENT, slots.get("Department"), QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.COURSE_NUM, slots.get("CourseNum"), QueryOperation.EQUAL));

            List<Course> resultList = db.read(queryList);
            List<QueryKey> types = Arrays.asList(QueryKey.SECTION, QueryKey.FIRST_NAME,
                QueryKey.LAST_NAME, QueryKey.DAYS, QueryKey.START, QueryKey.END,
                    QueryKey.LOCATION);

            response.append(crb.addSpellOut(slots.get("Department")) + slots.get("CourseNum")
                + " has ");
            List<String> courseVars = crb.convertCourseToList(resultList, types
            );

            response.append(slots.get("Type") + " section " + courseVars.get(0)
                    + " taught by " + courseVars.get(1) + " " + courseVars.get(2)
                + " on " + courseVars.get(3) + " from " + courseVars.get(4) + " to "
             + courseVars.get(5) + " in " + courseVars.get(6));
        }

        return response.toString();
    }
}
