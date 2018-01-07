package com.example.archit.meracut;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dhruv on 19-05-2016.
 */
public class StyleNameGridViewAdapter extends BaseAdapter {

    private Context _context;
    private ArrayList<StyleNameData> stylenames;

    public StyleNameGridViewAdapter(Context _context, ArrayList<StyleNameData> styles) {
        this._context = _context;
        this.stylenames = styles;
    }

    private class ViewHolder {
        TextView style;
        ImageView img;

    }

    @Override
    public int getCount() {

        return stylenames.size();
    }

    @Override
    public Object getItem(int position) {

        return stylenames.get(position);
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
            convertView = mInflater.inflate(R.layout.card_stylename, null);
            holder = new ViewHolder();
            holder.style = (TextView) convertView.findViewById(R.id.stylename);
            holder.img = (ImageView) convertView.findViewById(R.id.stylephoto);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StyleNameData stylelist = (StyleNameData) getItem(position);
        holder.style.setText(stylelist.getstylename());


        int loader = R.drawable.img1;
        String image_url = stylelist.geturl();
        Log.d("imageurl",image_url);

        Application app = stylelist.getapp();

        /*new DownloadImage(holder.img)
                .execute(image_url);*/

        Picasso.with(_context)
                .load(image_url)
                .placeholder(R.drawable.placeholder)
                .into(holder.img);

        return convertView;
    }
}

