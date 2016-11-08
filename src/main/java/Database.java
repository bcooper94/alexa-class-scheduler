/**
* Database class to provide CRUD functionality to the database
*
* @author  Joey Wilson
*/

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

// Create
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;

// Insert
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

// Read
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;

public class Database {

   AmazonDynamoDBClient client; 
   DynamoDB dynamoDB;
   int id;

   public Database () {
      client = new AmazonDynamoDBClient();
      client.withEndpoint("http://localhost:8000");
      dynamoDB = new DynamoDB(client);
      try {
         Table table = dynamoDB.createTable("CoursesTest", 
            Arrays.asList(new KeySchemaElement("id", KeyType.HASH)),
            Arrays.asList(
               new AttributeDefinition("id", ScalarAttributeType.N)
            ),
            new ProvisionedThroughput(1L, 1L)
         );
         table.waitForActive();
         System.out.println("Success. Table status: " + table.getDescription().getTableStatus());
      } catch (ResourceInUseException ex) {
         System.out.println("Table already exists, proceeding ...");
      } catch (Exception ex) {
         System.err.println("Unable to create table: ");
         ex.printStackTrace();
      }
      id = 0;
   }

   public void create (List<Course> course) {
      course.forEach( (c) -> {
         this.create(c);
      });    
   }
   public void create (Course course) {
      Table table = dynamoDB.getTable("CoursesTest");
      Map<String, Object> infoMap = course.toMap(); 
      try {
         System.out.println("Adding a new item...");
         Item item = new Item();
         item = item.withPrimaryKey("id", id);
         for (String key : infoMap.keySet()) {
            item.with(key, infoMap.get(key));
         } 
         PutItemOutcome outcome = table.putItem(item);
         id++;
         System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
      } catch (Exception ex) {
         System.err.println("Unable to add item:");
         ex.printStackTrace();
      }
   }
 
   public List<Course> read (List<Query> queryList) {
      Table table = dynamoDB.getTable("CoursesTest");
      List<Course> courses = new ArrayList<Course>();
      if (queryList == null || queryList.size() == 0) {
         return courses;
      }
      try {
         System.out.println("Attempting to read the item...");
         
         // Query Variables
         StringBuilder expression = new StringBuilder();
         Map<String, String> nameMap = new <String, String>HashMap();
         Map<String, Object> valueMap = new <String, Object>HashMap();
         
         // Build the query
         for (Query query: queryList) {
            nameMap.put("#"+query.key, query.key);
            valueMap.put(":v_"+query.key, query.value);
            expression.append(String.format("#%s = :v_%s AND ", query.key, query.key));
         }
         
         // Remove trailing AND or OR
         String expressionString = expression.toString();
         int lastAnd = expressionString.lastIndexOf("AND");
         int lastOr = expressionString.lastIndexOf("OR");
         if (lastAnd > lastOr) {
            expressionString = expressionString.substring(0, lastAnd);
         } else if (lastOr > lastAnd) {
            expressionString = expressionString.substring(0, lastOr);
         }
         
         // Print the query
         System.out.println(expressionString); 
         System.out.println(nameMap); 
         System.out.println(valueMap); 
        
         // Run the query 
         table.scan(
            expressionString,
            nameMap,
            valueMap
         )
         .forEach((item) -> {
            courses.add(new Course(
               (String)item.get("department"),
               (String)item.get("courseNumber"),
               (String)item.get("section"),
               (String)item.get("profFirstName"),
               (String)item.get("profLastName"),
               (String)item.get("requirement"),
               (String)item.get("type"),
               (String)item.get("days"),
               (String)item.get("start"),
               (String)item.get("end"),
               (String)item.get("quarter"),
               (String)item.get("year"),
               (String)item.get("location")
            ));
         });
     } catch (Exception ex) {
         System.err.println("Unable to read item:");
         ex.printStackTrace();
      }
      return courses;
   } 
}
