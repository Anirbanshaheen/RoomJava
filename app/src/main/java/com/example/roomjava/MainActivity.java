package com.example.roomjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Adapter.InterfaceAdapter {

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

        adapter = new Adapter(MainActivity.this, dataList, this);
        recyclerView.setAdapter(adapter);

        isInternetConnected();

        //Log.d("ooo", ""+ temp.getText());


        btAdd.setOnClickListener(view -> {
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
        });

        btReset.setOnClickListener(view -> {
            database.mainDao().reset(dataList);

            dataList.clear();
            dataList.addAll(database.mainDao().getAll());
            adapter.notifyDataSetChanged();
        });
    }

    private void isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            internetConnection = true;

            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute();

            /*Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    *//*synchronized (this) {
                        try {
                            wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*//*
             *//*for (MainData temp: dataList) {
                        try {
                            Thread.sleep(2000);
                            MainData data = new MainData();
                            data = temp;
                            database.mainDao().delete(data);
                            MainData finalData = data;
                            MainActivity.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    adapter.dataList.remove(adapter.dataList.indexOf(finalData));
                                    adapter.notifyItemRemoved(adapter.dataList.indexOf(finalData));
                                    adapter.notifyItemRangeChanged(adapter.dataList.indexOf(finalData),adapter.dataList.size());
                                }
                            });
                            //Log.d("check", "yes "+i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*//*

                    if (dataList.size() > 0) {
                        for (int i = 0; i<dataList.size(); i++) {
                            try {
                                Thread.sleep(2000);
                                MainData data = new MainData();
                                data = dataList.get(i);
                                database.mainDao().delete(data);
                                Object finalData = data;
                                MainActivity.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        adapter.dataList.remove(finalData);
                                        adapter.notifyItemRemoved(adapter.dataList.indexOf(finalData));
                                        //adapter.notifyItemRangeChanged(adapter.dataList.indexOf(finalData), adapter.dataList.size());
                                    }
                                });
                                Log.d("check", "yes " + i);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();*/

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

    @Override
    public void observeDataInterface(MainData mainData) {
        Log.d("data", "data call back " + mainData.getText());
        database.mainDao().delete(mainData);
        if (dataList.size() > 0) {
            removeRoomData(dataList.get(0));
        }
    }

    private class AsyncTaskRunner extends AsyncTask<MainData, MainData, MainData> {

        @Override
        protected MainData doInBackground(MainData... params) {
            //MainData data = null;
            if (dataList.size() > 0) {
                removeRoomData(dataList.get(0));
            }
            return null;
        }

        @Override
        protected void onPostExecute(MainData result) {

     /*       if (result!=null){
                MainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    database.mainDao().delete(result);

                    adapter.dataList.remove(result);
                    adapter.notifyItemRemoved(adapter.dataList.indexOf(result));
                    //adapter.notifyItemRangeChanged(adapter.dataList.indexOf(data), adapter.dataList.size());
                }
            });
            }*/

            /*MainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    adapter.dataList.remove(result);
                    adapter.notifyItemRemoved(adapter.dataList.indexOf(result));
                    //adapter.notifyItemRangeChanged(adapter.dataList.indexOf(data), adapter.dataList.size());
                }
            });*/
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(MainData... text) {

        }
    }

    private void removeRoomData(MainData result) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Log.d("data", "remove done  = " + result.getText());
            adapter.removeSingleItem(result, adapter.dataList.indexOf(result)); //
        }, 2000);

   /*     MainActivity.this.runOnUiThread(() -> {
            adapter.removeSingleItem(result);
            database.mainDao().delete(result);
           *//* adapter.dataList.remove(result);
            adapter.notifyItemRemoved(adapter.dataList.indexOf(result));*//*
            Log.d("check", "remove done  = " + result.getText());
        });*/
    }
}