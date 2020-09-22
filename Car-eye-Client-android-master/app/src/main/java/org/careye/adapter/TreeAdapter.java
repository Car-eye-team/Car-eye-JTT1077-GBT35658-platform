package org.careye.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.BaseAdapter;
import android.support.annotation.IntDef;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.careye.CarApplication;
import org.careye.model.DepartmentCar;
import org.careye.utils.DensityUtil;
import org.careye.utils.Tools;
import org.careye.utils.TreeUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.careye.CarEyeClient.R;

public class TreeAdapter extends BaseAdapter {

    private Context mContext;
    private String keyword;

    private List<DepartmentCar> deptList;

    /**
     * 两种操作模式  点击 或者 选择
     */
    private static final int ModeClick = 1;
    private static final int ModeSelect = 2;

    @IntDef({ModeClick, ModeSelect})
    public @interface Mode {

    }

    public TreeAdapter(final Context context, List<DepartmentCar> deptList) {
        this.mContext = context;
        this.deptList = deptList;
    }

    /**
     * 搜索的时候，先关闭所有的条目，然后，按照条件，找到含有关键字的数据
     * 如果是叶子节点，
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return deptList.size();
    }

    @Override
    public Object getItem(int position) {
        return deptList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.device_item, null);
            holder = new ViewHolder();
            holder.text = convertView.findViewById(R.id.text);
            holder.icon = convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DepartmentCar deptCar = (DepartmentCar) getItem(position);
        int level = TreeUtils.getLevel(deptCar, CarApplication.deptMap);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.icon.getLayoutParams();
        lp.leftMargin = 40 * level;
        holder.icon.setLayoutParams(lp);

        if (deptCar.getNodetype() == 1) {   // 如果为父节点
            if (!deptCar.isExpand()) {      // 不展开显示加号
                holder.icon.setImageResource(R.drawable.icon_coalesce_hover);
            } else {                        // 展开显示减号
                holder.icon.setImageResource(R.drawable.icon_expand_hover);
            }
        } else {   // 如果叶子节点，不占位显示
            holder.icon.setImageResource(deptCar.getImageResource(deptCar.getCarstatus()));
        }

        // 如果存在搜索关键字
        String name = deptCar.getNodeName();
        if (!Tools.isNull(keyword) && name.contains(keyword)) {
            int index = name.indexOf(keyword);
            int len = keyword.length();

            String str = name.substring(0, index)
                    + "<font color='#FF0000'>"
                    + name.substring(index, index + len) + "</font>"
                    + name.substring(index + len, name.length());

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.text.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.text.setText(Html.fromHtml(str));
            }
        } else {
            holder.text.setText(name);
        }

        holder.text.setCompoundDrawablePadding(dip2px(mContext, 10));
        return convertView;
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    class ViewHolder {
        TextView text;
        ImageView icon;
    }
}
