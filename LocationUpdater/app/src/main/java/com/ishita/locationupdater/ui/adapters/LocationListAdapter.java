package com.ishita.locationupdater.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ishita.locationupdater.R;
import com.ishita.locationupdater.model.LocationInformation;
import com.ishita.locationupdater.utility.Category;
import com.ishita.locationupdater.utility.JSONUtils;

import java.util.ArrayList;

public class LocationListAdapter extends BaseAdapter{

    private ArrayList<LocationInformation> information;
    private Activity activity;
    private LayoutInflater inflater;

    public LocationListAdapter(Activity activity) {
        Log.d(Category.CATEGORY_ADAPTER,"LocationListAdapter constructor");
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setListAndNotify();
    }

    public void setListAndNotify() {
        ArrayList<LocationInformation> infos = JSONUtils.getArrayListFromJSONData(activity);
        if(infos!=null) {
            information = new ArrayList<>(infos);
        }else {
            information = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(information!= null){
            return information.size();
        }
        return 0;
    }

    @Override
    public LocationInformation getItem(int position) {
        return information.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.location_info_item,parent,false);
        }
        LocationInformation information = getItem(position);
        TextView tvLocationName = convertView.findViewById(R.id.tv_location_name);
        tvLocationName.setText(information.getLocationName());
        TextView tvStateCity = convertView.findViewById(R.id.tv_state_city_info);
        tvStateCity.setText(activity.getString(R.string.lbl_city_state,information.getCity(),information.getState()));
        return convertView;
    }
}
