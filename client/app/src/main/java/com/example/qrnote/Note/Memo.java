package com.example.qrnote.Note;

import org.json.JSONObject;

import java.io.Serializable;

public class Memo implements Serializable {
    private Long id;
    private String title;
    private String currMemo;

    public Memo(Long id, String title, String currMemo) {
        this.id = id;
        this.title = title;
        this.currMemo = currMemo;
    }
    public String getTitle() {
        return title;
    }
    public Long getId() {
        return id;
    }
    public String getCurrMemo() { return currMemo; }
}

