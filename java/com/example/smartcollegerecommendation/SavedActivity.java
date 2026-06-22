package com.example.smartcollegerecommendation;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SavedActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> displayList;
    ArrayList<String[]> rawList;
    CollegeDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        listView = findViewById(R.id.listSaved);
        dbHelper = new CollegeDatabaseHelper(this);

        loadData();
    }

    private void loadData() {

        displayList = new ArrayList<>();
        rawList = new ArrayList<>();

        Cursor cursor = dbHelper.getSavedColleges();

        if (cursor.getCount() == 0) {
            displayList.add("No saved colleges");
        } else {
            while (cursor.moveToNext()) {

                String name = cursor.getString(1);
                String city = cursor.getString(2);
                String branch = cursor.getString(3);

                String item = name + "\n" + city + "\n" + branch;
                displayList.add(item);

                rawList.add(new String[]{name, city, branch});
            }
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener((parent, view, position, id) -> {

            if (displayList.get(position).equals("No saved colleges")) {
                return true;
            }

            String[] selected = rawList.get(position);

            String name = selected[0];
            String city = selected[1];
            String branch = selected[2];

            dbHelper.deleteCollege(name, city, branch);

            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();

            loadData();

            return true;
        });
    }
}