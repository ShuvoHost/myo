package com.example.myapps;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class DataViewModel extends AndroidViewModel {

    private DataRepository repository;
    private LiveData<List<DataEntity>> allData;
    //private LiveData<List<DataEntity>> monthData;
    private MutableLiveData<List<DataEntity>> monthData = new MutableLiveData<>();



    public DataViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
        allData = repository.getAllData();
    }

    public LiveData<List<DataEntity>> getAllData() {
        return allData;
    }

    public void insert(DataEntity data) {
        repository.insert(data);
    }

    public void update(DataEntity data) {
        repository.update(data);
    }

    public void delete(DataEntity data) {
        repository.delete(data);
    }

    public LiveData<List<DataEntity>> getDataByMonth(String month, String year) {
        return repository.getDataByMonth(month, year);
    }


    public LiveData<List<DataEntity>> getMonthData() {
        return monthData;
    }







    public void loadMonth(String month, String year) {
        // observeForever দিয়ে নতুন data set করে monthData কে update করব
        repository.getDataByMonth(month, year)
                .observeForever(list -> monthData.setValue(list));
    }


}

