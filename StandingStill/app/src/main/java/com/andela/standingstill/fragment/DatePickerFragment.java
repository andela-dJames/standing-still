package com.andela.standingstill.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;

import org.joda.time.DateTime;

/**
 * Custom DatePickerFragment
 */
public class DatePickerFragment extends DialogFragment{

    public static final String DEFAULT = "default_value";

    private DatePickerDialog.OnDateSetListener listener;

    public DatePickerFragment() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DateTime defaultValue = DateTime.parse(getArguments().getString(DEFAULT));

        DatePickerDialog dialog =  new DatePickerDialog(getActivity(), listener, defaultValue.getYear(), defaultValue.getMonthOfYear() - 1, defaultValue.getDayOfMonth());

        dialog.getDatePicker().setMaxDate(DateTime.now().getMillis());

        return dialog;
    }

    public DatePickerDialog.OnDateSetListener getListener() {
        return listener;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }
}
