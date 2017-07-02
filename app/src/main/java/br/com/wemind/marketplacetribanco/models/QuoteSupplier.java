package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by kmkraiker on 25/05/2017.
 */

public class QuoteSupplier implements Parcelable, Comparable<QuoteSupplier> {
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
    private String price = "0.00";
    @SerializedName("quantity")
    private int quantity;

    private NumberFormat brDoubleFormat =
            NumberFormat.getNumberInstance(new Locale("pt", "BR"));

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

    public String getPrice() {
        return price;
    }

    public QuoteSupplier setPrice(String price) {
        this.price = price;
        return this;
    }

    public double getPriceDouble() {
        try {
            return brDoubleFormat.parse(price).doubleValue();

        } catch (ParseException e) {
            Log.w("QUOTE_SUPP", "String to Double parsing failed, using fallback case ", e);

            try {
                return Double.valueOf(price);

            } catch (NumberFormatException e1) {
                Log.w("QUOTE_SUPP", "Fallback failed, returning default value", e1);
                return 0.;
            }
        }
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
    public int compareTo(@NonNull QuoteSupplier that) {
        if (this.quantity == 0 || this.price == null) return -1;
        return (int) Math.round(this.getPriceDouble() - that.getPriceDouble());
    }
}
