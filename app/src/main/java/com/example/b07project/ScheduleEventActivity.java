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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ScheduleEventActivity extends AppCompatActivity implements CreatesEvent {
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private String venueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_event);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        TextView timeText = (TextView) findViewById(R.id.time_text);
        TextView dateText = (TextView) findViewById(R.id.date_text);

        setText("time_string", timeText);
        setText("date_string", dateText);

        EditText minutes_text = (EditText) findViewById(R.id.ctrMinutes);
        EditText hours_text = (EditText) findViewById(R.id.ctrHours);
        EditText days_text = (EditText) findViewById(R.id.ctrDays);
        EditText event_text = (EditText) findViewById(R.id.eventNameSchedule);
        EditText max_text = (EditText) findViewById(R.id.eventMaxPlayersSchedule);

        setText("minutes_string", minutes_text);
        setText("hours_string", hours_text);
        setText("days_string", days_text);
        setText("event_string", event_text);
        setText("max_string", max_text);

        if (getIntent().getStringExtra("venue_string") != null) {
            venueName = getIntent().getStringExtra("venue_string");
            Log.d("victortest", venueName);
        }

    }

    public void setText(String s, TextView v) {
        if (getIntent().getStringExtra(s) != null) {
            Log.d("megajuice", getIntent().getStringExtra(s));
            v.setText(getIntent().getStringExtra(s));
        }
    }

    public Bundle getSaveBundle() {
        EditText minutes_text = (EditText) findViewById(R.id.ctrMinutes);
        EditText hours_text = (EditText) findViewById(R.id.ctrHours);
        EditText days_text = (EditText) findViewById(R.id.ctrDays);
        EditText event_text = (EditText) findViewById(R.id.eventNameSchedule);
        EditText max_text = (EditText) findViewById(R.id.eventMaxPlayersSchedule);

        Bundle bundle = new Bundle();
        bundle.putString("venue_string", venueName);
        bundle.putString("minutes_string", minutes_text.getText().toString());
        bundle.putString("hours_string", hours_text.getText().toString());
        bundle.putString("days_string", days_text.getText().toString());
        bundle.putString("event_string", event_text.getText().toString());
        bundle.putString("max_string", max_text.getText().toString());
        return bundle;
    }


    // Code below from https://developer.android.com/guide/topics/ui/controls/pickers#TimePicker
    public void showTimePickerDialog(View v) {
        TextView dateText = (TextView) findViewById(R.id.date_text);

        Bundle bundle = getSaveBundle();
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
        Bundle bundle = getSaveBundle();
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

        // VALIDATE USER INPUT
        String event_text =  ((EditText) findViewById(R.id.eventNameSchedule)).getText().toString();
        String date_text = ((TextView) findViewById(R.id.date_text)).getText().toString();
        String time_text = ((TextView) findViewById(R.id.time_text)).getText().toString();

        Log.d("victortest", date_text + " " + time_text);
        if (!(date_text.compareTo("Pick a date") != 0 && time_text.compareTo("Pick a time") != 0)) {
           Toast.makeText(ScheduleEventActivity.this, "Pick a valid date and/or time", Toast.LENGTH_SHORT).show();
           return;
        }

        if (!Pattern.matches("^([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\s]*[A-Za-z0-9])$", event_text)) {
           Toast.makeText(ScheduleEventActivity.this, "Event name can only contain alphanumeric characters", Toast.LENGTH_LONG).show();
           return;
        }

        String maxcustomers_text = ((EditText) findViewById(R.id.eventMaxPlayersSchedule)).getText().toString();

        int maxCustomers = mapTime(maxcustomers_text);

        if (maxCustomers <= 1) {
            Toast.makeText(ScheduleEventActivity.this, "Max customers must be greater than 1", Toast.LENGTH_SHORT).show();
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

        Log.d("victortest", venueName);
        if (start_time == end_time) {
            Toast.makeText(ScheduleEventActivity.this, "Your start and end time cannot be the same", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("victortest", "End Time: " + String.valueOf(end_time));
        Log.d("victortest", "Start Time: " + String.valueOf(start_time));

        // CREATE NEW EVENT OBJECT
        if (venueName == null || venueName.matches("")) {
            Log.d("andre-testing", "VENUE NULL");
        }

        Log.d("victortest", auth.getCurrentUser().getUid());
        Event event = new Event(event_text, venueName, auth.getCurrentUser().getUid(), maxCustomers, start_time, end_time);
        DatabaseFunctions.createEvent(db, event, this);
    }


    @Override
    public void onCreateEventSuccess(Event event) {
        Toast.makeText(this, event.getName() + " at " + event.getVenueKey() + " successfully created", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCreateEventError(String errorMessage) {
        Toast.makeText(this, "Event could not be created: " + errorMessage, Toast.LENGTH_SHORT).show();
    }
}