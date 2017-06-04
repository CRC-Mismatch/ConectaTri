package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;

public class Supplier implements Parcelable, Comparable {
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
    private long id;
    private String name;
    // TODO: cnpj
    private String cnpj = "12123123412312";
    // TODO: info
    private String info = "Apenas um fornecedor";
    private List<SupplierRep> representatives;

    public Supplier(long id, String name, List<SupplierRep> representatives) {
        this.id = id;
        this.name = name;
        this.representatives = representatives;
    }

    protected Supplier(Parcel in) {
        id = in.readLong();
        name = in.readString();
        cnpj = in.readString();
        info = in.readString();
        representatives = in.createTypedArrayList(SupplierRep.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(cnpj);
        dest.writeString(info);
        dest.writeTypedList(representatives);
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getInfo() {
        return info;
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
