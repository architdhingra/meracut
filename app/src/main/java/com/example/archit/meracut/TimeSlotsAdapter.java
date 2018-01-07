package com.example.archit.meracut;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dhruv on 24-05-2016.
 */
public class TimeSlotsAdapter extends BaseAdapter {

    private Context _context;
    private ArrayList<StyleNameData> timeslots;

    public TimeSlotsAdapter(Context _context, ArrayList<StyleNameData> times) {
        this._context = _context;
        this.timeslots = times;
    }

    private class ViewHolder {
        TextView txt_timeslots;

    }

    @Override
    public int getCount() {

        return timeslots.size();
    }

    @Override
    public Object getItem(int position) {

        return timeslots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.timeslots_grid, null);
            holder = new ViewHolder();
            holder.txt_timeslots = (TextView) convertView.findViewById(R.id.slot_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StyleNameData timelist = (StyleNameData) getItem(position);
        holder.txt_timeslots.setText(timelist.gettime());

        return convertView;
    }
}


