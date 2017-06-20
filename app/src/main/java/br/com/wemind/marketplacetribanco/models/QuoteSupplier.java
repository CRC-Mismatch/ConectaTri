package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

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
    @SerializedName("id")
    private long id;
    @SerializedName("representative")
    private Supplier supplier;
    @SerializedName("price")
    private String price = "0,00";
    @SerializedName("quantity")
    private int quantity;

    public QuoteSupplier() {
    }

    private QuoteSupplier(Parcel in) {
        this.id = in.readLong();
        this.supplier = in.readParcelable(Supplier.class.getClassLoader());
        this.price = in.readString();
        this.quantity = in.readInt();
    }

    public long getId() {
        return id;
    }

    public QuoteSupplier setId(long id) {
        this.id = id;
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
        return Double.valueOf(price.replaceAll(",", "."));
    }

    public QuoteSupplier setPrice(double price) {
        this.price = String.valueOf(price);
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
        dest.writeParcelable(supplier, 0);
        dest.writeString(price);
        dest.writeInt(quantity);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        QuoteSupplier that = (QuoteSupplier) o;
        if (this.quantity == 0 || this.price == null) return -1;
        return (int) Math.round(Double.valueOf(this.price) - Double.valueOf(that.price));
    }
}
