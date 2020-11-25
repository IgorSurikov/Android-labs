package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DB {



    private final Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_INSERT_DATE = "insert_date";
    public static final String COLUMN_TYPE = "type";

    public DB(Context context) {
        this.context = context;
    }

    public void open() {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (dbHelper != null) dbHelper.close();
    }

    public Cursor getAllData() {
        Cursor notes = database.query("notes", null, null, null, null, null, COLUMN_INSERT_DATE);
        return notes;
    }

    public Cursor getNote(long id){
        Cursor note = database.query("notes", null, COLUMN_ID + " = " + id, null, null, null, null);
        return note;
    }

    public void updateRec(long id, String title, String text, String type){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_TEXT, text);
        cv.put(COLUMN_TYPE, type);
        database.update("notes",cv, COLUMN_ID + '=' + id, null);
    }

    public void addRec(String title, String text, String type) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_TEXT, text);
        cv.put(COLUMN_TYPE, type);
        database.insert("notes", null, cv);
    }

    public void delRec(long id) {
        database.delete("notes", COLUMN_ID + " = " + id, null);
    }

    public void delAll() {
        database.delete("notes", null, null);
    }
}
