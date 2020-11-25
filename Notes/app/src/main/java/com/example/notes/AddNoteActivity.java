package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    EditText title;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.desc);


    }

    public void Cansel(View view) {
        finish();
    }

    public void AddNote(View view) {
        if (title.getText().toString().equals("") || description.getText().toString().equals("")) {
            Toast.makeText(this, "Wrong input", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("title", title.getText().toString());
        intent.putExtra("description", description.getText().toString());
        setResult(1, intent);
        finish();
    }
}