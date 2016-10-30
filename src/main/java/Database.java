/**
* Database class to provide CRUD functionality to the database
*
* @author  Joey Wilson
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.List;
import java.util.ArrayList;

public class Database {
   
   private Connection connection;

   public Database() {
      try {
         connection = DriverManager.getConnection("jdbc:sqlite:app.db");
         Statement statement = connection.createStatement();
         statement.execute(
            "CREATE TABLE IF NOT EXISTS Courses (" +
            "   department TEXT," +
            "   course_number TEXT," +
            "   section INTEGER," +
            "   prof_first_name TEXT," +
            "   prof_last_name TEXT," +
            "   requirement TEXT," +
            "   type TEXT," +
            "   days TEXT," +
            "   start TEXT," +
            "   end TEXT," +
            "   quarter TEXT," +
            "   year INTEGER," +
            "   location TEXT," +
            "   PRIMARY KEY(department, course_number, section, quarter, year)" +
            ");");
          statement.close();
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   public void create(Course course) {
       try {
          PreparedStatement preparedStatement = connection.prepareStatement(
             "INSERT INTO Courses (department, course_number, section, prof_first_name, prof_last_name, requirement, type, days, start, end, quarter, year, location) " +
             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
          );
          preparedStatement.setString(1, course.department); 
          preparedStatement.setInt(2, course.courseNumber);
          preparedStatement.setInt(3, course.section);
          preparedStatement.setString(4, course.profFirstName);
          preparedStatement.setString(5, course.profLastName);
          preparedStatement.setString(6, course.requirement);
          preparedStatement.setString(7, course.type);
          preparedStatement.setString(8, course.days);
          preparedStatement.setString(9, course.start);
          preparedStatement.setString(10, course.end);
          preparedStatement.setString(11, course.quarter);
          preparedStatement.setInt(12, course.year);
          preparedStatement.setString(13, course.location);
          preparedStatement.execute();
          preparedStatement.close();
       } catch (Exception ex) {
          ex.printStackTrace();
       }
   }

   public List<Course> read() {
      List<Course> courseList = new ArrayList<Course>();
      try {
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery("SELECT * FROM Courses;");
         while(resultSet.next()) {
            courseList.add(new Course(
               resultSet.getString("department"),
               resultSet.getInt("course_number"),
               resultSet.getInt("section"),
               resultSet.getString("prof_first_name"),
               resultSet.getString("prof_last_name"),
               resultSet.getString("requirement"),
               resultSet.getString("type"),
               resultSet.getString("days"),
               resultSet.getString("start"),
               resultSet.getString("end"),
               resultSet.getString("quarter"),
               resultSet.getInt("year"),
               resultSet.getString("location"))
            );
         }
         statement.close(); 
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      return courseList;
   }

   /*public List<Course> read(Course course) {
      List<Course> courseList = new ArrayList<Course>();
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Courses WHERE name = ?");
         preparedStatement.setString(1, course.name);
         ResultSet resultSet = preparedStatement.executeQuery();         
         while(resultSet.next()) {
            courseList.add(new Course(
               resultSet.getString("name"),
               resultSet.getInt("section"),
               resultSet.getString("professor"),
               resultSet.getString("type"),
               resultSet.getString("days"),
               resultSet.getString("start"),
               resultSet.getString("end"),
               resultSet.getString("location"))
            );
         }
         preparedStatement.close(); 
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      return courseList; 
   }*/

   public void update(Course oldCourse, Course newCourse) {
      // TODO
   }

   public void delete(Course course) {
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Courses WHERE department = ? AND course_number = ? AND section = ?");
         preparedStatement.setString(1, course.department);
         preparedStatement.setInt(2, course.courseNumber);
         preparedStatement.setInt(3, course.section);
         preparedStatement.execute();
         preparedStatement.close(); 
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }
}