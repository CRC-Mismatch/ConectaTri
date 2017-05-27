package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ActivityBaseCreateBinding;

public abstract class BaseCreateActivity extends AppCompatActivity {
    protected ActivityBaseCreateBinding b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_base_create);

        setSupportActionBar(b.toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_close);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // Cancel creation, return to caller
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (R.id.action_menu_save == id) {
            // User pressed "Save",
            if (validateForm()) {
                // pack result and return to caller activity
                Intent i = getResultIntent();
                setResult(RESULT_OK, i);
                finish();
            }
            return true;
        }

        return false;
    }

    /**
     * This method is called when the user presses the "Save" button. Use it to
     * validate the form.
     * If this returns <em>true</em>, getResultIntent() will be called next.
     *
     * @return <em>true</em> if form is valid; <em>false</em> otherwise
     */
    protected abstract boolean validateForm();

    /**
     * Packs the results into an Intent to be sent back to the caller activity
     *
     * @return instance of Intent containing the results
     */
    protected abstract Intent getResultIntent();
}
