import java.util.HashMap;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;

public class ProductInventory {
    HashMap<Barcode, ItemProductPair> inventory;
    public ProductInventory() {
        inventory = new HashMap<Barcode, ItemProductPair>();
    }

    public void addInventory (Barcode a, BarcodedItem b, BarcodedProduct c) {
        this.inventory.put(a, new ItemProductPair(b, c));
    }

    public ItemProductPair getInventory (Barcode a) {
        return this.inventory.get(a);
    }
}