package breakable.toy1.breakable_toy_1;

import java.util.ArrayList;
//import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class ProductStorage {
    public ArrayList<Product> productStorage = new ArrayList<>();

    private int obtainProductPositionById(Long id){
        for(Product product : this.productStorage){
            if(product.getId() == id){
                return this.productStorage.indexOf(product);
            }
        }
        return -1;
    }

    public Product saveProduct(Product product) throws Exception{
        if(product == null){
            throw new Exception("Invalid Product");
        } else {
            productStorage.add(product);
            return product;
        }
    }

    public int getSize() {
        return this.productStorage.size();
    }

    public Product getProductById(Long id) {
        int position = obtainProductPositionById(id);
        if(position > -1){
            return this.productStorage.get(position);
        }
        return null;
    }

    public void deleteProduct(Long id) throws Exception {
        int position = obtainProductPositionById(id);
        if(position > -1){
            this.productStorage.remove(position);
        } else {
            throw new Exception("Invalid id");
        }
    }

    public void updateProductById(Product product, Long id) throws Exception {
        if(id == null){
            throw new Exception("Invalid id");
        }
        if(product == null){
            throw new Exception("Invalid Product");
        }

        int position = obtainProductPositionById(id);
        if(position > -1){
            this.productStorage.set(position, product);
        } else {
            throw new Exception("Invalid id");
        }
    }

    public List<Product> getAll(){
        return new ArrayList<>(productStorage);
    }

    public void clear(){
        Product.resetidCounter();
        productStorage.clear();
    }

/*  public Iterator<Product> Iterator() { return this.productStorage.iterator(); }

    public void sortByName() { this.productStorage.sort(ProductComparator.BY_NAME); }

    public void sortByNameReverse() { this.productStorage.sort(ProductComparator.BY_NAME_DESC); }

    public void sortByCategory() { this.productStorage.sort(ProductComparator.BY_CATEGORY); }

    public void sortByCategoryReverse() { this.productStorage.sort(ProductComparator.BY_CATEGORY_DESC); }

    public void sortByPrice() { this.productStorage.sort(ProductComparator.BY_PRICE); }

    public void sortByPriceReverse() { this.productStorage.sort(ProductComparator.BY_PRICE_DESC); }

    public void sortByStock() { this.productStorage.sort(ProductComparator.BY_STOCK); }

    public void sortByStockReverse() { this.productStorage.sort(ProductComparator.BY_STOCK_DESC); }

    public void sortByExpirationDate() { this.productStorage.sort(ProductComparator.BY_EXPIRATION_DATE); }

    public void sortByExpirationDateReverse() { this.productStorage.sort(ProductComparator.BY_EXPIRATION_DATE_DESC); }

    public void sortByNameThenByCategory() { this.productStorage.sort(ProductComparator.BY_NAME_THEN_CATEGORY); }

    public void sortByNameReverseThenByCategory() { this.productStorage.sort(ProductComparator.BY_NAME_DESC_THEN_CATEGORY); }

    public void sortByNameThenByCategoryReverse() { this.productStorage.sort(ProductComparator.BY_NAME_THEN_CATEGORY_DESC); }

    public void sortByNameReverseThenByCategoryReverse() { this.productStorage.sort(ProductComparator.BY_NAME_DESC_THEN_CATEGORY_DESC); }

    public void sortByNameThenByPrice() { this.productStorage.sort(ProductComparator.BY_NAME_THEN_PRICE); }

    public void sortByNameReverseThenByPrice() { this.productStorage.sort(ProductComparator.BY_NAME_DESC_THEN_PRICE); }

    public void sortByNameThenByPriceReverse() { this.productStorage.sort(ProductComparator.BY_NAME_THEN_PRICE_DESC); }

    public void sortByNameReverseThenByPriceReverse() { this.productStorage.sort(ProductComparator.BY_NAME_DESC_THEN_PRICE_DESC); }

    public void sortByNameThenStock() { this.productStorage.sort(ProductComparator.BY_NAME_THEN_STOCK); }

    public void sortByNameReverseThenStock() { this.productStorage.sort(ProductComparator.BY_NAME_DESC_THEN_STOCK); }

    public void sortByNameThenStockReverse() { this.productStorage.sort(ProductComparator.BY_NAME_THEN_STOCK_DESC); }

    public void sortByNameReverseThenStockReverse() { this.productStorage.sort(ProductComparator.BY_NAME_DESC_THEN_STOK_DESC); }

    public void sortByNameThenExpiration() { this.productStorage.sort(ProductComparator.BY_NAME_THEN_EXPIRATION); }

    public void sortByNameReverseThenExpiration() { this.productStorage.sort(ProductComparator.BY_NAME_DESC_THEN_EXPIRATION); }

    public void sortByNameThenExpirationReverse() { this.productStorage.sort(ProductComparator.BY_NAME_THEN_EXPIRATION_DESC); }

    public void sortByNameReverseThenExpirationReverse() { this.productStorage.sort(ProductComparator.BY_NAME_DESC_THEN_EXPIRATION_DESC); }

    public void sortByCategoryThenName() { this.productStorage.sort(ProductComparator.BY_CATEGORY_THEN_NAME); }

    public void sortByCategoryReverseThenName() { this.productStorage.sort(ProductComparator.BY_CATEGORY_DESC_THEN_NAME); }

    public void sortByCategoryThenNameReverse() { this.productStorage.sort(ProductComparator.BY_CATEGORY_THEN_NAME_DESC); }

    public void sortByCategoryReverseThenNameReverse() { this.productStorage.sort(ProductComparator.BY_CATEGORY_DESC_THEN_NAME_DESC); }

    public void sortByCategoryThenPrice() { this.productStorage.sort(ProductComparator.BY_CATEGORY_THEN_PRICE); }

    public void sortByCategoryReverseThenPrice() { this.productStorage.sort(ProductComparator.BY_CATEGORY_DESC_THEN_PRICE); }

    public void sortByCategoryReverseThenPriceReverse() { this.productStorage.sort(ProductComparator.BY_CATEGORY_DESC_THEN_PRICE_DESC); }

    public void sortByCategoryThenStock() { this.productStorage.sort(ProductComparator.BY_CATEGORY_THEN_STOCK); }

    public void sortByCategoryReverseThenStock() { this.productStorage.sort(ProductComparator.BY_CATEGORY_DESC_THEN_STOCK); }

    public void sortByCategoryThenStockReverse() { this.productStorage.sort(ProductComparator.BY_CATEGORY_THEN_STOCK_DESC); }

    public void sortByCategoryReverseThenStockReverse() { this.productStorage.sort(ProductComparator.BY_CATEGORY_DESC_THEN_STOCK_DESC); } */
}
