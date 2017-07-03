package br.com.wemind.marketplacetribanco.api.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Login implements Serializable {

    public static class Request {

        @SerializedName("cnpj")
        private String username;

        @SerializedName("password")
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String email) {
            this.username = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public static class Builder {
            private Request request;

            public Builder() {
                request = new Request();
            }

            public Request build() {
                return request;
            }

            public Builder setUsername(String email) {
                request.setUsername(email);
                return this;
            }

            public Builder setPassword(String password) {
                request.setPassword(password);
                return this;
            }
        }
    }

    public static class Response extends Status {

        @SerializedName("token")
        private String token;

        public String getToken() {
            return token;
        }
    }
}
