package br.com.wemind.marketplacetribanco.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.awt.font.TextAttribute;
import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ItemSupplierBinding;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ViewHolder> {

    private Context context;
    private List data;

    public SupplierAdapter(Context context, List data) {
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
    public void onBindViewHolder(ViewHolder vh, final int position) {
        // FIXME: bind data and event handlers
        vh.b.txtSupplierName.setText("Fornecedor " + position);
        vh.b.txtContactName.setText("Juvenildo Souza");
        vh.b.txtContactEmail.setText("juvenildo@fornecedor" + position + ".com.br");
        vh.b.txtContactPhone.setText("(11) 5656-000" + position);

        vh.b.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete " + position, Toast.LENGTH_SHORT).show();
            }
        });

        vh.b.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Edit " + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        // FIXME: 24/05/2017
        return 10;
        // return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSupplierBinding b;

        public ViewHolder(ItemSupplierBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
