package br.com.wemind.marketplacetribanco.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.SupplierAdapter;
import br.com.wemind.marketplacetribanco.databinding.ContentSuppliersListBinding;

public class ProductsListActivity extends BaseDrawerActivity {

    private ContentSuppliersListBinding cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup content view
        cb = DataBindingUtil.inflate(getLayoutInflater(), R.layout.content_suppliers_list,
                b.contentFrame, true);

        cb.list.setLayoutManager(new LinearLayoutManager(this));

        // FIXME: 24/05/2017 actually retrieve data
        retrieveData();
    }

    private void retrieveData() {
        // FIXME: 24/05/2017 start data retrieval here
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                onDataReceived();
            }
        }, 2000);
    }

    private void onDataReceived() {
        cb.list.setAdapter(new SupplierAdapter(this, null));
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_suppliers;
    }
}
