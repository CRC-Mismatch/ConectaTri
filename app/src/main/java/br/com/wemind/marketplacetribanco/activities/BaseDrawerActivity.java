package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ActivityBaseDrawerBinding;

/**
 * Base class for all activities which require the side navigation drawer
 */
public abstract class BaseDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final long MAIN_CONTENT_FADEOUT_DURATION = 300;
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

    private void goToNavDrawerItem(int id) {
        if (id == R.id.nav_lists) {
            // TEST CODE, REMOVE THIS
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();

        } else if (id == R.id.nav_products) {
            Intent i = new Intent(this, ProductsListActivity.class);
            startActivity(i);
            finish();

        } else if (id == R.id.nav_suppliers) {
            Intent i = new Intent(this, SuppliersListActivity.class);
            startActivity(i);
            finish();

        } else if (id == R.id.nav_remote_quote) {

        } else if (id == R.id.nav_manual_quote) {

        } else if (id == R.id.nav_tutorial) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {

        }
    }

    protected abstract int getSelfNavDrawerItem();
}