package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class SupplierRep implements Parcelable, Comparable {
    public static final Creator<SupplierRep> CREATOR = new Creator<SupplierRep>() {
        @Override
        public SupplierRep createFromParcel(Parcel in) {
            return new SupplierRep(in);
        }

        @Override
        public SupplierRep[] newArray(int size) {
            return new SupplierRep[size];
        }
    };
    // TODO: we'll also need a unique id for each supplier
    private long id;
    private String name;
    private String email;
    private String phone;
    private String phoneDdd;

    public SupplierRep(long id, String name, String email,
                       String phoneDdd, String phone) {
        this.id = id;
        this.name = name;
    }

    protected SupplierRep(Parcel in) {
        id = in.readLong();
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        phoneDdd = in.readString();
    }

    public long getId() {
        return id;
    }

    public SupplierRep setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SupplierRep setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public SupplierRep setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public SupplierRep setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getPhoneDdd() {
        return phoneDdd;
    }

    public SupplierRep setPhoneDdd(String phoneDdd) {
        this.phoneDdd = phoneDdd;
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
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(phoneDdd);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SupplierRep && ((SupplierRep) obj).id == id;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof SupplierRep) {
            return (int) (id - ((SupplierRep)o).id);
        } else {
            throw new ClassCastException();
        }
    }
}
