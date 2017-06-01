package br.com.wemind.marketplacetribanco.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.activities.QuoteProductActivity;
import br.com.wemind.marketplacetribanco.databinding.ItemQuoteSupplierBinding;
import br.com.wemind.marketplacetribanco.databinding.ItemSimpleProductBinding;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;
import br.com.wemind.marketplacetribanco.models.QuoteSupplier;

public class QuoteSupplierAdapter extends RecyclerView.Adapter<QuoteSupplierAdapter.ViewHolder> {

    private Context context;
    private ArrayList<QuoteSupplier> data;

    public QuoteSupplierAdapter(Context context, QuoteProduct product) {
        this.context = context;
        this.data = new ArrayList<>(product.getSuppliers());
    }

    @Override
    public QuoteSupplierAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuoteSupplierAdapter.ViewHolder(ItemQuoteSupplierBinding.inflate(((Activity)context).getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(QuoteSupplierAdapter.ViewHolder vh, int position) {
        final QuoteSupplier quoteSupplier = data.get(position);
        vh.b.price.setText(context.getString(R.string.money, quoteSupplier.getPrice()));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuoteSupplierBinding b;

        public ViewHolder(ItemQuoteSupplierBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
