package br.com.wemind.marketplacetribanco.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ItemSupplierBinding;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ViewHolder> {

    private Context context;
    private List<Supplier> data;

    public SupplierAdapter(Context context, List<Supplier> data) {
        this.context = context;
        this.data = data;
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
        final Supplier supplier = data.get(position);
        vh.b.txtSupplierName.setText(supplier.getSupplierName());
        vh.b.txtContactName.setText(supplier.getContactName());
        vh.b.txtContactEmail.setText(supplier.getContantEmail());
        vh.b.txtContactPhone.setText(supplier.getContantPhone());

        // FIXME: 25/05/2017 bind event handlers
        vh.b.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete " + supplier.getSupplierName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        vh.b.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Edit " + supplier.getSupplierName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class Supplier {
        private String name;
        private String contactName;
        private String contantEmail;
        private String contantPhone;

        public Supplier(String name, String contactName,
                        String contantEmail, String contantPhone) {
            this.name = name;
            this.contactName = contactName;
            this.contantEmail = contantEmail;
            this.contantPhone = contantPhone;
        }

        public String getSupplierName() {
            return name;
        }

        public String getContactName() {
            return contactName;
        }

        public String getContantEmail() {
            return contantEmail;
        }

        public String getContantPhone() {
            return contantPhone;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSupplierBinding b;

        public ViewHolder(ItemSupplierBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
