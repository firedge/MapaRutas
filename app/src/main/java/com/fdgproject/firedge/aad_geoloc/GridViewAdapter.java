package com.fdgproject.firedge.aad_geoloc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<String> {

    private Context cnt;
    private ArrayList<String> list;
    private int rec;
    private static LayoutInflater lin;

    public static class ViewHolder{
        public TextView tv;
    }

    public GridViewAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        this.cnt = context;
        this.list = objects;
        this.rec = resource;
        this.lin = (LayoutInflater)cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null) {
            convertView = lin.inflate(rec, null);
            vh =new ViewHolder();
            vh.tv = (TextView)convertView.findViewById(R.id.text);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }
        vh.tv.setText(list.get(position));
        return convertView;
    }
}
