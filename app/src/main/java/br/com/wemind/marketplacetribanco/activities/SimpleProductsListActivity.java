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
import br.com.wemind.marketplacetribanco.adapters.SimpleProductAdapter;
import br.com.wemind.marketplacetribanco.databinding.ContentSimpleProductsListBinding;
import br.com.wemind.marketplacetribanco.models.Product;

public class SimpleProductsListActivity extends BaseDrawerActivity {

    public static final int CREATE_PRODUCT = 1;
    public static final int EDIT_PRODUCT = 2;
    private ContentSimpleProductsListBinding cb;
    /**
     * Entire data payload received from retrieveData()
     */
    private ArrayList<Product> data;
    private SimpleProductAdapter adapter;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        // FIXME: 24/05/2017 actually retrieve data
        retrieveData();
    }

    private void retrieveData() {
        // FIXME: 24/05/2017 start data retrieval here
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create and send dummy data
                ArrayList<Product> data = new ArrayList<>(100);

                for (int i = 1; i <= 100; ++i) {
                    data.add(new Product().setName("Produto " + i));
                }
                onDataReceived(data);
            }
        }, 2000);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_PRODUCT) {
            if (resultCode == RESULT_OK) {
                Product edited = data.getBundleExtra(ProductCreateActivity.RESULT_BUNDLE)
                        .getParcelable(ProductCreateActivity.RESULT_PRODUCT);

                if (edited != null) {
                    // FIXME: 25/05/2017 send new data to server
                    Toast.makeText(
                            this,
                            edited.getName() + " foi editado",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            } else {

            }
        } else if (requestCode == CREATE_PRODUCT) {
            if (resultCode == RESULT_OK) {
                Product edited = data.getBundleExtra(ProductCreateActivity.RESULT_BUNDLE)
                        .getParcelable(ProductCreateActivity.RESULT_PRODUCT);

                if (edited != null) {
                    // FIXME: 25/05/2017 send new data to server
                    Toast.makeText(
                            this,
                            edited.getName() + " foi adicionado",
                            Toast.LENGTH_SHORT
                    ).show();
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
}
