package br.com.wemind.marketplacetribanco.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.api.objects.AccessToken;
import br.com.wemind.marketplacetribanco.databinding.ActivityBaseDrawerBinding;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Base class for all activities which require the side navigation drawer
 */
public abstract class BaseDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Indicates this activity does not have a navigation button associated with it
     * but should not be finished when a drawer navigation button is selected
     */
    public static final int ID_NONE_PERSISTENT = -1;
    /**
     * Indicates this activity does not have a navigation button associated with it
     * and should be finished when a drawer navigation button is selected
     */
    public static final int ID_NONE_VOLATILE = -2;
    private static final long MAIN_CONTENT_FADEOUT_DURATION = 300;
    public static final String WWW_TRIBANCO_HOMEPAGE = "http://www.tribanco.com.br/";
    protected ActivityBaseDrawerBinding b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_base_drawer);

        setSupportActionBar(b.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, b.drawerLayout, b.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        b.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        b.navView.setNavigationItemSelectedListener(this);

        b.search.setLayoutParams(new Toolbar.LayoutParams(Gravity.END));
    }

    @Override
    public void onBackPressed() {
        if (b.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            b.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == getSelfNavDrawerItem()) {
            b.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        /*
        // TODO: Possibly use this approach for a smoother transition
        // launch the target Activity after a short delay, to allow the close animation to play
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNavDrawerItem(id);
            }
        }, NAVDRAWER_LAUNCH_DELAY);
        */

        // fade out the main content
        b.contentFrame.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);

        b.drawerLayout.closeDrawer(GravityCompat.START);

        goToNavDrawerItem(id);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // fade back the main content
        b.contentFrame.setAlpha(1);
    }

    private void goToNavDrawerItem(int id) {
        // FIXME: 27/05/2017 There's no option to go to the home panel
        if (id == R.id.nav_listings) {
            Intent i = new Intent(this, ListingsListActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_products) {
            Intent i = new Intent(this, SimpleProductsListActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_suppliers) {
            Intent i = new Intent(this, SuppliersListActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_remote_quote) {
            Intent i = new Intent(this, RemoteQuotesActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_manual_quote) {
            Intent i = new Intent(this, QuoteCreationFlowController.class);
            i.putExtra(QuoteCreationFlowController.INPUT_IS_MANUAL, true);
            startActivity(i);

        } else if (id == R.id.nav_about) {
            Intent browserIntent =
                    new Intent(Intent.ACTION_VIEW, Uri.parse(WWW_TRIBANCO_HOMEPAGE));
            startActivity(browserIntent);

        } else if (id == R.id.nav_tutorial) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            // Try to log out.
            Api.api.logout().enqueue(new LogoutCallback(this));
        }

        if (getSelfNavDrawerItem() != ID_NONE_PERSISTENT) {
            finish();
        }
    }

    protected abstract int getSelfNavDrawerItem();

    private static class LogoutCallback extends Callback<String> {

        public LogoutCallback(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(String response) {
            cleanUp();
            finish();
        }

        private void cleanUp() {
            Api.setAccessToken(null);
        }

        @Override
        public void onError(Call<String> call, Response<String> response) {
            finish();
        }

        private void finish() {
            Activity activity = (Activity) this.context;
            if (activity != null) {
                Intent i = new Intent(activity, LoginActivity.class);
                activity.startActivity(i);
                activity.finish();
            }
        }
    }
}