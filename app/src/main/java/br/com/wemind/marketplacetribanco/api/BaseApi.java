package br.com.wemind.marketplacetribanco.api;

import java.util.List;

import br.com.wemind.marketplacetribanco.api.objects.Login;
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.models.Supplier;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BaseApi {

    //String baseUrl = "https://demo8948757.mockable.io/";
    String PRODUCT = "product";
    String SUPPLIER = "representative";
    String baseUrl = "http://conectatri.wemind.com.br/api/";

    @POST("login")
    Call<Login.Response> login(@Body Login.Request body);

    @DELETE("logout")
    Call<String> logout();

    /*
     * PRODUCT
     */
    @GET(PRODUCT)
    Call<List<Product>> getAllProducts();

    @GET(PRODUCT + "/{id}")
    Call<Product> getProduct(@Path("id") Long id);

    @POST(PRODUCT)
    Call<Product> addProduct(@Body Product product);

    @PUT(PRODUCT + "/{id}")
    Call<Product> editProduct(@Body Product product, @Path("id") Long id);

    @DELETE(PRODUCT + "/{id}")
    Call<Product> deleteProduct(@Path("id") Long id);
    /*
     * END OF PRODUCT
     */

    /*
     * SUPPLIER
     */
    @GET(SUPPLIER)
    Call<List<Supplier>> getAllSuppliers();

    @GET(SUPPLIER + "/{id}")
    Call<Supplier> getSupplier(@Path("id") Long id);

    @POST(SUPPLIER)
    Call<Supplier> addSupplier(@Body Supplier supplier);

    @PUT(SUPPLIER + "/{id}")
    Call<Supplier> editSupplier(@Body Supplier supplier, @Path("id") Long id);

    @DELETE(SUPPLIER + "/{id}")
    Call<Supplier> deleteSupplier(@Path("id") Long id);
    /*
     * END OF SUPPLIER
     */

    /*
     * QUOTE
     */
}
