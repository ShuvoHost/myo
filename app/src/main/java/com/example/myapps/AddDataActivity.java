package com.example.myapps;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddDataActivity extends AppCompatActivity {

    DataViewModel viewModel;
    private EditText etNumber;
    private Spinner spinner;
    private Button btnSave;
    private int updateId = -1;
    String updateSpinnerValue = "";
    private TextView tvdate;
    String selectedDate = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        // All Initialisser Method must have be abov
        allInitaliger();
        viewModel = new ViewModelProvider(AddDataActivity.this).get(DataViewModel.class);


        //spinnar Method
        spinnerMethod();

        //Data Save Method btnSave
        saveDataMethod();

        //Defolt today date method
        defoltTodaDate();

        //Date / Calander Method
        calanderMethod();



    }

    private void defoltTodaDate() {
        Calendar calendar = Calendar.getInstance();

        // dd/MM/yyyy format
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDate = sdf.format(calendar.getTime());

        tvdate.setText(selectedDate);
    }

    private void calanderMethod() {
        tvdate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(v.getContext(),
                    (view, y, m, d) -> {
                        Calendar selectedCal = Calendar.getInstance();
                        selectedCal.set(y, m, d);

                        // Format date properly
                       SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                       selectedDate = sdf.format(selectedCal.getTime());

                       // @SuppressLint("DefaultLocale") String date = String.format("%04d-%02d-%02d", y, m + 1, d);
                       // etDate.setText(date);
                        tvdate.setText(selectedDate);
                    },
                    year, month, day
            );
            dialog.show();
        });
    }

    private void allInitaliger() {
        etNumber = findViewById(R.id.etNumber);
        spinner = findViewById(R.id.spinner);
        btnSave = findViewById(R.id.btnSave);
        tvdate = findViewById(R.id.tvDate);
    }

    private void saveDataMethod() {
        btnSave.setOnClickListener(v -> {

            String number = etNumber.getText().toString().trim();
            String spinnerValue = spinner.getSelectedItem().toString();
            String date = tvdate.getText().toString().trim();//new

            if (number.isEmpty()) {
                etNumber.setError("Enter Overtime");
                return;
            }


            if (updateId == -1) {
                viewModel.insert(new DataEntity(0, number, spinnerValue, selectedDate));
                Toast.makeText(v.getContext(), "Saved", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.update(new DataEntity(updateId, number, spinnerValue, selectedDate));
                Toast.makeText(v.getContext(),"Update", Toast.LENGTH_SHORT).show();
            }

            finish(); // back to list
        });

    }

    @SuppressLint("SetTextI18n")
    private void spinnerMethod() {

        // Spinner data
        String[] items = {"Regular ", "Day Shift", "Night Shift" ,"OFF Day", "Leave", "Absent"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                spinner.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                items
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner.setAdapter(adapter);

        // 👉 Update mode check
        if (getIntent().hasExtra("id")) {
            updateId = getIntent().getIntExtra("id", -1);

            String number = getIntent().getStringExtra("number");
            updateSpinnerValue = getIntent().getStringExtra("spinner");
            selectedDate = getIntent().getStringExtra("date");

            tvdate.setText(selectedDate);
            etNumber.setText(number);
            btnSave.setText("Update");

            // Spinner old value set
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(updateSpinnerValue)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }

    }
}

