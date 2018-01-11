package com.bringletech.looviedraft.livechat.ui.ENDLESS;

/**
 * Created by Lincoln on 18/05/16.
 */
public class Album {
    private String name;
    private int numOfSongs;
    private int thumbnail;
    private String pic;

    public Album() {
    }

    public Album(String name, int numOfSongs, String pic) {
        this.name = name;
        this.numOfSongs = numOfSongs;

        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
