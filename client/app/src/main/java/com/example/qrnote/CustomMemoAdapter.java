package com.example.qrnote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomMemoAdapter extends ArrayAdapter {
    public CustomMemoAdapter(Context context, ArrayList teams) {
        super(context, 0, teams);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.memo_layout, parent, false);
        }

        // Get the data item for this position
        Memo memo = (Memo) getItem(position);

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.memo_title);
        // Populate the data into the template view using the data object
        tvName.setText(memo.getTitle());

        // Return the completed view to render on screen
        return convertView;
    }
}
