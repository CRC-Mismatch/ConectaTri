package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Listing implements Parcelable, Comparable, Serializable {
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

    @SerializedName("id")
    private long id;
    @SerializedName("type")
    private int type;
    @SerializedName("name")
    private String name;
    @SerializedName("listingProducts")
    private ArrayList<ListingProduct> products;
    @SerializedName("suppliers")
    private ArrayList<Supplier> suppliers;
    @SerializedName("description")
    private String description;

    public Listing(long id, String name, int type, ArrayList<ListingProduct> products,
                   ArrayList<Supplier> suppliers) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.products = products;
        this.suppliers = suppliers;
    }

    protected Listing(Parcel in) {
        id = in.readLong();
        name = in.readString();
        type = in.readInt();
        products = new ArrayList<>();
        in.readTypedList(products, ListingProduct.CREATOR);
        suppliers = new ArrayList<>();
        in.readTypedList(suppliers, Supplier.CREATOR);
        description = in.readString();
    }

    public long getId() {
        return id;
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
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeTypedList(products);
        dest.writeTypedList(suppliers);
        dest.writeString(description);
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<ListingProduct> getProducts() {
        return products;
    }

    public ArrayList<Supplier> getSuppliers() {
        return suppliers;
    }

    public Listing setSuppliers(ArrayList<Supplier> suppliers) {
        this.suppliers = suppliers;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Listing && ((Listing) obj).id == id;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Listing) {
            return (int) (id - ((Listing) o).id);
        } else {
            throw new ClassCastException();
        }
    }
}
