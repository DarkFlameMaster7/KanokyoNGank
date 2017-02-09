package me.tomoya.kanojyongank.annotation;

import android.support.annotation.LayoutRes;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by piper on 17-2-7.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PropertiesInject {

	@LayoutRes int contentViewId() default -1;

	@LayoutRes int menuId() default -1;

	boolean enableSlideExit() default false;

	boolean isStatusBarTranslucent() default false;

	boolean hasNavigationView() default false;
}
