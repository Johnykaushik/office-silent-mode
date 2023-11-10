package com.officeMode;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthViewHolder> {

    private List<MonthData> monthDataList;
    private ScheduleStore storage;
    private Utils utils = new Utils();

    private IButtonClick iButtonClick;

    public MonthAdapter(List<MonthData> monthDataList,ScheduleStore store, IButtonClick iButtonClick) {
        this.monthDataList = monthDataList;
        storage = store;
        this.iButtonClick = iButtonClick;
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_month_dates, parent, false);
        return new MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {
        MonthData monthData = monthDataList.get(position);

        String storeDates = storage.getMonthDate();
        Log.d("TAG", "onBindViewHolder: " + monthData.getMonthName() + " get day: " + monthData.getDays());
        holder.monthTextView.setText(monthData.getMonthName());
        holder.dateTextView.setText(monthData.getDays().toString());
        holder.unixDate.setText(monthData.getUnixDate().toString());
        holder.unixDate.setVisibility(View.GONE);
        Utils utils = new Utils();
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iButtonClick.clickItem(position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return monthDataList.size();
    }

    static class MonthViewHolder extends RecyclerView.ViewHolder {
        TextView monthTextView;
        TextView dateTextView;
        TextView unixDate;
        ImageView deleteButton;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.monthName);
            dateTextView = itemView.findViewById(R.id.dateList);
            unixDate = itemView.findViewById(R.id.unixDate);
            deleteButton = itemView.findViewById(R.id.delete_calender_date);

        }
    }
}

