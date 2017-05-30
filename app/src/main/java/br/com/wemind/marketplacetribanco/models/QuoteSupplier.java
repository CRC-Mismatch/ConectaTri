package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kmkraiker on 25/05/2017.
 */

public class QuoteSupplier implements Parcelable {
    public static final Creator<QuoteSupplier> CREATOR = new Creator<QuoteSupplier>() {
        @Override
        public QuoteSupplier createFromParcel(Parcel in) {
            return new QuoteSupplier(in);
        }

        @Override
        public QuoteSupplier[] newArray(int size) {
            return new QuoteSupplier[size];
        }
    };

    public QuoteSupplier() {
        this.products = new ArrayList<>();
    }

    private QuoteSupplier(Parcel in) {
        this.id = in.readLong();
        this.products = in.createTypedArrayList(QuoteProduct.CREATOR);
    }

    private long id;
    private List<QuoteProduct> products;

    public long getId() {
        return id;
    }

    public QuoteSupplier setId(long id) {
        this.id = id;
        return this;
    }

    public List<QuoteProduct> getProducts() {
        return products;
    }

    public QuoteSupplier setProducts(List<QuoteProduct> products) {
        this.products = products;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeTypedList(products);
    }
}
