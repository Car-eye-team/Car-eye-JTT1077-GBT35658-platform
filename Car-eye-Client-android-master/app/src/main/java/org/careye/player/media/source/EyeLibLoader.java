/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */

package org.careye.player.media.source;

public interface EyeLibLoader {
    void loadLibrary(String libName) throws UnsatisfiedLinkError,
            SecurityException;
}
