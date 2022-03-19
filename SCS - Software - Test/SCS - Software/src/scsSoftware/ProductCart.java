package scsSoftware;
import java.math.BigDecimal;
import java.util.*;

public class ProductCart {
	public ArrayList<ItemProductPair> cart;
	public ArrayList<String> items;
	public BigDecimal totalPrice;
    public ProductCart() {
        cart = new ArrayList<ItemProductPair>();
        items = new ArrayList<String>();
        totalPrice = new BigDecimal(0);
    }

    public void addToCart (ItemProductPair newItem) {
    	cart.add(newItem);
    	items.add(newItem.getDescription());
    	totalPrice = totalPrice.add(newItem.getPrice());
    }
    
    public void removeFromCart(ItemProductPair removeMe) {
    	cart.remove(removeMe);
    	items.remove(removeMe.getDescription());
    	totalPrice = totalPrice.subtract(removeMe.getPrice());
    }
    
    public BigDecimal getTotalPrice() {
    	return this.totalPrice;
    }
    
    public ArrayList<String> getItemNames(){
    	return this.items;
    }
}
