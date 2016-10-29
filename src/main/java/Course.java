/**
* Course POJO to hold the database information
*
* @author  Joey Wilson
*/
public class Course {
   public String department;   
   public int courseNumber;
   public int section;
   public String profFirstName;
   public String profLastName;
   public String requirement;
   public String type;
   public String days;
   public String start;
   public String end;
   public String quarter;
   public int year;
   public String location;
   
   public Course() {
 
   }

   public Course(String department, int courseNumber, int section, 
                 String profFirstName, String profLastName, 
                 String requirement, String type, String days, 
                 String start, String end, String quarter, 
                 int year, String location) {
      this.department = department;
      this.section = section;
      this.profFirstName = profFirstName;
      this.profLastName = profLastName;
      this.requirement = requirement;
      this.type = type;
      this.days = days;
      this.start = start;
      this.end = end;
      this.quarter = quarter;
      this.year = year;
      this.location = location;
      this.courseNumber = courseNumber;
   }
}
