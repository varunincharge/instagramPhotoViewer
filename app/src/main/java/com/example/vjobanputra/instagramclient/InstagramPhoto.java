package com.example.vjobanputra.instagramclient;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class InstagramPhoto {
    public String username;
    public String caption;
    public String imageUrl;
    public String profilePicUrl;
    public int imageHeight;
    public int likesCount;
    public long createdTime;
    ArrayList<PhotoComment> comments;
}
