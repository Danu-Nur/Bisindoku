package com.bisindoku.model;

public class MenuItems {
    private String id;
    private String namaItem;
    private String linkVideo;

    public MenuItems(String id, String namaItem, String linkVideo) {
        this.id = id;
        this.namaItem = namaItem;
        this.linkVideo = linkVideo;
    }

    public String getId() {
        return id;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public String getLinkVideo() {
        return linkVideo;
    }
}
