package com.example.gift.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gift.Models.HolllidayModel;
import com.example.gift.R;
import com.example.gift.databinding.ItemHolidayBinding;
import com.example.gift.databinding.ItemHolidayssBinding;

import java.util.ArrayList;

public class HolllidaysAdapter extends RecyclerView.Adapter<HolllidaysAdapter.viewHolder> {

    Context context;
    ArrayList<HolllidayModel>list;
    DeleteListener listener;
    String categoryName;

    public HolllidaysAdapter(Context context, ArrayList<HolllidayModel> list, String categoryName, DeleteListener listener) {
        this.context = context;
        this.list = list;
        this.categoryName = categoryName;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_holidayss,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        HolllidayModel model = list.get(position);

        holder.binding.question.setText(model.getHoliday());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onLongClick(position,list.get(position).getKey());

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ItemHolidayssBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemHolidayssBinding.bind(itemView);

        }
    }
    public interface DeleteListener{

        public void onLongClick(int position,String id);
    }

}


