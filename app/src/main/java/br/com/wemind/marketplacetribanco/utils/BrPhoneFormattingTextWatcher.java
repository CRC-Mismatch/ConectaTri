package br.com.wemind.marketplacetribanco.utils;

import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;

public class BrPhoneFormattingTextWatcher implements TextWatcher {
    private boolean selfChange = false;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (selfChange) {
            return;
        }

        String formatted = Formatting.formatBrazilianPhone(s.toString());

        selfChange = true;
        s.replace(0, s.length(), formatted);
        Selection.setSelection(s, s.length());
        selfChange = false;
    }
}
