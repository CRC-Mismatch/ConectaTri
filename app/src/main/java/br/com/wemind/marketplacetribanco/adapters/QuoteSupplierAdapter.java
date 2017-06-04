package br.com.wemind.marketplacetribanco.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ItemQuoteSupplierBinding;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;
import br.com.wemind.marketplacetribanco.models.QuoteSupplier;

public class QuoteSupplierAdapter extends RecyclerView.Adapter<QuoteSupplierAdapter.ViewHolder> {

    private Context context;
    private ArrayList<QuoteSupplier> data;

    public QuoteSupplierAdapter(Context context, QuoteProduct product) {
        this.context = context;
        this.data = new ArrayList<>(product.getSuppliers());
        Collections.sort(this.data, new Comparator<QuoteSupplier>() {
            @Override
            public int compare(QuoteSupplier o1, QuoteSupplier o2) {
                return o1.compareTo(o2);
            }
        });
    }

    @Override
    public QuoteSupplierAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuoteSupplierAdapter.ViewHolder(ItemQuoteSupplierBinding.inflate(((Activity)context).getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(QuoteSupplierAdapter.ViewHolder vh, int position) {
        final QuoteSupplier quoteSupplier = data.get(position);
        vh.b.price.setText(context.getString(R.string.money, quoteSupplier.getPrice()));
        vh.b.qty.setText(String.valueOf(quoteSupplier.getQuantity()));
        vh.b.supplier.setText(quoteSupplier.getSupplierRep().getSupplierName());
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
