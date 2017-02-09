package me.tomoya.kanojyongank.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tomoya-Hoo on 2016/11/3.
 */

public class ShortFilmData extends BaseData {
    public @SerializedName("results")
    List<ShortFilm> results;
}
