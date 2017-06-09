package br.com.wemind.marketplacetribanco.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
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
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.models.Quote;
import br.com.wemind.marketplacetribanco.models.Supplier;

public class QuoteCreateActivity extends BaseCreateActivity {

    private static final String RESULT_QUOTE = "result_quote";
    public static final String RESULT_BUNDLE = "result_bundle";
    private ContentQuoteCreateBinding cb;
    /**
     * For storing the start and end dates to be formatted and
     * passed to the API
     */
    private HashMap<EditText, Calendar> dates = new HashMap<>(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup DatePickerDialog
        final Calendar calendar = Calendar.getInstance();
        final OnDateSetListener dateListener = new OnDateSetListener(calendar);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        cb = DataBindingUtil.setContentView(this, R.layout.content_quote_create);
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

    @NonNull
    private static String formatHour(int selectedHour, int selectedMinute) {
        return String.format("%02d", selectedHour)
                + ":"
                + String.format("%02d", selectedMinute);
    }

    private void updateDateEditText(Calendar calendar, EditText target) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        target.setText(dateFormat.format(calendar.getTime()));
        dates.put(target, (Calendar) calendar.clone());
    }

    @Override
    protected boolean validateForm() {
        // TODO: 02/06/2017
        // Check for empty name field
        if (cb.edtName.length() <= 0) {
            Toast.makeText(this,
                    getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for valid dates
        // Check if start date < end date
        if (dates.get(cb.edtDateFrom).getTimeInMillis()
                > dates.get(cb.edtDateUntil).getTimeInMillis()) {
            Toast.makeText(this,
                    getString(R.string.error_invalid_dates), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected Intent getResultIntent() {
        Quote quote = new Quote();
        quote.setName(cb.edtName.getText().toString());
        quote.setSuppliers(new ArrayList<Supplier>());
        //FIXME
        quote.setProducts(new ArrayList<Product>());

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
