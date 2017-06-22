package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

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
    @SerializedName("id")
    private long id;
    @SerializedName("product")
    private Product product;
    @SerializedName("quote_suppliers")
    private List<QuoteSupplier> suppliers;

    public QuoteProduct() {
        suppliers = new ArrayList<>();
    }

    private QuoteProduct(Parcel in) {
        this.id = in.readLong();
        this.product = in.readParcelable(Product.class.getClassLoader());
        this.suppliers = in.createTypedArrayList(QuoteSupplier.CREATOR);
    }

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

    public List<QuoteSupplier> getQuoteSuppliers() {
        return suppliers;
    }

    public QuoteProduct setQuoteSuppliers(List<QuoteSupplier> suppliers) {
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QuoteProduct) {
            return ((QuoteProduct) obj).getId() == this.getId();

        }
        return super.equals(obj);
    }
}
