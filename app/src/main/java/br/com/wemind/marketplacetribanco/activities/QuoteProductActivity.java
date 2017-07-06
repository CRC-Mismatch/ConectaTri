package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import java.util.List;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.QuoteSupplierAdapter;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.ListCallback;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.databinding.ActivityQuoteProductBinding;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;
import br.com.wemind.marketplacetribanco.models.Supplier;
import retrofit2.Call;

public class QuoteProductActivity extends BaseCreateActivity {

    public static final String QUOTE_PRODUCT = "QUOTE_PRODUCT";
    public static final String INPUT_IS_EDITABLE = "input_is_editable";
    public static final String RESULT_QUOTE_PRODUCT = "result_quote_product";

    ActivityQuoteProductBinding b;
    private boolean isEditable;
    private QuoteProduct quoteProduct;
    public TreeSet<Supplier> suppliers = new TreeSet<>();
    private QuoteSupplierAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_quote_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        quoteProduct = getIntent().getParcelableExtra(QUOTE_PRODUCT);
        b.productName.setText(quoteProduct.getProduct().getName());
        b.productEan.setText(quoteProduct.getProduct().getEAN());

        isEditable = getIntent().getBooleanExtra(INPUT_IS_EDITABLE, false);
        adapter = new QuoteSupplierAdapter(this, quoteProduct, isEditable, suppliers);
        b.quotes.setAdapter(adapter);

        b.quotes.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Api.api.getAllSuppliers().enqueue(new ListCallback<List<Supplier>>(this) {

            @Override
            public void onSuccess(List<Supplier> responseBody) {
                suppliers = new TreeSet<>(responseBody);
                adapter.setSupplierData(suppliers);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Call<List<Supplier>> call, ApiError responseErrorBody) {

            }
        });
    }

    @Override
    protected boolean validateForm() {
        return true;
    }

    @Override
    protected Intent getResultIntent() {
        Intent i = new Intent();
        if (isEditable) {
            i.putExtra(RESULT_QUOTE_PRODUCT, (Parcelable) quoteProduct);
        }
        return i;
    }

}
