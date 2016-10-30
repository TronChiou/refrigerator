package com.example.jameschiou.refrigerator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dbHelper.DBHelper;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private FloatingActionButton fab1;
    ListView alert;
    ListView item;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        dbHelper = new DBHelper(context);
        db = dbHelper.getReadableDatabase();
        alert = (ListView)findViewById(R.id.alert_list);
        item = (ListView)findViewById(R.id.item);
        refreshListView(alert);
        refreshListView(item);
        item.setOnItemClickListener(new MyOnItemClickListener());
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabClick();
            }
        });


    }

    public void fabClick(){
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.activity_add_grocery, (ViewGroup)findViewById(R.id.root));
        Button add = (Button)layout.findViewById(R.id.save_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.add_title);
        builder.setView(layout);
        builder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText edit_name = (EditText)layout.findViewById(R.id.edit_name);
                EditText edit_count = (EditText)layout.findViewById(R.id.edit_count);
                EditText edit_unit = (EditText)layout.findViewById(R.id.edit_unit);
                EditText edit_expire = (EditText)layout.findViewById(R.id.edit_expire);
                String name = edit_name.getText().toString();
                int count = Integer.parseInt(edit_count.getText().toString());
                String unit = edit_unit.getText().toString();
                int expire = Integer.parseInt(edit_expire.getText().toString());
                String str = "INSERT INTO grocery_list(item_name, number, unit, end_date) VALUES('"
                        + name +"', "+ count+", '" +  unit+"', "+  expire +"  )";
                try {
                    db.execSQL(str);
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"broken",Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getApplicationContext(),name + count + unit+ expire,Toast.LENGTH_LONG).show();
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

    public Cursor getAll(){
        Cursor cursor = db.rawQuery("SELECT _id, item_name, number, unit, end_date FROM grocery_list limit 5", null);
        return cursor;
    }

    private void refreshListView(ListView listView){
        if(cursor == null) {
            cursor = getAll();

            adapter = new SimpleCursorAdapter(context, R.layout.list_row, cursor, new String[] {"_id","item_name", "number", "unit", "end_date"}
                    , new int[] {R.id.itemId, R.id.itemName, R.id.itemCount, R.id.itemUnit, R.id.itemExpire}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

            listView.setAdapter(adapter);
            cursor = null;

        }
        else{
            if (cursor.isClosed()) {
                cursor = null;
                refreshListView(listView);
            }
        }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent intent = new Intent(MainActivity.this, GroceryList.class);
            startActivity(intent);
        }
    }




}
