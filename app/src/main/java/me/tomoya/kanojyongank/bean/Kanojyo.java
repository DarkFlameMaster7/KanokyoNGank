package me.tomoya.kanojyongank.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Tomoya-Hoo on 2016/10/13.
 */


public class Kanojyo implements Serializable {
    public String url;
    public String type;
    public String desc;
    public String who;
    public boolean used;
    public Date createdAt;
    public Date updatedAt;
    public Date publishedAt;
    public int imageWidth;
    public int imageHeight;
    public int color;
    public String video_url;
}
