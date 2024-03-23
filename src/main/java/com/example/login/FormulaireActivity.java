package com.example.login;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FormulaireActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private EditText editTextTitre, editTextTypeContrat, editTextDescription;
    private Spinner spinnerCategorie, spinnerSecteur, spinnerVille;
    private Button buttonSuivant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire);

        editTextTitre = findViewById(R.id.editTextTitre);
        editTextTypeContrat = findViewById(R.id.editTextTypeContrat);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategorie = findViewById(R.id.spinnerCategorie);
        spinnerSecteur = findViewById(R.id.spinnerSecteur);
        spinnerVille = findViewById(R.id.spinnerVille);
        buttonSuivant = findViewById(R.id.buttonSuivant);

        databaseHelper = new DatabaseHelper(this);

        setupSpinners();

        buttonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToDatabase();
            }
        });
    }

    private void setupSpinners() {
        List<String> categories = new ArrayList<>();
        categories.add("Category 1");
        categories.add("Category 2");
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(categoryAdapter);

        List<String> sectors = new ArrayList<>();
        sectors.add("Sector 1");
        sectors.add("Sector 2");
        ArrayAdapter<String> sectorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sectors);
        sectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSecteur.setAdapter(sectorAdapter);

        List<String> cities = new ArrayList<>();
        cities.add("City 1");
        cities.add("City 2");
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVille.setAdapter(cityAdapter);
    }

    private void sendDataToDatabase() {
        String titre = editTextTitre.getText().toString().trim();
        String typeContrat = editTextTypeContrat.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String categorie = spinnerCategorie.getSelectedItem().toString();
        String secteur = spinnerSecteur.getSelectedItem().toString();
        String ville = spinnerVille.getSelectedItem().toString();

        if (titre.isEmpty() || typeContrat.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isSuccess = databaseHelper.insertData(titre, typeContrat, description, categorie, secteur, ville);

        if (isSuccess) {
            Intent intent = new Intent(FormulaireActivity.this, AfficheVille.class);
            intent.putExtra("selectedCity", ville);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to send data to database", Toast.LENGTH_SHORT).show();
        }
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "users.db";
        private static final String TABLE_NAME = "formulaire";
        private static final String COL_ID = "id";
        private static final String COL_TITRE = "titre";
        private static final String COL_TYPE_CONTRAT = "type_contrat";
        private static final String COL_DESCRIPTION = "description";
        private static final String COL_CATEGORIE = "categorie";
        private static final String COL_SECTEUR = "secteur";
        private static final String COL_VILLE = "ville";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " +
                    TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TITRE + " TEXT, " +
                    COL_TYPE_CONTRAT + " TEXT, " +
                    COL_DESCRIPTION + " TEXT, " +
                    COL_CATEGORIE + " TEXT, " +
                    COL_SECTEUR + " TEXT, " +
                    COL_VILLE + " TEXT)";
            db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        public boolean insertData(String titre, String typeContrat, String description,
                                  String categorie, String secteur, String ville) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_TITRE, titre);
            contentValues.put(COL_TYPE_CONTRAT, typeContrat);
            contentValues.put(COL_DESCRIPTION, description);
            contentValues.put(COL_CATEGORIE, categorie);
            contentValues.put(COL_SECTEUR, secteur);
            contentValues.put(COL_VILLE, ville);
            long result = db.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        }
    }
}