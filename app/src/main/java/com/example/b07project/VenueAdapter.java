package com.example.b07project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class VenueAdapter extends ArrayAdapter<VenueItem> {

    public VenueAdapter(Context context, ArrayList<VenueItem> venueList) {
        super(context, 0, venueList);

        // Access values with getContext() or getItem(int pos)
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_row, parent, false);
        }

        TextView eventName = convertView.findViewById(R.id.spinnerNameRow);

        // get current EventItem row
        VenueItem currentItem = getItem(position);

        if (currentItem !=  null)
            eventName.setText(currentItem.getVenueName());

        return convertView;
    }
}
