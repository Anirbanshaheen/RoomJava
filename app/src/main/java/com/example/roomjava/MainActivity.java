package com.example.roomjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button btAdd, btReset;
    RecyclerView recyclerView;

    List<MainData> dataList = new ArrayList();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    Adapter adapter;
    boolean internetConnection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_text);
        btAdd = findViewById(R.id.addBtn);
        btReset = findViewById(R.id.resetBtn);
        recyclerView = findViewById(R.id.recyclerView);

        database = RoomDB.getInstance(this);
        dataList = database.mainDao().getAll();

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Adapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        isInternetConnected();

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sText = editText.getText().toString().trim();
                if (!sText.equals("")) {
                    MainData data = new MainData();
                    data.setText(sText);
                    database.mainDao().insert(data);
                    editText.setText("");

                    dataList.clear();
                    dataList.addAll(database.mainDao().getAll());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.mainDao().reset(dataList);

                dataList.clear();
                dataList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            internetConnection = true;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    /*synchronized (this) {
                        try {
                            wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/
                    for (int i=0; i<dataList.size(); i++) {
                        try {
                            Thread.sleep(2000);
                            MainData data = new MainData();
                            database.mainDao().delete(data);
                            Log.d("check", "yes "+i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            };
            Thread thread = new Thread(runnable);
            thread.start();

            /*new Handler().postDelayed(new Runnable() {
                public void run() {
                    while (!dataList.isEmpty()) {
                        MainData data = new MainData();
                        database.mainDao().delete(data);
                        Log.d("check", "yes");
                    }
                }
            }, 1000);*/

        } else {
            internetConnection = false;
            Log.d("check", "no");
        }
    }
}