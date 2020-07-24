package com.news.studentpioneer;

public class post {
    private String title;

    private int likes;
private  String photo;
private  String type;
private String more;

    public post() {
    }

    public post(String title, int likes, String photo, String type, String more) {
        this.title = title;
        this.likes = likes;
        this.photo = photo;
        this.type = type;
        this.more = more;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }
}