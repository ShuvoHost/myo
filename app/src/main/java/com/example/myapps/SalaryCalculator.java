package com.example.myapps;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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

        // 1. Required inputs
        Double salaryValue = readNumber(
                etTotalSalary, true, "Enter total salary"
        );

        Double otHourValue = readNumber(
                etHour, true, "Enter OT hour"
        );

        if (salaryValue == null || otHourValue == null) {
            return;
        }

        // 2. Tiffin and Night validation
        if (!validateTiffin() || !validateNight()) {
            return;
        }

        // 3. Optional inputs: empty হলে 0
        Double attendanceValue = readNumber(
                etAttendanceBonus, false, ""
        );

        Double transportValue = readNumber(
                etTransport, false, ""
        );

        Double othersValue = readNumber(
                etOters, false, ""
        );

        Double absentValue = readNumber(
                etAbsentDay, false, ""
        );

        Double tiffinDayValue = readNumber(
                etTiffinDay, false, ""
        );

        Double tiffinTakaValue = readNumber(
                etTiffinTaka, false, ""
        );

        Double nightDayValue = readNumber(
                etNightDay, false, ""
        );

        Double nightTakaValue = readNumber(
                etNightTaka, false, ""
        );

        // Invalid number হলে calculation বন্ধ
        if (attendanceValue == null ||
                transportValue == null ||
                othersValue == null ||
                absentValue == null ||
                tiffinDayValue == null ||
                tiffinTakaValue == null ||
                nightDayValue == null ||
                nightTakaValue == null) {
            return;
        }

        // 4. Values
        double totalSalary = salaryValue;
        double otHour = otHourValue;
        double attendance = attendanceValue;
        double transportInput = transportValue;
        double others = othersValue;
        double absent = absentValue;

        double tiffinDay = tiffinDayValue;
        double tiffinTaka = tiffinTakaValue;

        double nightDay = nightDayValue;
        double nightTaka = nightTakaValue;

        // 5. Salary calculation
        double basicWithoutHouse =
                totalSalary - health - transportFixed - food;

        double finalBasic =
                basicWithoutHouse / bariVaraPercent;

        double absentFormula = finalBasic / 30;

        double absentFinalCost =
                absent * absentFormula;

        double overtimeRate = (finalBasic / 208) * 2;

        double otAmount = otHour * overtimeRate;

        // 6. Tiffin and Night bill
        double tiffinBillFormula =
                tiffinDay * tiffinTaka;

        double nightBillFormula =
                nightDay * nightTaka;

        // 7. Final salary
        double totalSalaryAll =
                otAmount
                        + attendance
                        + transportInput
                        + others
                        + totalSalary;

        double totalSalaryWithAbsentCost =
                totalSalaryAll - absentFinalCost;

        // 8. Send results
        salaryCalculator(
                totalSalaryWithAbsentCost,
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
        private Double readNumber(
                EditText editText,
        boolean required,
        String errorMessage) {

            String value = editText.getText()
                    .toString()
                    .trim();

            if (value.isEmpty()) {

                if (required) {
                    editText.setError(errorMessage);
                    return null;
                }

                return 0.0;
            }

            try {
                double number = Double.parseDouble(value);

                if (!Double.isFinite(number)) {
                    editText.setError("Enter a valid number");
                    return null;
                }

                editText.setError(null);
                return number;

            } catch (NumberFormatException e) {
                editText.setError("Enter a valid number");
                return null;
            }
        }

        private boolean validateNight() {

            String day = etNightDay.getText()
                    .toString().trim();

            String taka = etNightTaka.getText()
                    .toString().trim();

            etNightDay.setError(null);
            etNightTaka.setError(null);

            // দুটোই খালি হলে Night বাদ
            if (day.isEmpty() && taka.isEmpty()) {
                return true;
            }

            if (day.isEmpty()) {
                etNightDay.setError("Enter Night Day");
                return false;
            }

            if (taka.isEmpty()) {
                etNightTaka.setError("Enter Night Bill Taka");
                return false;
            }

            return true;
        }

        private boolean validateTiffin() {

            String day = etTiffinDay.getText()
                    .toString().trim();

            String taka = etTiffinTaka.getText()
                    .toString().trim();

            etTiffinDay.setError(null);
            etTiffinTaka.setError(null);

            // দুটোই খালি হলে Tiffin বাদ
            if (day.isEmpty() && taka.isEmpty()) {
                return true;
            }

            // একটি দিলে অন্যটিও দিতে হবে
            if (day.isEmpty()) {
                etTiffinDay.setError("Enter Tiffin Day");
                return false;
            }

            if (taka.isEmpty()) {
                etTiffinTaka.setError("Enter Tiffin Taka");
                return false;
            }

            return true;
        }


    // Ager Code
/*    private void calculateSalary() {

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


            boolean tiffinValid = validateTiffin();
            boolean nightValid = validateNight();

            if (!tiffinValid || !nightValid) {
                return;
            }

            // এখানে তোমার ডাটাবেজে সেভ করার কোড বসবে
            // Tiffin এবং Night দুটোই বাদ দেওয়া থাকলেও
            // অন্য ইনপুটের তথ্য সেভ করা যাবে।







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


    private boolean validateNight() {

        String day = etNightDay.getText().toString().trim();
        String taka = etNightTaka.getText().toString().trim();

        // দুটোই খালি থাকলে Night বাদ
        if (day.isEmpty() && taka.isEmpty()) {
            return true;
        }

        if (day.isEmpty() || taka.isEmpty()) {
            etNightDay.setError(
                    day.isEmpty() ? "Night Day দিন" : null
            );

            etNightTaka.setError(
                    taka.isEmpty() ? "Night Bill Taka দিন" : null
            );

            return false;
        }

        return true;
    }

    private boolean validateTiffin() {

        String day = etTiffinDay.getText().toString().trim();
        String taka = etTiffinTaka.getText().toString().trim();

        // দুটোই খালি থাকলে Tiffin বাদ
        if (day.isEmpty() && taka.isEmpty()) {
            return true;
        }

        // একটি পূরণ করা হয়েছে, অন্যটি খালি
        if (day.isEmpty() || taka.isEmpty()) {
            etTiffinDay.setError(
                    day.isEmpty() ? "Tiffin Day দিন" : null
            );

            etTiffinTaka.setError(
                    taka.isEmpty() ? "Tiffin Taka দিন" : null
            );

            return false;
        }

        return true;
    }*/
}