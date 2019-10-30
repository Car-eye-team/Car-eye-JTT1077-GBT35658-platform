/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */

package org.careye.player.media.source;

import android.graphics.Rect;

public final class EyeTimedText {

    private Rect mTextBounds = null;
    private String mTextChars = null;

    public EyeTimedText(Rect bounds, String text) {
        mTextBounds = bounds;
        mTextChars = text;
    }

    public Rect getBounds() {
        return mTextBounds;
    }

    public String getText() {
        return mTextChars;
    }
}
