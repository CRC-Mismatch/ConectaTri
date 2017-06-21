package br.com.wemind.marketplacetribanco.api.objects;

import com.google.gson.annotations.SerializedName;

public class ApiError {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public ApiError setMessage(String message) {
        this.message = message;
        return this;
    }
}