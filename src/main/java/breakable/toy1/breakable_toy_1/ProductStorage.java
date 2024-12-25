package breakable.toy1.breakable_toy_1;

import java.util.ArrayList;
//import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class ProductStorage {
    public ArrayList<Product> productStorage = new ArrayList<>();

    private int getProductPositionById(Long id){
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
        int position = getProductPositionById(id);
        if(position > -1){
            return this.productStorage.get(position);
        }
        return null;
    }

    public void deleteProduct(Long id) throws Exception {
        int position = getProductPositionById(id);
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

        int position = getProductPositionById(id);
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

    public void outOfStock(Long id) throws Exception {
        Product product = getProductById(id);
        if (product == null) {
            throw new Exception("Invalid id");
        } else {
            product.setQuantityInStock(0l);
        }
    }

    public void inStock(Long id) throws Exception {
        Product product = getProductById(id);
        if(product == null){
            throw new Exception("Invalid id");
        } else {
            product.setQuantityInStock(10l);
        }
    }
}
