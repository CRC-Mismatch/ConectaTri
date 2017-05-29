package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ActivityQuoteTypeBinding;
import br.com.wemind.marketplacetribanco.databinding.ActivityRemoteQuotesBinding;

import static android.view.View.GONE;

public class RemoteQuotesActivity extends BaseDrawerActivity {

    ActivityRemoteQuotesBinding cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b.search.setVisibility(GONE);
        cb = ActivityRemoteQuotesBinding.inflate(getLayoutInflater(), b.contentFrame, true);
        cb.btnRemoteQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RemoteQuotesActivity.this, ListingsSelectActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_remote_quote;
    }

}
