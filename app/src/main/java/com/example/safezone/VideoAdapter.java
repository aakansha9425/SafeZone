package com.example.safezone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    List<YoutubeVideo> youtubeVideos;

    public VideoAdapter(List<YoutubeVideo> youtubeVideos) {
        this.youtubeVideos = youtubeVideos;
    }

    public VideoAdapter() {
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.videoweb.loadData(youtubeVideos.get(position).getVideourl(), "text/html", "utf-8");
    }


    @Override
    public int getItemCount() {
        return youtubeVideos.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        WebView videoweb;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoweb = (WebView) itemView.findViewById(R.id.cardvideoview);
            videoweb.getSettings().setJavaScriptEnabled(true);
            videoweb.setWebChromeClient(new WebChromeClient() {

            });
        }


    }
}

