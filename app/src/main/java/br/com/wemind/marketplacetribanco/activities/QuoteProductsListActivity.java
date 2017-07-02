package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.QuoteProductAdapter;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.databinding.ContentSimpleProductsListBinding;
import br.com.wemind.marketplacetribanco.models.Quote;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;
import retrofit2.Call;

public class QuoteProductsListActivity extends BaseSelectActivity {

    public static final String QUOTE = "QUOTE";
    public static final String INPUT_IS_EDITABLE = "input_is_editable";
    public static final int REQUEST_EDIT_QUOTE_PRODUCT = 1;
    private ContentSimpleProductsListBinding cb;
    private QuoteProductAdapter adapter;
    private boolean isEditable;
    private Quote quote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quote = getIntent().getParcelableExtra(QUOTE);

        // Setup response to search query
        // Reset filtered data when user closes search view
        b.search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter.getFilter().filter("");
                return false;
            }
        });

        // Search payload data and update filtered data upon user input
        b.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);

                // Hide keyboard and clear focus
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                b.search.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        // End of search view setup

        // Setup content view
        cb = DataBindingUtil.inflate(getLayoutInflater(), R.layout.content_simple_products_list,
                b.contentFrame, true);

        cb.list.setLayoutManager(new LinearLayoutManager(this));

        isEditable = getIntent().getBooleanExtra(INPUT_IS_EDITABLE, false);

        adapter = new QuoteProductAdapter(
                this,
                quote,
                isEditable
        );
        cb.list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return isEditable && super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_QUOTE_PRODUCT) {
            if (resultCode == RESULT_OK) {
                QuoteProduct quoteProduct =
                        data.getParcelableExtra(QuoteProductActivity.RESULT_QUOTE_PRODUCT);

                if (quoteProduct != null) {
                    List<QuoteProduct> qpList = quote.getQuoteProducts();
                    qpList.remove(quoteProduct);
                    qpList.add(quoteProduct);
                    adapter = new QuoteProductAdapter(
                            this,
                            quote,
                            isEditable
                    );
                    cb.list.setAdapter(adapter);
                }
            }
        }
    }

    @Override
    protected void packResultIntent() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_ok) {
            if (mayContinue()) {
                Api.api.editQuote(quote, quote.getId())
                        .enqueue(new EditQuoteCallback());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean mayContinue() {
        return true;
    }

    private class EditQuoteCallback extends Callback<Quote> {
        public EditQuoteCallback() {
            super(QuoteProductsListActivity.this);
        }

        @Override
        public void onSuccess(Quote response) {
            finish();
        }

        @Override
        public void onError(Call<Quote> call, ApiError response) {
            finish();
        }
    }
}
