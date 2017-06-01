package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by kmkraiker on 25/05/2017.
 */

public class Product implements Parcelable, Comparable {
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
    // TODO: we'll also need a unique id for each product
    private long id;
    private String EAN;
    private String department;
    private String section;
    private String category;
    private String subCategory;
    private String fullDescription;
    private String name;
    private String brand;
    private double quantity;
    private String unit;

    public Product() {
    }

    private Product(Parcel in) {
        this.id = in.readLong();
        this.EAN = in.readString();
        this.name = in.readString();
        this.fullDescription = in.readString();
        this.department = in.readString();
        this.section = in.readString();
        this.category = in.readString();
        this.subCategory = in.readString();
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

    public String getDepartment() {
        return department;
    }

    public Product setDepartment(String department) {
        this.department = department;
        return this;
    }

    public String getSection() {
        return section;
    }

    public Product setSection(String section) {
        this.section = section;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Product setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public Product setSubCategory(String subCategory) {
        this.subCategory = subCategory;
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
        dest.writeString(department);
        dest.writeString(section);
        dest.writeString(category);
        dest.writeString(subCategory);
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
