package com.example.carapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CarViewHolder> {

    private ArrayList<Car>cars ;
    private OnRecyclerVIewClick click;
    public RecyclerViewAdapter(ArrayList<Car> cars,OnRecyclerVIewClick click){
        this.cars = cars;
        this.click = click;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_car_layout,null,false);
        CarViewHolder viewHolder = new CarViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car c =cars.get(position);
        if(c.getImage() != null && !c.getImage().isEmpty()){
            Log.d("image_name",c.getImage());
            holder.iv.setImageURI(Uri.parse(c.getImage()));
        }else{
            holder.iv.setImageResource(R.drawable.carphoto);
        }
        holder.tv_model.setText(c.getModel());
        holder.tv_color.setText(c.getColor());
        try {
            holder.tv_color.setTextColor(Color.parseColor(c.getColor()));
        }catch (Exception ex){

        }
        holder.tv_dpl.setText(String.valueOf(c.getDpl()));

        holder.iv.setTag(c.getId());
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    class  CarViewHolder extends  RecyclerView.ViewHolder{

        ImageView iv;
        TextView tv_model,tv_color,tv_dpl;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.custom_car_iv);
            tv_model = itemView.findViewById(R.id.custom_car_tv_model);
            tv_color = itemView.findViewById(R.id.custom_car_tv_color);
            tv_dpl = itemView.findViewById(R.id.custom_car_tv_dpl);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = (int)iv.getTag();
                    click.OnItemClick(id);
                }
            });
        }
    }

}
