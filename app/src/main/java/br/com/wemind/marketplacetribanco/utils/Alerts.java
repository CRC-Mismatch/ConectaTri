package br.com.wemind.marketplacetribanco.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import br.com.wemind.marketplacetribanco.R;

public class Alerts {

    public static AlertDialog.Builder getDeleteConfirmationAlert(
            String itemName, Context context,
            DialogInterface.OnClickListener onPositive,
            DialogInterface.OnClickListener onNegative) {

        return new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.text_attention))
                .setMessage(context.getString(R.string.text_confirm_delete_item_name, itemName))
                .setPositiveButton(android.R.string.yes, onPositive)
                .setNegativeButton(android.R.string.no, onNegative);
    }
}
