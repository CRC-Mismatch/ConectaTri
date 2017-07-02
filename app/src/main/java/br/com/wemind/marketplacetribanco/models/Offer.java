package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Offer implements Parcelable {
    public static final Creator<Offer> CREATOR = new Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };
    private Product product;
    private Supplier supplier;
    private Price price;

    public Offer() {

    }

    protected Offer(Parcel in) {
        product = in.readParcelable(Product.class.getClassLoader());
        supplier = in.readParcelable(Supplier.class.getClassLoader());
        price = in.readParcelable(Price.class.getClassLoader());
    }

    public Product getProduct() {
        return product;
    }

    public Offer setProduct(Product product) {
        this.product = product;
        return this;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Offer setSupplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public Price getPrice() {
        return price;
    }

    public Offer setPrice(Price price) {
        this.price = price;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(product, flags);
        dest.writeParcelable(supplier, flags);
        dest.writeParcelable(price, flags);
    }
}
