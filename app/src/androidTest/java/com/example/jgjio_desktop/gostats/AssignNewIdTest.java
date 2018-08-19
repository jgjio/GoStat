package com.example.jgjio_desktop.gostats;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class AssignNewIdTest {

    private StatisticalListDao mStatListDao;
    private AppDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mStatListDao = mDb.statisticalListDao();
    }


    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void getNewListId() throws Exception {

    }



}
