package me.tomoya.kanojyongank.common;

import java.io.File;
import me.tomoya.kanojyongank.GankApplication;

/**
 * Created by piper on 17-2-7.
 */

public class GContants {
	public static final String BASE_URL  = "http://gank.io/api/";
	public static final String PATH_DATA =
			GankApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

	public static final String PATH_CACHE = PATH_DATA + "/NetCache";
}
