package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ActivityQuoteTypeBinding;

public class QuoteTypeActivity extends AppCompatActivity {

    ActivityQuoteTypeBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_quote_type);
        Toolbar toolbar = b.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        b.btnRemoteQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        QuoteTypeActivity.this, QuoteCreationFlowController.class);
                startActivity(i);
                finish();
            }
        });

        b.btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        QuoteTypeActivity.this, QuoteCreationFlowController.class);
                i.putExtra(QuoteCreationFlowController.INPUT_IS_MANUAL, true);
                startActivity(i);
                finish();
            }
        });

        b.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuoteTypeActivity.this, QuotesListActivity.class);
                i.putExtra(QuotesListActivity.REMOTE_ONLY, true);
                startActivity(i);
                finish();
            }
        });
    }

}
