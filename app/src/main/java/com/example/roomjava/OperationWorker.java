package com.example.roomjava;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.ArrayList;
import java.util.List;

public class OperationWorker extends Worker implements Adapter.InterfaceAdapter{

    List<MainData> dataList = new ArrayList();
    RoomDB database;
    Adapter adapter;
    Context context;

    public OperationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        database = RoomDB.getInstance(getApplicationContext());
        dataList = database.mainDao().getAll();
        adapter = new Adapter(context, dataList, this);

        if (dataList.size() > 0) {
            Log.d("data",""+"open");
            removeRoomData(dataList.get(0));
        }

        return Result.success();
    }

    @Override
    public void observeDataInterface(MainData mainData) {
        Log.d("data", "data call back " + mainData.getText());
        database.mainDao().delete(mainData);
        if (dataList.size() > 0) {
            removeRoomData(dataList.get(0));
        }
    }

    /*private void removeRoomData(MainData result) {

        if (dataList.size() > 0) {
            //database.mainDao().delete(result);
            //Log.d("data",""+dataList.size());
            adapter.removeSingleItem(result, adapter.dataList.indexOf(result));
            Log.d("data",""+dataList.size());
        }
        *//*for (MainData d: dataList) {
            Log.d("data", ""+d.getText());
            database.mainDao().delete(d);
            adapter.removeSingleItem(d, adapter.dataList.indexOf(d));
        }*//*
    }*/

    private void removeRoomData(MainData result) {
        database.mainDao().delete(result);
        adapter.removeSingleItem(result, adapter.dataList.indexOf(result));
        Log.d("tesla", ""+dataList);
        /*new Handler(Looper.getMainLooper()).postDelayed(() -> {
            database.mainDao().delete(result);
            Log.d("data", "remove done  = " + result.getText());
            adapter.removeSingleItem(result, adapter.dataList.indexOf(result));
        }, 2000);*/
    }
}
