package org.careye.model;

import java.io.Serializable;
import java.util.Date;

public class AppUpdate implements Serializable {

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    private Integer versionCode;

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    private String versionName;

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionName() {
        return versionName;
    }

    private String versionInfo;

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    private int updateFlag;

    public void setUpdateFlag(int updateFlag) {
        this.updateFlag = updateFlag;
    }

    public int getUpdateFlag() {
        return updateFlag;
    }

    private Integer enabled;

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getEnabled() {
        return enabled;
    }

    private String apkUrl;

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    private Date createTime;

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    private String createUser;

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateUser() {
        return createUser;
    }

    private Date updateTime;

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    private String updateUser;

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }
}
