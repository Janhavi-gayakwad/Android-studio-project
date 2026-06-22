package com.example.smartcollegerecommendation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etPercentage, etBudget;
    Spinner spBranch, spCity;
    Button btnRecommend, gobutton;
    TextView tvResult;

    CollegeDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPercentage = findViewById(R.id.etPercentage);
        etBudget = findViewById(R.id.etBudget);
        spBranch = findViewById(R.id.spBranch);
        spCity = findViewById(R.id.spCity);
        btnRecommend = findViewById(R.id.btnRecommend);
        tvResult = findViewById(R.id.tvResult);
        gobutton = findViewById(R.id.gobutton);
        Button btnSaved = findViewById(R.id.btnSaved);

        btnSaved.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SavedActivity.class));
        });

        gobutton.setOnClickListener(v -> finish());

        ArrayAdapter<CharSequence> branchAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.engineering_branches,
                android.R.layout.simple_spinner_item
        );
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBranch.setAdapter(branchAdapter);

        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.cities,
                android.R.layout.simple_spinner_item
        );
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(cityAdapter);

        dbHelper = new CollegeDatabaseHelper(this);

        btnRecommend.setOnClickListener(v -> {

            String p = etPercentage.getText().toString().trim();
            String b = etBudget.getText().toString().trim();
            String branch = spBranch.getSelectedItem().toString();
            String city = spCity.getSelectedItem().toString();

            if (p.isEmpty() || b.isEmpty()) {
                tvResult.setText("Please enter all details");
                return;
            }

            if (branch.equals("Select Engineering Stream")) {
                tvResult.setText("Please select engineering stream");
                return;
            }

            if (!p.matches("\\d{1,2}(\\.\\d{1,2})?")) {
                etPercentage.setError("Enter valid percentage (e.g. 99 or 99.28)");
                return;
            }

            double percentage = Double.parseDouble(p);

            if (percentage > 100) {
                etPercentage.setError("Percentage cannot exceed 100");
                return;
            }

            int budget;
            try {
                budget = Integer.parseInt(b);
            } catch (Exception e) {
                etBudget.setError("Enter valid budget");
                return;
            }

            Cursor cursor = dbHelper.getRecommendedColleges(branch, city, percentage, budget);

            String result;

            if (cursor.getCount() == 0) {
                result = "No suitable engineering colleges found.";
            } else {
                StringBuilder sb = new StringBuilder("Recommended Colleges:\n\n");

                while (cursor.moveToNext()) {
                    sb.append(cursor.getString(0)) // College name
                            .append(" (")
                            .append(cursor.getString(1)) // City
                            .append(")\n")
                            .append(cursor.getString(2)) // Type
                            .append(" | ")
                            .append(cursor.getString(3)) // Status
                            .append("\n\n");
                }

                result = sb.toString();
            }

            cursor.close();

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("result", result);
            intent.putExtra("branch", branch);
            intent.putExtra("city", city);
            startActivity(intent);
        });
    }
}