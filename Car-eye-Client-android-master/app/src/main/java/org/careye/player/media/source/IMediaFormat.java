/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */

package org.careye.player.media.source;

public interface IMediaFormat {
    // Common keys
    String KEY_MIME = "mime";

    // Video Keys
    String KEY_WIDTH = "width";
    String KEY_HEIGHT = "height";

    String getString(String name);

    int getInteger(String name);
}
