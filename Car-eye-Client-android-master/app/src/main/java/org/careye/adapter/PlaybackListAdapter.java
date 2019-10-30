package org.careye.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.careye.CarEyeClient.R;

import org.careye.model.TerminalFile;

import java.util.List;

public class PlaybackListAdapter extends BaseAdapter {

    private Context context;
    private List<TerminalFile> terminalFileList;

    public PlaybackListAdapter(Context context, List<TerminalFile> terminalFileList) {
        this.context = context;
        this.terminalFileList = terminalFileList;
    }

    @Override
    public int getCount() {
        return terminalFileList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return terminalFileList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.playback_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        TerminalFile terminalFile = terminalFileList.get(position);
        holder.tvStartTime.setText(terminalFile.getStartTime());
        holder.tvEndTime.setText(terminalFile.getEndTime());

        return convertView;
    }

    class ViewHolder {
        TextView tvStartTime, tvEndTime;

        public ViewHolder(View view) {
            tvStartTime = view.findViewById(R.id.lyPlaybackItem_tvTime);
            tvEndTime = view.findViewById(R.id.lyPlaybackItem_tvType);
        }
    }
}
