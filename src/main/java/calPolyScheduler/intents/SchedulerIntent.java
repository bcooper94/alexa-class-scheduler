package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazonaws.auth.AnonymousAWSCredentials;
import dal.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by brandon on 11/1/16.
 */
public abstract class SchedulerIntent {
    protected static Logger log = LoggerFactory.getLogger(SchedulerIntent.class);
    protected Intent intent;
    protected Session session;
    protected Map<String, String> slots;
    protected static Database db;

    private static final int JANUARY = 1;
    private static final int MARCH = 3;
    private static final int JUNE = 6;
    private static final int SEPTEMBER = 9;
    private static final int DECEMBER = 12;

    public SchedulerIntent(Intent intent, Session session) {
        this.intent = intent;
        this.session = session;
        this.slots = new HashMap<>();
        Map<String, Slot> slots = intent.getSlots();
        StringBuilder slotStr = new StringBuilder("[");
        Slot curSlot;
        for (String key : slots.keySet()) {
            curSlot = slots.get(key);
            if (curSlot.getValue() != null) {
                this.slots.put(key, curSlot.getValue().toUpperCase());
                slotStr.append(String.format("%s: %s ", key, curSlot.getValue().toUpperCase()));
            }
        }
        String slotsLog = slotStr.toString().trim() + "]";
        log.info("{} slots={}", this.intent.getName(), slotsLog);
        if (db == null)
            db = new Database();

        Boolean isDone = (Boolean) session.getAttribute("isDone");
        if (isDone == null) {
            log.info("Setting session isDone attribute to false");
            session.setAttribute("isDone", false);
        }
    }

    protected SpeechletResponse setFollowUpQuestion(String voiceOuput, String repromptText) {
        Reprompt reprompt = new Reprompt();
        PlainTextOutputSpeech repromptOutput = new PlainTextOutputSpeech();
        PlainTextOutputSpeech askOutput = new PlainTextOutputSpeech();

        repromptOutput.setText(repromptText);
        reprompt.setOutputSpeech(repromptOutput);
        askOutput.setText(voiceOuput);

        return SpeechletResponse.newAskResponse(askOutput, reprompt);
    }

    protected SpeechletResponse setAnswer(String voiceOuput, String cardTitle, String appOutput) {
        SpeechletResponse response;
        SimpleCard card = new SimpleCard();
        card.setTitle(cardTitle);
        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
        card.setContent(appOutput);

        Boolean isDone = (Boolean) session.getAttribute("isDone");
        if (isDone) {
            voiceOuput = "<speak>" + voiceOuput + "</speak>";
            outputSpeech.setSsml(voiceOuput);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
        }
        else {
            Reprompt reprompt = new Reprompt();
            PlainTextOutputSpeech repromptText = new PlainTextOutputSpeech();

            voiceOuput = "<speak>" + voiceOuput + "<break time=\"2s\"/> Would you like to ask about more courses?</speak>";
            outputSpeech.setSsml(voiceOuput);
            repromptText.setText("Can you repeat that?");
            reprompt.setOutputSpeech(repromptText);

            response = SpeechletResponse.newAskResponse(outputSpeech, reprompt, card);
        }

        return response;
    }

    public abstract SpeechletResponse createResponse();

    protected String getCurrentQuarter() {
        String month;
        int monthCode = Calendar.getInstance().get(Calendar.MONTH);

        if (monthCode >= SEPTEMBER && monthCode <= DECEMBER) {
            month = "FALL";
        }
        else if (monthCode >= JANUARY && monthCode <= MARCH) {
            month = "WINTER";
        }
        else if (monthCode > MARCH && monthCode <= JUNE) {
            month = "SPRING";
        }
        else {
            month = "SUMMER";
        }

        return month;
    }

    protected String getCurrentYear() {
        return "" + Calendar.getInstance().get(Calendar.YEAR);
    }

//    public abstract void sendQuery();

    //Use this at the start of a query
    //@param queryList - the statement to add the quarter and/or year if they aren't null
    //@return a string to add to start the response alexa will replay with
    protected String addQuarterYear(List<Query> queryList) {
        String response = "";
        if (slots.get("Quarter") != null && slots.get("Year") != null) {
            queryList.add(new Query(QueryKey.QUARTER, slots.get("Quarter"), QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.YEAR, slots.get("Year"), QueryOperation.EQUAL, QueryLogic.AND));
            response = "In " + slots.get("Quarter") + " " + slots.get("Year") + " ";
        }
        else if (slots.get("Quarter") != null) {
            queryList.add(new Query(QueryKey.QUARTER, slots.get("Quarter"), QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.YEAR, getCurrentYear(), QueryOperation.EQUAL, QueryLogic.AND));
            response = "In " + slots.get("Quarter") + " " + getCurrentYear() + " ";
        }
        else {
            queryList.add(new Query(QueryKey.QUARTER, getCurrentQuarter(), QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.YEAR, getCurrentYear(), QueryOperation.EQUAL, QueryLogic.AND));
            response = "In " + getCurrentQuarter() + " " + getCurrentYear() + " ";
        }
        return response;
    }
}
