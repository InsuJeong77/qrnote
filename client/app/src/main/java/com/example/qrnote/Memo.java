package com.example.qrnote;

public class Memo {
    private Long id;
    private String title;

    public Memo(Long id, String title) {
        this.id = id;
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public Long getId() {
        return id;
    }
}

