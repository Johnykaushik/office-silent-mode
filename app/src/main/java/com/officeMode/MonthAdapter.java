package com.officeMode;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthViewHolder> {

    private List<MonthData> monthDataList;
    private ScheduleStore storage;
    private Utils utils = new Utils();
    public MonthAdapter(List<MonthData> monthDataList,ScheduleStore store) {
        this.monthDataList = monthDataList;
        storage = store;
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
        holder.monthTextView.setText(monthData.getMonthName());
        holder.dateTextView.setText(monthData.getDays().toString());
        HashMap<String, HashSet<Integer>> monthDate = utils.getMonthDateFromStore(storage);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String monthName =  holder.monthTextView.getText().toString();
                String date = holder.dateTextView.getText().toString();
                if(monthDate.containsKey(monthName)){
                    HashSet<Integer> monthDateList = monthDate.get(monthName);
                    int targetDate = Integer.parseInt(date);
                    if(monthDateList.contains(targetDate)){
                        monthDate.get(monthName).remove(targetDate);
                        if(monthDate.get(monthName).size() == 0){
                            monthDate.remove(monthName);
                        }

                        StringBuilder formattedDates = new StringBuilder();

                        for(Map.Entry<String, HashSet<Integer>> entry : monthDate.entrySet()){
                            String nameOfMonth = entry.getKey();
                            HashSet<Integer> allDays =  entry.getValue();
                            formattedDates.append(nameOfMonth + "=");
                            for(int i : allDays){
                                formattedDates.append(i+"_");
                            }
                            formattedDates.append(",");
                        }
                        storage.setMonthDate(String.valueOf(formattedDates));

                        try {
                            Context context = v.getContext();
                            while (context instanceof ContextWrapper) {
                                if (context instanceof MainActivity) {
                                    MainActivity parentActivity = (MainActivity) context;
                                    parentActivity.setMonthDateFromStore(true); // Replace 'parentMethod' with your actual method name
                                    break;
                                }
                                context = ((ContextWrapper) context).getBaseContext();
                            }
                        }catch(Exception ex){
                            System.out.println("error message " + ex.getMessage());
                        }
                        System.out.println("Clicked item 2 "  +monthName + " < month " + storage.getMonthDate());
                    }
                }
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
        Button deleteButton;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.monthName);
            dateTextView = itemView.findViewById(R.id.dateList);
            deleteButton = itemView.findViewById(R.id.deleteButton);

        }
    }
}

