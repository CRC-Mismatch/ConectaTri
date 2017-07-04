package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.api.objects.Status;

/**
 * Created by kmkraiker on 25/05/2017.
 */

public class Quote extends Status implements Parcelable, Serializable {
    public static final int TYPE_REMOTE = 1;
    public static final int TYPE_MANUAL = 2;
    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name = "";
    @SerializedName("type")
    private int type;
    @SerializedName("quote_products")
    private List<QuoteProduct> quoteProducts;
    @SerializedName("begins_at")
    private Date beginningDate = new Date();
    @SerializedName("expires_at")
    private Date expirationDate = new Date();
    @SerializedName("closed")
    private boolean closed = false;

    public Quote() {
    }

    public Quote(long id, String name, int type, List<Product> products, List<Supplier> suppliers) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quoteProducts = new ArrayList<>();
    }

    private Quote(Parcel in) {
        id = in.readLong();
        name = in.readString();
        type = in.readInt();
        quoteProducts = in.createTypedArrayList(QuoteProduct.CREATOR);
        beginningDate = (Date) in.readSerializable();
        expirationDate = (Date) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeTypedList(quoteProducts);
        dest.writeSerializable(beginningDate);
        dest.writeSerializable(expirationDate);
    }

    public long getId() {
        return id;
    }

    public Quote setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Quote setName(String name) {
        this.name = name;
        return this;
    }

    public int getType() {
        return type;
    }

    public Quote setType(int type) {
        this.type = type;
        return this;
    }

    public List<QuoteProduct> getQuoteProducts() {
        return quoteProducts;
    }

    public Quote setQuoteProducts(List<QuoteProduct> quoteProducts) {
        this.quoteProducts = quoteProducts;
        return this;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public Quote setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public Date getBeginningDate() {
        return beginningDate;
    }

    public Quote setBeginningDate(Date beginningDate) {
        this.beginningDate = beginningDate;
        return this;
    }

    public ArrayList<Supplier> getSuppliers() {
        Set<Supplier> result = new TreeSet<>();

        // TODO: 01/07/2017 revisit this. Can this lead to performance issues in the future?
        for (QuoteProduct quoteProduct : getQuoteProducts()) {
            for (QuoteSupplier quoteSupplier : quoteProduct.getQuoteSuppliers()) {
                result.add(quoteSupplier.getSupplier());
            }
        }
        return new ArrayList<>(result);
    }
}
