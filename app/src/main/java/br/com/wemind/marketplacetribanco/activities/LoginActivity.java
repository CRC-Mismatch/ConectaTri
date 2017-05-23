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
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ActivityLoginBinding;
import br.com.wemind.marketplacetribanco.utils.Formatting;
import br.com.wemind.marketplacetribanco.utils.FormattingTextWatcher;

public class LoginActivity extends AppCompatActivity {

    public static final String SAVED_USER = "SAVED_USER";
    public static final String SAVED_HASH = "SAVED_HASH";

    public static final int REQUEST_AUTO_ADHESION = 1;

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);


        binding.user.addTextChangedListener(new FormattingTextWatcher(new Formatting.CpfFormatter()));
        binding.user.setText(getPreferences(MODE_PRIVATE).getString(SAVED_USER, ""));

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

        binding.autoAdhesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAutoAdhesion();
            }
        });

        KeyboardVisibilityEvent.setEventListener(this, new LoginKeyboardEventListener());
    }

    protected void attemptLogin() {
        if (binding.user.getText().toString().equals("123.456.789-12") && binding.password.getText().toString().equals("123456")) {
            showProgress(true);
            Timer t = new Timer();
            t.schedule(new LoginPlaceholder(), 2000);
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

    public void launchAutoAdhesion() {
        //Intent autoAdhesionIntent = new Intent(this, AutoAdhesionActivity.class);
        //autoAdhesionIntent.putExtra(AutoAdhesionActivity.CPF_NUM, binding.user.getText().toString());
        //startActivityForResult(autoAdhesionIntent, REQUEST_AUTO_ADHESION);
    }

    public void resultAutoAdhesion(int resultCode, Intent data) {
        //if (resultCode != AutoAdhesionActivity.REGISTERED || data == null) return;
        //showProgress(true);
    }

    protected void finishLogin() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent summaryActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
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
            binding.buttonRecoverySpace.setVisibility(visibility);
            binding.buttonBottomSpace.setVisibility(visibility);
        }
    }
}
