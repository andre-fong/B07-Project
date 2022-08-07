package com.example.b07project;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

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

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        String time = hourOfDay + ":" + minute;
        Intent intent = new Intent(getActivity(), ScheduleEventActivity.class);
        intent.putExtra("date_string",this.getArguments().getString("date_string"));
        intent.putExtra("time_string", time);
        startActivity(intent);

        //((ScheduleEventActivity)getActivity()).timeText.setText(time);
    }
}
