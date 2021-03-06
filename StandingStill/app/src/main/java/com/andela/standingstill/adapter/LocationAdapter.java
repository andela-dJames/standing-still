package com.andela.standingstill.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andela.standingstill.R;
import com.andela.standingstill.model.Place;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by andeladev on 21/01/2016.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder>{

    private List<Place> places;
    private Context context;

    public LocationAdapter(Context context) {
        this.context = context;
        places = new ArrayList<>();
    }

    public LocationAdapter(List<Place> places, Context context) {
        this.places = places;
        this.context = context;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_items, parent, false);
        LocationViewHolder viewholder = new LocationViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        if (places.isEmpty()){
            holder.placeIcon.setVisibility(View.INVISIBLE);
            holder.totalTimeView.setVisibility(View.INVISIBLE);
            holder.addressView.setVisibility(View.INVISIBLE);


        }
        else {
            Place place = places.get(position);
            holder.placeIcon.setImageResource(R.drawable.ic_place_list);
            holder.addressView.setText(place.getAddress());
            holder.totalTimeView.setText(place.getTimeSpentToString());


        }

    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout parent;

        ImageView placeIcon;

        TextView addressView;

        TextView totalTimeView;

        TextView noItemsView;

        public LocationViewHolder(View itemView) {
            super(itemView);
            parent = (RelativeLayout) itemView.findViewById(R.id.parent_layout);
            placeIcon = (ImageView) itemView.findViewById(R.id.place_icon);
            addressView = (TextView) itemView.findViewById(R.id.address_text);
            totalTimeView = (TextView) itemView.findViewById(R.id.total_time_text);
        }
    }
}
