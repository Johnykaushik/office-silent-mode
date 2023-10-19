package com.officeMode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.HashSet;
import java.util.Set;

public class CustomCalendarDialogFragment extends DialogFragment {

    private CalendarView calendarView;
    private Button cancelButton;
    private Button okButton;

    private Set<Long> selectedDateSet = new HashSet<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_calendar, null);

        calendarView = view.findViewById(R.id.calendarView);
        cancelButton = view.findViewById(R.id.cancelButton);
        okButton = view.findViewById(R.id.okButton);

        // Set up CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Convert selected date to timestamp
                long selectedDate = getDateTimestamp(year, month, dayOfMonth);
                if (selectedDateSet.contains(selectedDate)) {
                    selectedDateSet.remove(selectedDate);
                } else {
                    selectedDateSet.add(selectedDate);
                }
            }
        });

        // Handle cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Handle OK button
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process selected dates
                // You can send the selected dates to your activity/fragment here

                dismiss();
            }
        });

        return new AlertDialog.Builder(requireContext())
                .setTitle("Select Dates")
                .setView(view)
                .create();
    }

    private long getDateTimestamp(int year, int month, int dayOfMonth) {
        // Convert year, month, and day to a timestamp
        // You can implement this method based on your requirements
        // For example: return timestamp;
        return 111111111;
    }
}
