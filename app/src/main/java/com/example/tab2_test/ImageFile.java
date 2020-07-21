package com.example.tab2_test;

public class ImageFile {
    String name;
    int likes;

    ImageFile(String name, int likes){
        this.name = name;
        this.likes = likes;
    }

    public int getLikes() {
        return likes;
    }

    public String getName() {
        return name;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setName(String name) {
        this.name = name;
    }
}
