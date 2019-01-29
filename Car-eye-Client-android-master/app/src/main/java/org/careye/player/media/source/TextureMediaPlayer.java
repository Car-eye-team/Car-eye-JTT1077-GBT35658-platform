/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */

package org.careye.player.media.source;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class TextureMediaPlayer extends MediaPlayerProxy implements IMediaPlayer, ISurfaceTextureHolder {
    private SurfaceTexture mSurfaceTexture;
    private ISurfaceTextureHost mSurfaceTextureHost;

    public TextureMediaPlayer(IMediaPlayer backEndMediaPlayer) {
        super(backEndMediaPlayer);
    }

    public void releaseSurfaceTexture() {
        if (mSurfaceTexture != null) {
            if (mSurfaceTextureHost != null) {
                mSurfaceTextureHost.releaseSurfaceTexture(mSurfaceTexture);
            } else {
                mSurfaceTexture.release();
            }
            mSurfaceTexture = null;
        }
    }

    //--------------------
    // IMediaPlayer
    //--------------------
    @Override
    public void reset() {
        super.reset();
        releaseSurfaceTexture();
    }

    @Override
    public void release() {
        super.release();
        releaseSurfaceTexture();
    }

    @Override
    public void setDisplay(SurfaceHolder sh) {
        if (mSurfaceTexture == null)
            super.setDisplay(sh);
    }

    @Override
    public void setSurface(Surface surface) {
        if (mSurfaceTexture == null)
            super.setSurface(surface);
    }

    //--------------------
    // ISurfaceTextureHolder
    //--------------------

    @Override
    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        if (mSurfaceTexture == surfaceTexture)
            return;

        releaseSurfaceTexture();
        mSurfaceTexture = surfaceTexture;
        if (surfaceTexture == null) {
            super.setSurface(null);
        } else {
            super.setSurface(new Surface(surfaceTexture));
        }
    }

    @Override
    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }

    @Override
    public void setSurfaceTextureHost(ISurfaceTextureHost surfaceTextureHost) {
        mSurfaceTextureHost = surfaceTextureHost;
    }
}
