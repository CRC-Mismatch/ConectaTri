package br.com.wemind.marketplacetribanco.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.api.objects.PasswordRecovery;
import br.com.wemind.marketplacetribanco.databinding.ActivityRecoverPasswordBinding;
import br.com.wemind.marketplacetribanco.databinding.ActivitySignUpBinding;
import br.com.wemind.marketplacetribanco.models.SignUpInfo;
import br.com.wemind.marketplacetribanco.utils.Formatting;
import br.com.wemind.marketplacetribanco.utils.FormattingTextWatcher;
import retrofit2.Call;
import retrofit2.Response;

public class RecoverPasswordActivity extends AppCompatActivity {

    private ActivityRecoverPasswordBinding b;
    private PasswordRecovery recoverData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_recover_password);

        b.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    recoverData.setPassword(b.passwordText.getText().toString());
                    Api.api.endRecovery(recoverData).enqueue(new RecoverCallback(RecoverPasswordActivity.this));
                }
            }
        });
        Gson gson = new Gson();
        String jsonData = new String(Base64.decode(getIntent().getData().toString().replaceFirst("https://rs\\.conectatri\\.com\\.br/", ""), Base64.URL_SAFE | Base64.NO_PADDING));
        recoverData = gson.fromJson(jsonData, PasswordRecovery.class);
    }

    private boolean validateForm() {
        boolean isValid = true;
        View errorView = null;

        // Validate passwords
        if (b.passwordText.length() <= 0) {
            // If password is empty
            b.passwordText.setError(getString(R.string.error_field_required));
            errorView = b.passwordText;

        } else if (b.passwordText.length() < 8) {
            // If password is not at least 8 characters long
            b.passwordText.setError(getString(R.string.error_invalid_password_min_chars));
            errorView = b.passwordText;

        } else if (!b.passwordText.getText().toString()
                .equals(b.passwordConfText.getText().toString())) {
            // If passwords don't match
            b.passwordConfText.setError(getString(R.string.error_passwords_dont_match));
            errorView = b.passwordConfText;
        }

        if (errorView != null) {
            isValid = false;
            errorView.requestFocus();
        }

        return isValid;
    }

    public class RecoverCallback extends Callback<ApiError> {

        public RecoverCallback(@NonNull Context context) {
            super(context);
        }

        @Override
        public void onSuccess(ApiError response) {
            Intent i = new Intent(RecoverPasswordActivity.this, LoginActivity.class);
            i.setAction("RECOVER");
            startActivity(i);
            finish();
        }

        @Override
        public void onError(Call<ApiError> call, Response<ApiError> response) {
            Toast.makeText(context,
                    "Erro: Código " + response.code() + " " + response.message(),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
