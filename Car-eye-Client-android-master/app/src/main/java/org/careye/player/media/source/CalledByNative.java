/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */

package org.careye.player.media.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * is used by the JNI generator to create the necessary JNI
 * bindings and expose this method to native code.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface CalledByNative {
    /*
     * If present, tells which inner class the method belongs to.
     */
    String value() default "";
}