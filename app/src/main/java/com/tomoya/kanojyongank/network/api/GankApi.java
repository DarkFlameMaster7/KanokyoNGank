package com.tomoya.kanojyongank.network.api;

import com.tomoya.kanojyongank.bean.GankData;
import com.tomoya.kanojyongank.bean.KanojyoData;
import com.tomoya.kanojyongank.bean.ShortFilmData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Tomoya-Hoo on 2016/10/13.
 */

public interface GankApi {
    //分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
    @GET("data/福利/10/{page}")
    Observable<KanojyoData> getKanojyoData(@Path("page") int page);

    //每日数据: http://gank.io/api/day/年/月/日
    @GET("day/{year}/{month}/{day}")
    Observable<GankData> getGankData(@Path("year") int year, @Path("month") int month, @Path("day") int day);

    //休閒視頻 http://gank.io/api/data/休视频/10/1
    @GET("data/休息视频/10/{page}")
    Observable<ShortFilmData> getShortFilmData(@Path("page") int page);
}
