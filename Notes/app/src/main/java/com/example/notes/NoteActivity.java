package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NoteActivity extends AppCompatActivity {

    TextView title;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Intent intent = getIntent();

        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.desc);

        title.setText(intent.getStringExtra("title"));
        description.setText(intent.getStringExtra("description"));

    }

    public void Back(View view) {
        finish();
    }
}