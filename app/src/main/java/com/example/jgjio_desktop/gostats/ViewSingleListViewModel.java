package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

public class ViewSingleListViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<PagedList<DataPoint>> listDataPoints;

    public ViewSingleListViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<PagedList<DataPoint>> getListById(int listId) {
        listDataPoints = new LivePagedListBuilder<>(
                mRepository.getDataPointsInListById(listId), 30).build();
        return listDataPoints;
    }

    String getName(int listId) {
        return mRepository.getListName(listId);
    }

    void updateListName(String name, int id) {
        mRepository.updateListName(name, id);
    }

}
