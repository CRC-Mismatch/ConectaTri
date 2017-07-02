package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Price implements Parcelable {
    public static final Creator<Price> CREATOR = new Creator<Price>() {
        @Override
        public Price createFromParcel(Parcel in) {
            return new Price(in);
        }

        @Override
        public Price[] newArray(int size) {
            return new Price[size];
        }
    };
    private Integer quantity;
    private Double unitPrice;

    public Price() {

    }

    protected Price(Parcel in) {
        quantity = in.readInt();
        unitPrice = in.readDouble();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Price setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public Price setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeDouble(unitPrice);
    }
}
