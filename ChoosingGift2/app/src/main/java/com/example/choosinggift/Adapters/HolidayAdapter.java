package com.example.choosinggift.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choosinggift.HolidaysActivity;
import com.example.choosinggift.Models.HolidaysModel;
import com.example.choosinggift.Models.HolllidayModel;
import com.example.choosinggift.R;
import com.example.choosinggift.SetsActivity;
import com.example.choosinggift.databinding.ItemHolidayBinding;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.viewHolder> {

    Context context;

    ArrayList<HolidaysModel> list = new ArrayList<HolidaysModel>();

    public HolidayAdapter(Context context, ArrayList<HolidaysModel> list) {
        this.context = context;
        this.list = list;
    }

    public HolidayAdapter(HolidaysActivity context, ArrayList<HolllidayModel> list) {
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_holiday,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        HolidaysModel model = list.get(position);

        holder.binding.categoryName.setText(model.getCategoryName());

        Picasso.get()
                .load(model.getCategoryImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.categoryImages);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, SetsActivity.class);
                intent.putExtra("category",model.getCategoryName());
                intent.putExtra("sets",model.getSetNum());
                intent.putExtra("key",model.getKey());

                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ItemHolidayBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemHolidayBinding.bind(itemView);

        }
    }
}
