package com.example.nasaapi;

public class ImageModel {
    private int id;
    private String URL;

    public ImageModel(int id, String URL) {
        this.id = id;
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", URL='" + URL + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
