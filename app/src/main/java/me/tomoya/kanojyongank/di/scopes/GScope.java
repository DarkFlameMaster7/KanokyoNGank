package me.tomoya.kanojyongank.di.scopes;

import java.lang.annotation.Retention;
import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by piper on 17-2-7.
 */
@Scope
@Retention(RUNTIME)
public @interface GScope {
}
