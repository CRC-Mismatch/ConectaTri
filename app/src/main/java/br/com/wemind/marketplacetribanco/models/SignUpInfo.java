package br.com.wemind.marketplacetribanco.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignUpInfo implements Parcelable, Serializable {

    public static final Creator<SignUpInfo> CREATOR = new Creator<SignUpInfo>() {
        @Override
        public SignUpInfo createFromParcel(Parcel in) {
            return new SignUpInfo(in);
        }

        @Override
        public SignUpInfo[] newArray(int size) {
            return new SignUpInfo[size];
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
    private String state;
    @SerializedName("city")
    private String city;
    @SerializedName("address")
    private String address;

    public SignUpInfo() {
    }

    protected SignUpInfo(Parcel in) {
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

    public SignUpInfo setCnpj(String cnpj) {
        this.cnpj = cnpj.replaceAll("[^0-9]", "");
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SignUpInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public SignUpInfo setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public SignUpInfo setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getCellphone() {
        return cellphone;
    }

    public SignUpInfo setCellphone(String cellphone) {
        this.cellphone = cellphone;
        return this;
    }

    public String getFantasyName() {
        return fantasyName;
    }

    public SignUpInfo setFantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public SignUpInfo setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public String getCep() {
        return cep;
    }

    public SignUpInfo setCep(String cep) {
        this.cep = cep;
        return this;
    }

    public String getState() {
        return state;
    }

    public SignUpInfo setState(String state) {
        this.state = state;
        return this;
    }

    public String getCity() {
        return city;
    }

    public SignUpInfo setCity(String city) {
        this.city = city;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public SignUpInfo setAddress(String address) {
        this.address = address;
        return this;
    }
}
