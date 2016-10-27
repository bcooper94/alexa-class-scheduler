/**
* Course POJO to hold the database information
*
* @author  Joey Wilson
*/
public class Course {
   public String name;   
   public int section;
   public String professor;
   public String type;
   public String days;
   public String start;
   public String end;
   public String location;
   
   public Course() {
 
   }

   public Course(String name, int section, String professor, String type, String days, String start, String end, String location) {
      this.name = name;
      this.section = section;
      this.professor = professor;
      this.type = type;
      this.days = days;
      this.start = start;
      this.end = end;
      this.location = location;
   }
}
