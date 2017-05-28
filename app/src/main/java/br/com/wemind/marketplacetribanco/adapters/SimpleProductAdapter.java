package br.com.wemind.marketplacetribanco.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.activities.SupplierCreateActivity;
import br.com.wemind.marketplacetribanco.activities.SuppliersListActivity;
import br.com.wemind.marketplacetribanco.databinding.ItemSimpleProductBinding;
import br.com.wemind.marketplacetribanco.databinding.ItemSupplierBinding;
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.models.Supplier;

public class SimpleProductAdapter extends RecyclerView.Adapter<SimpleProductAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Product> data;
    private SimpleProductAdapter.Filter filter = new SimpleProductAdapter.Filter();
    private ArrayList<Product> filteredData;

    public SimpleProductAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
    }

    @Override
    public SimpleProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleProductAdapter.ViewHolder((ItemSimpleProductBinding) DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_simple_product,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(SimpleProductAdapter.ViewHolder vh, int position) {
        final Product product= filteredData.get(position);
        vh.b.product.setText(product.getSimpleDescription());
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    @Override
    public SimpleProductAdapter.Filter getFilter() {
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSimpleProductBinding b;

        public ViewHolder(ItemSimpleProductBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    public class Filter extends android.widget.Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO: inspect this
            // Naive filtering
            ArrayList<Product> filtered = new ArrayList<>();
            for (Product product : data) {
                if (product.getSimpleDescription().contains(constraint)) {
                    filtered.add(product);
                }
            }

            // Pack and return results
            FilterResults result = new FilterResults();
            result.count = filtered.size();
            result.values = filtered;
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Product>) results.values;
            notifyDataSetChanged();
        }
    }
}