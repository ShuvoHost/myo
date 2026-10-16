package com.example.myapps;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AgeCalculator extends AppCompatActivity {

    private EditText dayEt, monthEt, yearEt;
    private Button calculateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_calculator);

        dayEt = findViewById(R.id.dayEt);
        monthEt = findViewById(R.id.monthEt);
        yearEt = findViewById(R.id.yearEt);
        calculateBtn = findViewById(R.id.calculateBtn);

        calculateBtn.setOnClickListener(v -> calculateAge());
    }

    private void calculateAge() {

        String dayText = dayEt.getText().toString().trim();
        String monthText = monthEt.getText().toString().trim();
        String yearText = yearEt.getText().toString().trim();

        if (dayText.isEmpty()
                || monthText.isEmpty()
                || yearText.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter day, month and year",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int day, month, year;

        try {
            day = Integer.parseInt(dayText);
            month = Integer.parseInt(monthText);
            year = Integer.parseInt(yearText);
        } catch (NumberFormatException e) {
            Toast.makeText(
                    this,
                    "Invalid date",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Calendar birthDate = Calendar.getInstance();
        birthDate.clear();
        birthDate.setLenient(false);
        birthDate.set(year, month - 1, day);

        try {
            birthDate.getTimeInMillis();
        } catch (IllegalArgumentException e) {
            Toast.makeText(
                    this,
                    "Please enter a valid date",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Calendar today = Calendar.getInstance();

        // Remove the time portion
        today.set(
                Calendar.HOUR_OF_DAY, 0
        );
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if (birthDate.after(today)) {
            Toast.makeText(
                    this,
                    "Birth date cannot be in the future",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        int years = today.get(Calendar.YEAR)
                - birthDate.get(Calendar.YEAR);

        int months = today.get(Calendar.MONTH)
                - birthDate.get(Calendar.MONTH);

        int days = today.get(Calendar.DAY_OF_MONTH)
                - birthDate.get(Calendar.DAY_OF_MONTH);

        // Adjust days when current day is smaller
        if (days < 0) {
            Calendar previousMonth =
                    (Calendar) today.clone();

            previousMonth.set(
                    Calendar.DAY_OF_MONTH, 1
            );
            previousMonth.add(Calendar.MONTH, -1);

            days += previousMonth.getActualMaximum(
                    Calendar.DAY_OF_MONTH
            );

            months--;
        }

        // Adjust months when current month is smaller
        if (months < 0) {
            months += 12;
            years--;
        }

        showAgeDialog(years, months, days);
    }

    private void showAgeDialog(
            int years, int months, int days) {

        String message =
                "Years: " + years
                        + "\nMonths: " + months
                        + "\nDays: " + days;

        new AlertDialog.Builder(this)
                .setTitle("Your Age")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}
