package br.com.wemind.marketplacetribanco.utils;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;

public class BrPhoneFormattingTextWatcher implements TextWatcher {
    private boolean selfChange = false;
    private boolean hasMaxlength = false;
    private int maxLength;

    public BrPhoneFormattingTextWatcher() {
        this(15);
    }

    public BrPhoneFormattingTextWatcher(int maxLength) {
        this.maxLength = maxLength;
        hasMaxlength = maxLength > 0;
    }

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

        int maxLength = this.maxLength - 1;
        if (hasMaxlength && s.length() > maxLength) {
            // If this Editable exceeds maxLength, trim off exceeding chars
            selfChange = true;
            if (Formatting.onlyNumbers(s.toString()).charAt(2) == '9' || Formatting.onlyNumbers(s.toString()).charAt(2) == '0') {
                maxLength += 1;
            }
            s.delete(maxLength, s.length());
            selfChange = false;
        }

        String formatted = Formatting.formatBrazilianPhone(s.toString());

        selfChange = true;
        s.replace(0, s.length(), formatted);
        Selection.setSelection(s, s.length());
        selfChange = false;
    }
}
