package br.com.wemind.marketplacetribanco.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ActivityBaseSelectBinding;

public abstract class BaseSelectActivity extends AppCompatActivity {
    protected ActivityBaseSelectBinding b;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_ok) {
            if (mayContinue()) {
                packResultIntent();
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok, menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = DataBindingUtil.setContentView(this, R.layout.activity_base_select);

        setSupportActionBar(b.toolbar);

        b.search.setLayoutParams(new Toolbar.LayoutParams(Gravity.END));
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    /**
     * Sets the result intent via <code>setResult()</code> for finishing
     * this activity.
     */
    protected abstract void packResultIntent();

    /**
     * This method is called when the user clicks the FAB.
     * Implementations should verify whether or not the activity is
     * ready for packing its results and finish. If <em>true</em>
     * is returned, <code>packResultIntent()</code> will be called
     * next and <code>finish()</code> right afterwards.
     *
     * @return <em>true</em> if the activity is ready to finish and return its results
     */
    protected abstract boolean mayContinue();
}
