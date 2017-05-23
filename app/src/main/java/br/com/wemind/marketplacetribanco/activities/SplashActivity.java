package br.com.wemind.marketplacetribanco.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

import br.com.wemind.marketplacetribanco.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Timer t = new Timer();
        t.schedule(new TransitionTask(), SPLASH_DELAY);
    }

    private void startLoginActivity() {
        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        SplashActivity.this.startActivity(loginIntent);
        SplashActivity.this.finish();
    }

    private class StartLoginOnTransitionEnd implements Transition.TransitionListener {
        @Override
        public void onTransitionStart(Transition transition) {

        }

        @Override
        public void onTransitionEnd(Transition transition) {
            startLoginActivity();
            overridePendingTransition(0, 0);
        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    }

    private class TransitionTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ChangeBounds changeBounds = new ChangeBounds();
                    changeBounds.addTarget(R.id.header);

                    Fade fade = new Fade();
                    fade.addTarget(R.id.login_fields);

                    TransitionSet set = new TransitionSet();
                    set.addTransition(changeBounds)
                            .addTransition(fade)
                            .setOrdering(TransitionSet.ORDERING_SEQUENTIAL);

                    set.addListener(new StartLoginOnTransitionEnd());

                    TransitionManager.go(
                            Scene.getSceneForLayout(
                                    (ViewGroup) findViewById(R.id.activity_splash),
                                    R.layout.activity_login,
                                    SplashActivity.this
                            ),
                            set
                    );
                }
            });
        }
    }

}
