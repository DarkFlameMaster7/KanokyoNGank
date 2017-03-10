package me.tomoya.kanojyongank.common;

import java.io.File;
import me.tomoya.kanojyongank.GankApplication;

/**
 * Created by piper on 17-2-7.
 */

public class GContants {

	public static final String FLAG_START = "start_data";
	public static final String FLAG_DATA = "data";
	public static final String FLAG_PHOTODATA_IMG = "photo_data_imgurl";
	public static final String FLAG_PHOTODATA_COL= "photo_data_color";
	public static final String BASE_URL  = "http://gank.io/api/";
	public static final String PATH_DATA =
			GankApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

	public static final String PATH_CACHE = PATH_DATA + "/netcache";
	public static final String PATH_CACHE_WEB = PATH_DATA + "/webcache";
}
