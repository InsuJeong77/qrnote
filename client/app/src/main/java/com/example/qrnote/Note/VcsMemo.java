package com.example.qrnote.Note;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class VcsMemo {
    private Long id;
    private String title;
    private String contents;
    private String gTime;
    private String writer;

    public VcsMemo(Long id, String title, String contents, String gTime, String writer) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.gTime = gTime;
        this.writer = writer;
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }

    public String getgTime() {
        return gTime;
    }
}
