package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScheduleEventActivity extends AppCompatActivity {

    public TextView dateText;
    public TextView timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_event);

//        try{
//        dateText.findViewById(R.id.date_text);
//        String date_string = getIntent().getStringExtra("date_string");
//        dateText.setText(date_string);
//        }
//        catch(Exception e){}
    }

    // Code below from https://developer.android.com/guide/topics/ui/controls/pickers#TimePicker
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

//        timeText.findViewById(R.id.time_text);
//        String time_string = getIntent().getStringExtra("time_string");
//        dateText.setText(time_string);
    }

    // Code below from https://developer.android.com/guide/topics/ui/controls/pickers#DatePicker
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");


    }

//    public void showUserUpdatedTime(String date){
//        dateText.setText(date);
//    }
}