package dal;

/**
* Query class to provide a dal.Query Object for the read API
*
* @author  Joey Wilson
*/

public class Query {
   // paranthese values
   public static final String OPEN = "(";
   public static final String CLOSE = ")";
   
   public QueryKey key;
   public Object value;
   public QueryOperation operation;
   public QueryLogic logic;
   public String paren;
   
   public Query() {}

   public Query(QueryKey key, Object value, QueryOperation operation) {
      if (key.equals(QueryKey.SECTION)) {
         String section = (String) value;
         if (Integer.parseInt(section) < 10 && !section.startsWith("0")) {
            value = "0" + section;
         }
      }
      else if (key.equals(QueryKey.COURSE_NUM)) {
         String courseNum = (String) value;
         if (courseNum.startsWith("0")) {
            value = courseNum.substring(1);
            System.out.println(String.format("Changed %s to %s", courseNum, value));
         }
      }
      this.key = key;
      this.value = value;
      this.operation = operation;
      this.logic = QueryLogic.AND;
   }

   public Query(QueryKey key, Object value, QueryOperation operation, QueryLogic logic) {
      this(key, value, operation);
      this.logic = logic;
   }

   public Query(QueryKey key, Object value, QueryOperation operation, QueryLogic logic, String paren) {
      this(key, value, operation, logic);
      this.paren = paren;
   }
}
