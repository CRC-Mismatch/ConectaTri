package br.com.wemind.marketplacetribanco.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.api.objects.Status;
import retrofit2.Call;

public abstract class Callback<T> implements retrofit2.Callback<T> {

    protected final Context context;
    protected Call<T> newCall;
    protected int count = 0;

    private Callback() {
        context = null;
    }

    public Callback(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void onResponse(Call<T> call, retrofit2.Response<T> response) {
        if (response.isSuccessful()) {
            T responseBody = response.body();
            onSuccess(responseBody);
        } else {
            onError(call, response);
        }
    }

    public abstract void onSuccess(T response);

    public abstract void onError(Call<T> call,
                                 retrofit2.Response<T> response);

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
            Toast.makeText(context,
                    R.string.text_connection_failed,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
