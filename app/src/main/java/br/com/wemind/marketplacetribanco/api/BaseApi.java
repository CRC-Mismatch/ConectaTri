package br.com.wemind.marketplacetribanco.api;

import java.util.List;

import br.com.wemind.marketplacetribanco.api.objects.Login;
import br.com.wemind.marketplacetribanco.models.Product;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BaseApi {

    String baseUrl = "https://demo8948757.mockable.io/api/";
    //String baseUrl = "http://localhost/conectatri/web/app.php/api/";

    @POST("login")
    Call<Login.Response> login(@Body Login.Request body);

    @GET("product")
    Call<List<Product>> getAllProducts();

    @GET("product/{id}")
    Call<Product> getProduct(@Path("id") Integer id);
}
