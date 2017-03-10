package me.tomoya.kanojyongank.network.api;

import me.tomoya.kanojyongank.bean.GankData;
import me.tomoya.kanojyongank.bean.KanojyoData;
import me.tomoya.kanojyongank.bean.ShortFilmData;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Tomoya-Hoo on 2016/10/13.
 */

public interface GankApi {
	String BASE = "http://gank.io/api/";

	//分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
	@GET("data/福利/10/{page}")
	Observable<KanojyoData> getKanojyoData(@Path("page") int page);

	//每日数据: http://gank.io/api/day/年/月/日
	@GET("day/{year}/{month}/{day}")
	Observable<GankData> getGankData(@Path("year") int year, @Path("month") int month,
			@Path("day") int day);

	//休閒視頻 http://gank.io/api/data/休息视频/10/1
	@GET("data/休息视频/10/{page}")
	Observable<ShortFilmData> getShortFilmData(@Path("page") int page);

	@Streaming // instead of moving the entire file into memory, it`ll pas along the bytes right way
	@GET
	Observable<ResponseBody> downloadImageFromNet(@Url String imgUrl);
}
