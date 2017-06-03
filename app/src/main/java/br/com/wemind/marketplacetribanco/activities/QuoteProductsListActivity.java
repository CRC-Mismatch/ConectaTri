package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.QuoteProductAdapter;
import br.com.wemind.marketplacetribanco.adapters.SimpleProductAdapter;
import br.com.wemind.marketplacetribanco.databinding.ContentSimpleProductsListBinding;
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.models.Quote;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;

public class QuoteProductsListActivity extends BaseSelectActivity {

    public static final String QUOTE = "QUOTE";

    private ContentSimpleProductsListBinding cb;
    /**
     * Entire data payload received from retrieveData()
     */
    private ArrayList<QuoteProduct> data;
    private SimpleProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        cb.list.setAdapter(new QuoteProductAdapter(this, getIntent().<Quote>getParcelableExtra(QUOTE)));
    }

    @Override
    protected void packResultIntent() {

    }

    @Override
    protected boolean mayContinue() {
        return false;
    }
}
