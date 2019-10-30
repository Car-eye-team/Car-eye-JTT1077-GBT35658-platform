/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */

package org.careye.player.media.source;

public interface ITrackInfo {
    int MEDIA_TRACK_TYPE_AUDIO = 2;
    int MEDIA_TRACK_TYPE_METADATA = 5;
    int MEDIA_TRACK_TYPE_SUBTITLE = 4;
    int MEDIA_TRACK_TYPE_TIMEDTEXT = 3;
    int MEDIA_TRACK_TYPE_UNKNOWN = 0;
    int MEDIA_TRACK_TYPE_VIDEO = 1;

    IMediaFormat getFormat();

    String getLanguage();

    int getTrackType();

    String getInfoInline();
}
