package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Supplier implements Parcelable, Comparable, Serializable {
    public static final Creator<Supplier> CREATOR = new Creator<Supplier>() {
        @Override
        public Supplier createFromParcel(Parcel in) {
            return new Supplier(in);
        }

        @Override
        public Supplier[] newArray(int size) {
            return new Supplier[size];
        }
    };
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("contactName")
    private String contactName;
    @SerializedName("contactEmail")
    private String contactEmail;
    @SerializedName("contactPhone")
    private String contactPhone;
    // TODO cnpj
    @SerializedName("cnpj")
    private String cnpj = "12123123412312";
    @SerializedName("contactPhoneDdd")
    private String contactPhoneDdd;

    public Supplier() {}

    public Supplier(long id, String name, String contactName, String contactEmail,
                    String contactPhoneDdd, String contactPhone) {
        this.id = id;
        this.name = name;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhoneDdd = contactPhoneDdd;
        this.contactPhone = contactPhone;
    }

    protected Supplier(Parcel in) {
        id = in.readLong();
        name = in.readString();
        contactName = in.readString();
        contactEmail = in.readString();
        contactPhoneDdd = in.readString();
        contactPhone = in.readString();
    }

    public long getId() {
        return id;
    }

    public Supplier setId(long id) {
        this.id = id;
        return this;
    }

    public String getSupplierName() {
        return name;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(contactName);
        dest.writeString(contactEmail);
        dest.writeString(contactPhoneDdd);
        dest.writeString(contactPhone);
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getContactPhoneDdd() {
        return contactPhoneDdd;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Supplier && ((Supplier) obj).id == id;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Supplier) {
            return (int) (id - ((Supplier)o).id);
        } else {
            throw new ClassCastException();
        }
    }
}
