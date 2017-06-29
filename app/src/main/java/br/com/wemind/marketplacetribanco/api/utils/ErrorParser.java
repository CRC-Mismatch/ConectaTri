package br.com.wemind.marketplacetribanco.api.utils;

import java.lang.annotation.Annotation;

import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.api.objects.Status;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by kmkraiker on 30/03/2017.
 */

public class ErrorParser {
    public static ApiError parse(Response<?> response) {
        Converter<ResponseBody, ApiError> converter = Api.retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);
        try {
            return converter.convert(response.errorBody());
        } catch (Throwable e) {
            e.printStackTrace();
            ApiError error = new ApiError();
            error.setMessage("Houve um erro ao carregar os dados");
            return error;
        }
    }

    public static <T extends Status> ApiError parse(T response) {
        ApiError error = new ApiError();
        error.setMessage(response.getMessage());
        return error;
    }
}