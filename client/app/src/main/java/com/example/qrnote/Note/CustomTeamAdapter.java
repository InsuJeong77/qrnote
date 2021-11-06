package com.example.qrnote.Note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qrnote.Note.Team;
import com.example.qrnote.R;

import java.util.ArrayList;

public class CustomTeamAdapter extends ArrayAdapter {
    public CustomTeamAdapter(Context context, ArrayList teams) {
        super(context, 0, teams);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.team_layout, parent, false);
        }

        // Get the data item for this position
        Team team = (Team) getItem(position);

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.team_name);
        // Populate the data into the template view using the data object
        tvName.setText(team.getName());

        // Return the completed view to render on screen
        return convertView;
    }

}
