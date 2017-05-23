package br.com.wemind.marketplacetribanco.utils;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;

import br.com.wemind.marketplacetribanco.utils.Formatting.StringFormatter;

public class FormattingTextWatcher implements TextWatcher {
    private boolean selfChange;
    private StringFormatter formatter;

    public FormattingTextWatcher(StringFormatter formatter) {
        selfChange = false;
        this.formatter = formatter;
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

        String formatted = formatter.format(s.toString());
        if (formatted != null) {
            selfChange = true;
            s.replace(0, s.length(), formatted);
            Selection.setSelection(s, s.length());
            selfChange = false;
        }
    }
}