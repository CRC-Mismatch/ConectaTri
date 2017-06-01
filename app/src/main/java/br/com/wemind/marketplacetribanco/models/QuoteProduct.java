package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kmkraiker on 25/05/2017.
 */

public class QuoteProduct implements Parcelable {
    public static final Creator<QuoteProduct> CREATOR = new Creator<QuoteProduct>() {
        @Override
        public QuoteProduct createFromParcel(Parcel in) {
            return new QuoteProduct(in);
        }

        @Override
        public QuoteProduct[] newArray(int size) {
            return new QuoteProduct[size];
        }
    };

    public QuoteProduct() {
        suppliers = new ArrayList<>();
    }

    private QuoteProduct(Parcel in) {
        this.id = in.readLong();
        this.product = in.readParcelable(Product.class.getClassLoader());
        this.suppliers = in.createTypedArrayList(QuoteSupplier.CREATOR);
    }

    private long id;
    private Product product;
    private List<QuoteSupplier> suppliers;

    public long getId() {
        return id;
    }

    public QuoteProduct setId(long id) {
        this.id = id;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public QuoteProduct setProduct(Product product) {
        this.product = product;
        return this;
    }

    public List<QuoteSupplier> getSuppliers() {
        return suppliers;
    }

    public QuoteProduct setSuppliers(List<QuoteSupplier> suppliers) {
        this.suppliers = suppliers;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(product, 0);
        dest.writeTypedList(suppliers);
    }
}
