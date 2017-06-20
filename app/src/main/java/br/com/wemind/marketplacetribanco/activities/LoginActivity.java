package br.com.wemind.marketplacetribanco.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.TimerTask;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.api.objects.AccessToken;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.api.objects.Login;
import br.com.wemind.marketplacetribanco.databinding.ActivityLoginBinding;
import br.com.wemind.marketplacetribanco.models.SignUpInfo;
import br.com.wemind.marketplacetribanco.utils.Formatting;
import br.com.wemind.marketplacetribanco.utils.FormattingTextWatcher;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static final String SAVED_USER = "SAVED_USER";
    public static final String SAVED_HASH = "SAVED_HASH";

    public static final int REQUEST_SIGN_UP = 1;

    ActivityLoginBinding binding;
    private Call ongoingLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);


        binding.user.addTextChangedListener(new FormattingTextWatcher(new Formatting.CnpjFormatter()));
        binding.user.setText(getPreferences(MODE_PRIVATE).getString(SAVED_USER, ""));

        //TEST CODE
        binding.user.setText("12345678123456");
        binding.password.setText("123456");

        binding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        binding.showPasswordBtn.setOnClickListener(new View.OnClickListener() {
            private boolean isShowing = false;

            @Override
            public void onClick(View v) {
                int sStart = binding.password.getSelectionStart();
                int sEnd = binding.password.getSelectionEnd();

                if (isShowing) {
                    isShowing = false;
                    binding.password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    isShowing = true;
                    binding.password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }

                binding.password.setSelection(sStart, sEnd);
            }
        });

        binding.userSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignUp();
            }
        });

        KeyboardVisibilityEvent.setEventListener(this, new LoginKeyboardEventListener());
    }

    protected void attemptLogin() {

        // If a call is already underway, do nothing
        if (ongoingLogin == null) {
            Call<Login.Response> loginCall = Api.api.login(
                    new Login.Request.Builder()
                            .setEmail(binding.user.getText().toString().replaceAll("[^0-9]", ""))
                            .setPassword(binding.password.getText().toString())
                            .build());

            ongoingLogin = loginCall;
            loginCall.enqueue(new LoginCallback());
        }
    }

    private void showProgress(final boolean show) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        binding.userLoginForm.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.userLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
        binding.loginProgress.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGN_UP) {
            signUpReturned(resultCode, data);
        }
    }

    public void launchSignUp() {
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivityForResult(signUpIntent, REQUEST_SIGN_UP);
    }

    public void signUpReturned(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            SignUpInfo signUpInfo =
                    data.getParcelableExtra(SignUpActivity.RESULT_SIGN_UP_INFO);

            if (signUpInfo != null) {
                Api.api.register(signUpInfo).enqueue(new RegisterCallback(this));
            }
        }
    }

    protected void finishLogin() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent summaryActivityIntent =
                        new Intent(LoginActivity.this, MainActivity.class);
                startActivity(summaryActivityIntent);
                finish();
            }
        });
    }

    private class LoginPlaceholder extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finishLogin();
                }
            });
        }
    }

    private class LoginKeyboardEventListener implements KeyboardVisibilityEventListener {
        @Override
        public void onVisibilityChanged(boolean isOpen) {
            int visibility = isOpen ? View.GONE : View.VISIBLE;
            binding.loginTitle.setVisibility(visibility);
            binding.buttonBottomSpace.setVisibility(visibility);
        }
    }

    private class LoginCallback extends Callback<Login.Response> {

        public LoginCallback() {
            super(LoginActivity.this);
        }

        @Override
        public void onSuccess(Login.Response response) {
            AccessToken at = new AccessToken();
            at.setToken(response.getToken());
            Api.setAccessToken(at);

            ongoingLogin = null;
            finishLogin();
        }

        @Override
        public void onError(Call<Login.Response> call,
                            Response<Login.Response> response) {
            // TODO: 07/06/2017
            Toast.makeText(context,
                    "Code " + response.code() + " " + response.message(),
                    Toast.LENGTH_SHORT
            ).show();

            ongoingLogin = null;
        }
    }

    private class RegisterCallback extends Callback<ApiError> {
        public RegisterCallback(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(ApiError response) {

        }

        @Override
        public void onError(Call<ApiError> call, Response<ApiError> response) {

        }
    }
}
