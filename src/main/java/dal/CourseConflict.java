package dal;

/**
 * Created by brandon on 11/8/16.
 */
public class CourseConflict {
    private Course firstCourse;
    private Course secondCourse;

    public CourseConflict(Course firstCourse, Course secondCourse) {
        this.firstCourse = firstCourse;
        this.secondCourse = secondCourse;
    }
}
