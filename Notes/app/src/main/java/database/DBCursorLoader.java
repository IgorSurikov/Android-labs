package database;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;

public class DBCursorLoader extends CursorLoader {

    private final DB db;

    public DBCursorLoader(@NonNull Context context, DB db) {
        super(context);
        this.db = db;
    }

    @Override
    public Cursor loadInBackground() {
        return db.getAllData();
    }
}
