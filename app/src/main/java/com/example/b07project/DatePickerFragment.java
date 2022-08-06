package com.example.b07project;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;


import androidx.fragment.app.DialogFragment;

// Code below from https://developer.android.com/guide/topics/ui/controls/pickers#DatePicker

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

//    private TextView dateText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

//        String date = month + "/" + day + "/" + year;
//
//        Intent intent = new Intent(getActivity(), ScheduleEventActivity.class);
//        intent.putExtra("date_string", date);
//        startActivity(intent);

        //((ScheduleEventActivity) getActivity()).showUserUpdatedTime(date);

        //((ScheduleEventActivity)getActivity()).dateText.setText(date);
    }
}
