package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import br.com.wemind.marketplacetribanco.api.objects.Status;

/**
 * Created by kmkraiker on 25/05/2017.
 */

public class Product extends Status implements Parcelable, Comparable, Serializable {
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    @SerializedName("id")
    private long id;
    @SerializedName("ean")
    private String EAN = "EAN_DE_TESTES";
    @SerializedName("type")
    private String type;
    @SerializedName("full_description")
    private String fullDescription;
    @SerializedName("name")
    private String name;
    @SerializedName("brand")
    private String brand;
    @SerializedName("quantity")
    private double quantity;
    @SerializedName("unit")
    private String unit;

    public Product() {
    }

    private Product(Parcel in) {
        this.id = in.readLong();
        this.EAN = in.readString();
        this.name = in.readString();
        this.fullDescription = in.readString();
        this.type = in.readString();
        this.brand = in.readString();
        this.quantity = in.readDouble();
        this.unit = in.readString();
    }

    public long getId() {
        return id;
    }

    public Product setId(long id) {
        this.id = id;
        return this;
    }

    public String getEAN() {
        return EAN;
    }

    public Product setEAN(String EAN) {
        this.EAN = EAN;
        return this;
    }

    public String getType() {
        return type;
    }

    public Product setType(String type) {
        this.type = type;
        return this;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public Product setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public Product setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public double getQuantity() {
        return quantity;
    }

    public Product setQuantity(double quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public Product setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(EAN);
        dest.writeString(name);
        dest.writeString(fullDescription);
        dest.writeString(type);
        dest.writeString(brand);
        dest.writeDouble(quantity);
        dest.writeString(unit);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Product && ((Product) obj).getId() == id;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Product) {
            return (int) (id - ((Product) o).getId());
        } else {
            throw new ClassCastException(o.toString());
        }
    }
}
