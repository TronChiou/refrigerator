package refresh;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.content.Context;
import com.example.jameschiou.refrigerator.R;
import dbHelper.DBHelper;


public class Refresh {
    private Cursor cursor;
    private Context context;
    private SQLiteDatabase db;
    private SimpleCursorAdapter adapter;
    private ListView listView;
    private int LayoutId;

    public Refresh(int LayoutId, SQLiteDatabase db,Context context, Cursor cursor, ListView listView){
        this.LayoutId = LayoutId;
        this.db = db;
        this.context = context;
        this.cursor = cursor;
        this.listView = listView;
    }

    public void execute(){
        adapter = new SimpleCursorAdapter(context, LayoutId, cursor, new String[] {"item_name", "number", "unit", "date(start_date + end_date)"}
                , new int[] {R.id.itemName, R.id.itemCount, R.id.itemUnit, R.id.itemExpire}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        listView.setAdapter(adapter);
        cursor = null;
    }
}

