package com.example.safezone;

public class YoutubeVideo {
    String Videourl;

    public YoutubeVideo(String videourl) {
        Videourl = videourl;
    }

    public YoutubeVideo() {
    }

    public void setVideourl(String videourl) {
        Videourl = videourl;
    }

    public String getVideourl() {
        return Videourl;
    }
}
