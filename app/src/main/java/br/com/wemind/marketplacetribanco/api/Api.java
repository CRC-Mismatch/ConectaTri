package br.com.wemind.marketplacetribanco.api;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.wemind.marketplacetribanco.api.objects.AccessToken;
import br.com.wemind.marketplacetribanco.api.objects.SearchQuery;
import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.models.Product;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class Api {

    public static Retrofit retrofit;
    public static BaseApi api;
    private static OkHttpClient client;
    private static AccessToken accessToken = new AccessToken();

    static {
        client = new OkHttpClient.Builder()
                .authenticator(new TokenAuthenticator())
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();


        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseApi.baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(BaseApi.class);
    }

    @NonNull
    public static ArrayList<Product> syncSearchProduct(CharSequence constraint) {
        return syncGetResponseList(Api.api.searchProduct(new SearchQuery(constraint)));
    }

    @NonNull
    public static ArrayList<Listing> syncSearchListing(CharSequence constraint) {
        return syncGetResponseList(Api.api.searchListing(new SearchQuery(constraint)));
    }

    @NonNull
    private static <T> ArrayList<T> syncGetResponseList(Call<List<T>> call) {
        try {
            retrofit2.Response<List<T>> response = call.execute();
            List<T> responseData = response.body();

            if (responseData != null) {
                return new ArrayList<>(responseData);
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void setAccessToken(AccessToken accessToken) {
        if (accessToken == null) {
            accessToken = new AccessToken();
        }
        Api.accessToken = accessToken;
    }

    private static class TokenAuthenticator implements Authenticator {

        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            /*if (accessToken == null || accessToken.hasExpired()) {
                accessToken = api.accessToken(new AccessToken.Request()).execute().body();
            }*/
            return response.request().newBuilder()
                    .addHeader("Api-Token", accessToken.getToken())
                    .build();
        }
    }
}
