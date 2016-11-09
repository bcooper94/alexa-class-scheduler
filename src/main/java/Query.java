/**
* Query class to provide a Query Object for the read API
*
* @author  Joey Wilson
*/

public class Query {

   // operation values 
   public static String EQUAL = "=";
   public static String LESS_THAN = "<";
   public static String GREATER_THAN = ">";
   public static String LESS_THAN_OR_EQUAL = "<=";
   public static String GREATER_THAN_OR_EQUAL = ">=";
 
   // logic values 
   public static String AND = "AND";
   public static String OR = "OR";

   // paranthese values
   public static String OPEN = "(";
   public static String CLOSE = ")";
   
   public String key;
   public Object value;
   public String operation;
   public String logic;
   public String paren;
   
   public Query() {}
   public Query(String key, Object value, String operation, String logic, String paren) {
      this.key = key;
      this.value = value;
      this.operation = operation;
      this.logic = logic;
      this.paren = paren;
   }
}
