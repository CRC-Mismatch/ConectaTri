package br.com.wemind.marketplacetribanco.utils;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;

import br.com.wemind.marketplacetribanco.utils.Formatting.StringFormatter;

public class FormattingTextWatcher implements TextWatcher {
    private final boolean hasMaxlength;
    private boolean selfChange;
    private StringFormatter formatter;
    private int maxLength;

    /**
     * Convenience for FormattingTextWatcher(<tt>formatter</tt>, 0)
     *
     * @param formatter
     */
    public FormattingTextWatcher(StringFormatter formatter) {
        this(formatter, 0);
    }


    /**
     * TextWatcher which applies <tt>formatter's</tt> {@link StringFormatter}.format() method
     * to its input {@link Editable}.
     *
     * @param formatter an implementation of {@link StringFormatter}
     * @param maxLength maximum length of the final output {@link Editable}.
     *                  If this parameter is less than 1, maximum length is
     *                  considered to be <em>unlimited</em>.
     */
    public FormattingTextWatcher(StringFormatter formatter, int maxLength) {
        selfChange = false;
        this.formatter = formatter;
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
        if (hasMaxlength && s.length() > maxLength) {
            // If this Editable exceeds maxLength, trim off exceeding chars
            selfChange = true;
            s.delete(maxLength, s.length());
            selfChange = false;
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