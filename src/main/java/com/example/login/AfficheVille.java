package com.example.login;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AfficheVille extends AppCompatActivity {

    private TextView textViewVille;
    private TextView textViewNumber;

    private FormulaireActivity.DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiche_ville);


        textViewVille = findViewById(R.id.textViewVille);
        textViewNumber = findViewById(R.id.textViewNumber);

        databaseHelper = new FormulaireActivity.DatabaseHelper(this);

        String selectedCity = getIntent().getStringExtra("selectedCity");

        int cityNumber = getCityNumber(selectedCity);

        textViewVille.setText(selectedCity);
        textViewNumber.setText(String.valueOf(cityNumber));
    }
    private int getCityNumber(String city) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM formulaire WHERE ville = ?", new String[]{city});
        int cityCount = 0;
        if (cursor.moveToFirst()) {
            cityCount = cursor.getInt(0);
        }
        cursor.close();
        return cityCount;
    }
}