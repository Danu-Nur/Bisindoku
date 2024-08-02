package com.bisindoku.model;

public class Kuis {
    private String id;
    private String pertanyaan;
    private String link_video;
    private String benar;
    private String salah;

    public Kuis(String id, String pertanyaan, String link_video, String benar, String salah) {
        this.id = id;
        this.pertanyaan = pertanyaan;
        this.link_video = link_video;
        this.benar = benar;
        this.salah = salah;
    }

    public String getIdKuis() {
        return id;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public String getLink_video() {
        return link_video;
    }

    public String getBenar() {
        return benar;
    }

    public String getSalah() {
        return salah;
    }

}
