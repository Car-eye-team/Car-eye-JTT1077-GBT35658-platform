package org.careye.adapter;

import android.content.Context;
import android.widget.BaseAdapter;
import android.app.Activity;
import android.support.annotation.IntDef;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.careye.model.DepartmentCar;
import org.careye.utils.DensityUtil;
import org.careye.utils.TreeUtils;

import java.util.HashMap;
import java.util.List;

import com.careye.CarEyeClient.R;

public class TreeAdapter extends BaseAdapter {

    private Context mcontext;
    private Activity mActivity;
    private List<DepartmentCar> deptList;
    private String keyword = "";
    private HashMap<String, DepartmentCar> pointMap = new HashMap<>();

    //两种操作模式  点击 或者 选择
    private static final int ModeClick = 1;
    private static final int ModeSelect = 2;

    @IntDef({ModeClick,ModeSelect})
    public @interface Mode{

    }

    public TreeAdapter(final Context mcontext, List<DepartmentCar> deptList, HashMap<String, DepartmentCar> pointMap) {
        this.mcontext = mcontext;
        this.mActivity = (Activity) mcontext;
        this.deptList = deptList;
        this.pointMap = pointMap;
    }

    /**
     * 搜索的时候，先关闭所有的条目，然后，按照条件，找到含有关键字的数据
     * 如果是叶子节点，
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
        for (DepartmentCar deptCar : deptList) {
            deptCar.setExpand(false);
        }
        if (!"".equals(keyword)) {
            for (DepartmentCar deptCar : deptList) {
                if (deptCar.getNodetype() == 2 && deptCar.getNodeName().contains(keyword)) {
                    //展开从最顶层到该点的所有节点
                    openExpand(pointMap.get(deptCar.getNodeId()));
                }
            }
        }
        this.notifyDataSetChanged();
    }

    /**
     * 从DepartmentCar开始一直展开到顶部
     * @param departmentCar
     */
    private void openExpand(DepartmentCar departmentCar) {
        if ("0".equals(departmentCar.getParentId())) {
            departmentCar.setExpand(true);
        } else {
            pointMap.get(departmentCar.getParentId()).setExpand(true);
            openExpand(pointMap.get(departmentCar.getParentId()));
        }
    }


    //第一要准确计算数量
    @Override
    public int getCount() {
        int count = 0;
        for (DepartmentCar departmentCar : deptList) {
            if (TreeUtils.getLevel(departmentCar,pointMap) == 0) {
                count++;
            } else {
                if (getItemIsExpand(departmentCar.getParentId())) {
                    count++;
                }
            }
        }
        return count;
    }

    //判断当前Id的tempPoint是否展开了
    private boolean getItemIsExpand(String ID) {
        for (DepartmentCar departmentCar : deptList) {
            if (ID.equals(departmentCar.getNodeId())) {
                return departmentCar.isExpand();
            }
        }
        return false;
    }

    @Override
    public Object getItem(int position) {
        return deptList.get(convertPostion(position));
    }

    private int convertPostion(int position) {
        int count = 0;
        for (int i = 0; i < deptList.size(); i++) {
            DepartmentCar deptCar = deptList.get(i);
            if (TreeUtils.getLevel(deptCar,pointMap) == 0) {
                count++;
            } else {
                if (getItemIsExpand(deptCar.getParentId())) {
                    count++;
                }
            }
            if (position == (count - 1)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.device_item, null);
            holder = new ViewHolder();
            holder.text = convertView.findViewById(R.id.text);
            holder.icon = convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final DepartmentCar deptCar = (DepartmentCar) getItem(position);
        int level = TreeUtils.getLevel(deptCar,pointMap);
        holder.icon.setPadding(25 * level, 0, 25 * level, 0);
        if (deptCar.getNodetype() == 1) {  //如果为父节点
            if (!deptCar.isExpand()) {    //不展开显示加号
                holder.icon.setImageResource(R.drawable.icon_coalesce_hover);
            } else {                        //展开显示减号
                holder.icon.setImageResource(R.drawable.icon_expand_hover);
            }
        } else {   //如果叶子节点，不占位显示
            holder.icon.setImageResource(deptCar.getImageResource(deptCar.getCarstatus()));
        }
        //如果存在搜索关键字
        if (keyword != null && !"".equals(keyword) && deptCar.getNodeName().contains(keyword)) {
            int index = deptCar.getNodeName().indexOf(keyword);
            int len = keyword.length();
            Spanned temp = Html.fromHtml(deptCar.getNodeName().substring(0, index)
                    + "<font color=#FF0000>"
                    + deptCar.getNodeName().substring(index, index + len) + "</font>"
                    + deptCar.getNodeName().substring(index + len, deptCar.getNodeName().length()));

            holder.text.setText(temp);
        } else {
            holder.text.setText(deptCar.getNodeName());
        }
        holder.text.setCompoundDrawablePadding(DensityUtil.dip2px(mcontext, 10));
        return convertView;
    }



    public void onItemClick(int position) {
        DepartmentCar deptCar = (DepartmentCar) getItem(position);
        if (deptCar.getNodetype() == 2) {   //点击叶子节点
            //处理回填

        } else {  //如果点击的是父类
            if (deptCar.isExpand()) {
                for (DepartmentCar tempPoint : deptList) {
                    if (tempPoint.getParentId().equals(deptCar.getNodeId())) {
                        if (deptCar.getNodetype() == 1) {
                            tempPoint.setExpand(false);
                        }
                    }
                }
                deptCar.setExpand(false);
            } else {
                deptCar.setExpand(true);
            }
        }
        this.notifyDataSetChanged();
    }


    //选择操作
//    private void onModeSelect(TreePoint treePoint){
//        if ("1".equals(treePoint.getISLEAF())) {   //选择叶子节点
//            //处理回填
//            treePoint.setSelected(!treePoint.isSelected());
//        } else {                                   //选择父节点
//            int position = pointList.indexOf(treePoint);
//            boolean isSelect = treePoint.isSelected();
//            treePoint.setSelected(!isSelect);
//            if(position == -1){
//                return ;
//            }
//            if(position == pointList.size()-1){
//                return;
//            }
//            position++;
//            for(;position < pointList.size();position++){
//                TreePoint tempPoint =  pointList.get(position);
//                if(tempPoint.getPARENTID().equals(treePoint.getPARENTID())){    //如果找到和自己同级的数据就返回
//                    break;
//                }
//                tempPoint.setSelected(!isSelect);
//            }
//        }
//        this.notifyDataSetChanged();
//    }

    //选中所有的point
//    private void selectPoint(TreePoint treePoint) {
//        if(){
//
//        }
//    }




//    private String getSubmitResult(TreePoint treePoint) {
//        StringBuilder sb = new StringBuilder();
//        addResult(treePoint, sb);
//        String result = sb.toString();
//        if (result.endsWith("-")) {
//            result = result.substring(0, result.length() - 1);
//        }
//        return result;
//    }
//
//    private void addResult(TreePoint treePoint, StringBuilder sb) {
//        if (treePoint != null && sb != null) {
//            sb.insert(0, treePoint.getNNAME() + "-");
//            if (!"0".equals(treePoint.getPARENTID())) {
//                addResult(pointMap.get(treePoint.getPARENTID()), sb);
//            }
//        }
//    }


    class ViewHolder {
        TextView text;
        ImageView icon;
    }

}
