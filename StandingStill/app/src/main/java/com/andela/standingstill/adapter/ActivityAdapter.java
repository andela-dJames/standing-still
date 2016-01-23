package com.andela.standingstill.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andela.standingstill.R;
import com.andela.standingstill.model.Movement;

import java.util.List;


public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private Context context;
    private List<Movement> movements;

    public ActivityAdapter(List<Movement> movements) {
        this.movements = movements;
    }

    public ActivityAdapter(Context context, List<Movement> movements) {
        this.context = context;
        this.movements = movements;
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
        ActivityViewHolder viewHolder = new ActivityViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ActivityViewHolder holder, int position) {
        Movement movement = movements.get(position);
        String type = movement.movementTypeToString();
        if (type.equals("Standing Still")){
            holder.linearLayout.setBackgroundColor(Color.parseColor("yellow"));
        }
        else if (type.equals("Transport")){
            holder.linearLayout.setBackgroundColor(Color.parseColor("darkgray"));
        }

        else{
            holder.linearLayout.setBackgroundColor(Color.parseColor("magenta"));
        }
        holder.locationTextView.setText(movements.get(position).getAddress());
        holder.timeTextView.setText(movements.get(position).getTimeSpentToString());
        holder.activityTextView.setText(movements.get(position).movementTypeToString());

    }

    @Override
    public int getItemCount() {
        return movements.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder{

        TextView timeTextView;
        TextView activityTextView;
        TextView locationTextView;
        LinearLayout linearLayout;


        public ActivityViewHolder(View itemView) {
            super(itemView);
            timeTextView = (TextView) itemView.findViewById(R.id.timeline_time);
            activityTextView = (TextView) itemView.findViewById(R.id.timeline_activity);
            locationTextView = (TextView) itemView.findViewById(R.id.timeline_location);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.timeline_indicator);
        }



    }
}
