package com.example.jameschiou.refrigerator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import dbHelper.DBHelper;

public class GroceryList extends AppCompatActivity {

    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private SimpleCursorAdapter adapter;
    ListView item_list;
    CardView cardView;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        context = this;
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        item_list = (ListView)findViewById(R.id.item_list);
        item_list.setOnItemClickListener(new MyOnItemClickListener());

        refreshListView();


    }

    public Cursor getAll(){
        Cursor cursor = db.rawQuery("SELECT _id, item_name, number, unit, end_date FROM grocery_list", null);
        return cursor;
    }

    private void refreshListView(){
            cursor = getAll();

            adapter = new SimpleCursorAdapter(context, R.layout.cardview, cursor, new String[] {"_id","item_name", "number", "unit", "end_date"}
                    , new int[] {R.id.itemId, R.id.itemName, R.id.itemCount, R.id.itemUnit, R.id.itemExpire}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            item_list.setAdapter(adapter);
            cursor = null;


    }
    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                final long id) {
            LayoutInflater inflater = getLayoutInflater();
            final View layout = inflater.inflate(R.layout.activity_add_grocery, (ViewGroup)findViewById(R.id.root));
            final EditText edit_name = (EditText)layout.findViewById(R.id.edit_name);
            final EditText edit_count = (EditText)layout.findViewById(R.id.edit_count);
            final EditText edit_unit = (EditText)layout.findViewById(R.id.edit_unit);
            final EditText edit_expire = (EditText)layout.findViewById(R.id.edit_expire);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

            cursor = db.rawQuery("SELECT item_name, number, unit, end_date FROM grocery_list WHERE _id = " + id, null);
            if(cursor.moveToFirst()){
                edit_name.setText(cursor.getString(0));
                edit_count.setText(cursor.getString(1));
                edit_unit.setText(cursor.getString(2));
                edit_expire.setText(cursor.getString(3));
            }
            else
                Toast.makeText(getApplicationContext(), "No data found!", Toast.LENGTH_LONG).show();



            builder.setTitle(R.string.modify);
            builder.setView(layout);
            builder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = edit_name.getText().toString();
                    int count = Integer.parseInt(edit_count.getText().toString());
                    String unit = edit_unit.getText().toString();
                    int expire = Integer.parseInt(edit_expire.getText().toString());

                    final String str = "UPDATE grocery_list SET item_name ='"
                            + name +"', number = "+ count+", unit = '" +  unit+"', end_date = "+  expire
                            + " WHERE _id =" + id;

                    try {
                        db.execSQL(str);
                        Log.d("sql", str);
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(),"broken",Toast.LENGTH_LONG).show();
                    }
                    refreshListView();

                }
            });
            builder.setNeutralButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        db.execSQL("DELETE FROM grocery_list WHERE _id = "+ id);
                        Log.d("sql", "DELETE FROM grocery_list WHERE _id = "+ id);
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(),"broken delete",Toast.LENGTH_LONG).show();
                    }
                    refreshListView();
                }
            });
            builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
            }

        }


}
