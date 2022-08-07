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
import android.widget.TextView;

import org.w3c.dom.Text;

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

//    public void showUserUpdatedTime(String date){
//        dateText.setText(date);
//    }
}