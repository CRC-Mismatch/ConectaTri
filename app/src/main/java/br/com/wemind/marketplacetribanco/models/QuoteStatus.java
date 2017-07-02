package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;

public class QuoteStatus implements Parcelable {
    public static final Creator<QuoteStatus> CREATOR = new Creator<QuoteStatus>() {
        @Override
        public QuoteStatus createFromParcel(Parcel in) {
            return new QuoteStatus(in);
        }

        @Override
        public QuoteStatus[] newArray(int size) {
            return new QuoteStatus[size];
        }
    };
    private Supplier[] suppliers = new Supplier[0];
    private boolean[] hasResponded = new boolean[0];

    public QuoteStatus() {

    }

    protected QuoteStatus(Parcel in) {
        suppliers = in.createTypedArray(Supplier.CREATOR);
        hasResponded = in.createBooleanArray();
    }

    public Supplier getSupplier(int i) {
        return suppliers[i];
    }

    public boolean getHasResponded(int i) {
        return hasResponded[i];
    }

    public int size() {
        return suppliers.length;
    }

    public QuoteStatus setItems(Supplier[] suppliers, boolean[] hasResponded) {
        if (hasResponded.length != suppliers.length) {
            throw new IllegalStateException(
                    "The lengths of hasResponded and suppliers must match");
        }
        this.suppliers = suppliers;
        this.hasResponded = hasResponded;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(suppliers, flags);
        dest.writeBooleanArray(hasResponded);
    }
}