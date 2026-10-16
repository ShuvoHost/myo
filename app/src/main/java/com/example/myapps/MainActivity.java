package com.example.myapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LinearLayout iconAdd,history,jobyear;
    private TextView overtimeTV;
    DataViewModel dataViewModel;
    private Calendar currentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        iconAdd = findViewById(R.id.add_icon);
        history = findViewById(R.id.history_icon);
        overtimeTV = findViewById(R.id.otMain);
        jobyear = findViewById(R.id.JobYear_icon);


        // Calendar
        currentCalendar = Calendar.getInstance();

        //viewmodel


        dataViewModel = new ViewModelProvider(MainActivity.this).get(DataViewModel.class);


        // First load
        updateMonthUI();
        loadMonthData();



        dataViewModel.getMonthData().observe(this, new Observer<List<DataEntity>>() {
            @Override
            public void onChanged(List<DataEntity> dataList) {

              //  adapter.setData(dataList);

                int total = 0;
                for (DataEntity d : dataList) {
                    try {
                        total += Integer.parseInt(d.getNumberText());
                    } catch (Exception ignored) {
                    }
                }
             //   totalOt.setText("Total : " + total);
                 overtimeTV.setText("Total : " + total);
            }
        });










        iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, AddDataActivity.class));
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, ListActivity.class));


            }
        });

        jobyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, AgeCalculator.class));


            }
        });





    }


    @Override
    protected void onResume() {
        super.onResume();

        // Read the current device date again
        currentCalendar = Calendar.getInstance();

        // Reload data for the current month
        loadMonthData();
    }


    private void loadMonthData() {

        currentCalendar = Calendar.getInstance();
        int month = currentCalendar.get(Calendar.MONTH) +1;
        int year = currentCalendar.get(Calendar.YEAR);
        String monthStr = String.format(Locale.getDefault(), "%02d", month);

        dataViewModel.loadMonth(monthStr, String.valueOf(year));
    }



   private void updateMonthUI() {
        SimpleDateFormat sdf =
                new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
       // tvMonth.setText(sdf.format(currentCalendar.getTime()));
    }


}