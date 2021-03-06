package dal; /**
* dal.Course POJO to hold the database information
*
* @author  Joey Wilson
*/

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Course {

   public String department;
   public String courseNumber;
   public String section;
   public String profFirstName;
   public String profLastName;
   public String requirement;
   public String type;
   public String days;
   public String start;
   public String end;
   public String quarter;
   public String year;
   public String location;

   public Course() {

   }

   public Course(String department, String courseNumber, String section,
                 String profFirstName, String profLastName,
                 String requirement, String type, String days,
                 String start, String end, String quarter,
                 String year, String location) {
      this.department = department.toUpperCase();
      this.section = section.toUpperCase();
      this.profFirstName = profFirstName.toUpperCase();
      this.profLastName = profLastName.toUpperCase();
      this.requirement = requirement.toUpperCase();
      this.type = type.toUpperCase();
      this.days = days.toUpperCase();
      this.start = start;
      this.end = end;
      this.quarter = quarter.toUpperCase();
      this.year = year;
      this.location = location;
      this.courseNumber = courseNumber;
   }

   public List<CourseConflict> getConflicts(List<Course> schedule) {
      List<CourseConflict> conflicts = new ArrayList<>();

      for (Course course : schedule) {
         if (isTimeConflict(course)) {
            conflicts.add(new CourseConflict(this, course));
         }
      }

      return conflicts;
   }

   private boolean isTimeConflict(Course other) {
       // TODO: Figure out time format for DynamoDB to create and compare times
       return false;
   }

   public boolean equals(Course c) {
      return this.department.equals(c.department) &&
             this.section.equals(c.section) &&
             this.profFirstName.equals(c.profFirstName) &&
             this.profLastName.equals(c.profLastName) &&
             this.requirement.equals(c.requirement) &&
             this.type.equals(c.type) &&
             this.days.equals(c.days) &&
             this.start.equals(c.start) &&
             this.end.equals(c.end) &&
             this.quarter.equals(c.quarter) &&
             this.year.equals(c.year) &&
             this.location.equals(c.location) &&
             this.courseNumber.equals(c.courseNumber);
   }

   public Map toMap() {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("department",this.department);
      map.put("section",this.section);
      map.put("profFirstName",this.profFirstName);
      map.put("profLastName",this.profLastName);
      map.put("requirement",this.requirement);
      map.put("type",this.type);
      map.put("days",this.days);
      map.put("start",this.start);
      map.put("end",this.end);
      map.put("quarter",this.quarter);
      map.put("year",this.year);
      map.put("location",this.location);
      map.put("courseNumber",this.courseNumber);
      return map;
   }

   @Override
   public String toString() {
      return "Course{" +
              "department='" + department + '\'' +
              ", courseNumber=" + courseNumber +
              ", section=" + section +
              ", profFirstName='" + profFirstName + '\'' +
              ", profLastName='" + profLastName + '\'' +
              ", requirement='" + requirement + '\'' +
              ", type='" + type + '\'' +
              ", days='" + days + '\'' +
              ", start='" + start + '\'' +
              ", end='" + end + '\'' +
              ", quarter='" + quarter + '\'' +
              ", year=" + year +
              ", location='" + location + '\'' +
              '}';
   }
}
