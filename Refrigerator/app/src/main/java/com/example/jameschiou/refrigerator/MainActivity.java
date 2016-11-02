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


import dbHelper.DBHelper;
import refresh.Refresh;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private FloatingActionButton fab1;
    private String str_alert;
    private String str_list;
    ListView alert;
    ListView item;
    Refresh refresh;

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
        item.setOnItemClickListener(new MyOnItemClickListener());
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabClick();
            }
        });


    }
    protected void onResume(){
        super.onResume();
        str_alert = "SELECT _id, item_name, number, unit,start_date, date(start_date + end_date) FROM grocery_list ORDER BY (start_date + end_date) ASC  limit 5";
        str_list = "SELECT _id, item_name, number, unit, date(start_date + end_date) FROM grocery_list ORDER BY _id DESC limit 5";
        cursor = db.rawQuery(str_alert, null);
        refresh = new Refresh(R.layout.list_row, db, context, cursor, alert);
        refresh.execute();

        cursor = db.rawQuery(str_list, null);
        refresh = new Refresh(R.layout.list_row, db, context, cursor, item);
        refresh.execute();

    }

    public void fabClick(){
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.activity_add_grocery, (ViewGroup)findViewById(R.id.root));

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
                int count;
                if(edit_count.getText().toString().equals("")){
                    count = 0;
                }
                else
                    count = Integer.parseInt(edit_count.getText().toString());

                String unit = edit_unit.getText().toString();

                int expire;
                if(edit_expire.getText().toString().equals(""))
                    expire = 0;
                else
                    expire = Integer.parseInt(edit_expire.getText().toString()) ;

                String str = "INSERT INTO grocery_list(item_name, number, unit, start_date, end_date) VALUES('"
                        + name +"', "+ count+", '" +  unit+"', julianday('now'), "+  expire +"  )";
                Log.d("str1", str);
                try {
                    db.execSQL(str);
                    onResume();
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"broken",Toast.LENGTH_LONG).show();
                    Log.d("str", str);
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




    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent intent = new Intent(MainActivity.this, GroceryList.class);
            startActivity(intent);
        }
    }




}
