package dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "grocery.db";
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_TABLE_SQL =
            "CREATE TABLE " +
            "grocery_list (_id INTEGER PRIMARY KEY, item_name TEXT, number NUMERIC, "+
            "unit TEXT, start_date NUMERIC, end_date NUMERIC)";
    public static final String DROP_TABLE_SQL =
            "DROP TABLE IF EXISTS grocery_list";
    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onDropTable(db);
        onCreate(db);
    }

    public void onDropTable(SQLiteDatabase db) {
        db.execSQL(DROP_TABLE_SQL);
    }
}
