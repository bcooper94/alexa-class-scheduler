import dal.Course;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Scraper {
    public static final String URL = "http://schedules.calpoly.edu/";
    private final String[] IMPORTANT_LINKS = {"college_", "all_subject_", "depts_", "all_person_", "all_require_"};
    private static final String REMOVE_NBSP = "\u00a0";
    private Document doc;
    ArrayList<String> links;
    private String[] date = new String[2]; //stored as quarter, year

    public enum Quarter {
        FALL("Fall"),
        WINTER("Winter"),
        SPRING("Spring");

        private String value;
        private Quarter(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    //expects a quarter in the format: fall, winter, or spring
    public Scraper(String targetQuarter) {
        try {
            doc = getQuarter(Jsoup.connect(URL).get(), getTargetQuarter(targetQuarter));
            links = collectLinks(doc);
        }
        catch (IOException io) {
                io.printStackTrace();
        }
    }

    //collects the useful links from the quarter page for later use
    private ArrayList<String> collectLinks(Document doc) throws IOException {
        ArrayList<String> usefuLinks = new ArrayList<String>();
        Elements links = doc.select("a[href]");

        for (Element link : links) {
//            System.out.println(link.attr("abs:href") + " \'" + link.text() + "\'");
            String cur = link.attr("abs:href");
            if (isUsefulLink(cur)) {
                usefuLinks.add(cur);
//                System.out.println("adding cur");
            }
        }
        return usefuLinks;
    }

    //determines if the link contains one of the important links
    private boolean isUsefulLink(String curLink) {
        for (String link : IMPORTANT_LINKS) {
            if (curLink.contains(link))
                return true;
        }
        return false;
    }

    //gets the correct document to get all the links from
    private Document getQuarter(Document doc, Quarter target) throws IOException {
        Elements elements = doc.getElementsByClass("term" + target.getValue());
        if (elements.size() == 0) {
            Elements quarterLinks = doc.select("a[href]"); //save this?
            String nextLink = null;
            for (Element link : quarterLinks) {
                if (link.text().equals("Next Term")) {
                    nextLink = link.attr("abs:href");
                    break;
                }
            }
            doc = getQuarter(Jsoup.connect(nextLink).get(), target);
        }
        else {
            //save date
            date[0] = elements.get(0).text().split(" ")[0];
            date[1] = elements.get(0).text().split(" ")[2];
        }
        return doc;
    }


    //gets the quarter based on some input string, expects fall, winter, or spring
    private Quarter getTargetQuarter(String target) {
        Quarter quarter;
        target = target == null ? "fall" : target.toLowerCase();
        if (target.equals("winter"))
            quarter = Quarter.WINTER;
        else if (target.equals("spring"))
            quarter = Quarter.SPRING;
        else
            quarter = Quarter.FALL;
        return quarter;
    }

    //gathers all the courses offered for a particular quarter
    //This expects a college code, ie CENG for engineering
    public ArrayList<Course> getCourseList(String college) {
        //get the appropriate link
        String targetLink = getSpecificLink(new String[] {IMPORTANT_LINKS[0], college});

        //get the html from the classes
        String[] classes = {"courseName", "courseSection", "personName", "courseRequirement",
                "courseType", "courseDays", "startTime", "endTime", "location"};
        ArrayList<ArrayList<String>> data = getValues(targetLink, classes);

        return extractCourses(data);
    }

    //gathers all the GEs offered
    public ArrayList<Course> getGEs() {
        ArrayList<Course> list = new ArrayList<Course>();

        //get the appropriate link
        String targetLink = getSpecificLink(new String[] {IMPORTANT_LINKS[4]});

        //get all GE links
        try {
            Document document = Jsoup.connect(targetLink).maxBodySize(0).get();
            Elements elements = document.select("a[href]");

            ArrayList<String> geLinks = new ArrayList<String>();
            for (Element e : elements) {
                if (e.attr("abs:href").contains("/require_")) {
                    geLinks.add(e.attr("abs:href"));
                }
            }

            String[] classes = {"courseName", "courseSection", "personName", "courseRequirement",
                    "courseType", "courseDays", "startTime", "endTime", "location", };
            ArrayList<ArrayList<String>> data;
            //get the courses for each link
            for (String geLink : geLinks) {
                list.addAll(extractCourses(getValues(geLink, classes)));
            }
        }
        catch (IOException io) {
            io.printStackTrace();
        }
        return list;
    }

    //inner arraylist must be filled with components in the exact order of the Course constructor
    //name, section, professor, requirement, type, days, start, end, location
    private ArrayList<Course> extractCourses(ArrayList<ArrayList<String>> data) {
        ArrayList<Course> courses = new ArrayList<Course>();

        for (int iter = 0; iter < data.get(0).size(); iter++) {
            //don't add if the course has no professor listed
            if (data.get(2).get(iter).replace(REMOVE_NBSP, "").length() > 0) {
                String[] courseName = data.get(0).get(iter).split(" ");
                String[] profName = data.get(2).get(iter).split(" ");
                Course c = new Course();
                c.department = courseName[0].toUpperCase();
                c.courseNumber = courseName[1].toUpperCase();
                c.section = data.get(1).get(iter).toUpperCase();
                c.profFirstName = profName[1].toUpperCase();
                c.profLastName = profName[0].replace(",", "").toUpperCase();
                c.requirement = data.get(3).get(iter).toUpperCase();
                c.type = data.get(4).get(iter).toUpperCase();
                c.days = data.get(5).get(iter).toUpperCase();
                c.start = data.get(6).get(iter).toUpperCase();
                c.end = data.get(7).get(iter).toUpperCase();
                c.quarter = date[0].toUpperCase();
                c.year = date[1];
                c.location = data.get(8).get(iter).toUpperCase();
                courses.add(c);
            }
        }
        return courses;
    }

    //gets a specific link from the useful links saved earlier provided it contains
    // all the strings in the values array
    private String getSpecificLink(String[] values) {
        for (String link : links) {
            boolean good = true;
            for (int iter = 0; good && iter < values.length; iter++) {
                good = link.contains(values[iter]);
            }
            if (good)
                return link;
        }

        System.err.print("Could not find specified link with values: " + Arrays.toString(values));
        return "";
    }

    //gets all the text in the html document classes specified in
    // target classes from the specified url
    private ArrayList<ArrayList<String>> getValues(String url, String[] targetClasses) {
        ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();

        try {
            //This may not always get the entire html, if it doesn't
            //try using: http://stackoverflow.com/questions/21683019/jsoup-missing-content
            Document document = Jsoup.connect(url).maxBodySize(0).get();

            for (String target : targetClasses) {
                ArrayList<String> classVals = new ArrayList<String>();
                Elements elements = document.getElementsByClass(target);
                for (Element element : elements) {
                    classVals.add(element.text());
                }
                values.add(classVals);
            }
        }
        catch (IOException io) {
            io.printStackTrace();
        }
        return values;
    }

    //saves the current document's html to a file (overwrites the file)
    public void saveToFile(String filename, String url) {
        try {
            File f = new File(filename);
            if (!f.exists())
                f.createNewFile();
            FileOutputStream output = new FileOutputStream(f);
            output.write(Jsoup.connect(url).maxBodySize(0).get().html().getBytes());
//            System.out.print(doc.html());
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void saveAssignmentshtml() {
        for (String link : links) {
            if (link.contains(IMPORTANT_LINKS[0])) {
                String name = link.split("_")[1].replaceAll("\\d+-", "") + "_assn.txt";
                saveToFile(name, link);
            }
        }
    }
}
