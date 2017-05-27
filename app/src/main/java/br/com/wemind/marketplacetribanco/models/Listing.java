package br.com.wemind.marketplacetribanco.models;

import android.app.ProgressDialog;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

public class Listing implements Parcelable {
    public static final int TYPE_COMMON = 1;
    public static final int TYPE_SEASONAL = 2;
    public static final int TYPE_WEEKLY = 3;
    public static final Creator<Listing> CREATOR = new Creator<Listing>() {
        @Override
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        @Override
        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };
    private int type;
    private String name;
    private ArrayList<Product> products;
    private String description;

    public Listing(String name, int type, ArrayList<Product> products) {
        this.name = name;
        this.type = type;
        this.products = products;
    }

    protected Listing(Parcel in) {
        name = in.readString();
        type = in.readInt();
        products = new ArrayList<>();
        in.readTypedList(products, Product.CREATOR);
        description = in.readString();
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeTypedList(products);
        dest.writeString(description);
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}
