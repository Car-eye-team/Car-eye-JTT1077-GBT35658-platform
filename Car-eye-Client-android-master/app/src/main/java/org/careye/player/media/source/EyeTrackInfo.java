/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */

package org.careye.player.media.source;

import android.text.TextUtils;

public class EyeTrackInfo implements ITrackInfo {
    private int mTrackType = MEDIA_TRACK_TYPE_UNKNOWN;
    private EyeMediaMeta.EyeStreamMeta mStreamMeta;

    public EyeTrackInfo(EyeMediaMeta.EyeStreamMeta streamMeta) {
        mStreamMeta = streamMeta;
    }

    public void setMediaMeta(EyeMediaMeta.EyeStreamMeta streamMeta) {
        mStreamMeta = streamMeta;
    }

    @Override
    public IMediaFormat getFormat() {
        return new EyeMediaFormat(mStreamMeta);
    }

    @Override
    public String getLanguage() {
        if (mStreamMeta == null || TextUtils.isEmpty(mStreamMeta.mLanguage))
            return "und";

        return mStreamMeta.mLanguage;
    }

    @Override
    public int getTrackType() {
        return mTrackType;
    }

    public void setTrackType(int trackType) {
        mTrackType = trackType;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '{' + getInfoInline() + "}";
    }

    @Override
    public String getInfoInline() {
        StringBuilder out = new StringBuilder(128);
        switch (mTrackType) {
            case MEDIA_TRACK_TYPE_VIDEO:
                out.append("VIDEO");
                out.append(", ");
                out.append(mStreamMeta.getCodecShortNameInline());
                out.append(", ");
                out.append(mStreamMeta.getBitrateInline());
                out.append(", ");
                out.append(mStreamMeta.getResolutionInline());
                break;
            case MEDIA_TRACK_TYPE_AUDIO:
                out.append("AUDIO");
                out.append(", ");
                out.append(mStreamMeta.getCodecShortNameInline());
                out.append(", ");
                out.append(mStreamMeta.getBitrateInline());
                out.append(", ");
                out.append(mStreamMeta.getSampleRateInline());
                break;
            case MEDIA_TRACK_TYPE_TIMEDTEXT:
                out.append("TIMEDTEXT");
                out.append(", ");
                out.append(mStreamMeta.mLanguage);
                break;
            case MEDIA_TRACK_TYPE_SUBTITLE:
                out.append("SUBTITLE");
                break;
            default:
                out.append("UNKNOWN");
                break;
        }
        return out.toString();
    }
}
