package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;

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

    public QuoteProduct() {}

    private QuoteProduct(Parcel in) {
        this.id = in.readLong();
        this.price = in.readDouble();
    }

    private long id;
    private double price;

    public long getId() {
        return id;
    }

    public QuoteProduct setId(long id) {
        this.id = id;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public QuoteProduct setPrice(double price) {
        this.price = price;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeDouble(price);
    }
}
