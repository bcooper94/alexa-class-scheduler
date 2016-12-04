import dal.Query;
import dal.QueryKey;
import dal.QueryOperation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by brandon on 12/3/16.
 */
public class QueryTest {
    @Test
    public void testSectionQuery() {
        Query q1 = new Query(QueryKey.SECTION, "1", QueryOperation.EQUAL),
                q2 = new Query(QueryKey.SECTION, "02", QueryOperation.EQUAL),
                q3 = new Query(QueryKey.SECTION, "3", QueryOperation.EQUAL),
                q4 = new Query(QueryKey.SECTION, "4", QueryOperation.EQUAL),
                q5 = new Query(QueryKey.SECTION, "05", QueryOperation.EQUAL),
                q6 = new Query(QueryKey.SECTION, "6", QueryOperation.EQUAL),
                q7 = new Query(QueryKey.SECTION, "07", QueryOperation.EQUAL),
                q8 = new Query(QueryKey.SECTION, "8", QueryOperation.EQUAL),
                q9 = new Query(QueryKey.SECTION, "9", QueryOperation.EQUAL),
                q10 = new Query(QueryKey.SECTION, "10", QueryOperation.EQUAL);
        assertEquals("Pads section with 0", "01", q1.value);
        assertEquals("Doesn't pad already padded section with 0", "02", q2.value);
        assertEquals("Pads section with 0", "03", q3.value);
        assertEquals("Pads section with 0", "04", q4.value);
        assertEquals("Doesn't pad already padded section with 0", "05", q5.value);
        assertEquals("Pads section with 0", "06", q6.value);
        assertEquals("Doesn't pad already padded section with 0", "07", q7.value);
        assertEquals("Pads section with 0", "08", q8.value);
        assertEquals("Pads section with 0", "09", q9.value);
        assertEquals("Doesn't pad sections above 9 with 0", "10", q10.value);
    }

    @Test
    public void testCourseNumQuery() {
        Query q1 = new Query(QueryKey.COURSE_NUM, "0101", QueryOperation.EQUAL),
                q2 = new Query(QueryKey.COURSE_NUM, "101", QueryOperation.EQUAL),
                q3 = new Query(QueryKey.COURSE_NUM, "0305", QueryOperation.EQUAL),
                q4 = new Query(QueryKey.COURSE_NUM, "357", QueryOperation.EQUAL);
        assertEquals("101", q1.value);
        assertEquals("101", q2.value);
        assertEquals("305", q3.value);
        assertEquals("357", q4.value);
    }
}
