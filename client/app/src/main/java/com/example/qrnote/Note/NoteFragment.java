package com.example.qrnote.Note;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.qrnote.AppHelper;
import com.example.qrnote.Note.CustomMemoAdapter;
import com.example.qrnote.Note.CustomTeamAdapter;
import com.example.qrnote.Note.MakeNote;
import com.example.qrnote.Note.Memo;
import com.example.qrnote.Note.Team;
import com.example.qrnote.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class NoteFragment extends Fragment {

    SharedPreferences tokenStore;
    String token;

    private ArrayList<Team>    teams;
    private ArrayList<Memo>    memos;
    private ListView    teamListView;
    private ListView    memoListView;
    private CustomTeamAdapter teamAdapter;
    private CustomMemoAdapter memoAdapter;
    private Long teamId = 0L;

    public NoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokenStore = getActivity().getSharedPreferences("tokenStore", MODE_PRIVATE);
        token = tokenStore.getString("token", null);
    }

    @Override
    public void onResume() {
        super.onResume();
        dataRefresh();
    }

    private void dataRefresh() {
        getMemos(token, teamId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        teams = new ArrayList<Team>();
        memos = new ArrayList<Memo>();
        getTeams(token);
        dataRefresh();

        teamListView= (ListView) view.findViewById(R.id.list_view);
        memoListView= (ListView) view.findViewById(R.id.memo_view);

        Button createBtn = view.findViewById(R.id.note_create_button);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MakeNote.class);
                intent.putExtra("teamId", teamId);
                startActivity(intent);
            }
        });

        teamAdapter = new CustomTeamAdapter(getActivity(), teams);
        memoAdapter = new CustomMemoAdapter(getActivity(), memos);
//        adapter =  new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        teamListView.setAdapter(teamAdapter);
        memoListView.setAdapter(memoAdapter);
        teamListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                teamId = teams.get(i).getId();
                getMemos(token, teamId);
            }
        });

        memoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                readMemo(memos.get(i));
            }
        });

        return view;
    }

    private void readMemo(Memo qrMemo) {
        Intent intent = new Intent(getActivity(), ReadNoteActivity.class);
        intent.putExtra("qrMemo", qrMemo);
        startActivity(intent);
    }

    private void getTeams(String token) {
        if(AppHelper.requestQueue == null){
            AppHelper.requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }
        String url = "http://" + AppHelper.hostUrl + "/memo/teams";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject o = (JSONObject) data.get(i);
                                teams.add(new Team(o.getLong("id"), o.getString("name")));
                            }
                            teamAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
//                            문제발생 종료
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("token", token);
                return header;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    private void getMemos(String token, Long teamId) {
        if(AppHelper.requestQueue == null){
            AppHelper.requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }
        String url = "http://" + AppHelper.hostUrl + "/memo/list/" + teamId;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            memos.clear();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject o = (JSONObject) data.get(i);
                                memos.add(new Memo(o.getLong("id"), o.getJSONObject("currMemo").getString("title"),
                                        o.getJSONObject("currMemo").toString(), o.getString("qrcode")));
                            }
                            memoAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
//                            문제발생 종료
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("token", token);
                return header;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
}