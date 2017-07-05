package br.com.wemind.marketplacetribanco.api;

import java.util.List;

import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.api.objects.Login;
import br.com.wemind.marketplacetribanco.api.objects.PasswordRecovery;
import br.com.wemind.marketplacetribanco.api.objects.SearchQuery;
import br.com.wemind.marketplacetribanco.api.objects.Status;
import br.com.wemind.marketplacetribanco.api.objects.GetCep;
import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.models.Quote;
import br.com.wemind.marketplacetribanco.models.Supplier;
import br.com.wemind.marketplacetribanco.models.UserInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import static br.com.wemind.marketplacetribanco.api.BaseApi.EMAIL;

public interface BaseApi {

    //String baseUrl = "https://demo8948757.mockable.io/";
    String PRODUCT = "product";
    String SUPPLIER = "representative";
    String baseUrl = "http://conectatri.wemind.com.br/api/";
    String QUOTE = "quote";
    String LISTING = "listing";
    String USER_INFO = "retailer";
    String EMAIL = "email";

    @POST("login")
    Call<Login.Response> login(@Body Login.Request body);

    @DELETE("logout")
    Call<Status> logout();


    /*
     * VALIDATION
     */
    @GET(EMAIL + "/{email}")
    Call<Boolean> validateEmail(@Path("email") String email);

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

    @PATCH(PRODUCT)
    Call<List<Product>> searchProduct(@Body SearchQuery searchQuery);
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
    @GET(QUOTE)
    Call<List<Quote>> getAllQuotes();

    @GET(QUOTE + "/{id}")
    Call<Quote> getQuote(@Path("id") Long id);

    @POST(QUOTE)
    Call<Quote> addQuote(@Body Quote Quote);

    @PUT(QUOTE + "/{id}")
    Call<Quote> editQuote(@Body Quote Quote, @Path("id") Long id);

    @DELETE(QUOTE + "/{id}")
    Call<Quote> deleteQuote(@Path("id") Long id);
    /*
     * END OF Quote
     */

    /*
     * Listing
     */
    @GET(LISTING)
    Call<List<Listing>> getAllListings();

    @GET(LISTING + "/{id}")
    Call<Listing> getListing(@Path("id") Long id);

    @POST(LISTING)
    Call<Listing> addListing(@Body Listing Listing);

    @PUT(LISTING + "/{id}")
    Call<Listing> editListing(@Body Listing Listing, @Path("id") Long id);

    @DELETE(LISTING + "/{id}")
    Call<Listing> deleteListing(@Path("id") Long id);

    @PATCH(LISTING)
    Call<List<Listing>> searchListing(@Body SearchQuery searchQuery);
    /*
     * END OF Listing
     */

    /*
     * Sign Up
     */
    @POST(USER_INFO)
    Call<ApiError> register(@Body UserInfo userInfo);

    @FormUrlEncoded
    @POST("recovery/begin")
    Call<ApiError> beginRecovery(@Field("cnpj") String cnpj);

    @POST("recovery/end")
    Call<ApiError> endRecovery(@Body PasswordRecovery data);
    /*
     * END OF Sign Up
     */

    /*
     * User Info
     */
    @GET(USER_INFO)
    Call<UserInfo> getUserInfo();

    @PUT(USER_INFO)
    Call<ApiError> editUserInfo(@Body UserInfo.Edit.Request newUserInfo);
    /*
     * END OF User Info
     */

    @GET("https://apidev-tribanco.sensedia.com/sandbox/v1/tricard/enderecos/{cep}/abreviado")

    Call<GetCep> getCepResponse(@Path("cep") String cep);

}
