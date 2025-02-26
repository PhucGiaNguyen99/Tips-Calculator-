package com.example.Lab1_pnguye68;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Now for HW4, because everything is in one activity, update the MainActivity.java to
    // handle both tip calculation and percentage updates.
    private EditText billTotal, numPeople, excellentTipInput, averageTipInput, belowAverageTipInput;

    private RadioGroup serviceGroup;
    private TextView tipTotal, totalAmount, totalPerPerson;
    private Button clearButton, updateButton;

    private float excellentTip = 0.25f;
    private float averageTip = 0.20f;
    private float belowAverageTip = 0.15f;

    private static final int REQUEST_CODE_UPDATE = 1;

    private RadioButton excellentService, averageService, belowAverageService;
    private static final String PREFS_NAME = "TipPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        billTotal = findViewById(R.id.billTotal);
        numPeople = findViewById(R.id.numPeople);
        serviceGroup = findViewById(R.id.serviceGroup);
        tipTotal = findViewById(R.id.tipTotal);
        totalAmount = findViewById(R.id.totalAmount);
        totalPerPerson = findViewById(R.id.totalPerPerson);

        updateButton = findViewById(R.id.updateButton);

        excellentService = findViewById(R.id.excellentService);
        averageService = findViewById(R.id.averageService);
        belowAverageService = findViewById(R.id.belowAverageService);

        excellentTipInput = findViewById(R.id.excellentTipInput);
        averageTipInput = findViewById(R.id.averageTipInput);
        belowAverageTipInput = findViewById(R.id.belowAverageTipInput);

        // First check saved instance state before loading preferences
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            loadTipPreferences();
        }

        // Set initial RadioButton text
        updateRadioButtonText();

        // Handle radio group selection
        serviceGroup.setOnCheckedChangeListener((group, checkedId) -> calculateTip());

        // Handle Update button
        updateButton.setOnClickListener(v -> updateTipPercentages());
    }

    // Load preferences if no saved state exists
    private void loadTipPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        excellentTip = prefs.getFloat("excellentTip", 0.25f);
        averageTip = prefs.getFloat("averageTip", 0.20f);
        belowAverageTip = prefs.getFloat("belowAverageTip", 0.15f);
    }

    // Save preferences when tip percentages change
    private void saveTipPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("excellentTip", excellentTip);
        editor.putFloat("averageTip", averageTip);
        editor.putFloat("belowAverageTip", belowAverageTip);
        editor.apply();
    }

    private void calculateTip() {
        try {
            String billInput = billTotal.getText().toString();
            if (billInput.isEmpty()) {
                Toast.makeText(this, "Enter bill amount", Toast.LENGTH_LONG).show();
                return;
            }

            float bill = Float.parseFloat(billInput);
            if (bill <= 0) {
                Toast.makeText(this, "Bill amount must be greater than zero", Toast.LENGTH_LONG).show();
                return;
            }

            String peopleInput = numPeople.getText().toString();
            int people = peopleInput.isEmpty() ? 1 : Integer.parseInt(peopleInput);
            if (people <= 0) {
                Toast.makeText(this, "Number of people must be greater than zero", Toast.LENGTH_LONG).show();
                return;
            }

            int checkId = serviceGroup.getCheckedRadioButtonId();
            float tipPercentage = 0;

            if (checkId == R.id.excellentService) {
                tipPercentage = excellentTip;
            } else if (checkId == R.id.averageService) {
                tipPercentage = averageTip;
            } else if (checkId == R.id.belowAverageService) {
                tipPercentage = belowAverageTip;
            } else {
                Toast.makeText(this, "Select service level", Toast.LENGTH_LONG).show();
                return;
            }

            float tip = bill * tipPercentage;
            float total = bill + tip;
            float totalPer = total / people;

            tipTotal.setText("$" + roundToTwoDigit(tip));
            totalAmount.setText("$" + roundToTwoDigit(total));
            totalPerPerson.setText("$" + roundToTwoDigit(totalPer));
        } catch (Exception e) {
            Toast.makeText(this, "Invalid input. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    private void updateTipPercentages() {
        if (!excellentTipInput.getText().toString().isEmpty()) {
            excellentTip = Float.parseFloat(excellentTipInput.getText().toString()) / 100;
        }
        if (!averageTipInput.getText().toString().isEmpty()) {
            averageTip = Float.parseFloat(averageTipInput.getText().toString()) / 100;
        }
        if (!belowAverageTipInput.getText().toString().isEmpty()) {
            belowAverageTip = Float.parseFloat(belowAverageTipInput.getText().toString()) / 100;
        }

        saveTipPreferences();
        updateRadioButtonText();
        calculateTip();
    }

    public static String roundToTwoDigit(float value) {
        return String.format("%.2f", value);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_UPDATE && resultCode == RESULT_OK && data != null) {
            excellentTip = data.getFloatExtra("excellentTip", excellentTip);
            averageTip = data.getFloatExtra("averageTip", averageTip);
            belowAverageTip = data.getFloatExtra("belowAverageTip", belowAverageTip);

            saveTipPreferences();
            updateRadioButtonText();
            calculateTip();
        }
    }

    private void updateRadioButtonText() {
        excellentService.setText("Excellent (" + (int) (excellentTip * 100) + "%)");
        averageService.setText("Average (" + (int) (averageTip * 100) + "%)");
        belowAverageService.setText("Below Average (" + (int) (belowAverageTip * 100) + "%)");
    }

    // Corrected Save Instance State
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("excellentTip", excellentTip);
        outState.putFloat("averageTip", averageTip);
        outState.putFloat("belowAverageTip", belowAverageTip);
    }

    // Restore instance state correctly
    private void restoreState(Bundle savedInstanceState) {
        excellentTip = savedInstanceState.getFloat("excellentTip", 0.25f);
        averageTip = savedInstanceState.getFloat("averageTip", 0.20f);
        belowAverageTip = savedInstanceState.getFloat("belowAverageTip", 0.15f);
        updateRadioButtonText();
    }
}
