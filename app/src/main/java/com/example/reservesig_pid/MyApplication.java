package com.example.reservesig_pid;

import android.app.Application;
import android.text.format.DateFormat;
import java.util.Calendar;
import java.util.Locale;


//la class est lancé avant le luncher activity
public class MyApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
    }

    //converti le timstamp dans un format de date
    public static final String formatTimeStamp(long timestamp){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        
        String date = DateFormat.format("dd/MM/yyyy",cal).toString();

        return date;
    }

}
