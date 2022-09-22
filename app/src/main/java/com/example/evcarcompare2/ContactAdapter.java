package com.example.evcarcompare2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{

    Context context;
    ArrayList<Car> list;
    private RecyclerViewClickListener mlistener;

    public ContactAdapter(Context context, ArrayList<Car> list, RecyclerViewClickListener listener){
        this.context = context;
        this.list = list;
        this.mlistener = listener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_contact, parent, false);
        return new ViewHolder(v, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.carName.setText(list.get(position).getName());
        holder.carPrice.setText(list.get(position).getDisPrice());
        holder.carImage.setImageBitmap(list.get(position).getBitmap());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView carName, carPrice;
        ImageView carImage;
        RecyclerViewClickListener listener;
        public ViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            carName = itemView.findViewById(R.id.carName);
            carPrice = itemView.findViewById(R.id.carPrice);
            carImage = itemView.findViewById(R.id.carImage);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }
}
