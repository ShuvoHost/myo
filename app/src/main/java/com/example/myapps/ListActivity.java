package com.example.myapps;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DataViewModel viewModel;
    DataAdapter adapter;

    private TextView totalOt, tvMonth;
    private ImageButton btnPrev, btnNext;

    private Calendar currentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // init views
        totalOt = findViewById(R.id.ttl_ot);
        tvMonth = findViewById(R.id.tvMonth);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter first
        adapter = new DataAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);


        // Calendar
        currentCalendar = Calendar.getInstance();

        // ViewModel
        viewModel = new ViewModelProvider(this).get(DataViewModel.class);

        // First load
        updateMonthUI();
        loadMonthData();

        // Previous month
        btnPrev.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            updateMonthUI();
            loadMonthData();
        });

        // Next month
        btnNext.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateMonthUI();
            loadMonthData();
        });

        viewModel.getMonthData().observe(this, dataList -> {
            adapter.setData(dataList);

            int total = 0;
            for (DataEntity d : dataList) {
                try {
                    total += Integer.parseInt(d.getNumberText());
                } catch (Exception ignored) {}
            }
            totalOt.setText("Total : " + total);
        });

    }

    // Month load
    private void loadMonthData() {
        int month = currentCalendar.get(Calendar.MONTH) + 1;
        int year = currentCalendar.get(Calendar.YEAR);
        String monthStr = String.format(Locale.getDefault(), "%02d", month);

        viewModel.loadMonth(monthStr, String.valueOf(year));
    }



    private void updateMonthUI() {
        SimpleDateFormat sdf =
                new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        tvMonth.setText(sdf.format(currentCalendar.getTime()));
    }
}

/*    private void loadMonthData() {

        int month = currentCalendar.get(Calendar.MONTH) + 1;
        int year = currentCalendar.get(Calendar.YEAR);

        String monthStr = String.format(Locale.getDefault(), "%02d", month);

        viewModel.getDataByMonth(monthStr, String.valueOf(year))
                .observe(this, dataList -> {

                    adapter.setData(dataList);

                    int total = 0;
                    for (DataEntity d : dataList) {
                        try {
                            total += Integer.parseInt(d.getNumberText());
                        } catch (Exception ignored) {}
                    }

                    totalOt.setText("Total : " + total);
                });
    }*/