package com.example.qrnote.Note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qrnote.R;

import java.util.ArrayList;

public class CustomVcsAdapter extends ArrayAdapter {
    public CustomVcsAdapter(Context context, ArrayList vcsMemos) {
        super(context, 0, vcsMemos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.vcs_layout, parent, false);
        }

        // Get the data item for this position
        VcsMemo memo = (VcsMemo) getItem(position);

        // Lookup view for data population
        TextView writer = (TextView) convertView.findViewById(R.id.writer);
        TextView day = (TextView) convertView.findViewById(R.id.modify_day);
        // Populate the data into the template view using the data object
        writer.setText(memo.getWriter());
        day.setText(memo.getgTime().toString());

        // Return the completed view to render on screen
        return convertView;
    }

}
