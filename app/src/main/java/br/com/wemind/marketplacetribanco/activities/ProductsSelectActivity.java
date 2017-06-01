package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.ProductsSelectAdapter;
import br.com.wemind.marketplacetribanco.databinding.ContentProductsSelectBinding;
import br.com.wemind.marketplacetribanco.models.Product;

public class ProductsSelectActivity extends BaseSelectActivity {

    public static final String SELECTED_LIST = "select_list";
    public static final String INPUT_PRODUCTS = "input_products";
    public static final String INPUT_SELECTED = "input_selected";
    public static final String INPUT_BUNDLE = "input_bundle";
    public static final String RESULT_BUNDLE = "result_bundle";
    public static final String RESULT_SELECTED = "result_selected";
    private ProductsSelectAdapter adapter;
    private ContentProductsSelectBinding cb;
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Product> selected = new ArrayList<>();

    @Override
    public void onBackPressed() {
        packForFinish();

        super.onBackPressed();
    }

    private void packForFinish() {
        if (adapter != null) {
            selected = adapter.getSelectedList();
        }

        Bundle b = new Bundle();
        b.putParcelableArrayList(RESULT_SELECTED, selected);

        Intent i = new Intent();
        i.putExtra(RESULT_BUNDLE, b);

        setResult(RESULT_OK, i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent i = getIntent();
        if (i != null) {
            Bundle bundleExtra = i.getBundleExtra(INPUT_BUNDLE);
            ArrayList<Product> inputSelected =
                    bundleExtra.getParcelableArrayList(INPUT_SELECTED);

            if (inputSelected != null) {
                selected = new ArrayList<>(inputSelected);
            }
        }

        b.fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_continue));
        b.fab.setVisibility(View.VISIBLE);
        // FIXME: Implement real list manipulation
        b.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packForFinish();
                finish();
            }
        });

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
        cb = ContentProductsSelectBinding
                .inflate(getLayoutInflater(), b.contentFrame, true);

        cb.list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductsSelectAdapter(this, products, new TreeSet<>(selected));
        cb.list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (products == null || products.size() == 0) {
            // TODO: 01/06/2017 start query to get product list from DB

            // TODO: remove this after implementation
            ArrayList<Product> dummyList = new ArrayList<>();
            for (int i = 0; i < 800; i++) {
                Product p = new Product();
                p.setName("Produto " + i);
                p.setBrand("Marca " + i);
                p.setId(i + 1);
                dummyList.add(p);
            }
            products = dummyList;
            adapter.setData(products);
            adapter.notifyDataSetChanged();
            // ---------
        }
    }
}
