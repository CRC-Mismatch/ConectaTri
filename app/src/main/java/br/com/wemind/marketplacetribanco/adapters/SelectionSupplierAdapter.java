package br.com.wemind.marketplacetribanco.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.activities.SuppliersSelectActivity;
import br.com.wemind.marketplacetribanco.databinding.ItemSupplierSelectBinding;
import br.com.wemind.marketplacetribanco.databinding.ItemSuppliersSelectHeaderBinding;
import br.com.wemind.marketplacetribanco.models.Supplier;

public class SelectionSupplierAdapter extends
        RecyclerView.Adapter<SelectionSupplierAdapter.ViewHolder>
        implements Filterable {

    private static final int HEADER = 0;
    private static final int ITEM = 1;

    private Context context;
    private ArrayList<Supplier> data;
    private Filter filter = new Filter();
    private ArrayList<Supplier> filteredData;
    private Set<Supplier> selectedData;
    private Set<ViewHolder> items;

    public SelectionSupplierAdapter(Context context, List<Supplier> data,
                                    List<Supplier> selected) {
        this.context = context;

        data = data == null ? new ArrayList<Supplier>() : data;
        this.data = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);

        selected = selected == null ? new ArrayList<Supplier>() : selected;
        this.selectedData = new TreeSet<>(selected);
        items = new HashSet<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        if (viewType == HEADER) {
            vh = new ViewHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(context),
                            R.layout.item_suppliers_select_header,
                            parent,
                            false
                    ),
                    viewType
            );
        } else {
            vh = new ViewHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(context),
                            R.layout.item_supplier_select,
                            parent,
                            false
                    ),
                    viewType
            );
            items.add(vh);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        if (position == 0) {
            vh.hb.swAllSuppliers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for (ViewHolder vh : items) {
                        vh.ib.swSupplier.setChecked(isChecked);
                    }
                    if (isChecked) {
                        selectedData.addAll(data);
                    } else {
                        selectedData.clear();
                    }
                }
            });
        } else {
            final Supplier supplier = filteredData.get(position - 1);
            vh.ib.txtSupplierName.setText(supplier.getSupplierName());
            vh.ib.txtContactName.setText(supplier.getContactName());
            vh.ib.txtContactEmail.setText(supplier.getContactEmail());
            vh.ib.txtContactPhone.setText(supplier.getContactPhone());
            vh.ib.swSupplier.setChecked(selectedData.contains(supplier));
            vh.ib.swSupplier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!selectedData.contains(supplier))
                            selectedData.add(supplier);
                    } else {
                        if (selectedData.contains(supplier))
                            selectedData.remove(supplier);
                    }
                }
            });
        }
    }

    public void setData(List<Supplier> data) {
        this.data = new ArrayList<>(data);
        filteredData = new ArrayList<>(data);
    }

    @Override
    public int getItemCount() {
        return filteredData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? HEADER : ITEM;
    }

    public ArrayList<Supplier> getSelectedData() {
        return new ArrayList<>(selectedData);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSupplierSelectBinding ib;
        private final ItemSuppliersSelectHeaderBinding hb;

        public ViewHolder(ViewDataBinding b, int viewType) {
            super(b.getRoot());
            if (viewType == HEADER) {
                ib = null;
                hb = (ItemSuppliersSelectHeaderBinding) b;
            } else {
                hb = null;
                ib = (ItemSupplierSelectBinding) b;
            }
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
