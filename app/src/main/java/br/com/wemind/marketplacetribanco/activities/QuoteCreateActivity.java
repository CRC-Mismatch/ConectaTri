package br.com.wemind.marketplacetribanco.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ContentQuoteCreateBinding;
import br.com.wemind.marketplacetribanco.models.Quote;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;

public class QuoteCreateActivity extends BaseCreateActivity {

    public static final String INPUT_BUNDLE = "input_bundle";
    public static final String INPUT_QUOTE = "input_quote";
    public static final String RESULT_QUOTE = "result_quote";
    public static final String RESULT_BUNDLE = "result_bundle";
    private ContentQuoteCreateBinding cb;
    /**
     * For storing the start and end dates to be formatted and
     * passed to the API
     */
    private HashMap<EditText, Calendar> dates = new HashMap<>(2);
    private Quote quote;

    @NonNull
    private static String formatHour(int selectedHour, int selectedMinute) {
        return String.format("%02d", selectedHour)
                + ":"
                + String.format("%02d", selectedMinute);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cb = ContentQuoteCreateBinding.inflate(getLayoutInflater(), b.contentFrame, true);

        // Setup DatePickerDialog
        final Calendar calendar = Calendar.getInstance();
        final OnDateSetListener dateListener = new OnDateSetListener(calendar);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        Bundle inputBundle = getIntent().getBundleExtra(INPUT_BUNDLE);
        if (inputBundle != null) {
            Quote inputQuote = inputBundle.getParcelable(INPUT_QUOTE);
            if (inputQuote != null) {
                quote = inputQuote;

                // If there's an input quote, initialize fields
                cb.edtName.setText(quote.getName());

                // Initialize dates and times
                Calendar inputCalendar = (Calendar) calendar.clone();
                inputCalendar.setTimeInMillis(quote.getBeginningDate().getTime());
                updateDateEditText(inputCalendar, cb.edtDateFrom);
                cb.edtTimeFrom.setText(formatHour(
                        inputCalendar.get(Calendar.HOUR_OF_DAY),
                        inputCalendar.get(Calendar.MINUTE)));

                inputCalendar.setTimeInMillis(quote.getExpirationDate().getTime());
                updateDateEditText(inputCalendar, cb.edtDateUntil);
                cb.edtTimeUntil.setText(formatHour(
                        inputCalendar.get(Calendar.HOUR_OF_DAY),
                        inputCalendar.get(Calendar.MINUTE)));

                cb.swCloseQuote.setChecked(quote.isClosed());
            }
        } else {
            cb.edtTimeFrom.setText(formatHour(
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE)));

            cb.edtTimeUntil.setText(formatHour(
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE)));

        }

        cb.btnDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateListener.target = cb.edtDateFrom;
                datePickerDialog.show();
            }
        });

        cb.btnDateUntil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateListener.target = cb.edtDateUntil;
                datePickerDialog.show();
            }
        });
        // End of DatePickerDialog setup

        // Setup TimePickerDialog
        cb.btnTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(QuoteCreateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                Calendar calendar = dates.get(cb.edtDateFrom);
                                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                calendar.set(Calendar.MINUTE, selectedMinute);
                                cb.edtTimeFrom
                                        .setText(formatHour(selectedHour, selectedMinute));
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        cb.btnTimeUntil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(QuoteCreateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                Calendar calendar = dates.get(cb.edtDateUntil);
                                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                calendar.set(Calendar.MINUTE, selectedMinute);
                                cb.edtTimeUntil
                                        .setText(formatHour(selectedHour, selectedMinute));
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

    }

    private void updateDateEditText(Calendar calendar, EditText target) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        target.setText(dateFormat.format(calendar.getTime()));
        dates.put(target, (Calendar) calendar.clone());
    }

    @Override
    protected boolean validateForm() {
        // Check for empty name field
        if (cb.edtName.length() <= 0) {
            cb.edtName.setError(getString(R.string.error_field_required));
            cb.edtName.requestFocus();
            return false;
        }

        // Check for valid dates
        // Check if start date < end date
        if (dates.get(cb.edtDateFrom).getTimeInMillis()
                >= dates.get(cb.edtDateUntil).getTimeInMillis()) {
            Toast.makeText(
                    this,
                    getString(R.string.error_invalid_dates),
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        }

        return true;
    }

    @Override
    protected Intent getResultIntent() {
        Quote quote = new Quote();
        if (this.quote == null) {
            quote.setQuoteProducts(new ArrayList<QuoteProduct>());

        } else {
            quote = this.quote;
        }

        quote.setName(cb.edtName.getText().toString())
                .setBeginningDate(dates.get(cb.edtDateFrom).getTime())
                .setExpirationDate(dates.get(cb.edtDateUntil).getTime());

        quote.setClosed(cb.swCloseQuote.isChecked());

        Bundle b = new Bundle();
        b.putParcelable(RESULT_QUOTE, quote);
        Intent i = new Intent();
        i.putExtra(RESULT_BUNDLE, b);

        return i;
    }

    private class OnDateSetListener implements DatePickerDialog.OnDateSetListener {

        public EditText target;
        public Calendar calendar;

        public OnDateSetListener(Calendar calendar) {
            this.calendar = calendar;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateEditText(calendar, target);
        }
    }
}
