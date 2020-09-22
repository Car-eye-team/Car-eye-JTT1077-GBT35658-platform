/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */

package org.careye.player.media.source;

import org.careye.player.media.EyeMediaPlayer;

public class MediaPlayerCompat {
    public static String getName(IMediaPlayer mp) {
        if (mp == null) {
            return "null";
        } else if (mp instanceof TextureMediaPlayer) {
            StringBuilder sb = new StringBuilder("TextureMediaPlayer <");
            IMediaPlayer internalMediaPlayer = ((TextureMediaPlayer) mp).getInternalMediaPlayer();
            if (internalMediaPlayer == null) {
                sb.append("null>");
            } else {
                sb.append(internalMediaPlayer.getClass().getSimpleName());
                sb.append(">");
            }
            return sb.toString();
        } else {
            return mp.getClass().getSimpleName();
        }
    }

    public static EyeMediaPlayer getIjkMediaPlayer(IMediaPlayer mp) {
        EyeMediaPlayer ijkMediaPlayer = null;
        if (mp == null) {
            return null;
        } if (mp instanceof EyeMediaPlayer) {
            ijkMediaPlayer = (EyeMediaPlayer) mp;
        } else if (mp instanceof MediaPlayerProxy && ((MediaPlayerProxy) mp).getInternalMediaPlayer() instanceof EyeMediaPlayer) {
            ijkMediaPlayer = (EyeMediaPlayer) ((MediaPlayerProxy) mp).getInternalMediaPlayer();
        }
        return ijkMediaPlayer;
    }

    public static void selectTrack(IMediaPlayer mp, int stream) {
        EyeMediaPlayer ijkMediaPlayer = getIjkMediaPlayer(mp);
        if (ijkMediaPlayer == null)
            return;
        ijkMediaPlayer.selectTrack(stream);
    }

    public static void deselectTrack(IMediaPlayer mp, int stream) {
        EyeMediaPlayer ijkMediaPlayer = getIjkMediaPlayer(mp);
        if (ijkMediaPlayer == null)
            return;
        ijkMediaPlayer.deselectTrack(stream);
    }

    public static int getSelectedTrack(IMediaPlayer mp, int trackType) {
        EyeMediaPlayer ijkMediaPlayer = getIjkMediaPlayer(mp);
        if (ijkMediaPlayer == null)
            return -1;
        return ijkMediaPlayer.getSelectedTrack(trackType);
    }
}
