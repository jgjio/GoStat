package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.util.List;

//TODO check for race conditions
//TODO finish so that it properly runs in background thread

public class AppRepository {
    private StatisticalListDao mListDao;
    private DataPointDao mDataPointDao;
    LiveData<List<StatisticalList>> mAllStatisticalLists;
    AppDatabase db;


    AppRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDatabase(application);
        mListDao = db.statisticalListDao();
        mDataPointDao = db.dataPointDao();
        mAllStatisticalLists = db.statisticalListDao().loadAllLists();
    }

    LiveData<List<DataPoint>> getDataPointsInList(int listId) {
        return mDataPointDao.getList(listId);
    }

    LiveData<List<StatisticalList>> getAllStatisticalLists() {
        return mAllStatisticalLists;
    }

    LiveData<String> getListName(int listId) {
        return mListDao.getListName(listId);
    }

    void insertDataPoints(List<DataPoint> listDataPoints) {
        new insertDataPointsAsyncTask(mDataPointDao).execute(listDataPoints);
    }

    private static class insertDataPointsAsyncTask extends AsyncTask<List<DataPoint>, Void, Void> {
        private DataPointDao dataPointDao;

        insertDataPointsAsyncTask(DataPointDao dao) {dataPointDao = dao;}

        @Override
        protected Void doInBackground(final List<DataPoint>... params) {
            dataPointDao.insertDataPoints(params[0]);
            return null;
        }
    }


    private static class insertListAsyncTask extends AsyncTask<StatisticalList, Void, Void> {

        private StatisticalListDao listDao;

        insertListAsyncTask(StatisticalListDao dao) {
            listDao = dao;
        }

        @Override
        protected Void doInBackground(final StatisticalList... params) {
            listDao.insert(params[0]);
            return null;
        }
    }




}
