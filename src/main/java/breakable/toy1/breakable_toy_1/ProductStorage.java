package breakable.toy1.breakable_toy_1;

import java.util.ArrayList;

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

    public void saveProduct(Product product) throws Exception{
        if(product == null){
            throw new Exception("Invalid Product");
        } else {
            productStorage.add(product);
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
}
