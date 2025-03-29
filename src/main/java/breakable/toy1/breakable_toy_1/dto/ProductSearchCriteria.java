package breakable.toy1.breakable_toy_1.dto;

import java.util.ArrayList;

import lombok.Data;

@Data
public class ProductSearchCriteria {
    private String name;
    private ArrayList<String> category;
    private String availability;
    private String sortBy;
    private String sortOrder;
    private String secondarySortBy;
    private String secondarySortOrder;
}