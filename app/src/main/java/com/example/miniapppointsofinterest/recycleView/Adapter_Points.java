package com.example.miniapppointsofinterest.recycleView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.miniapppointsofinterest.R;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniapppointsofinterest.model.object.ObjectBoundary;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class Adapter_Points extends RecyclerView.Adapter<Adapter_Points.PointViewHolder> {
    private ArrayList<ObjectBoundary> points;
    private OnPointClickListener onPointClickListener;

    public Adapter_Points(ArrayList<ObjectBoundary> points) {
        this.points = points;
    }

    public void updateList(ArrayList<ObjectBoundary> points) {
        this.points = points;
        notifyDataSetChanged();
    }

    public void setOnPointClickListener(OnPointClickListener onPointClickListener) {
        this.onPointClickListener = onPointClickListener;
    }

    @NonNull
    @Override
    public PointViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_point, viewGroup, false);
        return new PointViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointViewHolder holder, int position) {
        ObjectBoundary point = getItem(position);

        holder.point_LBL_title.setText(holder.point_LBL_title.getText() +" "+point.getAlias().toString());
        holder.point_LBL_user.setText(point.getCreatedBy().getUserId().getEmail().toString());
//        holder.point_LBL_type.setText(point.getObjectDetails().get("type").toString());
//        holder.point_RTG_rating.setRating((float) point.getObjectDetails().get("rating"));

//        Imager.me().imageCrop(holder.point_IMG_image, point.getObjectDetails().get("image"));

//        if (point.isLiked()) {
//            holder.game_IMG_like.setImageResource(R.drawable.ic_heart_filled);
//        } else {
//            holder.game_IMG_like.setImageResource(R.drawable.ic_heart_linear);
//        }
    }

    @Override
    public int getItemCount() {
        return points == null ? 0 : points.size();
    }

    private ObjectBoundary getItem(int position) {
        return points.get(position);
    }

    public interface OnPointClickListener {
        void onClick(View view, ObjectBoundary point, int position);
        void onLikeClicked(View view, ObjectBoundary point, int position);
    }

    public class PointViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView  point_IMG_image;
        private AppCompatImageView  point_IMG_like;
        private MaterialTextView    point_LBL_title;
        private MaterialTextView    point_LBL_type;
        private MaterialTextView    point_LBL_user;
        private AppCompatRatingBar  point_RTG_rating;

        PointViewHolder(View itemView) {
            super(itemView);
//            point_IMG_image = itemView.findViewById(R.id. point_IMG_image);
            point_IMG_like = itemView.findViewById(R.id.point_IMG_like);
            point_LBL_title = itemView.findViewById(R.id.point_LBL_title);
            point_LBL_type = itemView.findViewById(R.id.point_LBL_type);
            point_LBL_user = itemView.findViewById(R.id.point_LBL_user);
//            point_RTG_rating = itemView.findViewById(R.id.point_RTG_rating);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPointClickListener.onClick(v, getItem(getAdapterPosition()), getAdapterPosition());
                }
            });

            point_IMG_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPointClickListener.onLikeClicked(v, getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }

}