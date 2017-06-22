package br.com.wemind.marketplacetribanco.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ItemQuoteSupplierBinding;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;
import br.com.wemind.marketplacetribanco.models.QuoteSupplier;
import br.com.wemind.marketplacetribanco.utils.Formatting;

public class QuoteSupplierAdapter extends RecyclerView.Adapter<QuoteSupplierAdapter.ViewHolder> {

    private final boolean isEditable;
    private Context context;
    private List<QuoteSupplier> data;

    public QuoteSupplierAdapter(Context context, QuoteProduct product,
                                boolean isEditable) {
        this.context = context;
        this.data = product.getQuoteSuppliers();
        Collections.sort(this.data, new Comparator<QuoteSupplier>() {
            @Override
            public int compare(QuoteSupplier o1, QuoteSupplier o2) {
                return o1.compareTo(o2);
            }
        });
        this.isEditable = isEditable;
    }

    @Override
    public QuoteSupplierAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        return new QuoteSupplierAdapter.ViewHolder(
                ItemQuoteSupplierBinding.inflate(
                        ((Activity) context).getLayoutInflater(), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(QuoteSupplierAdapter.ViewHolder vh, int position) {
        final QuoteSupplier quoteSupplier = data.get(position);
        vh.b.price.setText(context.getString(R.string.money, quoteSupplier.getPrice()));
        vh.b.qty.setText(String.valueOf(quoteSupplier.getQuantity()));
        vh.b.supplier.setText(quoteSupplier.getSupplier().getSupplierName());

        vh.b.price.setEnabled(isEditable);
        vh.b.qty.setEnabled(isEditable);
        vh.b.supplier.setEnabled(isEditable);

        if (isEditable) {
            vh.b.price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    String value = s.toString().replaceAll("[^0-9\\.]","");
                    if (!value.equals("")) {
                        quoteSupplier.setPrice(Double.valueOf(value));
                    }
                }
            });

            vh.b.qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals("")) {
                        quoteSupplier.setQuantity(Integer.valueOf(s.toString()));
                    }
                }
            });
        }
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
