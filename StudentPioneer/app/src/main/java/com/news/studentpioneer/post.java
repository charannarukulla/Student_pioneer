package com.news.studentpioneer;

public class post {
    private String title;

    private String likes;
private  String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public post(String photo) {
        this.photo = photo;
    }

    public post(String title, String likes) {
        this.title = title;
        this.likes = likes;
    }

    public post() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}