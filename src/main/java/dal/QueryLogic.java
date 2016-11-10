package dal;

/**
 * Created by brandon on 11/8/16.
 */
public enum QueryLogic {
    AND("AND"),
    OR("OR");

    private String logic;

    QueryLogic(String logic) {
        this.logic = logic;
    }

    public String getLogic() {
        return this.logic;
    }
}
