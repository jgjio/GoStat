package com.example.jgjio_desktop.gostats;

import android.arch.persistence.room.TypeConverter;
import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long ToLong(Date date){
        return date == null ? null : date.getTime();
    }
}
