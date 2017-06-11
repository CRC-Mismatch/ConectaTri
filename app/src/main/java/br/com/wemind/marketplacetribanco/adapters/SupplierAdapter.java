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

import java.util.ArrayList;
import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.activities.SupplierCreateActivity;
import br.com.wemind.marketplacetribanco.activities.SuppliersListActivity;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.databinding.ItemSupplierBinding;
import br.com.wemind.marketplacetribanco.models.Supplier;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private ArrayList<Supplier> data;
    private Filter filter = new Filter();
    private ArrayList<Supplier> filteredData;

    public SupplierAdapter(Context context, List<Supplier> data) {
        this.context = context;
        this.data = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((ItemSupplierBinding) DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_supplier,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        final Supplier supplier = filteredData.get(position);
        vh.b.txtSupplierName.setText(supplier.getSupplierName());
        vh.b.txtContactName.setText(supplier.getContactName());
        vh.b.txtContactEmail.setText(supplier.getContactEmail());
        vh.b.txtContactPhone.setText(supplier.getContactPhone());
        
        final long id = data.get(position).getId();
        vh.b.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.api.deleteSupplier(id).enqueue(
                        ((SuppliersListActivity) context).new DeleteSupplierCallback());
            }
        });

        vh.b.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle toEdit = new Bundle();
                toEdit.putParcelable(SupplierCreateActivity.INPUT_SUPPLIER, supplier);

                Intent edit = new Intent(context, SupplierCreateActivity.class);
                edit.putExtra(SupplierCreateActivity.INPUT_BUNDLE, toEdit);
                ((Activity) context)
                        .startActivityForResult(edit, SuppliersListActivity.EDIT_SUPPLIER);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSupplierBinding b;

        public ViewHolder(ItemSupplierBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    public class Filter extends android.widget.Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO: inspect this
            // Naive filtering
            ArrayList<Supplier> filtered = new ArrayList<>();
            for (Supplier supplier : data) {
                if (supplier.getSupplierName().contains(constraint)) {
                    filtered.add(supplier);
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
            filteredData = (ArrayList<Supplier>) results.values;
            notifyDataSetChanged();
        }
    }
}
