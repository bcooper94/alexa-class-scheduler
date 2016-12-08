package calPolyScheduler.intents;

import calPolyScheduler.Schedule;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.services.dynamodbv2.xspec.S;
import dal.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleManagerIntent extends SchedulerIntent {
    private Schedule schedule;

    public ScheduleManagerIntent(Intent intent, Session session, Schedule schedule) {
        super(intent, session);
        this.schedule = schedule;
        log.info("Schedule={}", schedule.getList());
    }

    public SpeechletResponse createResponse() {
        log.info("Schedule Manager Intent");
        SpeechletResponse response;
        String cardTitle = "Schedule Manager";
        CourseResponseBuilder crb = new CourseResponseBuilder();
        List<Query> queryList = new ArrayList<Query>();
        String responseText = "";
        String card = "";
        String addRemove = slots.get("AddRemove"),
                department = slots.get("Department"),
                courseNum = slots.get("CourseNum"),
                section = slots.get("Section");

        if (section != null && addRemove == null && department == null && courseNum == null) {
            addRemove = (String) session.getAttribute("addRemove");
            department = (String) session.getAttribute(QueryKey.DEPARTMENT.getKey());
            courseNum = (String) session.getAttribute(QueryKey.COURSE_NUM.getKey());
        }

        if (addRemove != null && department != null && courseNum != null
                && section != null) {
            queryList.add(new Query(QueryKey.SECTION, section, QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.DEPARTMENT, department, QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.COURSE_NUM, courseNum, QueryOperation.EQUAL));
            List<Course> resultList = db.read(queryList);

            if (resultList.size() > 0) {
                //add
                if (addRemove.toLowerCase().equals("add")) {
                    responseText += schedule.addCourse(resultList.get(0)) ?
                            "Successfully added " : "Failed to add ";
                }
                //remove
                else {
                    responseText += schedule.removeCourse(resultList.get(0)) ?
                            "Successfully removed " : "Your schedule did not contain ";
                }
            }
            else {
                responseText += "Could not find the requested course ";
            }

            List<QueryKey> keys = Arrays.asList(QueryKey.DEPARTMENT, QueryKey.COURSE_NUM, QueryKey.SECTION);
            card = new String(responseText);
            responseText += crb.convertCourse(resultList, keys);
            card += crb.getCardContent(resultList, keys);
            response = setAnswer(responseText, cardTitle, card);
        }
        // User didn't specify section number
        else if (addRemove != null && department != null && courseNum != null) {
            queryList.add(new Query(QueryKey.COURSE_NUM, courseNum, QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.DEPARTMENT, department, QueryOperation.EQUAL));
            List<Course> courses = db.read(queryList);

            if (courses.size() > 0) {
                session.setAttribute(QueryKey.DEPARTMENT.getKey(), department);
                session.setAttribute(QueryKey.COURSE_NUM.getKey(), courseNum);
                session.setAttribute("addRemove", addRemove);
                responseText = askWhichSection(courses, department, courseNum);
                card = String.format("Clarifying which section of %s %s...",
                        department, courseNum);
            }
            else {
                responseText = "I wasn't able to find any matching courses.";
                card = String.format("No matching courses found for %s %s", department, courseNum);
            }

            response = setAnswer(responseText, cardTitle, card, false);
        }
        // User asked about section without previously having asked about adding/removing a course
        else if (section != null) {
            responseText = "Sorry, I have no record of which course you are trying to add or remove. " +
                    "Please tell me which course you'd like to add to, or remove from your schedule.";
            card = "Please tell me which course you'd like to add to or remove from your schedule.";
            response = setAnswer(responseText, cardTitle, card, false);
        }
        else {
            //list courses currently stored in schedule
            responseText += "Your schedule is " + schedule.getList();
            card += "Your schedule is " + schedule.getLisForList();
            response = setAnswer(responseText, cardTitle, card);
        }

        return response;
    }

    private String askWhichSection(List<Course> courses, String department,
                                   String courseNum) {
        Course curCourse;
        StringBuilder response = new StringBuilder(
                String.format("I found the following sections of %s %s. ", department, courseNum));

        for (int index = 0; index < courses.size() - 1; index++) {
            curCourse = courses.get(index);
            String section = curCourse.section;

            if (section != null && section.startsWith("0")) {
                section = section.substring(1);
            }

            response.append(String.format("Section %s on %s from %s to %s, ",
                    section, CourseResponseBuilder.convertDays(curCourse.days),
                    curCourse.start, curCourse.end));
        }

        if (courses.size() > 1) {
            response.append("and ");
        }

        curCourse = courses.get(courses.size() - 1);
        response.append(String.format("section %s on %s from %s to %s. Which section would you like?",
                curCourse.section, CourseResponseBuilder.convertDays(curCourse.days),
                curCourse.start, curCourse.end));

        return response.toString();
    }
}
