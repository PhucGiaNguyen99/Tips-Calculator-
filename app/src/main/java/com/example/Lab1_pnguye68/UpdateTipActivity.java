package com.example.Lab1_pnguye68;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateTipActivity extends AppCompatActivity {
    private EditText excellentTipInput, averageTipInput, belowAverageTipInput;
    private Button saveButton, cancelButton;

    private float excellentTip, averageTip, belowAverageTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tip);

        excellentTipInput = findViewById(R.id.excellentTipInput);
        averageTipInput = findViewById(R.id.averageTipInput);
        belowAverageTipInput = findViewById(R.id.belowAverageTipInput);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Get the current percentages from intent
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            excellentTip = intent.getFloatExtra("excellentTip", 0.25f);
            averageTip = intent.getFloatExtra("averageTip", 0.20f);
            belowAverageTip = intent.getFloatExtra("belowAverageTip", 0.15f);
        } else {
            // Restore values if the activity was recreated (e.g., after rotation)
            excellentTip = savedInstanceState.getFloat("excellentTip", 0.25f);
            averageTip = savedInstanceState.getFloat("averageTip", 0.20f);
            belowAverageTip = savedInstanceState.getFloat("belowAverageTip", 0.15f);
        }

        // Display values in input fields
        excellentTipInput.setHint(String.valueOf((int) (excellentTip * 100)));
        averageTipInput.setHint(String.valueOf((int) (averageTip * 100)));
        belowAverageTipInput.setHint(String.valueOf((int) (belowAverageTip * 100)));

        // Save button action
        saveButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();

            if (!excellentTipInput.getText().toString().isEmpty()) {
                resultIntent.putExtra("excellentTip", Float.parseFloat(excellentTipInput.getText().toString()) / 100);
            } else {
                resultIntent.putExtra("excellentTip", excellentTip);
            }

            if (!averageTipInput.getText().toString().isEmpty()) {
                resultIntent.putExtra("averageTip", Float.parseFloat(averageTipInput.getText().toString()) / 100);
            } else {
                resultIntent.putExtra("averageTip", averageTip);
            }

            if (!belowAverageTipInput.getText().toString().isEmpty()) {
                resultIntent.putExtra("belowAverageTip", Float.parseFloat(belowAverageTipInput.getText().toString()) / 100);
            } else {
                resultIntent.putExtra("belowAverageTip", belowAverageTip);
            }

            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Cancel button action (Back without saving)
        cancelButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    // Save user input during rotation
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("excellentTip", excellentTip);
        outState.putFloat("averageTip", averageTip);
        outState.putFloat("belowAverageTip", belowAverageTip);
        outState.putString("excellentTipInput", excellentTipInput.getText().toString());
        outState.putString("averageTipInput", averageTipInput.getText().toString());
        outState.putString("belowAverageTipInput", belowAverageTipInput.getText().toString());
    }

    // Restore input values after rotation
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        excellentTip = savedInstanceState.getFloat("excellentTip", 0.25f);
        averageTip = savedInstanceState.getFloat("averageTip", 0.20f);
        belowAverageTip = savedInstanceState.getFloat("belowAverageTip", 0.15f);
        excellentTipInput.setHint(savedInstanceState.getString("excellentTipInput"));
        averageTipInput.setHint(savedInstanceState.getString("averageTipInput"));
        belowAverageTipInput.setHint(savedInstanceState.getString("belowAverageTipInput"));
    }
}
