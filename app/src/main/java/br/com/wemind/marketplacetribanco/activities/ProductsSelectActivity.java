package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.ProductsSelectAdapter;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.ListCallback;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.databinding.ContentProductsSelectBinding;
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.utils.TimerManager;
import retrofit2.Call;
import retrofit2.Response;

public class ProductsSelectActivity extends BaseSelectActivity {

    public static final String INPUT_PRODUCTS = "input_products";
    public static final String INPUT_SELECTED = "input_selected";
    public static final String INPUT_BUNDLE = "input_bundle";
    public static final String RESULT_BUNDLE = "result_bundle";
    public static final String SELECTED_LIST = "result_selected";
    public static final int QUERY_CHANGED_TIMEOUT = 500; // milliseconds
    private ProductsSelectAdapter adapter;
    private ContentProductsSelectBinding cb;
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Product> selected = new ArrayList<>();
    private boolean isDataReady;
    private TimerManager timerManager;

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

            ArrayList<Product> inputProducts =
                    bundleExtra.getParcelableArrayList(INPUT_PRODUCTS);
            if (inputProducts != null) {
                products = new ArrayList<>(inputProducts);
            }
        }

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
        timerManager = new TimerManager("preQueryTimer");
        b.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public int cancelledTasks = 0;
            private TimerTask queryTask;

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
            public boolean onQueryTextChange(final String newText) {
                if (newText.length() > 2 || newText.length() <= 0) {
                    timerManager.schedule(QUERY_CHANGED_TIMEOUT, new TimerTask() {
                        @Override
                        public void run() {
                            adapter.getFilter().filter(newText);
                        }   
                    });
                }
                return true;
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
    protected void packResultIntent() {
        if (adapter != null) {
            selected = adapter.getSelectedList();
        }

        Bundle b1 = new Bundle();
        b1.putParcelableArrayList(SELECTED_LIST, selected);

        Intent i = new Intent();
        i.putExtra(RESULT_BUNDLE, b1);

        setResult(RESULT_OK, i);
    }

    @Override
    protected void onPause() {
        timerManager.cancel();
        super.onPause();
    }

    @Override
    protected boolean mayContinue() {
        if (!isDataReady) {
            return false;
        }

        List selected = adapter.getSelectedList();
        if (selected.size() <= 0) {
            // The user hasn't selected any items,
            // show error
            Toast.makeText(this,
                    getString(R.string.error_selection_required),
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerManager.restart();

        if (products == null || products.size() == 0) {
            // Disable "next" while data hasn't been retrieved
            isDataReady = false;

            // If no products are passed in as parameters,
            // get products from DB
            // FIXME: 09/06/2017 use API search method instead
            Api.api.getAllProducts().enqueue(new GetProductsCallback());
        } else {
            isDataReady = true;
        }
    }

    private void onDataReceived(List<Product> data) {
        if (data == null) {
            data = new ArrayList<>();
        }

        products = new ArrayList<>(data);
        adapter.setData(products);
        adapter.notifyDataSetChanged();
        isDataReady = true;
    }

    private class GetProductsCallback extends ListCallback<List<Product>> {
        public GetProductsCallback() {
            super(ProductsSelectActivity.this);
        }

        @Override
        public void onSuccess(List<Product> response) {
            onDataReceived(response);
        }

        @Override
        public void onError(Call<List<Product>> call, ApiError response) {
            Toast.makeText(context,
                    getString(R.string.text_connection_failed),
                    Toast.LENGTH_SHORT
            ).show();
            finish();
        }
    }
}
