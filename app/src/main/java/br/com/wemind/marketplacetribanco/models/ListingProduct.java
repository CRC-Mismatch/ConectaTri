package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class ListingProduct implements Parcelable, Comparable {
    public static final Creator<ListingProduct> CREATOR = new Creator<ListingProduct>() {
        @Override
        public ListingProduct createFromParcel(Parcel in) {
            return new ListingProduct(in);
        }

        @Override
        public ListingProduct[] newArray(int size) {
            return new ListingProduct[size];
        }
    };
    private long id;
    private Product product;
    private int quantity;

    public ListingProduct(long id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    protected ListingProduct(Parcel in) {
        id = in.readLong();
        product = in.readParcelable(Product.class.getClassLoader());
        quantity = in.readInt();
    }

    public long getId() {
        return id;
    }

    public ListingProduct setId(long id) {
        this.id = id;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public ListingProduct setProduct(Product product) {
        this.product = product;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ListingProduct setQuantity(int quantity) {
        this.quantity = quantity;
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
        dest.writeInt(quantity);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ListingProduct && ((ListingProduct)obj).id == id;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof ListingProduct) {
            return (int) (id - ((ListingProduct)o).id);
        } else {
            throw new ClassCastException();
        }
    }
}
