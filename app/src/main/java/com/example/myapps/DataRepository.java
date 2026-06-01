package com.example.myapps;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executors;

public class DataRepository {

    private DataDao dataDao;
    private LiveData<List<DataEntity>> allData;

    public DataRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        dataDao = db.dataDao();
        allData = dataDao.getAllData();
    }

    public LiveData<List<DataEntity>> getAllData() {
        return allData;
    }

    public void insert(DataEntity data) {
        Executors.newSingleThreadExecutor().execute(() ->
                dataDao.insert(data)
        );
    }

    public void update(DataEntity data) {
        Executors.newSingleThreadExecutor().execute(() ->
                dataDao.update(data)
        );
    }

    public void delete(DataEntity data) {
        Executors.newSingleThreadExecutor().execute(() ->
                dataDao.delete(data)
        );
    }

    public LiveData<List<DataEntity>> getDataByMonth(String month, String year) {
        return dataDao.getDataByMonth(month, year);
    }


}

