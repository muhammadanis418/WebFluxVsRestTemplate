package com.example.webfluxvsresttemplate.entity;

public class Tweets {
    String text;
    String username;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Tweets() {
    }
    public Tweets(String text, String username) {
        this.text = text;
        this.username = username;

    }
    @Override
    public String toString() {
        return "Tweets{" +
                "text='" + text + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
