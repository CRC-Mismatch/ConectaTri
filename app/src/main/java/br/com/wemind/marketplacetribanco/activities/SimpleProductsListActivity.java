package br.com.wemind.marketplacetribanco.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.SimpleProductAdapter;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.api.ListCallback;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.databinding.ContentSimpleProductsListBinding;
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.utils.TimerManager;
import retrofit2.Call;

public class SimpleProductsListActivity extends BaseDrawerActivity {

    public static final int CREATE_PRODUCT = 1;
    public static final int EDIT_PRODUCT = 2;
    public static final int QUERY_TIMER_DELAY = 500;
    private ContentSimpleProductsListBinding cb;
    /**
     * Entire data payload received from retrieveData()
     */
    private ArrayList<Product> data;
    private SimpleProductAdapter adapter;
    private TimerManager timerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b.fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white));
        b.fab.setVisibility(View.VISIBLE);
        b.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SimpleProductsListActivity.this, ProductCreateActivity.class);
                startActivityForResult(i, CREATE_PRODUCT);
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
        timerManager = new TimerManager("preQueryTimer");
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
            public boolean onQueryTextChange(final String newText) {
                if (newText.length() > 2 || newText.length() <= 0) {
                    timerManager.schedule(QUERY_TIMER_DELAY, new TimerTask() {
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
        cb = DataBindingUtil.inflate(getLayoutInflater(), R.layout.content_simple_products_list,
                b.contentFrame, true);

        cb.list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleProductAdapter(this, new ArrayList<Product>());
        cb.list.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerManager.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerManager.restart();
        adapter.refilter();
    }

    /*private void retrieveData() {
        Api.api.getAllProducts().enqueue(new GetProductsCallback(this));
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_PRODUCT) {
            if (resultCode == RESULT_OK) {
                Product edited = data.getBundleExtra(ProductCreateActivity.RESULT_BUNDLE)
                        .getParcelable(ProductCreateActivity.RESULT_PRODUCT);

                if (edited != null) {
                    Api.api.editProduct(edited, edited.getId()).enqueue(
                            new EditProductCallback(this)
                    );
                }
            } else {

            }
        } else if (requestCode == CREATE_PRODUCT) {
            if (resultCode == RESULT_OK) {
                Product edited = data.getBundleExtra(ProductCreateActivity.RESULT_BUNDLE)
                        .getParcelable(ProductCreateActivity.RESULT_PRODUCT);

                if (edited != null) {
                    // Check if user selected an existing product from the database
                    Bundle resultBundle =
                            data.getBundleExtra(ProductCreateActivity.RESULT_BUNDLE);
                    boolean userSelectedExistingProduct = resultBundle.getBoolean(
                            ProductCreateActivity.RESULT_EXISTING_PRODUCT, false);

                    if (userSelectedExistingProduct) {
                        // Edit instead of Add
                        Api.api.editProduct(edited, edited.getId()).enqueue(
                                new EditProductCallback(this)
                        );
                    } else {
                        Api.api.addProduct(edited).enqueue(
                                new CreateProductCallback(this)
                        );
                    }
                }
            } else {

            }
        }
    }

    private void onDataReceived(ArrayList<Product> data) {
        this.data = data;
        adapter = new SimpleProductAdapter(this, data);
        cb.list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_products;
    }

    private class GetProductsCallback extends ListCallback<List<Product>> {
        public GetProductsCallback(@NonNull Context context) {
            super(context);
        }

        @Override
        public void onSuccess(List<Product> response) {
            onDataReceived(new ArrayList<>(response));
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

    private class EditProductCallback extends Callback<Product> {
        public EditProductCallback(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(Product response) {
            refreshData();
        }

        @Override
        public void onError(Call<Product> call, ApiError response) {
            refreshData();
        }

        private void refreshData() {
            /*retrieveData();*/
        }
    }

    private class CreateProductCallback extends Callback<Product> {
        public CreateProductCallback(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(Product response) {
            refreshData();
        }

        @Override
        public void onError(Call<Product> call, ApiError response) {
            refreshData();
        }

        private void refreshData() {
            /*retrieveData();*/
        }
    }
}
