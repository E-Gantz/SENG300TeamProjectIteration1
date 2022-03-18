import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.products.BarcodedProduct;

public class ItemProductPair {
    BarcodedItem item;
    BarcodedProduct product;
    public ItemProductPair (BarcodedItem a, BarcodedProduct b) {
        this.item = a;
        this.product = b;
    }

    public BarcodedItem getItem() {
        return this.item;
    }

    public BarcodedProduct getProduct() {
        return this.product;
    }
}