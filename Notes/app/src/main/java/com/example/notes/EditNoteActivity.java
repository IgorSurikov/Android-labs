package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditNoteActivity extends AppCompatActivity {

    EditText title;
    EditText description;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent intent = getIntent();

        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.desc);

        title.setText(intent.getStringExtra("title"));
        description.setText(intent.getStringExtra("description"));
        id = intent.getLongExtra("id", -1);

    }

    public void EditNote(View view) {
        if (title.getText().toString().equals("") || description.getText().toString().equals("")) {
            Toast.makeText(this, "Wrong input", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("id", id);
        intent.putExtra("title", title.getText().toString());
        intent.putExtra("description", description.getText().toString());
        setResult(2, intent);
        finish();
    }

    public void Cansel(View view) {
        finish();
    }
}