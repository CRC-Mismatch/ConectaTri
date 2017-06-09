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
    private long id;
    private String name;
    private int type;
    private List<Supplier> suppliers;
    private List<QuoteProduct> quoteProducts;

    public Quote() {
    }
    public Quote(long id, String name, int type, List<Product> products, List<Supplier> suppliers) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.suppliers = suppliers;
        this.quoteProducts = new ArrayList<>();

        // FIXME: Remove from final version
        for (Product product : products) {
            QuoteProduct qP = new QuoteProduct().setProduct(product);
            for (Supplier supplier : suppliers) {
                QuoteSupplier qS = new QuoteSupplier().setProduct(product).setSupplier(supplier).setPrice(Math.round(Math.random() * 100000) / 100.0).setQuantity((int) Math.round(Math.random() * 100));
                qP.getSuppliers().add(qS);
            }
            quoteProducts.add(qP);
        }
    }
    private Quote(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.type = in.readInt();
        this.suppliers = in.createTypedArrayList(Supplier.CREATOR);
        this.quoteProducts = in.createTypedArrayList(QuoteProduct.CREATOR);
    }

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

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public Quote setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
        return this;
    }

    public List<QuoteProduct> getQuoteProducts() {
        return quoteProducts;
    }

    public Quote setQuoteProducts(List<QuoteProduct> quoteProducts) {
        this.quoteProducts = quoteProducts;
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
        dest.writeTypedList(suppliers);
        dest.writeTypedList(quoteProducts);
    }
}
