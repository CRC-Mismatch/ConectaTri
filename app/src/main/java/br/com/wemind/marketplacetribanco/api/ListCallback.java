package br.com.wemind.marketplacetribanco.api;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.api.utils.ErrorParser;
import retrofit2.Call;
import retrofit2.Response;

public abstract class ListCallback<T extends List> implements retrofit2.Callback<T> {

    protected final Context context;
    protected Call<T> newCall;
    private int count = 0;

    private ListCallback() {
        context = null;
    }

    public ListCallback(Context context) {
        this.context = context;
    }

    public abstract void onSuccess(T responseBody);

    public abstract void onError(Call<T> call, ApiError responseErrorBody);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            T responseBody = response.body();
            if (responseBody == null) {
                onError(call, ErrorParser.parse(response));
            } else {
                onSuccess(responseBody);
            }
        } else {
            onError(call, ErrorParser.parse(response));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (call.isCanceled()) {
            newCall = null;
            return;
        }
        t.printStackTrace();
        if (count < 3) {
            count++;
            newCall = call.clone();
            newCall.enqueue(this);
        } else {
            newCall = null;
            Toast.makeText(context, R.string.text_connection_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
