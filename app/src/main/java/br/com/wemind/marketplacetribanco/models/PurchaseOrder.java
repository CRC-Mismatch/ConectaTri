package br.com.wemind.marketplacetribanco.models;

import java.util.Map;
import java.util.TreeMap;

public class PurchaseOrder {
    private long id;
    private Supplier supplier;
    private Map<Product, Price> items = new TreeMap<>();

    public long getId() {
        return id;
    }

    public PurchaseOrder setId(long id) {
        this.id = id;
        return this;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public PurchaseOrder setSupplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public Map<Product, Price> getItems() {
        return items;
    }

    public PurchaseOrder setItems(Map<Product, Price> items) {
        this.items = items;
        return this;
    }

    public PurchaseOrder addItem(Product product, Price price) {
        items.put(product, price);
        return this;
    }

    public double getPriceSum() {
        double sum = 0.;
        for (Price price : getItems().values()) {
            sum += price.getUnitPrice() * price.getQuantity();
        }
        return sum;
    }

    public int getQuantitySum() {
        int sum = 0;
        for (Price price : getItems().values()) {
            sum += price.getQuantity();
        }
        return sum;
    }
}
