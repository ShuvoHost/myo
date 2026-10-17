package com.example.myapps;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SalaryCalculator extends AppCompatActivity{

    private EditText etTotalSalary, etAttendanceBonus, etTransport,
            etOters, etHour,etAbsentDay,etTiffinDay,etTiffinTaka,etNightDay,etNightTaka;
    private Button btnCalculate;

    private int health = 750;
    private int transportFixed = 450;
    private int food = 1250;
    private double bariVaraPercent = 1.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_calculator);

        etTotalSalary = findViewById(R.id.etTotalSalary);
        btnCalculate = findViewById(R.id.btnCalculate);
        etAttendanceBonus = findViewById(R.id.etAttendanceBonus);
        etTransport = findViewById(R.id.etTransport);
        etOters = findViewById(R.id.etOthers);
        etHour = findViewById(R.id.etHour);
        etAbsentDay = findViewById(R.id.etAbsent);
        etTiffinDay = findViewById(R.id.etTiffinDay);
        etTiffinTaka = findViewById(R.id.etTiffinTaka);
        etNightDay = findViewById(R.id.etNightDay);
        etNightTaka = findViewById(R.id.etNightTaka);

        btnCalculate.setOnClickListener(v -> calculateSalary());
    }

    // ===================== DIALOG =====================
    private void salaryCalculator(double totalByAll,
                                  double totalOtTaka,
                                  double attendance,
                                  double transport,
                                  double others,
                                  double basicSalary,
                                  double otRate,
                                  double absentCost,
                                  double tiffinBill,
                                  double nightBill) {

        Dialog dialog = new Dialog(SalaryCalculator.this);
        dialog.setContentView(R.layout.dialog_salary_result);

        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
        TextView tvTotalSalary = dialog.findViewById(R.id.tvTotalSalary);
        TextView tvBasicSalary = dialog.findViewById(R.id.tvBasicSalary);
        TextView tvOvertimeRate = dialog.findViewById(R.id.tvOvertimeRate);
        TextView tvAtendanceBonus = dialog.findViewById(R.id.tvAtendanceBonus);
        TextView tvTransport = dialog.findViewById(R.id.tvTransport);
        TextView tvOther = dialog.findViewById(R.id.tvOthers);
        TextView tvOvertimetaka = dialog.findViewById(R.id.tvOvertimeTaka);
        TextView tvabsentcost = dialog.findViewById(R.id.absentCost);
        TextView tvTiffinBill = dialog.findViewById(R.id.tvTiffinBill);
        TextView tvNightBill = dialog.findViewById(R.id.tvNightBill);

        // Set values
        tvTotalSalary.setText(String.format("%.2f", totalByAll));
        tvBasicSalary.setText(String.format("%.2f", basicSalary));
        tvOvertimeRate.setText(String.format("%.2f", otRate));
        tvAtendanceBonus.setText(String.format("%.2f", attendance));
        tvTransport.setText(String.format("%.2f", transport));
        tvOther.setText(String.format("%.2f", others));
        tvOvertimetaka.setText(String.format("%.2f", totalOtTaka));
        tvabsentcost.setText(String.format(String.format("%.2f", absentCost)));
        tvTiffinBill.setText(String.format(String.format("%.2f", tiffinBill)));
        tvNightBill.setText(String.format(String.format("%.2f", nightBill)));

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        dialog.show(); // ✅ only once
    }

    // ===================== CALCULATION =====================
    private void calculateSalary() {

        String stSalaryStr = etTotalSalary.getText().toString().trim();
        String stOtHourStr = etHour.getText().toString().trim();
        String stAttendanceBns = etAttendanceBonus.getText().toString().trim();
        String stTransport = etTransport.getText().toString().trim();
        String stOthers = etOters.getText().toString().trim();
        String stAbsent = etAbsentDay.getText().toString().trim();
        String stTiffinDay = etTiffinDay.getText().toString().trim();
        String stTiffinTaka = etTiffinTaka.getText().toString().trim();
        String stNightDay = etNightDay.getText().toString().trim();
        String stNIghtTaka = etNightTaka.getText().toString().trim();

        // ✅ Validation (important)
        if (stSalaryStr.isEmpty()) {
            etTotalSalary.setError("Enter total salary");
            return;
        }
        if (stOtHourStr.isEmpty()) {
            etHour.setError("Enter OT hour");
            return;
        }
        if (stAttendanceBns.isEmpty()) {
            etAttendanceBonus.setError("Enter attendance bonus");
            return;
        }
        if (stTransport.isEmpty()) {
            etTransport.setError("Enter transport");
            return;
        }
        if (stOthers.isEmpty()) {
            etOters.setError("Enter others");
            return;
        }

        double totalSalary = Double.parseDouble(stSalaryStr);
        double otHour = Double.parseDouble(stOtHourStr);
        double attendance = Double.parseDouble(stAttendanceBns);
        double transportInput = Double.parseDouble(stTransport);
        double others = Double.parseDouble(stOthers);
        double absent = Double.parseDouble(stAbsent);
        double tiffinday = Double.parseDouble(stTiffinDay);
        double tiffinTaka = Double.parseDouble(stTiffinTaka);
        double nightDay = Double.parseDouble(stNightDay);
        double nightTaka = Double.parseDouble(stNIghtTaka);

        // ===================== LOGIC =====================
        double basicWithoutHouse = totalSalary - health - transportFixed - food;
        double finalBasic = basicWithoutHouse / bariVaraPercent;

        double absentFormula = finalBasic / 30;
        double absentFinalCost = absent * absentFormula;

        double overtimeRate = (finalBasic / 208) * 2;
        double otAmount = otHour * overtimeRate;

        // Just Tiffin Bill And Night Bill
        double tiffinBillFormula = tiffinday * tiffinTaka;
        double nightBillFormula = nightDay * nightTaka;

        double totalSalaryAll = otAmount + attendance + transportInput + others + totalSalary;

        double totalSaleryWithABsentCost = totalSalaryAll - absentFinalCost;
        // ✅ Correct parameter passing
        salaryCalculator(
                totalSaleryWithABsentCost,
                otAmount,
                attendance,
                transportInput,
                others,
                finalBasic,
                overtimeRate,
                absentFinalCost,
                tiffinBillFormula,
                nightBillFormula
        );
    }
}