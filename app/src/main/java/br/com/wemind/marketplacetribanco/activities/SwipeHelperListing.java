package br.com.wemind.marketplacetribanco.activities;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import br.com.wemind.marketplacetribanco.adapters.ListingsAdapter;
import br.com.wemind.marketplacetribanco.adapters.SupplierAdapter;

/**
 * Created by Rafay on 7/9/2017.
 */

public class SwipeHelperListing extends ItemTouchHelper.SimpleCallback {

    ListingsAdapter adapter;

    public SwipeHelperListing(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }


    public SwipeHelperListing(ListingsAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT );
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        adapter.dismissItem(viewHolder.getAdapterPosition());

    }
}
