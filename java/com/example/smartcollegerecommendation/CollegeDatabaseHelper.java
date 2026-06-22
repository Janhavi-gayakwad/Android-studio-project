package com.example.smartcollegerecommendation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class CollegeDatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "coll1.db";
    private static final int DATABASE_VERSION = 2;

    public CollegeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 🔍 GET RECOMMENDED COLLEGES (FINAL FIXED)
    public Cursor getRecommendedColleges(String branch, String city, double percentage, int budget) {

        SQLiteDatabase db = getReadableDatabase();

        // 🔥 Normalize input
        branch = branch.trim();
        city = city.trim();

        if (city.equalsIgnoreCase("All Cities")) {

            // ✅ No city filter
            return db.rawQuery(
                    "SELECT College_Name, City, Type, [College status] FROM colleges " +
                            "WHERE Department = ? " +
                            "AND Cutoff_Percentile <= ? " +
                            "AND [Fees (₹/year)] <= ?",
                    new String[]{branch, String.valueOf(percentage), String.valueOf(budget)}
            );

        } else {

            // 🔥 FINAL FIX: TRIM + LOWER (handles spaces + case issues)
            return db.rawQuery(
                    "SELECT College_Name, City, Type, [College status] FROM colleges " +
                            "WHERE Department = ? " +
                            "AND TRIM(LOWER(City)) = TRIM(LOWER(?)) " +
                            "AND Cutoff_Percentile <= ? " +
                            "AND [Fees (₹/year)] <= ?",
                    new String[]{branch, city, String.valueOf(percentage), String.valueOf(budget)}
            );
        }
    }

    // 💾 SAVE COLLEGE (SAFE + DUPLICATE CHECK)
    public void saveCollege(String name, String city, String branch) {

        SQLiteDatabase db = getWritableDatabase();

        name = name.trim();
        city = city.trim();
        branch = branch.trim();

        // 🔍 Check duplicate
        Cursor cursor = db.rawQuery(
                "SELECT * FROM saved_colleges WHERE LOWER(college_name)=LOWER(?) AND LOWER(city)=LOWER(?) AND LOWER(branch)=LOWER(?)",
                new String[]{name, city, branch}
        );

        if (cursor.getCount() == 0) {

            ContentValues values = new ContentValues();
            values.put("college_name", name);
            values.put("city", city);
            values.put("branch", branch);

            db.insert("saved_colleges", null, values);
        }

        cursor.close();
    }

    // 📄 GET SAVED COLLEGES
    public Cursor getSavedColleges() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM saved_colleges", null);
    }

    // 🗑 DELETE COLLEGE
    public void deleteCollege(String name, String city, String branch) {

        SQLiteDatabase db = getWritableDatabase();

        name = name.trim();
        city = city.trim();
        branch = branch.trim();

        db.delete(
                "saved_colleges",
                "LOWER(college_name)=LOWER(?) AND LOWER(city)=LOWER(?) AND LOWER(branch)=LOWER(?)",
                new String[]{name, city, branch}
        );
    }

    // 🧹 DELETE ALL
    public void deleteAllColleges() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM saved_colleges");
    }
}