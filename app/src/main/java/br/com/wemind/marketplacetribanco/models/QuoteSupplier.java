package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by kmkraiker on 25/05/2017.
 */

public class QuoteSupplier implements Parcelable, Comparable {
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
    private long id;
    private Product product;
    private Supplier supplier;
    private double price;
    private int quantity;

    public QuoteSupplier() {
    }
    private QuoteSupplier(Parcel in) {
        this.id = in.readLong();
        this.product = in.readParcelable(Product.class.getClassLoader());
        this.supplier = in.readParcelable(Supplier.class.getClassLoader());
        this.price = in.readDouble();
        this.quantity = in.readInt();
    }

    public long getId() {
        return id;
    }

    public QuoteSupplier setId(long id) {
        this.id = id;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public QuoteSupplier setProduct(Product product) {
        this.product = product;
        return this;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public QuoteSupplier setSupplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public QuoteSupplier setPrice(double price) {
        this.price = price;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public QuoteSupplier setQuantity(int quantity) {
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
        dest.writeParcelable(supplier, 0);
        dest.writeDouble(price);
        dest.writeInt(quantity);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        QuoteSupplier that = (QuoteSupplier) o;
        if (this.quantity == 0 || this.price == 0) return -1;
        return (int) Math.round(this.price - that.price);
    }
}
