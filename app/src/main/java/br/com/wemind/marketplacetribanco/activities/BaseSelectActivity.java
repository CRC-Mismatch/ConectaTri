package br.com.wemind.marketplacetribanco.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ActivityBaseSelectBinding;

public abstract class BaseSelectActivity extends AppCompatActivity {
    protected ActivityBaseSelectBinding b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = DataBindingUtil.setContentView(this, R.layout.activity_base_select);

        setSupportActionBar(b.toolbar);

        b.search.setLayoutParams(new Toolbar.LayoutParams(Gravity.END));
    }
}
