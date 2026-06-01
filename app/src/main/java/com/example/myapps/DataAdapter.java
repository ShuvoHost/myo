package com.example.myapps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    Context context;
    List<DataEntity> list;

    public DataAdapter(Context context, List<DataEntity> list) {
        this.context = context;
        this.list = list;
    }



    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<DataEntity> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DataEntity data = list.get(position);

      //  holder.tvNumber.setText(data.numberText);
       // holder.tvSpinner.setText(data.spinnerText);
     //   holder.tvdate.setText(data.date);
        holder.tvNumber.setText(data.getNumberText());
        holder.tvSpinner.setText(data.getSpinnerText());
        holder.tvdate.setText(data.getDate());

        holder.ivMenu.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(context, holder.ivMenu);
            popupMenu.inflate(R.menu.menu_item);

            popupMenu.setOnMenuItemClickListener(item -> {

                if (item.getItemId() == R.id.menu_delete) {
                    AppDatabase.getInstance(context)
                            .dataDao().delete(data);

                    list.remove(position);
                    notifyItemRemoved(position);
                }

                if (item.getItemId() == R.id.menu_update) {
                    Intent intent = new Intent(context, AddDataActivity.class);
                    intent.putExtra("id", data.id);
                    intent.putExtra("number", data.numberText);
                    intent.putExtra("spinner", data.spinnerText);
                    intent.putExtra("date", data.date);
                    context.startActivity(intent);
                }

                return true;
            });

            popupMenu.show();
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNumber, tvSpinner,tvdate;
        ImageView ivMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvSpinner = itemView.findViewById(R.id.tvSpinner);
            tvdate = itemView.findViewById(R.id.tvDateRecyler);
            ivMenu = itemView.findViewById(R.id.ivMenu);
        }
    }
}
