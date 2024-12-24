package breakable.toy1.breakable_toy_1;

import java.time.LocalDate;

public class Product {
    private Long id = 0l;
    private static Long idCounter = 0l;
    private String name;
    private String category;
    private Double unitPrice;
    private LocalDate expirationDate;
    private Long quantityInStock;
    private LocalDate creationDate;
    private LocalDate updateDate;

    Product(String name, String category, Double unitPrice, LocalDate expirationDate, Long quantityInStock, LocalDate creationDate, LocalDate updateDate) throws Exception{
        this.id = assignId();
    
        if(name == null || name.length() <= 0 || name.length() > 120){
            throw new Exception("Invalid name");
        } else {
            this.name = name;
        }

        if(category == null || category.length() <= 0){
            throw new Exception("Invalid category");
        } else {
            this.category = category;
        }

        if(unitPrice == null || unitPrice <= 0.0){
            throw new Exception("Invalid Unit Price");
        } else {
            this.unitPrice = unitPrice;
        }

        this.expirationDate = expirationDate;

        if(quantityInStock == null || quantityInStock <= 0l) {
            throw new Exception("Invalid Quantity In Stock");
        } else {
            this.quantityInStock = quantityInStock;
        }
        
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    private Long assignId(){ return ++idCounter; }

    public Long getId(){ return this.id; }

    public void setId(Long id){ this.id = id; }

    public String getName(){ return this.name; }

    public void setName(String newName){ this.name = newName; }

    public String getCategory(){ return this.category; }

    public void setCategory(String newCategory){ this.category = newCategory; }

    public Double getUnitPrice(){ return this.unitPrice; }

    public void setUnitPrice(Double newUnitPrice){ this.unitPrice = newUnitPrice; }

    public LocalDate getExpirationDate(){ return this.expirationDate; }

    public void setExpirationDate(LocalDate newExpirationDate){ this.expirationDate = newExpirationDate; }

    public long getQuantityInStock(){ return this.quantityInStock; }

    public void setQuantityInStock(long newQuantityInStock){ this.quantityInStock = newQuantityInStock; }

    public LocalDate getCreationDate(){ return this.creationDate; }

    public LocalDate getUpdateDate(){ return this.updateDate; }

    public void setUpdateDate(LocalDate newUpdateDate){ this.updateDate = newUpdateDate; }

    public static void resetidCounter() {
        idCounter = 0l;
    }
}
