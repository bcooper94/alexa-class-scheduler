package dal;

/**
 * Created by brandon on 11/8/16.
 */
public enum QueryOperation {
    EQUAL("="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    LESS_THAN_OR_EQUAL("<="),
    GREATER_THAN_OR_EQUAL(">=");

    private String comparison;

    QueryOperation(String comparison) {
        this.comparison = comparison;
    }

    public String getComparison() {
        return this.comparison;
    }
}
