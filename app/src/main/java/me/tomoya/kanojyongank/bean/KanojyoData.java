package me.tomoya.kanojyongank.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tomoya-Hoo on 2016/10/13.
 */

public class KanojyoData extends BaseData {
    public @SerializedName("results") List<Kanojyo> results;
}
