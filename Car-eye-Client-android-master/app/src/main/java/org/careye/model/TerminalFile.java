package org.careye.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.careye.utils.DateUtil;

public class TerminalFile implements Parcelable {

    /**
     * 从本位置开始为一个资源列表的开始
     */
    private String logicChannel;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 大小
     */
    private long size;
    /**
     * 数据类型		0：音视频 1：音频 2：视频 3视频或音视频
     */
    private int mediaType;
    /**
     * 码流类型		0：所有码流 1：主码流 2 子码流
     */
    private int streamType;
    /**
     * 存储类型		0：所有存储器 1：主存储器 2：灾备服务器
     */
    private int memoryType;

    public String getLogicChannel() {
        return logicChannel;
    }

    public void setLogicChannel(String logicChannel) {
        this.logicChannel = logicChannel;
    }

    public String getStartTime() {
        if (startTime == null) {
            return "";
        }

        if (startTime.length() == 12) {
            StringBuffer sb = new StringBuffer(startTime);

            String year = sb.substring(0, 2);
            String month = sb.substring(2, 4);
            String day = sb.substring(4, 6);
            String hour = sb.substring(6, 8);
            String minute = sb.substring(8, 10);
            String second = sb.substring(10, 12);

            return "20" + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        }

        return startTime;
    }

    public void setStartTime(String startTime) {
        try {
            this.startTime = DateUtil.df1.format(DateUtil.df8.parse(startTime));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getEndTime() {
        if (endTime == null) {
            return "";
        }

        if (endTime.length() == 12) {
            StringBuffer sb = new StringBuffer(endTime);

            String year = sb.substring(0, 2);
            String month = sb.substring(2, 4);
            String day = sb.substring(4, 6);
            String hour = sb.substring(6, 8);
            String minute = sb.substring(8, 10);
            String second = sb.substring(10, 12);

            return "20" + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        }

        return endTime;
    }

    public void setEndTime(String endTime) {
        try {
            this.endTime = DateUtil.df1.format(DateUtil.df8.parse(endTime));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public int getStreamType() {
        return streamType;
    }

    public void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    public int getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(int memoryType) {
        this.memoryType = memoryType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(logicChannel);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeLong(size);
        dest.writeInt(mediaType);
        dest.writeInt(streamType);
        dest.writeInt(memoryType);
    }

    public static final Creator<TerminalFile> CREATOR = new Creator<TerminalFile>() {
        @Override
        public TerminalFile[] newArray(int size) {
            return new TerminalFile[size];
        }

        @Override
        public TerminalFile createFromParcel(Parcel source) {
            TerminalFile terminalFile = new TerminalFile();
            terminalFile.logicChannel = source.readString();
            terminalFile.startTime = source.readString();
            terminalFile.endTime = source.readString();
            terminalFile.size = source.readLong();
            terminalFile.mediaType = source.readInt();
            terminalFile.streamType = source.readInt();
            terminalFile.memoryType = source.readInt();

            return terminalFile;
        }
    };
}
