/**
* Query class to provide a Query Object for the read API
*
* @author  Joey Wilson
*/

public class Query {

   // operation values 
   public static int EQUAL = 0;
   public static int LESS_THAN = 1;
   public static int GREATER_THAN = 2;
   public static int LESS_THAN_OR_EQUAL = 3;
   public static int GREATER_THAN_OR_EQUAL = 4;
 
   // logic values 
   public static int AND = 0;
   public static int OR = 1;
   
   public String key;
   public Object value;
   public int operation;
   public int logic;
   
   public Query() {}
   public Query(String key, Object value, int operation, int logic) {
      this.key = key;
      this.value = value;
      this.operation = operation;
      this.logic = logic;
   }
}
