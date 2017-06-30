package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;

import br.com.wemind.marketplacetribanco.api.objects.Status;

public class UserInfo extends Status implements Parcelable, Serializable {

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
    @SerializedName("cnpj")
    private String cnpj;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("cellphone")
    private String cellphone;
    @SerializedName("fantasy_name")
    private String fantasyName;
    @SerializedName("company_name")
    private String companyName;
    @SerializedName("cep")
    private String cep;
    @SerializedName("state")
    @JsonAdapter(UserInfoStateAdapter.class)
    private String state;
    @SerializedName("city")
    private String city;
    @SerializedName("address")
    private String address;

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        cnpj = in.readString();
        password = in.readString();
        email = in.readString();
        phone = in.readString();
        cellphone = in.readString();
        fantasyName = in.readString();
        companyName = in.readString();
        cep = in.readString();
        state = in.readString();
        city = in.readString();
        address = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cnpj);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(cellphone);
        dest.writeString(fantasyName);
        dest.writeString(companyName);
        dest.writeString(cep);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getCnpj() {
        return cnpj;
    }

    public UserInfo setCnpj(String cnpj) {
        this.cnpj = cnpj.replaceAll("[^0-9]", "");
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserInfo setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserInfo setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getCellphone() {
        return cellphone;
    }

    public UserInfo setCellphone(String cellphone) {
        this.cellphone = cellphone;
        return this;
    }

    public String getFantasyName() {
        return fantasyName;
    }

    public UserInfo setFantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public UserInfo setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public String getCep() {
        return cep;
    }

    public UserInfo setCep(String cep) {
        this.cep = cep;
        return this;
    }

    public String getState() {
        return state;
    }

    public UserInfo setState(String state) {
        this.state = state;
        return this;
    }

    public String getCity() {
        return city;
    }

    public UserInfo setCity(String city) {
        this.city = city;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public UserInfo setAddress(String address) {
        this.address = address;
        return this;
    }

    public static class Edit {
        public static class Request {
            @SerializedName("password")
            private String password = "";
            @SerializedName("retailer")
            private UserInfo userInfo;

            public String getPassword() {
                return password;
            }

            public Request setPassword(String password) {
                this.password = password;
                return this;
            }

            public UserInfo getUserInfo() {
                return userInfo;
            }

            public Request setUserInfo(UserInfo userInfo) {
                this.userInfo = userInfo;
                return this;
            }
        }
    }

    private class UserInfoStateAdapter extends TypeAdapter<String> {

        public static final String SERIALIZED_NAME = "uf";

        @Override
        public void write(JsonWriter out, String value) throws IOException {
            out.value(value);
        }

        @Override
        public String read(JsonReader in) throws IOException {
            in.beginObject();
            String s = "";
            while (in.hasNext() && !(s = in.nextName()).equals(SERIALIZED_NAME)) {
                in.skipValue();
            }

            String state = null;
            if (s.equals(SERIALIZED_NAME)) {
                state = in.nextString();
            }

            in.endObject();

            return state;
        }
    }
}
