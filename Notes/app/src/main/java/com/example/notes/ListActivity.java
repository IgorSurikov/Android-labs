package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import database.DB;
import database.DBCursorLoader;
import database.DBHelper;

import static android.view.ContextMenu.*;
import static android.widget.AdapterView.*;

public class ListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTouchListener {

    ListView notesList;
    DB db;
    SimpleCursorAdapter scAdapter;
    float[] yEndList = new float[3];
    float[] yStartList = new float[]{-1, -1, -1};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        db = new DB(this);
        db.open();

        String[] from = new String[]{DB.COLUMN_TITLE, DB.COLUMN_TYPE, DB.COLUMN_INSERT_DATE};
        int[] to = new int[]{R.id.title, R.id.type, R.id.insertDate};

        scAdapter = new SimpleCursorAdapter(this, R.layout.list_item, null, from, to, 0);
        notesList = (ListView) findViewById(R.id.notesList);
        notesList.setAdapter(scAdapter);
        notesList.setOnItemClickListener(this::onItemSelected);
        notesList.setOnTouchListener(this);
        registerForContextMenu(notesList);

        LoaderManager.getInstance(this).initLoader(0, null, this);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, 0, "Delete note");
        menu.add(0, 1, 1, "Edit note");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
            db.delRec(menuInfo.id);
            LoaderManager.getInstance(this).getLoader(0).forceLoad();
            return true;
        }
        if (item.getItemId() == 1) {
            AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
            Intent intent = new Intent(this, EditNoteActivity.class);
            Cursor note = db.getNote(menuInfo.id);
            note.moveToFirst();
            intent.putExtra("id", menuInfo.id);
            intent.putExtra("title", note.getString(1));
            intent.putExtra("description", note.getString(2));
            startActivityForResult(intent, 2);
            LoaderManager.getInstance(this).getLoader(0).forceLoad();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, NoteActivity.class);
        Cursor note = db.getNote(id);
        note.moveToFirst();
        intent.putExtra("title", note.getString(1));
        intent.putExtra("description", note.getString(2));
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new DBCursorLoader(this, db);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        scAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) {
            return;
        }
        switch (resultCode) {
            case 0:
                return;
            case 1:
                db.addRec(data.getStringExtra("title"), data.getStringExtra("description"), "Text note");
                break;
            case 2:
                db.updateRec(data.getLongExtra("id", -1), data.getStringExtra("title"), data.getStringExtra("description"), "Text note");
                break;
        }
        LoaderManager.getInstance(this).getLoader(0).forceLoad();
        super.onActivityResult(requestCode, resultCode, data);

    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public void addNote(View view) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int actionMask = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerCount = event.getPointerCount();

        switch (actionMask) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (pointerCount == 3) {
                    for (int i = 0; i < yStartList.length; i++) {
                        yStartList[i] = event.getY(i);
                    }
                    return true;
                }
                return false;
            case MotionEvent.ACTION_UP:
                if (event.getPointerId(pointerIndex) > 2) {
                    return false;
                }
                yEndList[event.getPointerId(pointerIndex)] = event.getY(pointerIndex);
                for (int i = 0; i < yStartList.length; i++) {
                    if (yStartList[i] >= yEndList[i] || yStartList[i] == -1) {
                        return false;
                    }
                }

                db.delAll();
                for (int i = 0; i < yStartList.length; i++) {
                    yStartList[i] = -1;
                }
                LoaderManager.getInstance(this).getLoader(0).forceLoad();
                return true;

            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerId(pointerIndex) > 2) {
                    return false;
                }
                yEndList[event.getPointerId(pointerIndex)] = event.getY(pointerIndex);
                return true;
        }
        return false;
    }
}
