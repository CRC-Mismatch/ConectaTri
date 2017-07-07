package br.com.wemind.marketplacetribanco.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.MainButtonsAdapter;
import br.com.wemind.marketplacetribanco.databinding.ContentMainBinding;

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
        return R.id.nav_main;
    }
}
