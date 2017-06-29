package br.com.wemind.marketplacetribanco.api.objects;

import com.google.gson.annotations.SerializedName;

public class Status {
    public Status setMessage(String message) {
        this.message = message;
        return this;
    }

    @SerializedName("message")
    protected String message = "";

    public String getMessage() {
        return message;
    }
}
