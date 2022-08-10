package com.example.b07project;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

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

    public void startIntent(String date, String time) {
        Intent intent = new Intent(getActivity(), ScheduleEventActivity.class);
        intent.putExtra("date_string", date);
        intent.putExtra("time_string", time);
        startActivity(intent);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        //Month offset by 1 for some reason
        String date = (month+1) + "/" + day + "/" + year;
        String time = this.getArguments().getString("time_string");

        Date now = new Date();
        SimpleDateFormat dtf = new SimpleDateFormat("MM/dd/yyyy");
        int compare_date = -1;

        try {
            compare_date = dtf.parse(dtf.format(now)).compareTo(dtf.parse(date));
            if (compare_date > 0) {
                Toast.makeText(getActivity(), "The date you input has already happened. Please enter a later date", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //may want
        String[] times = this.getArguments().getString("time_string").split(":");
        if (compare_date == 0 && time.split(":").length > 1) {
            LocalTime local_time =  LocalTime.now();

            Log.d("victortest", times[0]);

            LocalTime attempt_time = LocalTime.of(Integer.valueOf(times[0]), Integer.valueOf(times[1]));
            if (local_time.compareTo(attempt_time) == 1) {
                Toast.makeText(getActivity(), "The time you input has already happened. Please enter an earlier time", Toast.LENGTH_SHORT).show();
                startIntent(date, " ");
                return;
            }
        }

        startIntent(date, time);


        //((ScheduleEventActivity) getActivity()).showUserUpdatedTime(date);

        //((ScheduleEventActivity)getActivity()).dateText.setText(date);
    }
}
