package dal;

/**
 * Keys for Courses in DynamoDB.
 * @author Brandon Cooper
 */
public enum QueryKey {
    DEPARTMENT("department"),
    COURSE_NUM("courseNumber"),
    SECTION("section"),
    FIRST_NAME("profFirstName"),
    LAST_NAME("profLastName"),
    REQUIREMENT("requirement"),
    TYPE("type"),
    DAYS("days"),
    START("start"),
    END("end"),
    QUARTER("quarter"),
    YEAR("year"),
    LOCATION("location"),
    COLLEGE("college"),
    TIME_RANGE("timeRange");

    private String key;

    QueryKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
