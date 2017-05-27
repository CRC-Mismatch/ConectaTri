package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Supplier implements Parcelable {
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
    // TODO: we'll also need a unique id for each supplier
    // private int id;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    // TODO: cnpj
    private String cnpj = "12123123412312";
    // TODO: info
    private String info = "Apenas um fornecedor";
    private String contactPhoneDdd;

    public Supplier(String name, String contactName, String contactEmail,
                    String contactPhoneDdd, String contactPhone) {
        this.name = name;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhoneDdd = contactPhoneDdd;
        this.contactPhone = contactPhone;
    }

    protected Supplier(Parcel in) {
        name = in.readString();
        contactName = in.readString();
        contactEmail = in.readString();
        contactPhoneDdd = in.readString();
        contactPhone = in.readString();
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
        dest.writeString(name);
        dest.writeString(contactName);
        dest.writeString(contactEmail);
        dest.writeString(contactPhoneDdd);
        dest.writeString(contactPhone);
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getInfo() {
        return info;
    }

    public String getContactPhoneDdd() {
        return contactPhoneDdd;
    }
}
