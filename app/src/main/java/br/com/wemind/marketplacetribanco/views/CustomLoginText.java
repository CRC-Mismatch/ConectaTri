package br.com.wemind.marketplacetribanco.views;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class CustomLoginText extends AppCompatEditText {

    public CustomLoginText(Context context) {
        this(context, null);
    }

    public CustomLoginText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public CustomLoginText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnFocusChangeListener(new SimpleOnFocusChangeListener());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (getOnFocusChangeListener() != null) getOnFocusChangeListener().onFocusChange(this, true);
    }

    private class SimpleOnFocusChangeListener implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                setPaddingRelative(
                        Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics())),
                        Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics())),
                        0,
                        Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()))
                );
            } else {
                if (getText().length() == 0)
                    setPaddingRelative(
                            Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics())),
                            0,
                            0,
                            Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()))
                    );
            }
        }
    }
}

