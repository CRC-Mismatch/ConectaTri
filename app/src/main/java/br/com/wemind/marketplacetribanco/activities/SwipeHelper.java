package br.com.wemind.marketplacetribanco.activities;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import br.com.wemind.marketplacetribanco.adapters.SupplierAdapter;

/**
 * Created by Rafay on 7/9/2017.
 */

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    SupplierAdapter adapter;

    public SwipeHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }


    public SwipeHelper(SupplierAdapter adapter) {
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
