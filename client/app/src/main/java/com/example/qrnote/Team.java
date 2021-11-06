package com.example.qrnote;

public class Team {
    private Long id;
    private String name;

    public Team(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public Long getId() {
        return id;
    }
}

