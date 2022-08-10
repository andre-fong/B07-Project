package com.example.b07project;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

// Code below from https://developer.android.com/guide/topics/ui/controls/pickers#TimePicker

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

//    private TextView timeText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void startIntent(String date_string, String time_string) {
        Intent intent = new Intent(getActivity(), ScheduleEventActivity.class);
        intent.putExtra("date_string", date_string);
        intent.putExtra("time_string", time_string);
        intent.putExtra("venue_string", this.getArguments().getString("venue_string"));
        intent.putExtra("minutes_string", this.getArguments().getString("minutes_string"));
        intent.putExtra("hours_string", this.getArguments().getString("hours_string"));
        intent.putExtra("days_string", this.getArguments().getString("days_string"));
        intent.putExtra("event_string", this.getArguments().getString("event_string"));
        intent.putExtra("max_string", this.getArguments().getString("max_string"));
        startActivity(intent);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        String date_planned = this.getArguments().getString("date_string");

        Date now = new Date();
        SimpleDateFormat dtf = new SimpleDateFormat("MM/dd/yyyy");
        boolean compare_flag = false;

        try {
            Date cur_time = dtf.parse(dtf.format(now));
            Date date_time = dtf.parse(date_planned);
            if (cur_time.compareTo(date_time) == 0) {
                compare_flag = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (compare_flag) {
            LocalTime local_time =  LocalTime.now();
            LocalTime attempt_time = LocalTime.of(hourOfDay, minute);
            if (local_time.compareTo(attempt_time) == 1) {
                Toast.makeText(getActivity(), "The time you input has already happened. Please enter an earlier time", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String minute_str = String.valueOf(minute);
        if (minute <= 9) {
            minute_str = "0" + minute;
        }

        String time = hourOfDay + ":" + minute_str;
        startIntent(date_planned, time);

        //((ScheduleEventActivity)getActivity()).timeText.setText(time);
    }
}
