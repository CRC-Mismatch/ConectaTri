package br.com.wemind.marketplacetribanco.api.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PasswordRecovery implements Serializable {
    @SerializedName("i")
    private int userId;

    @SerializedName("j")
    private int requestTimestamp;

    @SerializedName("p")
    private String password;

    @SerializedName("z")
    private String hash;

    public int getUserId() {
        return userId;
    }

    public PasswordRecovery setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getRequestTimestamp() {
        return requestTimestamp;
    }

    public PasswordRecovery setRequestTimestamp(int requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public PasswordRecovery setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public PasswordRecovery setHash(String hash) {
        this.hash = hash;
        return this;
    }
}
