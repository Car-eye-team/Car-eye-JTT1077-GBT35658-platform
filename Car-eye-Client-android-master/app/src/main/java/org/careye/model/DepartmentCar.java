package org.careye.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.careye.CarEyeClient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户机构车辆
 */
public class DepartmentCar implements Parcelable {
    /**
     * 车辆id/机构id
     */
    private String nodeId;
    /**
     * 父id  0表示根节点
     */
    private String parentId;
    /**
     * 车牌号/机构名称
     */
    private  String nodeName;
    /**
     * 节点类型 1组织机构 2 车辆
     */
    private int nodetype;
    /**
     * 车辆状态 节点是车辆才有值  车辆状态 1：长时间离线 2：离线 3：熄火 4：停车 5：行驶 6：报警 7：在线 8：未定位
     */
    private int carstatus;
    /**
     * 机构车辆总数  包括子机构数量
     */
    private int total;
    /**
     * 车辆离线总数  包括子机构数量
     */
    private int caroffline;
    /**
     * 车辆长离总数  包括子机构数量
     */
    private int longoffline;
    /**
     * 终端号
     */
    private String terminal;
    /**
     * 摄像头个数
     */
    private int channeltotals;
    /**
     * 是否是父节点  true代表有子节点，false代表无
     */
    private boolean parent;
    /**
     * 是否展开了 true代表展开，false代表未展开
     */
    private boolean isExpand;
    /**
     *  1       //同一个级别的显示顺序
     */
    private int DISPLAY_ORDER;
    /**
     * 展示图标
     */
    private int imageResource;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getNodetype() {
        return nodetype;
    }

    public void setNodetype(int nodetype) {
        this.nodetype = nodetype;
    }

    public int getCarstatus() {
        return carstatus;
    }

    public void setCarstatus(int carstatus) {
        this.carstatus = carstatus;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCaroffline() {
        return caroffline;
    }

    public void setCaroffline(int caroffline) {
        this.caroffline = caroffline;
    }

    public int getLongoffline() {
        return longoffline;
    }

    public void setLongoffline(int longoffline) {
        this.longoffline = longoffline;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public int getChanneltotals() {
        return channeltotals;
    }

    public void setChanneltotals(int channeltotals) {
        this.channeltotals = channeltotals;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public void setDISPLAY_ORDER(int DISPLAY_ORDER) {
        this.DISPLAY_ORDER = DISPLAY_ORDER;
    }

    public int getDISPLAY_ORDER() {
        return DISPLAY_ORDER;
    }

    public int getImageResource(int carstatus) {
        switch (carstatus) {
            case 1:
            case 2:
                imageResource = R.drawable.device_offline;
                break;
//            case 3:
//                imageResource = R.drawable.device_stopaccoff;
//                break;
//            case 4:
//                imageResource = R.drawable.device_stopaccon;
//                break;
//            case 5:
//                imageResource = R.drawable.device_io;
//                break;
            case 6:
                imageResource = R.drawable.device_alarm;
                break;
//            case 7:
//                imageResource = R.drawable.device_online;
//                break;
//            case 8:
//                imageResource = R.drawable.device_nogps;
//                break;
            default:
                imageResource = R.drawable.device_online;
                break;
        }
        return imageResource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nodeId);
        dest.writeString(parentId);
        dest.writeString(nodeName);
        dest.writeInt(nodetype);
        dest.writeInt(carstatus);
        dest.writeInt(total);
        dest.writeInt(caroffline);
        dest.writeInt(longoffline);
        dest.writeString(terminal);
        dest.writeInt(channeltotals);
    }

    public static final Creator<DepartmentCar> CREATOR = new Creator<DepartmentCar>() {

        @Override
        public DepartmentCar[] newArray(int size) {
            return new DepartmentCar[size];
        }

        @Override
        public DepartmentCar createFromParcel(Parcel source) {
            DepartmentCar departmentCar = new DepartmentCar();
            departmentCar.nodeId = source.readString();
            departmentCar.parentId = source.readString();
            departmentCar.nodeName = source.readString();
            departmentCar.nodetype = source.readInt();
            departmentCar.carstatus = source.readInt();
            departmentCar.total = source.readInt();
            departmentCar.caroffline = source.readInt();
            departmentCar.longoffline = source.readInt();
            departmentCar.terminal = source.readString();
            departmentCar.channeltotals = source.readInt();
            return departmentCar;
        }
    };

}
