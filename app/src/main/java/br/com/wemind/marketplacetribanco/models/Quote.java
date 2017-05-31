package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kmkraiker on 25/05/2017.
 */

public class Quote implements Parcelable {
    public static final int TYPE_REMOTE = 1;
    public static final int TYPE_MANUAL = 2;
    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    public Quote() {}

    public Quote(long id, String name, int type, List<Product> products, List<Supplier> suppliers) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.products = products;
        this.suppliers = suppliers;
        this.quoteSuppliers = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            QuoteSupplier qS = new QuoteSupplier().setId(supplier.getId());
            for (Product product : products) {
                QuoteProduct qP = new QuoteProduct().setId(product.getId()).setPrice(Math.round(Math.random() * 100000) / 100.0);
                qS.getProducts().add(qP);
            }
            quoteSuppliers.add(qS);
        }
    }

    private Quote(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.type = in.readInt();
        this.products = in.createTypedArrayList(Product.CREATOR);
        this.suppliers = in.createTypedArrayList(Supplier.CREATOR);
        this.quoteSuppliers = in.createTypedArrayList(QuoteSupplier.CREATOR);
    }

    private long id;
    private String name;
    private int type;
    private List<Product> products;
    private List<Supplier> suppliers;
    private List<QuoteSupplier> quoteSuppliers;

    public long getId() {
        return id;
    }

    public Quote setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Quote setName(String name) {
        this.name = name;
        return this;
    }

    public int getType() {
        return type;
    }

    public Quote setType(int type) {
        this.type = type;
        return this;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Quote setProducts(List<Product> products) {
        this.products = products;
        return this;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public Quote setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
        return this;
    }

    public List<QuoteSupplier> getQuoteSuppliers() {
        return quoteSuppliers;
    }

    public Quote setQuoteSuppliers(List<QuoteSupplier> quoteSuppliers) {
        this.quoteSuppliers = quoteSuppliers;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeTypedList(products);
        dest.writeTypedList(suppliers);
        dest.writeTypedList(quoteSuppliers);
    }
}
