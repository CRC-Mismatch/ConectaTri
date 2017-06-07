package br.com.wemind.marketplacetribanco.api.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Login implements Serializable {

    public static class Request {

        @SerializedName("email")
        private String email;

        @SerializedName("password")
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

            public Builder setEmail(String email) {
                request.setEmail(email);
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
