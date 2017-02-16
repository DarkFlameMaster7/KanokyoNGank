package me.tomoya.kanojyongank.util;

import java.util.List;
import me.tomoya.kanojyongank.bean.Gank;
import me.tomoya.kanojyongank.bean.GankData;

/**
 * Created by piper on 17-2-13.
 */

public class CommonUtils {
	public static List<Gank> getGankList(GankData.Result result,String category){
		switch (category){
			case "Android":
				return result.androidList;
			case "iOS":
				return result.iOSList;
			case "App":
				return result.appList;
			case "休息视频":
				return result.休息视频List;
			case "拓展资源":
				return result.拓展资源List;
			case "瞎推荐":
				return result.瞎推荐List;
			case "前端":
				return result.frontList;
			default:
				return null;
		}
	}
}
