package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    @SerializedName("listing_products")
    private ArrayList<ListingProduct> listingProducts = new ArrayList<>();
    @SerializedName("representatives")
    private ArrayList<Supplier> suppliers = new ArrayList<>();
    @SerializedName("description")
    private String description;

    public Listing() {
    }

    public Listing(long id, String name, int type, ArrayList<ListingProduct> listingProducts,
                   ArrayList<Supplier> suppliers) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.listingProducts = listingProducts;
        this.suppliers = suppliers;
    }
    protected Listing(Parcel in) {
        id = in.readLong();
        name = in.readString();
        type = in.readInt();
        listingProducts = new ArrayList<>();
        in.readTypedList(listingProducts, ListingProduct.CREATOR);
        suppliers = new ArrayList<>();
        in.readTypedList(suppliers, Supplier.CREATOR);
        description = in.readString();
    }

    public long getId() {
        return id;
    }

    public Listing setId(long id) {
        this.id = id;
        return this;
    }

    public int getType() {
        return type;
    }

    public Listing setType(int type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public Listing setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        for (ListingProduct lp :
                listingProducts) {
            products.add(lp.getProduct());
        }

        return products;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeTypedList(listingProducts);
        dest.writeTypedList(suppliers);
        dest.writeString(description);
    }

    public String getDescription() {
        return description;
    }

    public Listing setDescription(String description) {
        this.description = description;
        return this;
    }

    public ArrayList<ListingProduct> getListingProducts() {
        return listingProducts;
    }

    public Listing setListingProducts(ArrayList<ListingProduct> listingProducts) {
        this.listingProducts = listingProducts;
        return this;
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
