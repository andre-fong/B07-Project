package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class ScheduleEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_event);

        if (!(getIntent().getStringExtra("date_string") == null)) {
            TextView dateText = (TextView) findViewById(R.id.date_text);
            dateText.setText(getIntent().getStringExtra("date_string"));
        }

        if (!(getIntent().getStringExtra("time_string") == null)) {
            TextView timeText = (TextView) findViewById(R.id.time_text);
            timeText.setText(getIntent().getStringExtra("time_string"));
        }

    }

    // Code below from https://developer.android.com/guide/topics/ui/controls/pickers#TimePicker
    public void showTimePickerDialog(View v) {
        TextView dateText = (TextView) findViewById(R.id.date_text);
        Bundle bundle = new Bundle();
        bundle.putString("date_string", dateText.getText().toString());
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "timePicker");

//        timeText.findViewById(R.id.time_text);
//        String time_string = getIntent().getStringExtra("time_string");
//        dateText.setText(time_string);
    }

    // Code below from https://developer.android.com/guide/topics/ui/controls/pickers#DatePicker
    public void showDatePickerDialog(View v) {
        //DialogFragment newFragment = new DatePickerFragment();
        //newFragment.show(getSupportFragmentManager(), "datePicker");

        TextView timeText = (TextView) findViewById(R.id.time_text);
        Bundle bundle = new Bundle();
        bundle.putString("time_string", timeText.getText().toString());
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    public int mapTime(String s) {
        if (s.compareTo("") == 0) {
            return 0;
        }
        return Integer.valueOf(s);
    }

    public void onSchedule(View v) throws ParseException {


        String event_text =  ((EditText) findViewById(R.id.eventNameSchedule)).getText().toString();
        String date_text = ((TextView) findViewById(R.id.date_text)).getText().toString();
        String time_text = ((TextView) findViewById(R.id.time_text)).getText().toString();

        Log.d("victortest", date_text + " " + time_text);
        if (!(date_text.compareTo("Pick a date") != 0 && time_text.compareTo("Pick a time") != 0)) {
           Toast.makeText(ScheduleEventActivity.this, "Pick a valid date and/or time", Toast.LENGTH_SHORT).show();
           return;
        }

        if (!Pattern.matches("^[A-Za-z0-9]+$", event_text)) {
           Toast.makeText(ScheduleEventActivity.this, "Your event name field is incorrect. A valid event name is a string consisting of only alphanumeric characters", Toast.LENGTH_LONG).show();
           return;
        }

        String maxcustomers_text = ((EditText) findViewById(R.id.eventMaxPlayersSchedule)).getText().toString();
        int maxCustomers = mapTime(maxcustomers_text);

        if (maxCustomers == 0) {
            Toast.makeText(ScheduleEventActivity.this, "Max Customers must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        int hours = mapTime(((EditText) findViewById(R.id.ctrHours)).getText().toString());
        int minutes = mapTime(((EditText) findViewById(R.id.ctrMinutes)).getText().toString());
        int days = mapTime(((EditText) findViewById(R.id.ctrDays)).getText().toString());


        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date start_date = df.parse(date_text + " " + time_text);

        Calendar cal = Calendar.getInstance();
        cal.setTime(start_date);
        cal.add(Calendar.MINUTE, minutes);
        cal.add(Calendar.HOUR, hours);
        cal.add(Calendar.DAY_OF_MONTH, days);

        long start_time = start_date.getTime();
        long end_time = cal.getTimeInMillis();

        if (start_time == end_time) {
            Toast.makeText(ScheduleEventActivity.this, "Your start and end time cannot be the same", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("victortest", "End Time: " + String.valueOf(end_time));
        Log.d("victortest", "Start Time: " + String.valueOf(start_time));

    }

//    public void showUserUpdatedTime(String date){
//        dateText.setText(date);
//    }
}