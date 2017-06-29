package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import br.com.wemind.marketplacetribanco.api.objects.Status;

public class Supplier extends Status implements Parcelable, Comparable, Serializable {
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
    @SerializedName("supplier_id")
    private long companyId;
    @SerializedName("name")
    private String name;
    @SerializedName("contact_name")
    private String contactName;
    @SerializedName("contact_email")
    private String contactEmail;
    @SerializedName("contact_phone")
    private String contactPhone;
    @SerializedName("cnpj")
    private String cnpj;

    public Supplier() {
    }

    public Supplier(long id, String name, String contactName, String contactEmail,
                    String contactPhone) {
        this.id = id;
        this.name = name;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
    }

    protected Supplier(Parcel in) {
        id = in.readLong();
        name = in.readString();
        contactName = in.readString();
        contactEmail = in.readString();
        contactPhone = in.readString();
        cnpj = in.readString();
        companyId = in.readLong();
    }

    public String getName() {
        return name;
    }

    public Supplier setName(String name) {
        this.name = name;
        return this;
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

    public Supplier setContactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public Supplier setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public Supplier setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
        return this;
    }

    public String getCnpj() {
        return cnpj;
    }

    public Supplier setCnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public long getCompanyId() {
        return companyId;
    }

    public Supplier setCompanyId(long companyId) {
        this.companyId = companyId;
        return this;
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
        dest.writeString(contactPhone);
        dest.writeString(cnpj);
        dest.writeLong(companyId);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Supplier && ((Supplier) obj).id == id;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Supplier) {
            return (int) (id - ((Supplier) o).id);
        } else {
            throw new ClassCastException();
        }
    }
}
