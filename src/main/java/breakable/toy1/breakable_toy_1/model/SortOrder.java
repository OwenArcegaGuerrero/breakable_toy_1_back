package breakable.toy1.breakable_toy_1.model;

/**
 * Enum to represent sort direction
 */
public enum SortOrder {
    ASC,
    DESC;

    public static SortOrder fromString(String value) {
        if (value == null) {
            return ASC;
        }
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ASC;
        }
    }
}