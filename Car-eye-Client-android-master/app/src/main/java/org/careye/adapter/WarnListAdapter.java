package org.careye.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careye.CarEyeClient.R;

import org.careye.activity.WarnActivity;
import org.careye.model.Alarm;

import java.util.List;

public class WarnListAdapter extends BaseAdapter {

    private Context context;
    private List<Alarm> alarms;

    public WarnListAdapter(Context context, List<Alarm> alarms) {
        this.context = context;
        this.alarms = alarms;
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.warn_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final Alarm item = alarms.get(position);
        holder.warn_item_number_tv.setText(item.getCarNumber());
        holder.warn_item_desc_tv.setText(item.getAlarmDesc());
        holder.warn_item_time_tv.setText(item.getTime());

        holder.warn_item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WarnActivity.class);
                intent.putExtra("Alarm", item);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        LinearLayout warn_item_ll;
        TextView warn_item_number_tv, warn_item_desc_tv, warn_item_time_tv;

        public ViewHolder(View view) {
            warn_item_ll = view.findViewById(R.id.warn_item_ll);
            warn_item_number_tv = view.findViewById(R.id.warn_item_number_tv);
            warn_item_desc_tv = view.findViewById(R.id.warn_item_desc_tv);
            warn_item_time_tv = view.findViewById(R.id.warn_item_time_tv);
        }
    }
}
