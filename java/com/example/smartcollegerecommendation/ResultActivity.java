package com.example.smartcollegerecommendation;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ListView listColleges = findViewById(R.id.listColleges);
        Button btnBack = findViewById(R.id.gobutton);


        String result = getIntent().getStringExtra("result");
        String branch = getIntent().getStringExtra("branch");
        String city = getIntent().getStringExtra("city");

        if (result == null) result = "";

        CollegeDatabaseHelper dbHelper = new CollegeDatabaseHelper(this);


        ArrayList<String> collegeList = new ArrayList<>();

        String[] lines = result.split("\n");

        for (int i = 0; i < lines.length; i++) {

            if (lines[i].trim().isEmpty() || lines[i].contains("Recommended")) {
                continue;
            }

            String nameLine = lines[i];

            if (i + 1 < lines.length && !lines[i + 1].trim().isEmpty()) {

                String detailsLine = lines[i + 1];

                String fullData = nameLine + "\n" + detailsLine;
                collegeList.add(fullData);

                i++;
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                collegeList
        );

        listColleges.setAdapter(adapter);


        listColleges.setOnItemClickListener((parent, view, position, id) -> {

            try {
                String selected = collegeList.get(position);


                String[] parts = selected.split("\n");

                if (parts.length == 0) {
                    Toast.makeText(this, "Invalid data", Toast.LENGTH_SHORT).show();
                    return;
                }

                String nameLine = parts[0];


                String collegeName;
                if (nameLine.contains("(")) {
                    collegeName = nameLine.split("\\(")[0].trim();
                } else {
                    collegeName = nameLine.trim();
                }
                String realCity = city;

                if (nameLine.contains("(") && nameLine.contains(")")) {
                    int start = nameLine.indexOf("(");
                    int end = nameLine.indexOf(")");

                    if (start < end) {
                        realCity = nameLine.substring(start + 1, end).trim();
                    }
                }
                dbHelper.saveCollege(collegeName, realCity, branch);

                Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving college", Toast.LENGTH_LONG).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}