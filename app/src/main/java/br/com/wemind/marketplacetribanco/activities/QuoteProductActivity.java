package br.com.wemind.marketplacetribanco.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.QuoteSupplierAdapter;
import br.com.wemind.marketplacetribanco.databinding.ActivityQuoteProductBinding;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;

public class QuoteProductActivity extends AppCompatActivity {

    public static final String QUOTE_PRODUCT = "QUOTE_PRODUCT";
    public static final String INPUT_IS_EDITABLE = "input_is_editable";

    ActivityQuoteProductBinding b;
    private boolean isEditable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_quote_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        QuoteProduct product = getIntent().getParcelableExtra(QUOTE_PRODUCT);
        b.productName.setText(product.getProduct().getName());
        b.productEan.setText(product.getProduct().getEAN());

        // TODO: 18/06/2017 construct the adapter correctly if isEditable
        isEditable = getIntent().getBooleanExtra(INPUT_IS_EDITABLE, false);
        b.quotes.setAdapter(new QuoteSupplierAdapter(this, product, isEditable));

        b.quotes.setLayoutManager(new LinearLayoutManager(this));
    }

}
