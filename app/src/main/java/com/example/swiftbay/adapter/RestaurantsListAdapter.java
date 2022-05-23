package com.example.swiftbay.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.swiftbay.Model.Restaurant;
import com.example.swiftbay.R;
import com.example.swiftbay.helper.GeneralCallBack;
import com.example.swiftbay.restaurant_home_page;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListAdapter.MyViewHolder> {

    private Context context;
    GeneralCallBack callBack;
     ArrayList<Restaurant> results;

    public RestaurantsListAdapter(ArrayList<Restaurant> results, Context context) {
        this.results = results;
        this.context = context;
    }

    public RestaurantsListAdapter( Context context,GeneralCallBack callBack,ArrayList<Restaurant> results) {
        this.results = results;
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.restaurantlist_recyclerview_row, parent, false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    public void addAllItems(ArrayList<Restaurant> results) {
        this.results.addAll(results);
        notifyDataSetChanged();
    }

    public void deletedItem(int Position) {
        this.results.remove(Position);
        notifyDataSetChanged();
    }
    public void clearAllData() {
        this.results.clear();
        notifyDataSetChanged();
    }
    public ArrayList getAllItems() {
        return results;
    }

    public void setAllItems(ArrayList allItems) {
        this.results = allItems;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Restaurant restaurant = results.get(position);
        if (restaurant == null)
            return;
        if (restaurant.getImage() != null && !restaurant.getImage().equals("")) {
            Glide.with(context).load(restaurant.getImage()).into(holder.restaurant_logo);
        }
        holder.restaurant_name.setText(restaurant.getName());
       // holder.restaurant_address.setText(restaurant.getAddress());
        holder.restaurant_city.setText(restaurant.getCity());
        holder.itemView.setOnClickListener(v -> {
            callBack.onTap(position);
        });
        holder.updatebtn.setOnClickListener(v -> {
            callBack.onUpdate(position);
        });
        holder.deletebtn.setOnClickListener(v -> {
            callBack.onDelete(position);
        });

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView restaurant_logo;
        public TextView restaurant_name;
        public TextView restaurant_address;
        public TextView restaurant_city;
        public Button updatebtn;
        public Button deletebtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            deletebtn = itemView.findViewById(R.id.deletebtn);
            updatebtn = itemView.findViewById(R.id.updatebtn);
            restaurant_logo = itemView.findViewById(R.id.restaurant_logo);
            restaurant_name = itemView.findViewById(R.id.restaurant_name);
            restaurant_address = itemView.findViewById(R.id.restaurant_address);
            restaurant_city = itemView.findViewById(R.id.restaurant_city);
        }
    }
}