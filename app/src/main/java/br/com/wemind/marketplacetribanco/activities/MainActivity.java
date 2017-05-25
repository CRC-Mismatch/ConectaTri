package br.com.wemind.marketplacetribanco.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.MainButtonsAdapter;
import br.com.wemind.marketplacetribanco.databinding.ContentMainBinding;
import br.com.wemind.marketplacetribanco.databinding.ContentProductsListBinding;

public class MainActivity extends BaseDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ContentMainBinding cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cb = ContentMainBinding.inflate(getLayoutInflater(), b.contentFrame, true);
        cb.buttonsGrid.setAdapter(new MainButtonsAdapter(this));
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_products;
    }
}
